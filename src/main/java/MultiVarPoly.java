import exceptions.BadVariableExponentException;
import exceptions.InvalidTermOperationException;
import exceptions.InvalidVariableException;
import exceptions.UndefinedDifferentiationVariable;

import java.util.*;

public class MultiVarPoly {
    private SortedSet<Monomial> terms = new TreeSet<>();

    public MultiVarPoly(Monomial ... monomials) {
        for (Monomial monomial: monomials)
            this.add(monomial);
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

    public MultiVarPoly add(Monomial monomial) {

        MultiVarPoly p = new MultiVarPoly(this);

        for (Monomial term: p.terms) {
            if (term.similarTo(monomial)) {
                try {
                    p.replace(term, term.add(monomial));
                    return p;
                } catch (InvalidTermOperationException e) {
                    e.printStackTrace();
                }
            }
        }

        p.terms.add(monomial);

        return p;
    }

    public MultiVarPoly add(MultiVarPoly polynomial) {
        MultiVarPoly result = new MultiVarPoly(this);

        for (Monomial pTerm: polynomial.terms)
            result = result.add(pTerm);

        return result;
    }

    public MultiVarPoly add(double n) {
        return this.add(new Monomial(n));
    }

    public MultiVarPoly minus(MultiVarPoly polynomial) {
        return this.add(polynomial.multiply(-1));
    }

    public MultiVarPoly minus(double n) {
        return this.add(-n);
    }

    public MultiVarPoly multiply(Monomial monomial) {
        MultiVarPoly result = new MultiVarPoly(this);

        for (Monomial term: result.terms)
            result.replace(term, term.multiply(monomial));

        return result;
    }

    public MultiVarPoly multiply(MultiVarPoly polynomial) {
        return polynomial.terms.stream()
                .map(this::multiply)
                .reduce(new MultiVarPoly(), MultiVarPoly::add);
    }

    public MultiVarPoly multiply(double n) {
        return this.multiply(new Monomial(n));
    }

//    public MultiVarPoly[] div(MultiVarPoly polynomial) throws exceptions.DivisionByZeroException {
//        if (polynomial.terms.isEmpty())
//            throw new exceptions.DivisionByZeroException(
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
//            q = q.add(t);
//            r = r.minus(t.multiply(polynomial));
//        }
//        return new MultiVarPoly[]{ q, r };
//    }
//
//    public MultiVarPoly div(double n) throws exceptions.DivisionByZeroException {
//        return div(new Monomial(n))[0];
//    }


//    use Horner's method to compute and return the polynomial evaluated at x
//
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
                    poly = poly.add(term.evaluate(var, n));
                } catch (InvalidVariableException e) {
                    poly = poly.add(term);
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
//            poly = poly.add(term.evaluate());
//
//        return poly;
//    }

    // differentiate this polynomial and return it
    public MultiVarPoly differentiate() throws UndefinedDifferentiationVariable {

        MultiVarPoly deriv = new MultiVarPoly();

        String var = null;

        for (Monomial term: terms) {
            if (term.getVars().size() > 1)
                throw new UndefinedDifferentiationVariable();
            else if (var == null && term.getVars().size() == 1)
                var = term.getVars().firstKey();
            else
                continue;

            if (term.getVarExp(var) > 0) {
                try {
                    deriv = deriv.add(
                            term.setVarExp(var, term.getVarExp(var) - 1)
                            .multiply(term.getVarExp(var))
                    );
                } catch (InvalidVariableException | BadVariableExponentException e) {
                    e.printStackTrace();
                }
            }
        }
        return deriv;
    }

    // differentiate this polynomial and return it
    public MultiVarPoly differentiate(String var) {
        MultiVarPoly deriv = new MultiVarPoly();
        for (Monomial term: terms) {
            if (term.hasVar(var) && term.getVarExp(var) > 0) {
                try {
                    deriv = deriv.add(
                            term.setVarExp(var, term.getVarExp(var) - 1)
                            .multiply(term.getVarExp(var))
                    );
                } catch (InvalidVariableException | BadVariableExponentException e) {
                    e.printStackTrace();
                }
            }
        }
        return deriv;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Monomial term: terms) {
            if (!term.equals(terms.first())) {
                if (term.coeff() < 0) {
                    s.append(" - ");
                }
                else {
                    s.append(" + ");
                }
            }
            s.append(term.abs());
        }

        return s.toString();
    }

    public static void main(String[] args) {
        MultiVarPoly mvp = new MultiVarPoly()
                .add(
                        new Monomial(-2, "a", 1)
                        .multiply(new Monomial(1, "b", 2))
                        .multiply(new Monomial(1, "c", 3))
                        .multiply(new Monomial(1, "d", 4))

                )
                .add(
                        new Monomial(4, "a", 5)
                        .multiply(new Monomial(1, "b", 6))
                        .multiply(new Monomial(1, "c", 7))
                        .multiply(new Monomial(1, "d", 8))
                )
                .add(
                        new Monomial(-6, "a", 9)
                                .multiply(new Monomial(1, "b", 10))
                                .multiply(new Monomial(1, "c", 11))
                                .multiply(new Monomial(1, "d", 12))
                );

        System.out.println(mvp);
        System.out.println(mvp.differentiate("a"));
    }
}
