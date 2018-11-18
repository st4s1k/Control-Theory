public class DivisionByZeroException extends Exception {
    private final String numerator;
    private final String denominator;

    public DivisionByZeroException(String numerator, String denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public String getMessage() {
        return "Trouble dividing (" + numerator + ") by (" + denominator + ")";
    }
}
