package missaomarte.application;

import java.util.Random;

import missaomarte.domain.Asteroide;
import missaomarte.domain.Astronauta;
import missaomarte.domain.Engenheiro;
import missaomarte.domain.Inimigo;
import missaomarte.domain.Mapa;
import missaomarte.domain.Missao;
import missaomarte.domain.Nave;
import missaomarte.domain.Passageiro;
import missaomarte.domain.Posicao;
import missaomarte.domain.Professor;

public class MissaoFactory {

    private final Random random;

    public MissaoFactory(Random random) {
        this.random = random;
    }

    public Missao criar(Dificuldade dificuldade, int tamanho) {
        Mapa mapa = new Mapa(tamanho);
        Missao missao = new Missao(mapa, new Nave(dificuldade.capacidadeNave()));

        for (int i = 0; i < dificuldade.passageiros(); i++) {
            missao.adicionarPassageiro(criarPassageiro(i, sortearCelulaLivre(missao, mapa)));
        }
        for (int i = 0; i < dificuldade.asteroides(); i++) {
            missao.adicionarAsteroide(new Asteroide(sortearCelulaLivre(missao, mapa)));
        }
        for (int i = 0; i < dificuldade.inimigos(); i++) {
            missao.adicionarInimigo(new Inimigo(sortearCelulaLivre(missao, mapa)));
        }
        return missao;
    }

    // Sorteia x e depois y, como na Fase 0; repete enquanto a célula não for livre.
    // Com tamanho 1 no Médio/Difícil não há células livres suficientes e o laço
    // não termina (ponto d da Fase 0, limitação conhecida).
    private Posicao sortearCelulaLivre(Missao missao, Mapa mapa) {
        int faixa = mapa.getMax() - mapa.getMin() + 1;
        while (true) {
            int x = random.nextInt(faixa) + mapa.getMin();
            int y = random.nextInt(faixa) + mapa.getMin();
            Posicao candidata = new Posicao(x, y);
            if (!candidata.equals(Posicao.ORIGEM) && !missao.estaOcupada(candidata)) {
                return candidata;
            }
        }
    }

    private static Passageiro criarPassageiro(int indice, Posicao posicao) {
        switch (indice % 5) {
            case 0: return new Professor("Dr. Silva", posicao);
            case 1: return new Engenheiro("Eng. Rosa", posicao);
            case 2: return new Professor("Dr. Lima", posicao);
            case 3: return new Engenheiro("Eng. Carlos", posicao);
            default: return new Astronauta("Ast. Maria", posicao);
        }
    }
}
