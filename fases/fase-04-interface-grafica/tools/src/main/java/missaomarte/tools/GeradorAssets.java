package missaomarte.tools;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import missaomarte.gdx.catalogo.Imagem;

/** Gera todas as imagens e o CREDITOS.md numa pasta (por padrão, core/assets). */
public final class GeradorAssets {

    private GeradorAssets() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("uso: GeradorAssets <pasta de saída>");
            System.exit(1);
        }
        System.setProperty("java.awt.headless", "true");
        Path pasta = Paths.get(args[0]);
        gerar(pasta);
        System.out.println("Assets gerados em " + pasta);
    }

    /** (Re)escreve todos os arquivos do catálogo e os créditos em {@code pasta}. */
    public static void gerar(Path pasta) throws IOException {
        System.setProperty("java.awt.headless", "true");
        Files.createDirectories(pasta);
        for (Imagem imagem : Imagem.values()) {
            GeradorImagens.gravar(imagem, pasta);
        }
        Files.writeString(pasta.resolve("CREDITOS.md"), creditos(), StandardCharsets.UTF_8);
    }

    static String creditos() {
        StringBuilder texto = new StringBuilder();
        texto.append("# Créditos dos assets\n\n");
        texto.append("Todas as imagens e sons deste jogo foram gerados por programa próprio ")
                .append("(módulo `tools`), sem arte ou áudio de terceiros.\n\n");
        texto.append("Para gerá-los de novo, na pasta da fase:\n\n");
        texto.append("```\n./gradlew :tools:gerarAssets\n```\n\n");
        texto.append("## Imagens\n\n");
        for (Imagem imagem : Imagem.values()) {
            texto.append("- `").append(imagem.caminho()).append("`\n");
        }
        return texto.toString();
    }
}
