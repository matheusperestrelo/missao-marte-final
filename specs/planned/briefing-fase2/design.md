# Briefing (design): Fase 2

## Decisões já tomadas

- **Gate:** o `.workflow/check.sh` já roda `./gradlew clean build` em toda
  fase com `gradlew` na raiz da pasta. Não é preciso trocar o gate.
- **JDK 17 obrigatório:** o Gradle 8.7 não roda no JDK 25. Antes de começar,
  instalar com `brew install openjdk@17`. O gate encontra o JDK sozinho e
  falha com mensagem clara se ele não existir.
- **Bug encontrado na #9:** enquanto a #8 não estiver mergeada, a correção
  vai para a Fase 1 (`fix-` na fila) e a Fase 2 copia de novo. Depois do
  merge da #8, o bug fica registrado como limitação no tutorial (#10).
- **Testabilidade já preparada na Fase 1:** `Random` e `Clock` são
  injetados; `FileRankingRepository` recebe o `Path`; `RankingTop5` recebe a
  porta.

## Perguntas em aberto (para o grill da fase)

1. **Versão do JUnit:** recomendo o BOM `org.junit:junit-bom:5.10.x`, para
   não espalhar versões.
2. **Hash:** onde fica a evidência da comparação? Recomendo um script
   `scripts/comparar-hash.sh` na pasta da fase, com a saída colada no
   tutorial (#10).
3. **Teste do console:** fica de fora ou não? Recomendo de fora, porque a
   issue #9 não pede e o console não tem regra.
4. **Pasta `.gitignore` e README da fase:** README mínimo da fase na spec
   `fase2-gradle`; o README da raiz fica na #10.
