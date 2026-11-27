package missaomarte.tools;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

/** Leitura mínima de um WAV PCM gerado por {@link GeradorSons} (para os testes). */
public record Wav(int formato, int canais, int taxa, int bits, short[] amostras, byte[] dados) {

    public static Wav ler(Path arquivo) throws IOException {
        byte[] bytes = Files.readAllBytes(arquivo);
        ByteBuffer b = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        if (bytes.length < 44 || b.getInt(0) != 0x46464952 || b.getInt(8) != 0x45564157) {
            throw new IOException("não é um WAV RIFF: " + arquivo);
        }
        int formato = b.getShort(20);
        int canais = b.getShort(22);
        int taxa = b.getInt(24);
        int bits = b.getShort(34);
        int tamanho = b.getInt(40);
        byte[] dados = new byte[tamanho];
        System.arraycopy(bytes, 44, dados, 0, tamanho);
        short[] amostras = new short[tamanho / 2];
        ByteBuffer.wrap(dados).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(amostras);
        return new Wav(formato, canais, taxa, bits, amostras, dados);
    }

    public double duracao() {
        return (double) amostras.length / taxa;
    }

    public int pico() {
        int pico = 0;
        for (short s : amostras) {
            pico = Math.max(pico, Math.abs((int) s));
        }
        return pico;
    }
}
