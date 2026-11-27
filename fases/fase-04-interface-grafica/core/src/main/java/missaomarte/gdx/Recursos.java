package missaomarte.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import missaomarte.gdx.catalogo.Imagem;

/**
 * Recursos do jogo (imagens do catálogo), carregados uma vez pelo
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

    public void dispose() {
        assetManager.dispose();
    }
}
