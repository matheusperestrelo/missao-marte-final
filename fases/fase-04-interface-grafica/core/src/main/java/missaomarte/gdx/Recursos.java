package missaomarte.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import missaomarte.gdx.catalogo.Imagem;
import missaomarte.gdx.catalogo.Som;

/**
 * Recursos do jogo (imagens e sons do catálogo), carregados uma vez pelo
 * {@link MissaoMarteGame}. As telas só pedem texturas aqui; não criam nem
 * liberam nada. Exige contexto OpenGL, por isso não é instanciado em testes.
 */
public class Recursos {

    private final AssetManager assetManager = new AssetManager();

    /** Carrega todo o catálogo de forma síncrona; arquivo ausente lança erro com o caminho. */
    public void carregar() {
        for (Imagem imagem : Imagem.values()) {
            assetManager.load(imagem.caminho(), Texture.class);
        }
        for (Som som : Som.values()) {
            if (som.musica()) {
                assetManager.load(som.caminho(), Music.class);
            } else {
                assetManager.load(som.caminho(), Sound.class);
            }
        }
        assetManager.finishLoading();
        Gdx.app.log("Recursos", assetManager.getAssetNames().size + " arquivos carregados");
    }

    public Texture textura(Imagem imagem) {
        return assetManager.get(imagem.caminho(), Texture.class);
    }

    /** Recorte do quadro {@code i} (da esquerda para a direita) da faixa da imagem. */
    public TextureRegion quadro(Imagem imagem, int i) {
        return new TextureRegion(textura(imagem), i * imagem.larguraQuadro(), 0,
                imagem.larguraQuadro(), imagem.alturaQuadro());
    }

    public Sound som(Som som) {
        return assetManager.get(som.caminho(), Sound.class);
    }

    public Music musica() {
        return assetManager.get(Som.MUSICA.caminho(), Music.class);
    }

    public void dispose() {
        assetManager.dispose();
    }
}
