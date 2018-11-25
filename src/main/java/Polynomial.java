// Credit: https://gist.github.com/derlin/40545e447fffe7599d26d0a435d9b113
// see http://introcs.cs.princeton.edu/java/92symbolic/Polynomial.java.html

import java.text.DecimalFormat;
import java.util.Arrays;

public class Polynomial {

    private double[] coef;
    private char sym = 'x';

    public Polynomial(double c0, double ... coef) {
        this.coef = new double[coef.length + 1];
        this.coef[0] = c0;
        System.arraycopy(coef, 0, this.coef, 1, coef.length);
    }

    public Polynomial(Polynomial polynomial) {
        coef = new double[polynomial.coef.length];
        System.arraycopy(polynomial.coef, 0, coef, 0, polynomial.coef.length);
        sym = polynomial.sym;
    }

    public static Polynomial term(double coef, int deg) {
        Polynomial monomial = new Polynomial(0, new double[deg]);
        monomial.coef[deg] = coef;
        return monomial;
    }

    public Polynomial setSym(char sym) {
        this.sym = sym;
        return this;
    }

    public boolean isZero() {
        return coef == null || coef.length == 0 || coeff() == 0;
    }

    public int degree() {
        int deg = 0;
        for (int i = 0; i < coef.length; i++)
            if (coef[i] != 0) deg = i;
        return deg;
    }

    // get the coefficient for the highest deg
    public double coeff() { return coeff(degree()); }


    // get the coefficient for deg d
    public double coeff(int deg) {
        return deg > this.degree() ? 0 : coef[deg];
    }

    public Polynomial plus(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, new double[Math.max(a.degree(), b.degree())]);
        for (int i = 0; i <= a.degree(); i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.degree(); i++) c.coef[i] += b.coef[i];
        return c;
    }

    // return (a - b)
    public Polynomial minus(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, new double[Math.max(a.degree(), b.degree())]);
        for (int i = 0; i <= a.degree(); i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.degree(); i++) c.coef[i] -= b.coef[i];
        return c;
    }


    // return (a * b)
    public Polynomial times(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0, new double[a.degree() + b.degree()]);
        for (int i = 0; i <= a.degree(); i++)
            for (int j = 0; j <= b.degree(); j++)
                c.coef[i + j] += (a.coef[i] * b.coef[j]);
        return c;
    }

    /*
     Implement the division as described in wikipedia
        function n / d:
          require d ≠ 0
          q ← 0
          r ← n       # At each step n = d × q + r
          while r ≠ 0 AND degree(r) ≥ degree(d):
          t ← lead(r)/lead(d)     # Divide the leading terms
          q ← q + t
          r ← r − t * d
          return (q, r)
     */

    public Polynomial[] div(Polynomial polynomial) throws DivisionByZeroException {
        if (polynomial.isZero())
            throw new DivisionByZeroException(
                    "The denominator-polynomial is zero.",
                    this.toString(),
                    polynomial.toString()
            );
        Polynomial q = new Polynomial(0);
        Polynomial r = new Polynomial(this);
        while(!r.isZero() && r.degree() >= polynomial.degree()) {
            double k = r.coeff() / polynomial.coeff();
            int deg = r.degree() - polynomial.degree();
            Polynomial t = Polynomial.term(k, deg);
            q = q.plus(t);
            r = r.minus(t.times(polynomial));
        }
        return new Polynomial[]{ q, r };
    }

    public Polynomial div(double n) throws DivisionByZeroException {
        return div(Polynomial.term(n, 0))[0];
    }


    // use Horner's method to compute and return the polynomial evaluated at x
    public double evaluate(double n) {
        return Arrays.stream(coef).reduce(0, (result, k) -> result = k + (n * result));
    }

    public Complex evaluate(Complex z) {
        Complex s = new Complex();
        for (int i = 0; i <= this.degree(); i++)
            if (coef[i] != 0)
                s = s.plus(z.pow(i).times(coef[i]));
        return s;
    }

    // return this(polynomial(x)) - compute using Horner's method
    public Polynomial evaluate(Polynomial polynomial) {
        Polynomial result = new Polynomial(0);
        for (int i = this.degree(); i >= 0; i--)
            result = Polynomial.term(coef[i], 0).plus(polynomial.times(result));
        return result;
    }

    // differentiate this polynomial and return it
    public Polynomial differentiate() {
        if (this.degree() == 0) return new Polynomial(0);
        Polynomial deriv = new Polynomial(0, new double[this.degree() - 1]);
        for (int i = 0; i < this.degree(); i++)
            deriv.coef[i] = (i + 1) * coef[i + 1];
        return deriv;
    }

    // convert to string representation

    public String toString() {
        DecimalFormat coefFormat = new DecimalFormat("#.####");

        StringBuilder s = new StringBuilder();

        for (int i = this.degree(); i >= 0; i--)
            if (coef[i] != 0) {
                if (i < this.degree())
                    s.append(coef[i] < 0 ? " - " : " + ");
                else if (coef[i] < 0)
                    s.append("-");
                if (i > 0) {
                    if (Math.abs(coef[i]) != 1)
                        s.append(coefFormat.format(Math.abs(coef[i])));
                    s.append(sym);
                    if (i > 1)
                        s.append("^").append(i);
                } else s.append(coefFormat.format(Math.abs(coef[i])));
            }

        return s.toString();
    }
}