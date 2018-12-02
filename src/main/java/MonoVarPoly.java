// Credit: https://gist.github.com/derlin/40545e447fffe7599d26d0a435d9b113
// see http://introcs.cs.princeton.edu/java/92symbolic/Polynomial.java.html

import exceptions.DivisionByZeroException;
import org.apache.commons.math3.complex.Complex;

import java.text.DecimalFormat;
import java.util.Arrays;

public class MonoVarPoly {

    private double[] coef;
    private char sym = 'x';

    public MonoVarPoly(double c0, double ... coef) {
        this.coef = new double[coef.length + 1];
        this.coef[0] = c0;
        System.arraycopy(coef, 0, this.coef, 1, coef.length);
    }

    public MonoVarPoly(MonoVarPoly monoVarPoly) {
        coef = new double[monoVarPoly.coef.length];
        System.arraycopy(monoVarPoly.coef, 0, coef, 0, monoVarPoly.coef.length);
        sym = monoVarPoly.sym;
    }

    public static MonoVarPoly term(double coef, int deg) {
        MonoVarPoly monomial = new MonoVarPoly(0, new double[deg]);
        monomial.coef[deg] = coef;
        return monomial;
    }

    public MonoVarPoly setSym(char sym) {
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

    public MonoVarPoly add(MonoVarPoly b) {
        MonoVarPoly a = this;
        MonoVarPoly c = new MonoVarPoly(0, new double[Math.max(a.degree(), b.degree())]);
        for (int i = 0; i <= a.degree(); i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.degree(); i++) c.coef[i] += b.coef[i];
        return c;
    }

    // return (a - b)
    public MonoVarPoly minus(MonoVarPoly b) {
        MonoVarPoly a = this;
        MonoVarPoly c = new MonoVarPoly(0, new double[Math.max(a.degree(), b.degree())]);
        for (int i = 0; i <= a.degree(); i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.degree(); i++) c.coef[i] -= b.coef[i];
        return c;
    }


    // return (a * b)
    public MonoVarPoly multiply(MonoVarPoly b) {
        MonoVarPoly a = this;
        MonoVarPoly c = new MonoVarPoly(0, new double[a.degree() + b.degree()]);
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

    public MonoVarPoly[] div(MonoVarPoly monoVarPoly) throws DivisionByZeroException {
        if (monoVarPoly.isZero())
            throw new DivisionByZeroException(
                    "The denominator-monoVarPoly is zero.",
                    this.toString(),
                    monoVarPoly.toString()
            );
        MonoVarPoly q = new MonoVarPoly(0);
        MonoVarPoly r = new MonoVarPoly(this);
        while(!r.isZero() && r.degree() >= monoVarPoly.degree()) {
            double k = r.coeff() / monoVarPoly.coeff();
            int deg = r.degree() - monoVarPoly.degree();
            MonoVarPoly t = MonoVarPoly.term(k, deg);
            q = q.add(t);
            r = r.minus(t.multiply(monoVarPoly));
        }
        return new MonoVarPoly[]{ q, r };
    }

    public MonoVarPoly div(double n) throws DivisionByZeroException {
        return div(MonoVarPoly.term(n, 0))[0];
    }


    // use Horner's method to compute and return the polynomial evaluated at x
    public double evaluate(double n) {
        return Arrays.stream(coef).reduce(0, (result, k) -> result = k + (n * result));
    }

    public Complex evaluate(Complex z) {
        Complex s = new Complex(0);
        for (int i = 0; i <= this.degree(); i++)
            if (coef[i] != 0)
                s = s.add(z.pow(i).multiply(coef[i]));
        return s;
    }

    public MonoVarPoly evaluate(MonoVarPoly monoVarPoly) {
        MonoVarPoly result = new MonoVarPoly(0);
        for (int i = this.degree(); i >= 0; i--)
            result = MonoVarPoly.term(coef[i], 0).add(monoVarPoly.multiply(result));
        return result;
    }

    public MonoVarPoly differentiate() {
        if (this.degree() == 0) return new MonoVarPoly(0);
        MonoVarPoly deriv = new MonoVarPoly(0, new double[this.degree() - 1]);
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