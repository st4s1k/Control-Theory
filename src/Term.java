import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class Term {
    private final double coef;
    private final Map<Character, Integer> vars;

    public Term(double coef, int exp, char sym) {
        this.coef = coef;
        vars = new HashMap<>();
        vars.put(sym, exp);
    }

    public Term(double coef, Map<Character, Integer> vars) {
        this.coef = coef;
        this.vars = new HashMap<>(vars);
    }

    public Term(Term term) {
        coef = term.coef;
        vars = new HashMap<>(term.vars);
    }

    public Term evaluate(Character v, double num) throws InvalidVariableException {
        if (!vars.containsKey(v))
            throw new InvalidVariableException("No such variable!", v.toString());
        return new Term(
                this.coef*Math.pow(num, vars.remove(v)),
                new HashMap<>(vars)
        );
    }

    public Term evaluate(Character v, Term term) throws InvalidVariableException {
        if (!vars.containsKey(v))
            throw new InvalidVariableException("No such variable!", v.toString());
        return term.pow(vars.get(v)).times(coef);
    }

    public Term pow(Integer exp) {
        Map<Character, Integer> vars = new HashMap<>(this.vars);
        vars.forEach((k, v) -> vars.put(k, v*exp));
        return new Term(Math.pow(coef, exp), vars);
    }

    public Term times(double coef) {
        return new Term(
                this.coef*coef,
                vars
        );
    }

    public Term plus(Term term) throws InvalidTermOperationException {
        if (!vars.equals(term.vars))
            throw new InvalidTermOperationException("Terms' variables differ!", term.toString(), "plus");
        return new Term(
                coef+term.coef,
                vars
        );
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
        Term term = (Term) o;
        return Double.compare(term.coef, coef) == 0 &&
                Objects.equals(vars, term.vars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coef, vars.hashCode());
    }
}
