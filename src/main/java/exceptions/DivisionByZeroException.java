package exceptions;

import lombok.Getter;

@Getter
public class DivisionByZeroException extends Exception {
    private final String numerator;
    private final String denominator;

    public DivisionByZeroException(String message, String numerator, String denominator) {
        super(message);
        this.numerator = numerator;
        this.denominator = denominator;
    }
}
