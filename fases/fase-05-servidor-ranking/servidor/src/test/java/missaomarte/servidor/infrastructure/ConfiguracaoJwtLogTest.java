package missaomarte.servidor.infrastructure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class ConfiguracaoJwtLogTest {

    @Test
    void r6_2_segredoAleatorioAvisaNoLogQueOsTokensDeixamDeValerAoReiniciar(CapturedOutput saida) {
        new ConfiguracaoJwt().segredoJwt("");

        assertTrue(saida.getAll().contains("MISSAOMARTE_JWT_SEGREDO não definido"));
        assertTrue(saida.getAll().contains("Os tokens deixam de valer quando o servidor reiniciar"));
    }

    @Test
    void r6_4_segredoValidoNaoGeraAvisoNemApareceNoLog(CapturedOutput saida) {
        String valor = "segredo-de-teste-com-mais-de-32-bytes-ok";

        new ConfiguracaoJwt().segredoJwt(valor);

        assertFalse(saida.getAll().contains("deixam de valer"));
        assertFalse(saida.getAll().contains(valor));
    }
}
