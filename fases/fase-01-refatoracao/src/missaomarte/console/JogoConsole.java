package missaomarte.console;

import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import missaomarte.application.Acao;
import missaomarte.application.Dificuldade;
import missaomarte.application.GameSession;
import missaomarte.application.RankingEntry;
import missaomarte.application.RankingException;
import missaomarte.application.RankingTop5;
import missaomarte.application.ResumoPartida;
import missaomarte.application.ResultadoPartida;
import missaomarte.application.ResultadoTurno;
import missaomarte.domain.Missao;

/** Menu, leitura de entradas e laço da partida; sem regra de jogo. */
public class JogoConsole {

    private final Scanner scanner;
    private final PrintStream out;
    private final GameSession sessao;
    private final RankingTop5 ranking;
    private final DesenhoMapa desenho = new DesenhoMapa();
    private final MensagensConsole mensagens = new MensagensConsole();

    public JogoConsole(Scanner scanner, PrintStream out, GameSession sessao, RankingTop5 ranking) {
        this.scanner = scanner;
        this.out = out;
        this.sessao = sessao;
        this.ranking = ranking;
    }

    public void executar() {
        exibirBoasVindas();
        boolean rodando = true;
        while (rodando) {
            if (!scanner.hasNextLine()) {
                out.println("\nEntrada encerrada. Obrigado por jogar!");
                break;
            }
            exibirMenu();
            String opcao = lerLinha("Escolha uma opção: ", "1");
            switch (opcao) {
                case "1":
                    jogarPartida();
                    break;
                case "2":
                    exibirRanking();
                    break;
                case "3":
                    resetarRanking();
                    break;
                case "4":
                    rodando = false;
                    out.println("\nObrigado por jogar a Missão Marte Unifor!");
                    break;
                default:
                    out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void exibirBoasVindas() {
        out.println("================================================================");
        out.println("             MISSÃO MARTE UNIFOR - VERSÃO 10.0                  ");
        out.println("================================================================");
        out.println("  Pilote sua nave, salve os passageiros e desvie dos perigos!   ");
        out.println("================================================================");
    }

    private void exibirMenu() {
        out.println("\n--- MENU PRINCIPAL ---");
        out.println("1. Iniciar Nova Missão");
        out.println("2. Visualizar Ranking Top 5");
        out.println("3. Resetar Histórico de Ranking");
        out.println("4. Sair do Jogo");
        out.println("----------------------");
    }

    private void jogarPartida() {
        String piloto = lerLinha("\nDigite o nome do piloto: ", "Piloto Anônimo");
        if (piloto.isEmpty()) {
            piloto = "Piloto Anônimo";
        }
        Dificuldade dificuldade = lerDificuldade();
        int tamanho = lerTamanhoMapa();

        out.println("\nIniciando missão na dificuldade " + dificuldade + "...");
        out.println("Pressione Enter para decolar!");
        if (!scanner.hasNextLine()) {
            out.println("Entrada encerrada. Missão não iniciada.");
            return;
        }
        scanner.nextLine();

        sessao.iniciar(piloto, dificuldade, tamanho);
        while (!sessao.isEncerrada()) {
            out.println(desenho.desenhar(sessao));
            out.println(desenho.linhaStatus(sessao));

            String linha = lerLinha("Comando (w/s/a/d/c/q): ", "");
            if (linha.isEmpty()) {
                if (!scanner.hasNextLine()) {
                    out.println("Entrada encerrada. Missão abortada.");
                    return;
                }
                continue;
            }

            ResultadoTurno turno = sessao.jogar(Acao.deTexto(linha));
            if (turno.comandoInvalido()) {
                out.println("Comando inválido.");
                continue;
            }
            if (sessao.getResultado() == ResultadoPartida.ABORTADA) {
                out.println("Missão abortada pelo piloto.");
                return;
            }
            mensagens.traduzir(turno).forEach(out::println);
            if (sessao.getResultado() == ResultadoPartida.VITORIA) {
                exibirEstatisticasERegistrar();
            }

            Missao missao = sessao.getMissao();
            if (!sessao.isEncerrada() && missao.getRestantes() == 0 && missao.getABordo() > 0) {
                out.println("✨ ALERTA: Todos os passageiros resgatados! Retorne para a Plataforma de Pouso 'L' em (0,0) para completar a missão.");
            }
        }
    }

    private void exibirEstatisticasERegistrar() {
        ResumoPartida resumo = sessao.resumo();
        Optional<RankingEntry> recorde = ranking.recorde();
        boolean novoRecorde = ranking.seriaNovoRecorde(resumo.pontos());

        out.println("Estatísticas da Partida:");
        out.printf(" - Pontuação Final: %d pontos%n", resumo.pontos());
        out.printf(" - Movimentos Efetuados: %d%n", resumo.movimentos());
        out.printf(" - Tempo de Jogo: %d segundos%n", resumo.duracaoSegundos());
        out.printf(" - Passageiros Resgatados: %d%n", resumo.entregues());
        if (novoRecorde) {
            out.println("🏆 Novo recorde absoluto do sistema!");
        } else if (recorde.isPresent() && recorde.get().pontuacao() > 0) {
            out.printf(" - Recorde atual a ser batido: %d pontos (Piloto: %s)%n",
                    recorde.get().pontuacao(), recorde.get().nome());
        }
        out.println(MensagensConsole.SEPARADOR);

        boolean entrou;
        try {
            entrou = ranking.registrar(resumo);
        } catch (RankingException e) {
            out.println("Não foi possível salvar o ranking: " + e.getMessage());
            entrou = true;
        }
        if (entrou) {
            out.println("Parabéns! Você entrou para o Top 5 de pilotos!");
        }
    }

    private void exibirRanking() {
        List<RankingEntry> lista = ranking.listar();
        out.println("\n====== RANKING TOP 5 PILOTOS ======");
        if (lista.isEmpty()) {
            out.println(" - Nenhum registro encontrado. Seja o primeiro a jogar!");
        } else {
            int pos = 1;
            for (RankingEntry e : lista) {
                out.printf("%d. %s - %d pts | Dificuldade: %s | Coletados: %d | Tempo: %ds | %s%n",
                        pos++, e.nome(), e.pontuacao(), e.dificuldade(),
                        e.passageirosColetados(), e.tempoJogo(), e.dataHora());
            }
        }
        out.println("===================================");
    }

    private void resetarRanking() {
        String confirmacao = lerLinha(
                "Você realmente deseja limpar o histórico de ranking? (s/n): ", "n").toLowerCase();
        if (confirmacao.equals("s") || confirmacao.equals("sim")) {
            try {
                ranking.limpar();
                out.println("Ranking resetado com sucesso!");
            } catch (RankingException e) {
                out.println("Erro ao resetar ranking: " + e.getMessage());
            }
            return;
        }
        out.println("Operação cancelada.");
    }

    private Dificuldade lerDificuldade() {
        out.print("Escolha a Dificuldade (facil/medio/dificil): ");
        return Dificuldade.deString(lerLinha("", "medio"));
    }

    private int lerTamanhoMapa() {
        try {
            int tamanho = Integer.parseInt(lerLinha("Tamanho do mapa (ex: 5 para mapa de -5 a +5): ", "5"));
            return tamanho > 0 ? tamanho : 5;
        } catch (NumberFormatException e) {
            out.println("Entrada inválida, usando tamanho padrão (5).");
            return 5;
        }
    }

    private String lerLinha(String prompt, String alternativa) {
        if (prompt != null && !prompt.isEmpty()) {
            out.print(prompt);
        }
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return alternativa;
    }
}
