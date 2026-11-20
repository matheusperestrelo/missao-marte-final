package missaomarte.application;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import missaomarte.domain.Direcao;
import missaomarte.domain.Missao;
import missaomarte.domain.Nave;
import missaomarte.domain.Passageiro;

/** Estado e regras de uma partida; recebe uma Acao e devolve os eventos do turno. */
public class GameSession {

    private static final String PILOTO_PADRAO = "Piloto Anônimo";
    private static final int TAMANHO_PADRAO = 5;

    private final MissaoFactory fabrica;
    private final Random random;
    private final Clock clock;

    private Missao missao;
    private String piloto;
    private Dificuldade dificuldade;
    private int pontos;
    private int movimentos;
    private long inicioMillis;
    private long duracaoSegundos;
    private ResultadoPartida resultado = ResultadoPartida.EM_ANDAMENTO;

    public GameSession(MissaoFactory fabrica, Random random, Clock clock) {
        this.fabrica = fabrica;
        this.random = random;
        this.clock = clock;
    }

    public void iniciar(String piloto, Dificuldade dificuldade, int tamanho) {
        this.piloto = (piloto == null || piloto.trim().isEmpty()) ? PILOTO_PADRAO : piloto;
        this.dificuldade = dificuldade;
        int tamanhoFinal = tamanho < 1 ? TAMANHO_PADRAO : tamanho;
        // A fábrica sempre cria >= 4 passageiros; missão vazia não ocorre por aqui.
        this.missao = fabrica.criar(dificuldade, tamanhoFinal);
        this.pontos = dificuldade.pontuacaoInicial();
        this.movimentos = 0;
        this.duracaoSegundos = 0;
        this.resultado = ResultadoPartida.EM_ANDAMENTO;
        this.inicioMillis = clock.millis();
    }

    public ResultadoTurno jogar(Acao acao) {
        if (acao == Acao.INVALIDA) {
            return new ResultadoTurno(List.of(), true);
        }
        if (acao == Acao.SAIR) {
            resultado = ResultadoPartida.ABORTADA;
            congelarDuracao();
            return new ResultadoTurno(List.of(), false);
        }

        List<EventoJogo> eventos = new ArrayList<>();
        Nave nave = missao.getNave();

        if (acao.ehMovimento()) {
            nave.mover(acao.direcao(), missao.getMapa());
            pontos--;
            movimentos++;
            eventos.add(new EventoJogo(TipoEvento.MOVIMENTO, null, 0));
            int entregues = missao.desembarcarNaBase();
            if (entregues > 0) {
                eventos.add(new EventoJogo(TipoEvento.ENTREGA, null, entregues));
            }
        } else {
            embarcar(eventos);
        }

        missao.moverInimigos(() -> direcaoSorteada(random.nextInt(4)));

        if (missao.haColisao()) {
            nave.perderVida();
            eventos.add(new EventoJogo(TipoEvento.COLISAO, null, nave.getVidas()));
            if (nave.getVidas() == 0) {
                eventos.add(new EventoJogo(TipoEvento.DERROTA_NAVE_DESTRUIDA, null, 0));
                resultado = ResultadoPartida.DERROTA;
            }
        }
        if (pontos <= 0) {
            eventos.add(new EventoJogo(TipoEvento.DERROTA_PONTUACAO_ZERADA, null, 0));
            resultado = ResultadoPartida.DERROTA;
        }
        if (resultado == ResultadoPartida.EM_ANDAMENTO && missao.todosEntregues()) {
            eventos.add(new EventoJogo(TipoEvento.VITORIA, null, 0));
            resultado = ResultadoPartida.VITORIA;
        }
        if (resultado != ResultadoPartida.EM_ANDAMENTO) {
            congelarDuracao();
        }
        return new ResultadoTurno(List.copyOf(eventos), false);
    }

    // Embarque só por missao.embarcar: mantém a conta a bordo + entregues + restantes.
    private void embarcar(List<EventoJogo> eventos) {
        Passageiro passageiro = missao.passageiroNaPosicaoDaNave();
        if (passageiro == null) {
            eventos.add(new EventoJogo(TipoEvento.EMBARQUE_RECUSADO_SEM_PASSAGEIRO, null, 0));
        } else if (!missao.embarcar(passageiro)) {
            eventos.add(new EventoJogo(TipoEvento.EMBARQUE_RECUSADO_NAVE_CHEIA, null, 0));
        } else {
            pontos += passageiro.pontuacao();
            eventos.add(new EventoJogo(TipoEvento.EMBARQUE, passageiro.getNome(), passageiro.pontuacao()));
        }
    }

    // Mapeamento da Fase 0: 0 direita, 1 esquerda, 2 baixo, 3 cima.
    private static Direcao direcaoSorteada(int sorteio) {
        switch (sorteio) {
            case 0: return Direcao.DIREITA;
            case 1: return Direcao.ESQUERDA;
            case 2: return Direcao.BAIXO;
            default: return Direcao.CIMA;
        }
    }

    private void congelarDuracao() {
        duracaoSegundos = (clock.millis() - inicioMillis) / 1000;
    }

    public Missao getMissao() {
        return missao;
    }

    public String getPiloto() {
        return piloto;
    }

    public Dificuldade getDificuldade() {
        return dificuldade;
    }

    public int getPontos() {
        return pontos;
    }

    public int getVidas() {
        return missao.getNave().getVidas();
    }

    public int getMovimentos() {
        return movimentos;
    }

    public long getDuracaoSegundos() {
        return isEncerrada() ? duracaoSegundos : (clock.millis() - inicioMillis) / 1000;
    }

    public ResultadoPartida getResultado() {
        return resultado;
    }

    public boolean isEncerrada() {
        return resultado != ResultadoPartida.EM_ANDAMENTO;
    }

    public ResumoPartida resumo() {
        return new ResumoPartida(piloto, dificuldade, pontos, movimentos, missao.getEntregues(),
                getDuracaoSegundos(), resultado == ResultadoPartida.VITORIA);
    }
}
