import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Monomial implements Comparable<Monomial> {
    private final double coef;
    private final Map<Character, Integer> vars;

    public Monomial(double coef) {
        this.coef = coef;
        vars = new HashMap<>();
    }

    public Monomial(double coef, int exp, char sym) {
        this.coef = coef;
        vars = new HashMap<>();
        vars.put(sym, exp);
    }

    public Monomial(double coef, Map<Character, Integer> vars) {
        this.coef = coef;
        this.vars = new HashMap<>(vars);
    }

    public Monomial(Monomial monomial) {
        coef = monomial.coef;
        vars = new HashMap<>(monomial.vars);
    }

    public double coeff() {
        return coef;
    }

    public Integer degree() {
        return vars.entrySet().stream().mapToInt(Map.Entry::getValue).sum();
    }

    public Monomial evaluate(Character v, double num) throws InvalidVariableException {
        if (!vars.containsKey(v))
            throw new InvalidVariableException("No such variable!", v.toString());
        return new Monomial(
                this.coef*Math.pow(num, vars.remove(v)),
                new HashMap<>(vars)
        );
    }

    public Monomial evaluate(Character v, Monomial monomial) throws InvalidVariableException {
        if (!vars.containsKey(v))
            throw new InvalidVariableException("No such variable!", v.toString());
        return monomial.pow(vars.get(v)).times(coef);
    }

    public Monomial pow(Integer exp) {
        Map<Character, Integer> vars = new HashMap<>(this.vars);
        vars.forEach((k, v) -> vars.put(k, v*exp));
        return new Monomial(Math.pow(coef, exp), vars);
    }

    public Monomial times(double coef) {
        return new Monomial(
                this.coef*coef,
                vars
        );
    }

    public Monomial plus(Monomial monomial) throws InvalidTermOperationException {
        if (!this.similarTo(monomial))
            throw new InvalidTermOperationException("Terms' variables differ!", monomial.toString(), "plus");
        return new Monomial(
                coef+ monomial.coef,
                vars
        );
    }

    boolean similarTo(Monomial monomial) {
        return vars.equals(monomial.vars);
    }

    @Override
    public String toString() {
        StringBuilder vs = new StringBuilder();
        vs.append(coef);
        vars.forEach((k, v) -> {
            if (v > 0) {
                vs.append(k);
                if (v > 1) {
                    vs.append('^');
                    vs.append(v);
                }
            }
            else
                vs.append(1);
        });
        return vs.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Monomial monomial = (Monomial) o;
        return Double.compare(monomial.coef, coef) == 0 &&
                Objects.equals(vars, monomial.vars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coef, vars.hashCode());
    }

    @Override
    public int compareTo(Monomial monomial) {
        return this.degree() - monomial.degree();
    }
}
