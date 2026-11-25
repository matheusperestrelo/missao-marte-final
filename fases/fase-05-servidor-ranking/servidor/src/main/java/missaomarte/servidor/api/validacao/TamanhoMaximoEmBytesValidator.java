package missaomarte.servidor.api.validacao;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.nio.charset.StandardCharsets;

public class TamanhoMaximoEmBytesValidator implements ConstraintValidator<TamanhoMaximoEmBytes, String> {

    private int maximo;

    @Override
    public void initialize(TamanhoMaximoEmBytes anotacao) {
        this.maximo = anotacao.value();
    }

    @Override
    public boolean isValid(String valor, ConstraintValidatorContext contexto) {
        return valor == null || valor.getBytes(StandardCharsets.UTF_8).length <= maximo;
    }
}
