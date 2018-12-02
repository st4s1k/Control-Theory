import lombok.Getter;

@Getter
public class BadVariableExponentException extends Exception {
    private final String variable;
    private final String degree;

    public BadVariableExponentException(String message, String variable, String degree) {
        super(message);
        this.variable = variable;
        this.degree = degree;
    }
}
