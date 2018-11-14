public class DivisionByZeroException extends Exception {
    private final Complex numerator;

    public DivisionByZeroException(Complex numerator) {
        this.numerator = new Complex(numerator);
    }

    public Complex getNumerator() {
        return numerator;
    }
}
