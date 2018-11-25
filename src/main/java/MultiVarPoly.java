import java.text.DecimalFormat;
import java.util.*;

public class MultiVarPoly {
    private SortedSet<Monomial> terms = new TreeSet<>();

    public MultiVarPoly(Monomial ... monomials) {
        for (Monomial monomial: monomials)
            plus(monomial);
    }

    public MultiVarPoly(MultiVarPoly polynomial) {
        terms.clear();
        terms.addAll(polynomial.terms);
    }

    public boolean replace(Monomial oldTerm, Monomial newTerm) {
        return terms.remove(oldTerm) && terms.add(newTerm);
    }

    public int degree() {
        if (terms.isEmpty()) return 0;
        return terms.last().degree();
    }

    public double coeff() {
        if (terms.isEmpty()) return 0;
        return terms.last().coeff();
    }

    public MultiVarPoly plus(Monomial monomial) {

        MultiVarPoly p = new MultiVarPoly(this);

        for (Monomial term: p.terms) {
            if (term.similarTo(monomial)) {
                try {
                    p.replace(term, term.plus(monomial));
                } catch (InvalidTermOperationException e) {
                    e.printStackTrace();
                }
                return p;
            }
        }

        p.terms.add(monomial);

        return p;
    }

    public MultiVarPoly plus(MultiVarPoly polynomial) {
        MultiVarPoly result = new MultiVarPoly(this);

        for (Monomial pTerm: polynomial.terms)
            result = result.plus(pTerm);

        return result;
    }

    public MultiVarPoly plus(double n) {
        return this.plus(new Monomial(n));
    }

    public MultiVarPoly minus(MultiVarPoly polynomial) {
        return this.plus(polynomial.times(-1));
    }

    public MultiVarPoly minus(double n) {
        return this.plus(-n);
    }

    public MultiVarPoly times(Monomial monomial) {
        MultiVarPoly result = new MultiVarPoly(this);

        for (Monomial term: result.terms)
            result.replace(term, term.times(monomial));

        return result;
    }

    public MultiVarPoly times(MultiVarPoly polynomial) {
        return polynomial.terms.stream()
                .map(this::times)
                .reduce(new MultiVarPoly(), MultiVarPoly::plus);
    }

    public MultiVarPoly times(double n) {
        return times(new Monomial(n));
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

//    public MultiVarPoly[] div(MultiVarPoly polynomial) throws DivisionByZeroException {
//        if (polynomial.terms.isEmpty())
//            throw new DivisionByZeroException(
//                    "The denominator-polynomial is zero.",
//                    this.toString(),
//                    polynomial.toString()
//            );
//        MultiVarPoly q = new MultiVarPoly(0);
//        MultiVarPoly r = new MultiVarPoly(this);
//        while(!r.isZero() && r.degree() >= polynomial.degree()) {
//            double coef = r.coeff() / polynomial.coeff();
//            int deg = r.degree() - polynomial.degree();
//            MultiVarPoly t = new MultiVarPoly(new Monomial(coef, deg));
//            q = q.plus(t);
//            r = r.minus(t.times(polynomial));
//        }
//        return new MultiVarPoly[]{ q, r };
//    }
//
//    public MultiVarPoly div(double n) throws DivisionByZeroException {
//        return div(new Monomial(n))[0];
//    }


    // use Horner's method to compute and return the polynomial evaluated at x
//    public double evaluate(Complex n, String var) {
//        double p = 0;
//        for (int i = this.degree(); i >= 0; i--)
//            p = coef[i] + (n * p);
//        return p;
//    }

    public MultiVarPoly evaluate(double n, String var) {
        MultiVarPoly poly = new MultiVarPoly();

        for (Monomial term : terms) {
            if (term.coeff() != 0) {
                try {
                    poly = poly.plus(term.evaluate(var, n));
                } catch (InvalidVariableException e) {
                    poly = poly.plus(term);
                }
            }
        }

        return poly;
    }

//    // return this(p(x)) - compute using Horner's method
//    public MultiVarPoly evaluate(MultiVarPoly polynomial, String var) {
//        MultiVarPoly poly = new MultiVarPoly();
//
//        for (Monomial term : terms) {
//            poly = poly.plus(term.evaluate());
//
//        return poly;
//    }
//
//    // differentiate this polynomial and return it
//    public MultiVarPoly differentiate() {
//        if (this.degree() == 0) return new MultiVarPoly(0);
//        MultiVarPoly deriv = new MultiVarPoly(this.degree() - 1);
//        for (int i = 0; i < this.degree(); i++)
//            deriv.coef[i] = (i + 1) * coef[i + 1];
//        return deriv;
//    }
//
//    // convert to string representation
//
//    public String toString() {
//        DecimalFormat coefFormat = new DecimalFormat("#.###");
//        StringBuilder s = new StringBuilder();
//        for (int i = this.degree(); i >= 0; i--)
//            if (coef[i] != 0) {
//                if (i < this.degree())
//                    s.append(coef[i] < 0 ? " - " : " + ");
//                if (coef[i] < 0 && i == this.degree())
//                    s.append("-");
//                if (Math.abs(coef[i]) != 1 || i == 0)
//                    s.append(coefFormat.format(Math.abs(coef[i])));
//                if (i > 0) {
//                    s.append(varSymbol);
//                    if (i > 1)
//                        s.append("^").append(i);
//                }
//            }
//            else if (this.degree() == 0)
//                s = new StringBuilder(coefFormat.format(coef[i]));
//        return s.toString();
//    }
}
