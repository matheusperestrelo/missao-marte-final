# Briefing: Fase 6 — Android, toque e servidor configurável

> **Briefing, não spec.** Ele guarda o que já foi decidido para a Fase 6 até
> o planejamento desta fase. Na hora de planejá-la: grill das perguntas em
> aberto (`design.md`), criação das specs abaixo em `active/` e remoção
> deste briefing do backlog.

## Contexto

Levar o mesmo jogo ao Android (APK de debug ou evidência da limitação), com
controles por toque e endereço do servidor configurável, preservando o
desktop. Pasta: `fases/fase-06-android/` (`cliente/` com `core`, `lwjgl3`,
`android` e `tools`; `servidor/`).

## Specs previstas (fila aprovada)

| Ordem prevista | Spec | Issue | Trilha | Depende de |
|---|---|---|---|---|
| 15 | `fase6-android-build` | a criar | C | 14 (`fase5-cliente-telas-online`) |
| 16 | `fase6-toque` | a criar | B | 15 |
| 17 | `fase6-config-servidor` | a criar | A | 15 |

**Issues a criar:** uma "[Fase 6] ..." por spec, mais a de documentação, que
fica sem spec.

## Base dos requisitos (ESCOPO-PROJETO.md, seção 10, "Fase 6"), já com os cortes

- **fase6-android-build:** módulo `android` com `AndroidLauncher` montando
  as mesmas dependências do desktop; mesmos assets de `core/assets`;
  nativas para `armeabi-v7a`, `arm64-v8a`, `x86` e `x86_64`; manifesto só
  com `INTERNET`, paisagem e ícone gerado em `tools/`; ranking local do
  convidado na pasta privada do app; **módulo `android` só entra no build
  se houver SDK**; troca de APIs ausentes na API 26 (`Files.readString`/
  `writeString`, `List.of`, `String.isBlank`, `String.repeat`...); HTTP sem
  criptografia só no build de debug; `./gradlew :android:assembleDebug` ou
  evidência da limitação. **Servidor** (sem mudar rotas): endereço e porta
  por variável de ambiente (padrão `127.0.0.1:8080`), nome fixo do JAR,
  encerramento gracioso. _Sobreposição:_ o servidor de partida é o das
  specs 13 e 14 (`fase5-servidor-usuarios`, `fase5-servidor-partidas-ranking`),
  que fixam `server.address`/`server.port` só no `application.properties`.
- **fase6-toque:** botões virtuais na partida (4 setas, Embarcar, Abortar)
  gerando a mesma `Acao` das teclas; menus por toque chamando o mesmo
  código das teclas; botão Voltar do Android; modo toque no desktop com
  `-Dmissaomarte.toque=true` (sem a propriedade, o desktop fica igual à
  Fase 5); `ExtendViewport` com controles na área extra; botões sem
  sobreposição (testado); comportamento em 4:3 registrado; layout e
  tradução botão → ação sem libGDX e testados; campos de texto com a caixa
  nativa e mesmas regras de digitação.
- **fase6-config-servidor:** objeto de valor imutável (endereço, porta,
  HTTPS) que valida na criação (IPv4/hostname, porta 1–65535; recusa `@`,
  espaço e barra); gravado nas Preferences atrás de interface, nunca senha
  nem token; precedência salvo > padrão do launcher
  (`-Dmissaomarte.servidor` / `-PservidorPadrao`) > `http://localhost:8080`;
  tela com Salvar e Restaurar padrão; trocar de servidor encerra a sessão;
  aviso para HTTP fora de rede local.

## Fora de escopo (cortes intencionais)

- Controle Bluetooth.
- Repetição das setas por toque (dedo apoiado).
- Botão "Testar conexão" na tela de configuração (a rota `/api/saude`
  continua existindo no servidor desde a Fase 5).
- `Dockerfile` e `DEPLOY.md`.

**A confirmar no grill:** cortar o botão "Som" da partida. O escopo prevê o
botão, mas a tecla `M`, que liga e desliga o som, foi cortada na Fase 4
(`design.md`, pergunta 2).
