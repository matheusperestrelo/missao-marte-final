package missaomarte.servidor.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import missaomarte.servidor.application.CredenciaisInvalidasException;
import missaomarte.servidor.application.NomeJaExisteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class TratadorDeErros extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TratadorDeErros.class);

    public record ErroDeCampo(String campo, String mensagem) {
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        // um item por campo (o primeiro); o valor rejeitado nunca entra no corpo
        Map<String, String> porCampo = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            porCampo.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }
        List<ErroDeCampo> erros = porCampo.entrySet().stream()
                .map(e -> new ErroDeCampo(e.getKey(), e.getValue()))
                .toList();
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Dados inválidos.");
        problema.setTitle("Requisição inválida");
        problema.setProperty("erros", erros);
        return handleExceptionInternal(ex, problema, headers, status, request);
    }

    @ExceptionHandler(NomeJaExisteException.class)
    ProblemDetail nomeJaExiste(NomeJaExisteException e) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problema.setTitle("Conflito");
        return problema;
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    ResponseEntity<ProblemDetail> credenciaisInvalidas(CredenciaisInvalidasException e) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problema.setTitle("Não autorizado");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problema);
    }

    /** Falha de autenticação dentro do MVC (ex.: usuário do token não existe): 401, nunca 500. */
    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<ProblemDetail> naoAutenticado(AuthenticationException e) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                EntradaNaoAutenticada.DETALHE);
        problema.setTitle("Não autorizado");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer")
                .body(problema);
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail inesperado(Exception e) {
        LOG.error("Erro inesperado: {}", e.getClass().getName());
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor.");
        problema.setTitle("Erro interno");
        return problema;
    }
}
