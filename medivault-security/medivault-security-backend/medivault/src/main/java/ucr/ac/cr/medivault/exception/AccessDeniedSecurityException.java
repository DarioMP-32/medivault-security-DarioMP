package ucr.ac.cr.medivault.exception;

public class AccessDeniedSecurityException extends RuntimeException {

    private final Long recursoId;

    public AccessDeniedSecurityException(String msg) {
        this(msg, null);
    }

    public AccessDeniedSecurityException(String msg, Long recursoId) {
        super(msg);
        this.recursoId = recursoId;
    }

    public Long getRecursoId() {
        return recursoId;
    }
}