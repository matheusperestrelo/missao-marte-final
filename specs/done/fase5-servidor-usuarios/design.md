# Design: fase5-servidor-usuarios

## Abordagem

Criar `fases/fase-05-servidor-ranking/servidor/` como um projeto Spring
Boot 4.1.2 novo (Groovy DSL, Wrapper 9.8.2, Java 17), sem nenhuma ligação
com os builds do cliente. Pacote base `missaomarte.servidor`, dividido em
`domain` (entidade e repositório), `application` (serviços e portas),
`infrastructure` (JWT e segredo) e `api` (controllers, DTOs `record`,
tratamento de erros, segurança HTTP). A autenticação usa o resource server
OAuth2 do Spring Security com `NimbusJwtEncoder`/`NimbusJwtDecoder` em
HS256. Todo erro sai em `ProblemDetail`, inclusive os 401 gerados pelo
filtro de segurança.

## Decisões do alinhamento da Fase 5 (valem para esta spec)

1. A spec cria a pasta da fase só com `servidor/`, `README.md` e
   `.gitignore`. O `cliente/` nasce na `fase5-cliente-rede`, copiando a
   Fase 4.
2. Login pelo **nome normalizado** (`toLowerCase(Locale.ROOT)`). As
   respostas mostram o nome como foi cadastrado. A senha diferencia
   maiúsculas.
3. **Todo 401 é `ProblemDetail`**, inclusive o de token ausente, inválido
   ou expirado, com uma mensagem genérica única e `WWW-Authenticate:
   Bearer`.
4. **Segredo JWT com 1 a 31 bytes impede a inicialização.** Ausente ou
   vazio gera um segredo aleatório e um aviso.
5. A documentação da API nasce com cada spec (`README.md` e
   `requests.http`). O tutorial fica para depois do cliente.
6. Banco e autenticação externos (Supabase) foram descartados.

## Componentes afetados

Todos novos. Caminhos relativos a `fases/fase-05-servidor-ranking/`.

| Arquivo | Conteúdo |
|---|---|
| `README.md` | Visão da fase: hoje só `servidor/`; `cliente/` chega depois da Fase 4; por que dois builds independentes (versões de Gradle diferentes, 8.7 × 9.8.2; o servidor não depende do jogo, duplica o que precisar) |
| `.gitignore` | `build/`, `.gradle/`, `out/`, `ranking.json`, `local.properties`, `dados/` |
| `servidor/settings.gradle` | `rootProject.name = 'missao-marte-servidor'` |
| `servidor/build.gradle` | Plugins `java`, `org.springframework.boot` **4.1.2** e `io.spring.dependency-management`; `group = 'missaomarte'`; Java 17 (`sourceCompatibility`/`targetCompatibility` e `options.release = 17`, `options.encoding = 'UTF-8'`); dependências abaixo; `test { useJUnitPlatform() }` |
| `servidor/gradlew`, `gradlew.bat`, `gradle/wrapper/*` | Wrapper **9.8.2** (ver "Riscos": como gerar) |
| `servidor/requests.http` | Exemplos de R8.3 |
| `servidor/README.md` | Conteúdo de R8.2 |
| `servidor/src/main/resources/application.properties` | Ver "Configuração" |
| `servidor/src/main/java/missaomarte/servidor/MissaoMarteServidorApplication.java` | `@SpringBootApplication`, `main` |
| `.../domain/Usuario.java` | `@Entity` (`usuarios`): `id` (`Long`, `@GeneratedValue IDENTITY`), `nome` (20), `nomeNormalizado` (20, `unique = true`, `nullable = false`), `hashSenha` (60), `criadoEm` (`Instant`). Construtor protegido para o JPA e um construtor de criação; sem setters públicos; `toString` sem o hash |
| `.../domain/UsuarioRepository.java` | `JpaRepository<Usuario, Long>`; `Optional<Usuario> findByNomeNormalizado(String)`; `boolean existsByNomeNormalizado(String)` |
| `.../domain/NomeUsuario.java` | Utilitário puro: `static String normalizar(String nome)` → `nome.toLowerCase(Locale.ROOT)` |
| `.../application/CadastroService.java` | `cadastrar(String nome, String senha)` → `Usuario`. Confere a unicidade pelo nome normalizado (`NomeJaExisteException` → 409), gera o hash com `PasswordEncoder`, salva com `criadoEm = Instant.now(clock)`. `DataIntegrityViolationException` no `save` (corrida entre dois cadastros) também vira `NomeJaExisteException` |
| `.../application/AutenticacaoService.java` | `entrar(String nome, String senha)` → `TokenEmitido`. Busca pelo nome normalizado; usuário ausente → `passwordEncoder.matches(senha, hashFalso)` e `CredenciaisInvalidasException`; senha errada → a mesma exceção. O `hashFalso` é gerado uma vez no construtor (`passwordEncoder.encode` de um valor aleatório) |
| `.../application/EmissorDeToken.java` | Porta: `TokenEmitido emitir(long usuarioId)` |
| `.../application/TokenEmitido.java` | `record (String token, long expiraEmSegundos)`; `toString` sem o token |
| `.../application/UsuarioAutenticado.java` | `record (long id)`, o que as rotas protegidas recebem |
| `.../application/NomeJaExisteException.java`, `CredenciaisInvalidasException.java` | Exceções de aplicação (sem dados sensíveis na mensagem) |
| `.../infrastructure/SegredoJwt.java` | Resolução pura do segredo: `static SegredoJwt resolver(String valorDaVariavel, SecureRandom random)` → bytes do segredo e um indicador "aleatório". `null`/vazio → 32 bytes aleatórios. 1–31 bytes em UTF-8 → `IllegalStateException("MISSAOMARTE_JWT_SEGREDO precisa ter pelo menos 32 bytes.")`. ≥ 32 → os bytes. `toString` não mostra os bytes |
| `.../infrastructure/ConfiguracaoJwt.java` | `@Configuration`: lê `missaomarte.jwt.segredo` (`${MISSAOMARTE_JWT_SEGREDO:}`), chama `SegredoJwt.resolver`, registra o aviso (`log.warn`) quando o segredo for aleatório; beans `SecretKey` (`HmacSHA256`), `JwtEncoder` (`NimbusJwtEncoder` com `ImmutableSecret`), `JwtDecoder` (`NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256)` com `JwtTimestampValidator` usando o `Clock`) e `EmissorDeToken` |
| `.../infrastructure/JwtEmissorDeToken.java` | Implementa `EmissorDeToken`: `iat = now(clock)`, `exp = iat + 2 h`, `sub = id`, cabeçalho `HS256`. Não põe outras declarações |
| `.../api/ConfiguracaoSeguranca.java` | `SecurityFilterChain`: `csrf` desligado, `sessionManagement` `STATELESS`, `requestCache`/`formLogin`/`httpBasic`/`logout` desligados; `permitAll` para `POST /api/usuarios`, `POST /api/auth/login`, `GET /api/saude`, `/error`; `anyRequest().authenticated()`; `oauth2ResourceServer(jwt)` com `authenticationEntryPoint` e `accessDeniedHandler` próprios. Bean `PasswordEncoder` = `BCryptPasswordEncoder(10)`. Bean `Clock` = `Clock.systemUTC()` |
| `.../api/EntradaNaoAutenticada.java` | `AuthenticationEntryPoint`: escreve 401 `ProblemDetail` (`title` "Não autorizado", `detail` "Autenticação necessária ou token inválido.") em `application/problem+json`, com `WWW-Authenticate: Bearer` (pode reaproveitar o `BearerTokenAuthenticationEntryPoint` só para o cabeçalho). Usa o `JsonMapper` do Spring (Jackson 3) |
| `.../api/ResolvedorUsuarioAutenticado.java` | `HandlerMethodArgumentResolver` para parâmetros `UsuarioAutenticado`: lê o `Jwt` do `SecurityContext`, converte o `sub` em `long` e confere `usuarioRepository.existsById`; se não existir, lança `AuthenticationCredentialsNotFoundException`-equivalente tratado como 401 com o mesmo `ProblemDetail` da entrada não autenticada. Registrado num `WebMvcConfigurer` |
| `.../api/SaudeController.java` | `GET /api/saude` → `record Saude(String status)` com `"ok"` |
| `.../api/UsuarioController.java` | `POST /api/usuarios` (`@Valid CadastroRequest`) → 201 `UsuarioResponse(id, nome)` com `Location: /api/usuarios/{id}` |
| `.../api/AutenticacaoController.java` | `POST /api/auth/login` (`@Valid LoginRequest`) → 200 `LoginResponse(token, "Bearer", expiraEmSegundos)` |
| `.../api/dto/CadastroRequest.java` | `record (String nome, String senha)`: `nome` com `@NotBlank` e `@Pattern(regexp = "^[A-Za-z0-9_-]{3,20}$")`; `senha` com `@NotBlank`, `@Size(min = 8)` e `@TamanhoMaximoEmBytes(72)`; `toString` → `CadastroRequest[nome=..., senha=***]` |
| `.../api/dto/LoginRequest.java` | `record (String nome, String senha)`, ambos `@NotBlank`; `toString` com a senha mascarada |
| `.../api/dto/UsuarioResponse.java`, `LoginResponse.java` | `record`s de resposta; `LoginResponse.toString` sem o token |
| `.../api/validacao/TamanhoMaximoEmBytes.java` + validador | Anotação Bean Validation: `valor.getBytes(UTF_8).length <= max`; `null` é válido (o `@NotBlank` cuida) |
| `.../api/TratadorDeErros.java` | `@RestControllerAdvice extends ResponseEntityExceptionHandler`: validação → 400 com a propriedade `erros` (`[{campo, mensagem}]`, sem o valor rejeitado); JSON malformado → 400; `NomeJaExisteException` → 409 ("Já existe um usuário com esse nome."); `CredenciaisInvalidasException` → 401 ("Nome ou senha inválidos."); `Exception` → 500 com `detail` genérico ("Erro interno no servidor.") e log do erro **sem** o corpo da requisição |
| `servidor/src/test/resources/application.properties` | H2 em memória (`jdbc:h2:mem:testes;DB_CLOSE_DELAY=-1`), `ddl-auto=create-drop`, `missaomarte.jwt.segredo` com um valor visivelmente de teste de ≥ 32 bytes (ex.: `segredo-somente-para-testes-0123456789`) |
| `servidor/src/test/java/missaomarte/servidor/**` | Testes (ver "Estratégia de testes") e a rota protegida de teste |

### Dependências (`servidor/build.gradle`)

- `implementation`: `spring-boot-starter-webmvc`, `spring-boot-starter-validation`,
  `spring-boot-starter-data-jpa`, `spring-boot-starter-security`,
  `spring-boot-starter-oauth2-resource-server`.
- `runtimeOnly`: `com.h2database:h2`.
- `testImplementation`: os starters de teste do Boot 4 para MVC, segurança
  e JPA (no Boot 4 eles são modulares, ex.: `spring-boot-starter-webmvc-test`,
  `spring-boot-starter-security-test`; conferir os nomes na documentação
  da 4.1.2). `testRuntimeOnly`: `junit-platform-launcher`.
- Sem `spring-boot-starter-web` (nome antigo), sem Lombok, sem springdoc,
  sem `h2-console`.

### Configuração (`application.properties` de produção)

```properties
server.address=127.0.0.1
server.port=8080
spring.datasource.url=jdbc:h2:file:${MISSAOMARTE_DADOS:./dados}/missaomarte
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.h2.console.enabled=false
spring.mvc.problemdetails.enabled=true
server.error.include-stacktrace=never
server.error.include-message=never
missaomarte.jwt.segredo=${MISSAOMARTE_JWT_SEGREDO:}
```

Nenhum valor de segredo no arquivo.

## Fluxo

```
POST /api/usuarios {nome, senha}
  └─ filtro de segurança: rota liberada
  └─ UsuarioController → @Valid CadastroRequest (400 se inválido)
       └─ CadastroService.cadastrar
            ├─ existe nome normalizado? → 409
            ├─ hash BCrypt(10), criadoEm = now(clock)
            └─ save → 201 {id, nome}

POST /api/auth/login {nome, senha}
  └─ AutenticacaoService.entrar
       ├─ findByNomeNormalizado
       │    ├─ ausente  → matches(senha, hashFalso) → 401 "Nome ou senha inválidos."
       │    └─ presente → matches(senha, hash) ? emitir(id) : 401 (mesmo corpo)
       └─ 200 {token, "Bearer", 7200}

GET /qualquer-coisa  (Authorization: Bearer <jwt>)
  └─ BearerTokenAuthenticationFilter → JwtDecoder (HS256, exp com o Clock)
       ├─ sem token / inválido / expirado → EntradaNaoAutenticada → 401 ProblemDetail
       └─ válido → controller; parâmetro UsuarioAutenticado
                     └─ usuário do sub não existe → 401 ProblemDetail
```

## Organização dos pacotes e o gate

O `.workflow/check.sh` aplica a regra de camadas a qualquer arquivo em
`*/domain/*`, `*/application/*` e `*/infrastructure/*`:

- `domain` não importa `application`, `infrastructure` (nem libGDX/Android);
- `application` não importa `infrastructure`;
- `api` não tem regra no gate e pode importar todas as outras.

Por isso: a porta `EmissorDeToken` fica em `application` e a
implementação em `infrastructure`; os serviços dependem de
`PasswordEncoder` (interface do Spring Security Crypto), não de
`infrastructure`. A configuração de segurança HTTP fica em `api` porque
liga o filtro às rotas. Importar `org.springframework.*` e
`jakarta.persistence.*` é permitido em qualquer camada.

## Decisões e alternativas descartadas

- **Decisão:** JWT com o resource server do Spring Security
  (`NimbusJwtEncoder`/`NimbusJwtDecoder`).
  **Alternativa descartada:** biblioteca `jjwt` com filtro próprio. Seria
  mais código de segurança escrito à mão, e o escopo já aponta o
  `JwtDecoder` com `withSecretKey` como caminho.
- **Decisão:** 401 de token via `AuthenticationEntryPoint` próprio em
  `ProblemDetail`.
  **Alternativa descartada:** deixar o padrão do Spring (corpo vazio). O
  cliente teria um formato a mais para tratar.
- **Decisão:** usuário do `sub` inexistente → 401.
  **Alternativa descartada:** confiar só na assinatura. Um banco apagado
  com o mesmo segredo deixaria tokens antigos passarem com `id` órfão.
- **Decisão:** a resolução do segredo é uma função pura (`SegredoJwt`),
  testada sem subir o Spring.
  **Alternativa descartada:** testar só subindo o contexto com variáveis
  diferentes. Fica lento e frágil.
- **Decisão:** hash falso gerado no construtor do serviço.
  **Alternativa descartada:** hash falso fixo no código. Funciona, mas
  parece um segredo versionado e confunde a revisão.
- **Decisão:** unicidade conferida antes do `save` **e** pela restrição
  única do banco.
  **Alternativa descartada:** só a checagem prévia. Dois cadastros ao
  mesmo tempo criariam duplicata.
- **Decisão:** a senha mínima conta caracteres (`@Size(min = 8)`); a
  máxima conta bytes UTF-8 (≤ 72), por causa do limite do BCrypt.
- **Decisão:** `Clock` como bean, usado em `criadoEm`, `iat`/`exp` e na
  validação de expiração. Permite gerar token expirado nos testes.
- **Decisão:** pacote `missaomarte.servidor`, separado do
  `missaomarte.*` do cliente. O servidor não reutiliza código do jogo.
- **Descartado no alinhamento:** Supabase (banco/autenticação), Swagger,
  Docker.

## Riscos e impactos

- **Spring Boot 4 mudou nomes** (`ESCOPO-PROJETO.md`, seção 14):
  `spring-boot-starter-webmvc` (não `-web`), Jackson 3 (`tools.jackson.*`,
  `JsonMapper`), `@AutoConfigureMockMvc` em pacote novo, starters de teste
  modulares. Conferir na documentação da 4.1.2 antes de escrever o build.
- **HS256 recusado** se o `JwtDecoder` não declarar o algoritmo: usar
  `withSecretKey(...).macAlgorithm(MacAlgorithm.HS256)` e o mesmo
  algoritmo no cabeçalho do encoder.
- **Erro virando 401:** se `/error` não estiver liberado, um 400 ou 500
  vira 401. Há teste de 400 sem token (cadastro inválido) que pega isso.
- **Campo ausente virando valor padrão:** os `record`s usam tipos
  objeto e `@NotBlank`/`@NotNull`.
- **Gerar o Wrapper 9.8.2:** o Wrapper 8.7 das outras fases não roda o
  plugin do Boot 4. Gerar o Wrapper **antes** de criar o `build.gradle`
  (com um `settings.gradle` vazio), por exemplo
  `../../fase-02-gradle/gradlew -p . wrapper --gradle-version 9.8.2` dentro
  de `servidor/`, ou com um Gradle 9 instalado. Conferir
  `distributionUrl` com `gradle-9.8.2-bin.zip`.
- **JDK:** a máquina do usuário tem JDK 25 no `PATH`. O gate força JDK 17
  (`achar_jdk17`); o README precisa dizer para rodar com JDK 17.
- **Dois servidores no mesmo arquivo H2** travam o banco: documentar no
  README.
- **Fase 6** (`briefing-fase6`, `fase6-android-build`) vai copiar este
  servidor e torná-lo configurável (endereço, porta, JAR, encerramento).
  Nada aqui deve fixar o endereço fora do `application.properties`.
- **`docs/contratos.md`, seção 7,** ainda chama a API de "proposta". Esta
  spec e a 14 são a versão decidida. O tutorial da fase atualiza a
  referência.

## Estratégia de testes

Testes com JUnit 5. Os de API usam `@SpringBootTest` +
`@AutoConfigureMockMvc` com H2 em memória e as propriedades de teste.
Nenhum teste usa rede. Nomes de teste com o ID do critério (ex.:
`r3_6_nomeRepetidoComOutraCaixaDa409`).

| Requisito | Testes |
|---|---|
| R1 | Gate (`clean build`); inspeção dos arquivos e da configuração; manual: `bootRun`, arquivo em `MISSAOMARTE_DADOS`, reinício mantém usuários |
| R2 | `SaudeApiTest`: sem token e com token inválido → 200 `{"status":"ok"}` |
| R3 | `CadastroApiTest`: 201 com só `id`/`nome`; hash BCrypt custo 10 e sal diferente (lendo o `UsuarioRepository`); 409 com outra caixa e contagem; 400 nos limites do nome (2/3/20/21, espaço, acento, especial, vazio, ausente), senha 7/8, 72/73 bytes, acentuada, ausente, JSON malformado |
| R4 | `LoginApiTest`: 200 e formato; nome em outra caixa; senha em outra caixa recusada; declarações do token (decodificar com o `JwtDecoder` do contexto); corpos iguais para inexistente e senha errada; 400 com campos ausentes. `AutenticacaoServiceTest` (unidade, `PasswordEncoder` simulado): `matches` chamado com o hash falso quando o usuário não existe |
| R5 | `ProtecaoApiTest` com `RotaProtegidaDeTeste` (`@RestController` em `src/test`, `GET /api/teste/protegida` devolvendo o `id` do `UsuarioAutenticado`, importado por `@TestConfiguration`/`@Import`): sem token, malformado, assinatura de outro segredo, expirado (token emitido com `Clock` fixo 3 h atrás), `sub` inexistente → 401 com `ProblemDetail`, `detail` fixo e `WWW-Authenticate: Bearer`; válido → 200 com o `id`; rota inexistente sem token → 401; sem `Set-Cookie` |
| R6 | `SegredoJwtTest` (unidade): ausente, vazio, 31 bytes (exceção com a mensagem, sem o valor), 32 bytes, segredo aleatório com 32 bytes. Teste com `OutputCaptureExtension` (ou contexto com a propriedade vazia) confere o aviso no log |
| R7 | Testes de erro conferem `application/problem+json`; dois campos inválidos → dois itens em `erros`; senha inválida não aparece no corpo; `toString` de `CadastroRequest`, `LoginRequest`, `LoginResponse` e `TokenEmitido`; log capturado de cadastro + login sem senha nem token |
| R8 | Inspeção do README da fase, do `servidor/README.md` e do `requests.http`; QA manual seguindo o README |
