// credit: https://gist.github.com/derlin/40545e447fffe7599d26d0a435d9b113

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

public class Manydicks {
    private SortedSet<Term> terms;  // coefficients

    public Manydicks(Manydicks p) {
        terms = new TreeSet<>(p.terms);
    }

    public Manydicks(int deg) {
        terms.first().getVars().
    }

    public static Manydicks term(double coef, int deg) {
        Manydicks p = new Manydicks(deg);
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

    public Manydicks setVarSymbol(char varSymbol) {
        this.varSymbol = varSymbol;
        return this;
    }

    public char getVarSymbol() {
        return varSymbol;
    }

    public Manydicks plus(Manydicks p) {
        Manydicks c = new Manydicks(Math.max(this.degree(), p.degree()));
        for (int i = 0; i <= this.degree(); i++) c.coef[i] = c.coef[i] + this.coef[i];
        for (int i = 0; i <= p.degree(); i++) c.coef[i] = c.coef[i] + p.coef[i];
        return c;
    }

    public Manydicks plus(double n) {
        return plus(Manydicks.term(n, 0));
    }

    public Manydicks minus(Manydicks p) {
        Manydicks c = new Manydicks(Math.max(this.degree(), p.degree()));
        for (int i = 0; i <= this.degree(); i++) c.coef[i] = c.coef[i] + this.coef[i];
        for (int i = 0; i <= p.degree(); i++) c.coef[i] = c.coef[i] - p.coef[i];
        return c;
    }

    public Manydicks minus(double n) {
        return minus(Manydicks.term(n, 0));
    }

    public Manydicks times(Manydicks p) {
        Manydicks c = new Manydicks(this.degree() + p.degree());
        for (int i = 0; i <= this.degree(); i++)
            for (int j = 0; j <= p.degree(); j++)
                c.coef[i + j] = c.coef[i + j] + this.coef[i] * p.coef[j];
        return c;
    }

    public Manydicks times(double n) {
        return times(Manydicks.term(n, 0));
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
    public Manydicks[] div(Manydicks p) throws DivisionByZeroException {
        if (p.isZero()) throw new DivisionByZeroException(this.toString(), p.toString());
        Manydicks q = new Manydicks(0);
        Manydicks r = new Manydicks(this);
        while(!r.isZero() && r.degree() >= p.degree()) {
            double coef = r.coeff() / p.coeff();
            int deg = r.degree() - p.degree();
            Manydicks t = Manydicks.term(coef, deg);
            q = q.plus(t);
            r = r.minus(t.times(p));
        }
        return new Manydicks[]{ q, r };
    }

    public Manydicks div(double n) throws DivisionByZeroException {
        return div(Manydicks.term(n, 0))[0];
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Manydicks)) {
            return false;
        }

        Manydicks p = (Manydicks) obj;

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
    public Manydicks evaluate(Manydicks p) {
        Manydicks q = new Manydicks(0);
        for (int i = this.degree(); i >= 0; i--)
            q = Manydicks.term(coef[i], 0).plus(p.times(q));
        return q;
    }

    // differentiate this polynomial and return it
    public Manydicks differentiate() {
        if (this.degree() == 0) return new Manydicks(0);
        Manydicks deriv = new Manydicks(this.degree() - 1);
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
        Manydicks zero = new Manydicks(0);
        Manydicks p = Manydicks.term(1, 0)
                .plus(Manydicks.term(3, 2))
                .plus(Manydicks.term(4, 3));   // 4x^3 + 3x^2 + 1
        Manydicks q = Manydicks.term(3, 0)
                .plus(Manydicks.term(1, 1));

        Manydicks r = p.plus(q);
        Manydicks s = p.times(q);
        Manydicks t = p.evaluate(q);

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
