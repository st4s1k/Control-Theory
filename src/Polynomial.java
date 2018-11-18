// credit: https://gist.github.com/derlin/40545e447fffe7599d26d0a435d9b113

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;

public class Polynomial{
    private double[] coef;  // coefficients
    private char varSymbol = 'x';

    public Polynomial(Polynomial p) {
        coef = new double[p.degree() + 1];
        for (int i = 0; i <= p.degree(); i++)
            coef[i] = p.coeff(i);
        varSymbol = p.getVarSymbol();
    }

    public Polynomial(int deg) {
        this.coef = new double[deg + 1];
        this.coef[deg] = 0;
    }

    public static Polynomial term(double coef, int deg) {
        Polynomial p = new Polynomial(deg);
        p.coef[deg] = coef;
        return p;
    }

    public int degree() {
        int d = 0;
        for (int i = 0; i < coef.length; i++)
            if (coef[i] != 0) d = i;
        return d;
    }

    public double coeff() {return coeff(degree()); }

    public double coeff(int deg) {
        return deg > coef.length || deg < 0 ? 0 : coef[deg];
    }

    public void setCoef(double coef, int deg) {
        this.coef[deg] = coef;
    }

    public Polynomial setVarSymbol(char varSymbol) {
        this.varSymbol = varSymbol;
        return this;
    }

    public char getVarSymbol() {
        return varSymbol;
    }

    public Polynomial plus(Polynomial p) {
        Polynomial c = new Polynomial(Math.max(this.degree(), p.degree()));
        for (int i = 0; i <= this.degree(); i++) c.coef[i] = c.coef[i] + this.coef[i];
        for (int i = 0; i <= p.degree(); i++) c.coef[i] = c.coef[i] + p.coef[i];
        return c;
    }

    public Polynomial plus(double n) {
        return plus(Polynomial.term(n, 0));
    }

    public Polynomial minus(Polynomial p) {
        Polynomial c = new Polynomial(Math.max(this.degree(), p.degree()));
        for (int i = 0; i <= this.degree(); i++) c.coef[i] = c.coef[i] + this.coef[i];
        for (int i = 0; i <= p.degree(); i++) c.coef[i] = c.coef[i] - p.coef[i];
        return c;
    }

    public Polynomial minus(double n) {
        return minus(Polynomial.term(n, 0));
    }

    public Polynomial times(Polynomial p) {
        Polynomial c = new Polynomial(this.degree() + p.degree());
        for (int i = 0; i <= this.degree(); i++)
            for (int j = 0; j <= p.degree(); j++)
                c.coef[i + j] = c.coef[i + j] + this.coef[i] * p.coef[j];
        return c;
    }

    public Polynomial times(double n) {
        return times(Polynomial.term(n, 0));
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
    public Polynomial[] div(Polynomial p) throws DivisionByZeroException {
        if (p.isZero()) throw new DivisionByZeroException(this.toString(), p.toString());
        Polynomial q = new Polynomial(0);
        Polynomial r = new Polynomial(this);
        while(!r.isZero() && r.degree() >= p.degree()) {
            double coef = r.coeff() / p.coeff();
            int deg = r.degree() - p.degree();
            Polynomial t = Polynomial.term(coef, deg);
            q = q.plus(t);
            r = r.minus(t.times(p));
        }
        return new Polynomial[]{ q, r };
    }

    public Polynomial div(double n) throws DivisionByZeroException {
        return div(Polynomial.term(n, 0))[0];
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Polynomial)) {
            return false;
        }

        Polynomial p = (Polynomial) obj;

        if (this.degree() != p.degree()) return false;

        for (int i = this.degree(); i >= 0; i--)
            if (this.coef[i] != p.coef[i]) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(coef), varSymbol);
    }

    public boolean isZero() {
        for (double c : coef) if (c != 0) return false;
        return true;
    }

    // use Horner's method to compute and return the polynomial evaluated at x
    public double evaluate(double n) {
        double p = 0;
        for (int i = this.degree(); i >= 0; i--)
            p = coef[i] + (n * p);
        return p;
    }

    public Complex evaluate(Complex z) {
        Complex s = new Complex();
        for (int i = 0; i <= this.degree(); i++)
            if (coef[i] != 0)
                s = s.plus(z.pow(i).times(coef[i]));
        return s;
    }

    // return this(p(x)) - compute using Horner's method
    public Polynomial evaluate(Polynomial p) {
        Polynomial q = new Polynomial(0);
        for (int i = this.degree(); i >= 0; i--)
            q = Polynomial.term(coef[i], 0).plus(p.times(q));
        return q;
    }

    // differentiate this polynomial and return it
    public Polynomial differentiate() {
        if (this.degree() == 0) return new Polynomial(0);
        Polynomial deriv = new Polynomial(this.degree() - 1);
        for (int i = 0; i < this.degree(); i++)
            deriv.setCoef((i + 1) * coef[i + 1], i);
        return deriv;
    }

    // convert to string representation
    public String toString() {
        DecimalFormat coefFormat = new DecimalFormat("#.###");
        StringBuilder s = new StringBuilder();
        for (int i = this.degree(); i >= 0; i--)
            if (coef[i] != 0) {
                if (i < this.degree())
                    s.append(coef[i] < 0 ? " - " : " + ");
                if (coef[i] < 0 && i == this.degree())
                    s.append("-");
                if (Math.abs(coef[i]) != 1 || i == 0)
                    s.append(coefFormat.format(Math.abs(coef[i])));
                if (i > 0) {
                    s.append(varSymbol);
                    if (i > 1)
                        s.append("^").append(i);
                }
            }
            else if (this.degree() == 0)
                s = new StringBuilder(coefFormat.format(coef[i]));
        return s.toString();
    }

    // test client
    public static void main(String[] args) {
        Polynomial zero = new Polynomial(0);
        Polynomial p = Polynomial.term(1, 0)
                .plus(Polynomial.term(3, 2))
                .plus(Polynomial.term(4, 3));   // 4x^3 + 3x^2 + 1
        Polynomial q = Polynomial.term(3, 0)
                .plus(Polynomial.term(1, 1));

        Polynomial r = p.plus(q);
        Polynomial s = p.times(q);
        Polynomial t = p.evaluate(q);

        System.out.println("zero(x)     = " + zero);
        System.out.println("p(x)        = " + p);
        System.out.println("q(x)        = " + q);
        System.out.println("p(x) + q(x) = " + r);
        System.out.println("p(x) * q(x) = " + s);
        System.out.println("p(q(x))     = " + t);
        System.out.println("0 - p(x)    = " + zero.minus(p));
        System.out.println("p(3)        = " + p.evaluate(3));
        System.out.println("p'(x)       = " + p.differentiate());
        System.out.println("p''(x)      = " + p.differentiate().differentiate());
        try {
            System.out.printf("(%s) / (%s): %s, %s", p, q, p.div(q)[0], p.div(q)[1]);
        } catch (DivisionByZeroException e) {
            e.printStackTrace();
        }
    }

}
