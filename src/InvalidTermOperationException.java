import lombok.Getter;

@Getter
public class InvalidTermOperationException extends Exception {
    private final String term;
    private final String operation;

    public InvalidTermOperationException(String message, String term, String operation) {
        super(message);
        this.term = term;
        this.operation = operation;
    }
}
