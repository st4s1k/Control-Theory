import java.text.DecimalFormat;
import java.util.*;

public class Polynomial {
    private final List<Monomial> terms;

    public Polynomial() {
        terms = new ArrayList<>();
        terms.add(new Monomial(0));
    }

    public Polynomial(int deg) {
        terms = new ArrayList<>();
        terms.add(new Monomial(0, deg, 'x'));
    }

    public Polynomial(Polynomial p) {
        terms = new ArrayList<>(p.terms);
    }

    public int degree() {
        if (terms.isEmpty()) return 0;
        return terms.get(terms.size() - 1).degree();
    }

    public double coeff() {
        if (terms.isEmpty()) return 0;
        return terms.get(terms.size() - 1).coeff();
    }

    public Polynomial plus(Polynomial p) {
        Polynomial result = new Polynomial();
        terms.forEach(term -> result.plus(p.plus(term)));
        return result;
    }

    public Polynomial plus(Monomial term) {
        Polynomial p = new Polynomial(this);

        if (terms.isEmpty()) {
            p.terms.add(term);
        } else terms.stream()
                .filter(monomial -> monomial.similarTo(term))
                .findFirst()
                .ifPresent(monomial -> {
                    try {
                        monomial.plus(term);
                    } catch (InvalidTermOperationException e) {
                        e.printStackTrace();
                    }
                });
        return p;
    }

    public Polynomial plus(double n) {
        return this.plus(new Monomial(n));
    }

    public Polynomial minus(Polynomial p) {
        Polynomial c = new Polynomial(Math.max(this.degree(), p.degree()));
        for (int i = 0; i <= this.degree(); i++) c.coef[i] = c.coef[i] + this.coef[i];
        for (int i = 0; i <= p.degree(); i++) c.coef[i] = c.coef[i] - p.coef[i];
        return c;
    }

    public Polynomial minus(double n) {
        return this.minus(Polynomial.term(n, 0));
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
        if (p.terms.isEmpty())
            throw new DivisionByZeroException(
                    "The denominator-polynomial is zero.",
                    this.toString(),
                    p.toString()
            );
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
            deriv.coef[i] = (i + 1) * coef[i + 1];
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polynomial that = (Polynomial) o;
        return Objects.equals(terms, that.terms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terms);
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
