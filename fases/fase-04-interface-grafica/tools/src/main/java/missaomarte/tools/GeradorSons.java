package missaomarte.tools;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import missaomarte.gdx.catalogo.Som;

/**
 * Sintetiza os efeitos e a música do catálogo e escreve WAV PCM mono 22 050 Hz
 * 16 bits. Nada aleatório sem semente fixa: a mesma execução gera os mesmos bytes.
 */
public final class GeradorSons {

    public static final int TAXA = 22050;
    private static final double PICO_EFEITO = 0.80;
    private static final double PICO_MUSICA = 0.35;
    private static final long SEMENTE = 20261126L;

    private GeradorSons() {
    }

    /** Amostras do som, já começando e terminando em zero. */
    public static short[] sintetizar(Som som) {
        double[] onda = switch (som) {
            case MOVER -> tom(440, 520, 0.08, false);
            case SELECIONAR -> juntar(tom(660, 660, 0.05, true), tom(880, 880, 0.07, true));
            case EMBARCAR -> juntar(tom(392, 392, 0.09, false), tom(523, 523, 0.09, false),
                    tom(659, 659, 0.12, false));
            case ENTREGAR -> juntar(tom(523, 523, 0.09, true), tom(659, 659, 0.09, true),
                    tom(784, 784, 0.09, true), tom(1047, 1047, 0.18, true));
            case COLISAO -> misturar(ruido(0.45), tom(140, 50, 0.45, false));
            case VITORIA -> juntar(tom(523, 523, 0.14, true), tom(659, 659, 0.14, true),
                    tom(784, 784, 0.14, true), tom(1047, 1047, 0.5, true));
            case DERROTA -> juntar(tom(392, 392, 0.2, false), tom(330, 330, 0.2, false),
                    tom(262, 262, 0.2, false), tom(196, 196, 0.6, false));
            case MUSICA -> musica();
        };
        return converter(onda, som.musica() ? PICO_MUSICA : PICO_EFEITO);
    }

    /** Gera o som e grava em {@code pasta/<caminho do catálogo>}. */
    public static void gravar(Som som, Path pasta) throws IOException {
        Path arquivo = pasta.resolve(som.caminho());
        Files.createDirectories(arquivo.getParent());
        gravarWav(sintetizar(som), arquivo);
    }

    /** Escreve o cabeçalho RIFF/fmt/data à mão (PCM, mono, 22 050 Hz, 16 bits, little endian). */
    public static void gravarWav(short[] amostras, Path arquivo) throws IOException {
        int tamanho = amostras.length * 2;
        ByteBuffer b = ByteBuffer.allocate(44 + tamanho).order(ByteOrder.LITTLE_ENDIAN);
        b.put(new byte[] {'R', 'I', 'F', 'F'});
        b.putInt(36 + tamanho);
        b.put(new byte[] {'W', 'A', 'V', 'E'});
        b.put(new byte[] {'f', 'm', 't', ' '});
        b.putInt(16);
        b.putShort((short) 1);
        b.putShort((short) 1);
        b.putInt(TAXA);
        b.putInt(TAXA * 2);
        b.putShort((short) 2);
        b.putShort((short) 16);
        b.put(new byte[] {'d', 'a', 't', 'a'});
        b.putInt(tamanho);
        for (short amostra : amostras) {
            b.putShort(amostra);
        }
        Files.write(arquivo, b.array());
    }

    // ---- síntese ----

    /** Tom de frequência linear de {@code f0} a {@code f1}, com ataque curto e queda linear. */
    private static double[] tom(double f0, double f1, double duracao, boolean quadrada) {
        int n = (int) (duracao * TAXA);
        double[] saida = new double[n];
        double fase = 0;
        int ataque = Math.max(1, (int) (0.005 * TAXA));
        for (int i = 0; i < n; i++) {
            double t = (double) i / n;
            fase += 2 * Math.PI * (f0 + (f1 - f0) * t) / TAXA;
            double valor = quadrada ? Math.signum(Math.sin(fase)) * 0.6 : Math.sin(fase);
            double envelope = Math.min(1.0, (double) i / ataque) * (1.0 - t);
            saida[i] = valor * envelope;
        }
        return saida;
    }

    private static double[] ruido(double duracao) {
        Random sorteio = new Random(SEMENTE);
        int n = (int) (duracao * TAXA);
        double[] saida = new double[n];
        for (int i = 0; i < n; i++) {
            double t = (double) i / n;
            saida[i] = (sorteio.nextDouble() * 2 - 1) * (1.0 - t);
        }
        return saida;
    }

    private static double[] juntar(double[]... partes) {
        int total = 0;
        for (double[] parte : partes) {
            total += parte.length;
        }
        double[] saida = new double[total];
        int posicao = 0;
        for (double[] parte : partes) {
            System.arraycopy(parte, 0, saida, posicao, parte.length);
            posicao += parte.length;
        }
        return saida;
    }

    private static double[] misturar(double[] a, double[] b) {
        double[] saida = new double[Math.max(a.length, b.length)];
        for (int i = 0; i < a.length; i++) {
            saida[i] += a[i];
        }
        for (int i = 0; i < b.length; i++) {
            saida[i] += b[i];
        }
        return saida;
    }

    /** Música de 8 s: 16 notas de melodia (quadrada) sobre um baixo (senoidal) de 4 notas. */
    private static double[] musica() {
        double[] melodia = {
            523.25, 587.33, 659.25, 783.99, 659.25, 587.33, 523.25, 392.00,
            440.00, 523.25, 587.33, 659.25, 587.33, 523.25, 440.00, 392.00};
        double[] baixo = {130.81, 110.00, 87.31, 98.00};
        double[][] notas = new double[melodia.length][];
        for (int i = 0; i < melodia.length; i++) {
            notas[i] = tom(melodia[i], melodia[i], 0.5, true);
        }
        double[][] graves = new double[baixo.length][];
        for (int i = 0; i < baixo.length; i++) {
            graves[i] = tom(baixo[i], baixo[i], 2.0, false);
        }
        double[] grave = juntar(graves);
        double[] mistura = misturar(juntar(notas), grave);
        return mistura;
    }

    /** Normaliza para o pico pedido e zera a primeira e a última amostra. */
    private static short[] converter(double[] onda, double pico) {
        double maximo = 0;
        for (double v : onda) {
            maximo = Math.max(maximo, Math.abs(v));
        }
        double escala = maximo == 0 ? 0 : pico * Short.MAX_VALUE / maximo;
        short[] saida = new short[onda.length];
        for (int i = 0; i < onda.length; i++) {
            saida[i] = (short) Math.round(onda[i] * escala);
        }
        saida[0] = 0;
        saida[saida.length - 1] = 0;
        return saida;
    }
}
