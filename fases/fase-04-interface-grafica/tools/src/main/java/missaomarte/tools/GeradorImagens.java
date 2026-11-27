package missaomarte.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import javax.imageio.ImageIO;
import missaomarte.gdx.catalogo.Imagem;

/**
 * Desenha as imagens do catálogo com Java2D. Tudo é determinístico: o único
 * aleatório (estrelas e crateras) usa semente fixa.
 */
public final class GeradorImagens {

    private static final Color FERRUGEM = new Color(0x8C4A2F);
    private static final Color FERRUGEM_ESCURA = new Color(0x6E3A25);
    private static final Color CINZA = new Color(0xC9CED6);
    private static final Color CINZA_ESCURO = new Color(0x5B6270);
    private static final Color AZUL = new Color(0x2F6FDB);
    private static final Color VERDE = new Color(0x3FAE5A);
    private static final Color VERMELHO = new Color(0xD63A3A);
    private static final Color LARANJA = new Color(0xF28C28);
    private static final Color AMARELO = new Color(0xF7D547);
    private static final Color PELE = new Color(0xF2C9A0);

    private GeradorImagens() {
    }

    /** Desenha a faixa inteira (todos os quadros lado a lado). */
    public static BufferedImage desenhar(Imagem imagem) {
        BufferedImage faixa = new BufferedImage(
                imagem.quadros() * imagem.larguraQuadro(), imagem.alturaQuadro(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = faixa.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int quadro = 0; quadro < imagem.quadros(); quadro++) {
                Graphics2D gq = (Graphics2D) g.create();
                gq.translate(quadro * imagem.larguraQuadro(), 0);
                desenharQuadro(imagem, quadro, gq);
                gq.dispose();
            }
        } finally {
            g.dispose();
        }
        return faixa;
    }

    public static void gravar(Imagem imagem, Path pasta) throws IOException {
        Path arquivo = pasta.resolve(imagem.caminho());
        Files.createDirectories(arquivo.getParent());
        ImageIO.write(desenhar(imagem), "png", arquivo.toFile());
    }

    private static void desenharQuadro(Imagem imagem, int quadro, Graphics2D g) {
        switch (imagem) {
            case FUNDO -> fundo(g);
            case TERRENO -> terreno(g);
            case NAVE -> nave(g);
            case PROPULSOR -> propulsor(g, quadro);
            case BASE -> base(g, quadro);
            case INIMIGO -> inimigo(g, quadro);
            case ASTEROIDE -> asteroide(g);
            case PROFESSOR -> pessoa(g, new Color(0x7A4FC4), true);
            case ENGENHEIRO -> pessoa(g, LARANJA, false);
            case ASTRONAUTA -> pessoa(g, Color.WHITE, false);
            case GENERICA -> generica(g);
            case EXPLOSAO -> explosao(g, quadro);
            case VIDA_CHEIA -> coracao(g, VERMELHO, true);
            case VIDA_VAZIA -> coracao(g, CINZA_ESCURO, false);
            case VAGA_OCUPADA -> vaga(g, true);
            case VAGA_LIVRE -> vaga(g, false);
        }
    }

    private static void fundo(Graphics2D g) {
        g.setColor(new Color(0x0B0F2A));
        g.fill(new Rectangle2D.Double(0, 0, 800, 600));
        Random sorteio = new Random(42);
        for (int i = 0; i < 160; i++) {
            int x = sorteio.nextInt(800);
            int y = sorteio.nextInt(600);
            int brilho = 140 + sorteio.nextInt(116);
            g.setColor(new Color(brilho, brilho, 255));
            g.fill(new Rectangle2D.Double(x, y, 1 + sorteio.nextInt(2), 1 + sorteio.nextInt(2)));
        }
    }

    private static void terreno(Graphics2D g) {
        g.setColor(FERRUGEM);
        g.fill(new Rectangle2D.Double(0, 0, 48, 48));
        Random sorteio = new Random(7);
        g.setColor(FERRUGEM_ESCURA);
        for (int i = 0; i < 6; i++) {
            double d = 3 + sorteio.nextInt(5);
            g.fill(new Ellipse2D.Double(sorteio.nextInt(40), sorteio.nextInt(40), d, d));
        }
        g.setColor(new Color(0, 0, 0, 50));
        g.setStroke(new BasicStroke(1f));
        g.draw(new Rectangle2D.Double(0.5, 0.5, 47, 47));
    }

    private static void nave(Graphics2D g) {
        Path2D casco = new Path2D.Double();
        casco.moveTo(24, 4);
        casco.lineTo(36, 20);
        casco.lineTo(36, 38);
        casco.lineTo(12, 38);
        casco.lineTo(12, 20);
        casco.closePath();
        g.setColor(CINZA);
        g.fill(casco);
        g.setColor(CINZA_ESCURO);
        g.setStroke(new BasicStroke(1.5f));
        g.draw(casco);
        g.setColor(AZUL);
        g.fill(new Ellipse2D.Double(19, 16, 10, 10));
        g.setColor(VERMELHO);
        Path2D asaEsquerda = new Path2D.Double();
        asaEsquerda.moveTo(12, 28);
        asaEsquerda.lineTo(4, 42);
        asaEsquerda.lineTo(12, 38);
        asaEsquerda.closePath();
        g.fill(asaEsquerda);
        Path2D asaDireita = new Path2D.Double();
        asaDireita.moveTo(36, 28);
        asaDireita.lineTo(44, 42);
        asaDireita.lineTo(36, 38);
        asaDireita.closePath();
        g.fill(asaDireita);
    }

    private static void propulsor(Graphics2D g, int quadro) {
        double altura = quadro == 0 ? 12 : 20;
        Path2D chama = new Path2D.Double();
        chama.moveTo(18, 38);
        chama.lineTo(30, 38);
        chama.lineTo(24, 38 + altura - 6);
        chama.closePath();
        g.setColor(LARANJA);
        g.fill(chama);
        Path2D miolo = new Path2D.Double();
        miolo.moveTo(21, 38);
        miolo.lineTo(27, 38);
        miolo.lineTo(24, 38 + altura / 2 - 3);
        miolo.closePath();
        g.setColor(AMARELO);
        g.fill(miolo);
    }

    private static void base(Graphics2D g, int quadro) {
        g.setColor(CINZA_ESCURO);
        g.fill(new RoundRectangle2D.Double(6, 24, 36, 18, 6, 6));
        g.setColor(CINZA);
        g.fill(new java.awt.geom.Arc2D.Double(8, 6, 32, 36, 0, 180, java.awt.geom.Arc2D.PIE));
        g.setColor(AZUL);
        g.fill(new Ellipse2D.Double(19, 18, 10, 10));
        g.setColor(quadro == 0 ? VERDE : AMARELO);
        g.fill(new Ellipse2D.Double(22, 2, 5, 5));
        g.setStroke(new BasicStroke(2f));
        g.draw(new java.awt.geom.Line2D.Double(24.5, 6, 24.5, 12));
    }

    private static void inimigo(Graphics2D g, int quadro) {
        g.setColor(VERMELHO);
        g.fill(new Ellipse2D.Double(8, 10, 32, 26));
        g.setColor(new Color(0x7A1F1F));
        double deslocamento = quadro == 0 ? 0 : 3;
        g.fill(new Rectangle2D.Double(10 + deslocamento, 32, 6, 10));
        g.fill(new Rectangle2D.Double(32 - deslocamento, 32, 6, 10));
        g.setColor(Color.WHITE);
        g.fill(new Ellipse2D.Double(14, 17, 8, 8));
        g.fill(new Ellipse2D.Double(26, 17, 8, 8));
        g.setColor(Color.BLACK);
        g.fill(new Ellipse2D.Double(16 + deslocamento / 3, 19, 4, 4));
        g.fill(new Ellipse2D.Double(28 + deslocamento / 3, 19, 4, 4));
    }

    private static void asteroide(Graphics2D g) {
        Path2D rocha = new Path2D.Double();
        rocha.moveTo(10, 14);
        rocha.lineTo(22, 6);
        rocha.lineTo(36, 10);
        rocha.lineTo(42, 24);
        rocha.lineTo(36, 40);
        rocha.lineTo(20, 42);
        rocha.lineTo(8, 32);
        rocha.closePath();
        g.setColor(new Color(0x7B7F87));
        g.fill(rocha);
        g.setColor(new Color(0x4A4D54));
        g.fill(new Ellipse2D.Double(16, 16, 8, 8));
        g.fill(new Ellipse2D.Double(28, 26, 6, 6));
        g.setStroke(new BasicStroke(1.5f));
        g.draw(rocha);
    }

    private static void pessoa(Graphics2D g, Color traje, boolean oculos) {
        g.setColor(traje);
        g.fill(new RoundRectangle2D.Double(14, 24, 20, 20, 6, 6));
        g.setColor(PELE);
        g.fill(new Ellipse2D.Double(16, 6, 16, 16));
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(1.2f));
        g.draw(new Ellipse2D.Double(16, 6, 16, 16));
        if (oculos) {
            g.draw(new Ellipse2D.Double(18, 11, 5, 5));
            g.draw(new Ellipse2D.Double(25, 11, 5, 5));
        } else {
            g.setColor(Color.BLACK);
            g.fill(new Ellipse2D.Double(20, 12, 2.5, 2.5));
            g.fill(new Ellipse2D.Double(26, 12, 2.5, 2.5));
        }
    }

    private static void generica(Graphics2D g) {
        g.setColor(new Color(0xB04BC9));
        g.fill(new RoundRectangle2D.Double(8, 8, 32, 32, 8, 8));
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(3f));
        g.draw(new java.awt.geom.Line2D.Double(18, 18, 30, 30));
        g.draw(new java.awt.geom.Line2D.Double(30, 18, 18, 30));
    }

    private static void explosao(Graphics2D g, int quadro) {
        double[] raios = {8, 14, 20, 23};
        double r = raios[quadro];
        g.setColor(new Color(LARANJA.getRed(), LARANJA.getGreen(), LARANJA.getBlue(), 255 - quadro * 40));
        g.fill(new Ellipse2D.Double(24 - r, 24 - r, 2 * r, 2 * r));
        g.setColor(new Color(AMARELO.getRed(), AMARELO.getGreen(), AMARELO.getBlue(), 255 - quadro * 50));
        double menor = r * 0.6;
        g.fill(new Ellipse2D.Double(24 - menor, 24 - menor, 2 * menor, 2 * menor));
    }

    private static void coracao(Graphics2D g, Color cor, boolean cheio) {
        Path2D coracao = new Path2D.Double();
        coracao.moveTo(12, 21);
        coracao.curveTo(0, 12, 2, 2, 8, 3);
        coracao.curveTo(10, 3.5, 12, 6, 12, 7);
        coracao.curveTo(12, 6, 14, 3.5, 16, 3);
        coracao.curveTo(22, 2, 24, 12, 12, 21);
        coracao.closePath();
        g.setColor(cor);
        if (cheio) {
            g.fill(coracao);
        } else {
            g.setStroke(new BasicStroke(2f));
            g.draw(coracao);
        }
    }

    private static void vaga(Graphics2D g, boolean ocupada) {
        Color cor = ocupada ? VERDE : CINZA_ESCURO;
        g.setColor(cor);
        if (ocupada) {
            g.fill(new Ellipse2D.Double(3, 3, 18, 18));
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2f));
            g.draw(new java.awt.geom.Line2D.Double(8, 12, 11, 16));
            g.draw(new java.awt.geom.Line2D.Double(11, 16, 17, 8));
        } else {
            g.setStroke(new BasicStroke(2f));
            g.draw(new Ellipse2D.Double(3, 3, 18, 18));
        }
    }
}
