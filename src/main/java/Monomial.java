import java.util.Arrays;
import java.util.TreeMap;
import java.util.Map;
import java.util.Objects;

public class Monomial implements Comparable<Monomial> {
    private final double coef;
    private final Map<String, Integer> vars;

    public Monomial(double coef) {
        this.coef = coef;
        vars = new TreeMap<>();
    }

    public Monomial(double coef, int exp, String variable) {
        this.coef = coef;
        vars = new TreeMap<>();
        vars.put(variable, exp);
    }

    public Monomial(double coef, Map<String, Integer> vars) {
        this.coef = coef;
        this.vars = new TreeMap<>(vars);
    }

    public Monomial(Monomial monomial) {
        coef = monomial.coef;
        vars = new TreeMap<>(monomial.vars);
    }

    public boolean hasVar(String var) {
        return vars.containsKey(var);
    }

    public Integer getVarDegree(String var) {
        return vars.get(var);
    }

    public Map<String, Integer> getVars() {
        return new TreeMap<>(vars);
    }

    public double coeff() {
        return coef;
    }

    public int degree() {
        return vars.entrySet().stream().mapToInt(Map.Entry::getValue).sum();
    }

    public Monomial evaluate(String variable, double num) throws InvalidVariableException {
        if (!vars.containsKey(variable))
            throw new InvalidVariableException("No such variable!", variable);
        Map<String, Integer> variables = new TreeMap<>(vars);
        return new Monomial(
                this.coef*Math.pow(num, variables.remove(variable)),
                variables
        );
    }

//    public Monomial evaluate(String variable, Monomial monomial) throws InvalidVariableException {
//        if (!vars.containsKey(variable))
//            throw new InvalidVariableException("No such variable!", variable);
//        return monomial.pow(vars.get(variable)).times(coef);
//    }

    public Monomial pow(Integer exp) {
        Map<String, Integer> variables = new TreeMap<>(vars);
        variables.forEach((k, variable) -> variables.put(k, variable*exp));
        return new Monomial(Math.pow(coef, exp), variables);
    }

    public Monomial times(double coef) {
        return new Monomial(
                coef*coef,
                vars
        );
    }

    public Monomial times(Monomial monomial) {
        Map<String, Integer> variables = new TreeMap<>(vars);

        monomial.vars.forEach((var, exp) -> variables.put(
                var,
                (
                        variables.containsKey(var) ? variables.get(var) + exp : exp
                )
        ));

        return new Monomial(
                coef*monomial.coef,
                variables
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
        vars.forEach((k, variable) -> {
            if (variable > 0) {
                vs.append(k);
                if (variable > 1) {
                    vs.append('^');
                    vs.append(variable);
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
        int degRank = this.degree() - monomial.degree();
        int symRank = Arrays.hashCode(vars.keySet().toArray()) -
                Arrays.hashCode(monomial.vars.keySet().toArray());
        return degRank == 0 ? symRank : degRank ;
    }
}
