import java.text.DecimalFormat;
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

    public Monomial(double coef, String variable, int exp) {
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

    public Integer getVarExp(String var) {
        return this.hasVar(var) ? vars.get(var) : 0;
    }

    // Returns new monomial with modified degree

    public Monomial setVarExp(String var, Integer exp) throws InvalidVariableException,
            BadVariableExponentException {

        if (!vars.containsKey(var))
            throw new InvalidVariableException(
                    "No such variable!",
                    String.valueOf(var)
            );

        if (exp == null || exp < 0)
            throw new BadVariableExponentException(
                    "Bad degree!",
                    String.valueOf(var),
                    String.valueOf(exp)
            );

        Map<String, Integer> variables = new TreeMap<>(vars);

        if (exp == 0) {
            variables.remove(var);
        } else {
            variables.put(var, exp);
        }

        return new Monomial(
                coef,
                variables
        );
    }

    public Map<String, Integer> getVars() {
        return new TreeMap<>(vars);
    }

    public Monomial abs() {
        return new Monomial(
              Math.abs(coef),
              vars
        );
    }

    public double coeff() {
        return coef;
    }

    public int degree() {
        return vars.entrySet().stream().mapToInt(Map.Entry::getValue).sum();
    }

    public Monomial evaluate(String var, double num) throws InvalidVariableException {

        if (!vars.containsKey(var))
            throw new InvalidVariableException("No such variable!", var);

        Map<String, Integer> variables = new TreeMap<>(vars);

        return new Monomial(
                this.coef*Math.pow(num, variables.remove(var)),
                variables
        );
    }

//    public Monomial evaluate(String variable, Monomial monomial) throws InvalidVariableException {
//        if (!vars.containsKey(variable))
//            throw new InvalidVariableException("No such variable!", variable);
//        return monomial.pow(vars.get(variable)).multiply(coef);
//    }

    public Monomial pow(Integer pwr) {

        Map<String, Integer> variables = new TreeMap<>(vars);

        variables.forEach((var, exp) -> variables.put(var, exp*pwr));

        return new Monomial(Math.pow(coef, pwr), variables);
    }

    public Monomial multiply(double coef) {
        return new Monomial(
                this.coef*coef,
                vars
        );
    }

    public Monomial multiply(Monomial monomial) {
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

    public Monomial divide(double n) {
        return null;
    }

    public Monomial divide(Monomial monomial) {
        return null;
    }

    public Monomial add(Monomial monomial) throws InvalidTermOperationException {
        if (!this.similarTo(monomial))
            throw new InvalidTermOperationException("Terms' variables differ!", monomial.toString(), "add");
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

        vs.append(new DecimalFormat("#.###").format(coef));

        vars.forEach((var, deg) -> {
            if (deg > 0) {
                vs.append('*');
                vs.append(var);
                if (deg > 1) {
                    vs.append('^');
                    vs.append(deg);
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

    public static void main(String[] args) {
        System.out.println(
                new Monomial(2, "a", 1)
                .multiply(new Monomial(1, "b", 2))
                .multiply(new Monomial(1, "c", 3))
                .multiply(new Monomial(1, "d", 4))
        );
    }
}
