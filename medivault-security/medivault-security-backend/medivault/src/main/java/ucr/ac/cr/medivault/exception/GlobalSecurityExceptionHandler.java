package ucr.ac.cr.medivault.exception;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalSecurityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalSecurityExceptionHandler.class);

    private ProblemDetail build(HttpStatus status, String title, String detail, String slug) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(status, detail);
        p.setTitle(title);
        p.setType(URI.create("https://medivault.ucr.ac.cr/" + slug));
        return p;
    }

    @ExceptionHandler(AccessDeniedSecurityException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedSecurityException ex) {
        return build(HttpStatus.FORBIDDEN, "Acceso Prohibido (IDOR / BOLA)", ex.getMessage(), "forbidden");
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ProblemDetail handleSpringAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "Acceso Prohibido", "No tiene permisos para esta operacion.", "forbidden");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Recurso No Encontrado", ex.getMessage(), "not-found");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Fallo de Autenticacion", "Credenciales invalidas.", "unauthorized");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail p = build(HttpStatus.BAD_REQUEST, "Error de Validacion",
                "Validacion de datos fallida.", "bad-request");
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        p.setProperty("invalid_fields", errors);
        return p;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, "Solicitud Invalida",
                "El cuerpo de la solicitud es invalido.", "bad-request");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, "Parametro Invalido",
                "Parametro con formato invalido.", "bad-request");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        log.error("Error no controlado", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error Interno", "Error interno del servidor.", "internal");
    }
}