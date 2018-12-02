package exceptions;

import lombok.Getter;

@Getter
public class InvalidVariableException extends Exception {
    private final String variable;

    public InvalidVariableException(String message, String variable) {
        super(message);
        this.variable = variable;
    }
}
