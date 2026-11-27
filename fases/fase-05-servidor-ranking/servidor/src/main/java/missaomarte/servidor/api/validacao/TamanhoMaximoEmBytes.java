package missaomarte.servidor.api.validacao;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** O texto, em UTF-8, não pode passar de {@code value} bytes. */
@Documented
@Constraint(validatedBy = TamanhoMaximoEmBytesValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface TamanhoMaximoEmBytes {

    int value();

    String message() default "deve ter no máximo {value} bytes em UTF-8";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
