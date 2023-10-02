package com.st4s1k.ctt.model;

// credit: https://gist.github.com/derlin/40545e447fffe7599d26d0a435d9b113

import lombok.AllArgsConstructor;
import lombok.Value;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;

@Value
@AllArgsConstructor
public class Polynomial {

    private static final DecimalFormat COEFFICIENT_FORMAT = new DecimalFormat("#.###");

    double[] coefficients;

    private Polynomial() {
        coefficients = new double[1];
        Arrays.fill(coefficients, 0);
        coefficients[0] = 0;
    }

    private Polynomial(double coefficient) {
        coefficients = new double[1];
        Arrays.fill(coefficients, 0);
        coefficients[0] = coefficient;
    }

    private Polynomial(int degree) {
        coefficients = new double[degree + 1];
        Arrays.fill(coefficients, 0);
    }

    private Polynomial(double coefficient, int degree) {
        this.coefficients = new double[degree + 1];
        Arrays.fill(coefficients, 0);
        this.coefficients[degree] = coefficient;
    }

    private Polynomial(Polynomial other) {
        coefficients = new double[other.coefficients.length];
        System.arraycopy(other.coefficients, 0, this.coefficients, 0, other.coefficients.length);
    }

    public static Polynomial zero() {
        return new Polynomial();
    }

    public static Polynomial of(double coefficient) {
        return new Polynomial(coefficient);
    }

    public static Polynomial of(int degree) {
        return new Polynomial(degree);
    }

    public static Polynomial of(double coefficient, int degree) {
        return new Polynomial(coefficient, degree);
    }

    public int degree() {
        int d = 0;
        for (int exponent = 0; exponent < coefficients.length; exponent++) {
            if (coefficients[exponent] != 0) {
                d = exponent;
            }
        }
        return d;
    }

    public double coefficient() {
        return coefficient(degree());
    }

    public double coefficient(int degree) {
        if (degree > coefficients.length) {
            throw new ArithmeticException("The degree of the polynomial cannot exceed " + coefficients.length);
        }
        return coefficients[degree];
    }

    public Polynomial withCoefficient(double coefficient, int degree) {
        Polynomial polynomial = copy();
        polynomial.coefficients[degree] = coefficient;
        return polynomial;
    }

    public Polynomial plus(Polynomial other) {
        Polynomial sum = new Polynomial(Math.max(this.degree(), other.degree()));
        for (int thisExp = 0; thisExp <= this.degree(); thisExp++) {
            sum = sum.withCoefficient(sum.coefficient(thisExp) + this.coefficients[thisExp], thisExp);
        }
        for (int otherExp = 0; otherExp <= other.degree(); otherExp++) {
            sum = sum.withCoefficient(sum.coefficient(otherExp) + other.coefficients[otherExp], otherExp);
        }
        return sum;
    }

    public Polynomial plus(double coefficient, int degree) {
        Polynomial polynomial = new Polynomial(coefficient, degree);
        return plus(polynomial);
    }

    public Polynomial plus(double coefficient) {
        return plus(coefficient, 0);
    }

    public Polynomial minus(Polynomial other) {
        Polynomial difference = new Polynomial(Math.max(this.degree(), other.degree()));
        for (int thisExp = 0; thisExp <= this.degree(); thisExp++) {
            difference = difference.withCoefficient(difference.coefficients[thisExp] + this.coefficients[thisExp], thisExp);
        }
        for (int otherExp = 0; otherExp <= other.degree(); otherExp++) {
            difference = difference.withCoefficient(difference.coefficients[otherExp] - other.coefficients[otherExp], otherExp);
        }
        return difference;
    }

    public Polynomial minus(double coefficient, int degree) {
        Polynomial polynomial = new Polynomial(coefficient, degree);
        return minus(polynomial);
    }

    public Polynomial minus(double coefficient) {
        return minus(coefficient, 0);
    }

    public Polynomial times(Polynomial other) {
        Polynomial product = new Polynomial(this.degree() + other.degree());
        for (int thisExp = 0; thisExp <= this.degree(); thisExp++) {
            for (int otherExp = 0; otherExp <= other.degree(); otherExp++) {
                double newCoefficient = product.coefficients[thisExp + otherExp] +
                        (coefficients[thisExp] * other.coefficients[otherExp]);
                product = product.withCoefficient(newCoefficient, thisExp + otherExp);
            }
        }
        return product;
    }

    public Polynomial times(double coefficient, int degree) {
        Polynomial polynomial = new Polynomial(coefficient, degree);
        return times(polynomial);
    }

    public Polynomial times(double coefficient) {
        Polynomial polynomial = new Polynomial(coefficient, 0);
        return times(polynomial);
    }

    /**
     * Implement the division as described in wikipedia:
     * <pre>
     *   function n / d:
     *     require d ≠ 0
     *     q ← 0
     *     r ← n       # At each step n = d × q + r
     *     while r ≠ 0 AND degree(r) ≥ degree(d):
     *          t ← lead(r)/lead(d)     # Divide the leading terms
     *          q ← q + t
     *          r ← r − t * d
     *     return (q, r)
     * </pre>
     */
    public Polynomial[] div(Polynomial other) {
        Polynomial q = new Polynomial(0);
        Polynomial r = this;
        while (r.isNotZero() && r.degree() >= other.degree()) {
            Polynomial t = new Polynomial(
                    r.coefficient() / other.coefficient(),
                    r.degree() - other.degree()
            );
            q = q.plus(t);
            r = r.minus(t.times(other));
        }
        return new Polynomial[]{q, r};
    }

    public Polynomial[] div(double coefficient, int degree) {
        Polynomial polynomial = new Polynomial(coefficient, degree);
        return div(polynomial);
    }

    public Polynomial[] div(double coefficient) {
        return div(coefficient, 0);
    }

    public boolean isZero() {
        for (double coefficient : coefficients) {
            if (coefficient != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isNotZero() {
        return !isZero();
    }

    public boolean isConstant() {
        return degree() == 0;
    }

    /**
     * use Horner's method to compute and return the polynomial evaluated at x
     */
    public double evaluate(double x) {
        double result = 0;
        for (int exponent = degree(); exponent >= 0; exponent--) {
            result = coefficients[exponent] + (x * result);
        }
        return result;
    }

    public Complex evaluate(Complex z) {
        Complex result = new Complex();
        for (int exponent = 0; exponent <= degree(); exponent++) {
            if (coefficients[exponent] != 0) {
                result = result.plus(z.pow(exponent).times(coefficients[exponent]));
            }
        }
        return result;
    }

    /**
     * return this(p(x)) - compute using Horner's method
     */
    public Polynomial evaluate(Polynomial other) {
        Polynomial result = new Polynomial(0);
        for (int exponent = this.degree(); exponent >= 0; exponent--) {
            result = new Polynomial(coefficients[exponent], 0).plus(other.times(result));
        }
        return result;
    }

    /**
     * differentiate this polynomial and return it
     */
    public Polynomial differentiate() {
        if (degree() == 0) {
            return new Polynomial(0);
        }
        Polynomial derivative = new Polynomial(degree() - 1);
        for (int i = 0; i < degree(); i++) {
            derivative = derivative.withCoefficient((i + 1) * coefficients[i + 1], i);
        }
        return derivative;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int exponent = degree(); exponent >= 0; exponent--) {
            if (coefficients[exponent] == 0 && degree() != 0) {
                continue;
            }
            buildPolynomialString(exponent, sb);
        }
        return sb.toString();
    }

    private void buildPolynomialString(int exponent, StringBuilder sb) {
        if (exponent < degree() && coefficients[exponent] > 0) {
            sb.append(" + ");
        }
        if (coefficients[exponent] < 0) {
            sb.append(exponent == degree() ? "-" : " - ");
        }
        if (Math.abs(coefficients[exponent]) != 1 || exponent == 0) {
            sb.append(COEFFICIENT_FORMAT.format(Math.abs(coefficients[exponent])));
        }
        if (exponent > 0) {
            sb.append("s").append(exponent > 1 ? "^" + exponent : "");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Polynomial other)) {
            return false;
        }
        if (this.degree() != other.degree()) {
            return false;
        }
        for (int exponent = this.degree(); exponent >= 0; exponent--) {
            if (this.coefficients[exponent] != other.coefficients[exponent]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(degree());
        result = 31 * result + Arrays.hashCode(coefficients);
        return result;
    }

    public Polynomial copy() {
        return new Polynomial(this);
    }

    public Double doubleValue() {
        if (!isConstant()) {
            throw new ArithmeticException("Polynomial is not a constant");
        }
        return coefficient();
    }
}
