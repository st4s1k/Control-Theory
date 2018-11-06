// credit: https://gist.github.com/derlin/40545e447fffe7599d26d0a435d9b113

import java.text.DecimalFormat;

public class Polynomial{
    private double[] coef;  // coefficients
    private int deg;     // degree of polynomial (0 for the zero polynomial)

    public Polynomial() {
        coef = new double[1];
        coef[0] = 0;
        deg = 0;
    }

    public Polynomial(double coef, int deg) {
        this.coef = new double[deg + 1];
        this.coef[deg] = coef;
        this.deg = deg;
    }

    public Polynomial(int deg) {
        coef = new double[deg + 1];
        this.deg = deg;
    }

    public Polynomial(Polynomial p) {
        coef = new double[p.degree() + 1];
        for (int i = 0; i <= p.degree(); i++)
            coef[i] = p.coeff(i);
        deg = p.degree();
    }

    public int degree() {
        int d = 0;
        for (int i = 0; i < coef.length; i++)
            if (coef[i] != 0) d = i;
        return d;
    }

    public double coeff() {return coeff(degree()); }

    public double coeff(int degree) {
        if (degree > coef.length) throw new RuntimeException("bad degree");
        return coef[degree];
    }

    public void setCoef(double coef, int deg) {
        this.coef[deg] = coef;
    }

    public Polynomial plus(Polynomial p) {
        Polynomial c = new Polynomial(Math.max(deg, p.degree()));
        for (int i = 0; i <= deg; i++) c.setCoef(c.coeff(i) + coef[i], i);
        for (int i = 0; i <= p.degree(); i++) c.setCoef(c.coeff(i) + p.coeff(i), i);
        return c;
    }

    public Polynomial plus(double coef, int deg) {
        return plus(new Polynomial(coef, deg));
    }

    public Polynomial plus(double n) {
        return plus(n, 0);
    }

    public Polynomial minus(Polynomial p) {
        Polynomial c = new Polynomial(Math.max(deg, p.degree()));
        for (int i = 0; i <= deg; i++) c.setCoef(c.coeff(i) + coef[i], i);
        for (int i = 0; i <= p.degree(); i++) c.setCoef(c.coeff(i) - p.coeff(i), i);
        return c;
    }

    public Polynomial minus(double coef, int deg) {
        return minus(new Polynomial(coef, deg));
    }

    public Polynomial minus(double n) {
        return minus(n, 0);
    }

    public Polynomial times(Polynomial p) {
        Polynomial c = new Polynomial(deg + p.degree());
        for (int i = 0; i <= deg; i++)
            for (int j = 0; j <= p.degree(); j++)
                c.setCoef(c.coeff(i + j) + (coef[i] * p.coeff(j)), i + j);
        return c;
    }

    public Polynomial times(double coef, int deg) {
        return times(new Polynomial(coef, deg));
    }

    public Polynomial times(double n) {
        return times(new Polynomial(n, 0));
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
    public Polynomial[] div(Polynomial p) {
        Polynomial q = new Polynomial(0);
        Polynomial r = new Polynomial(this);
        while(!r.isZero() && r.degree() >= p.degree()) {
            double coef = r.coeff() / p.coeff();
            int deg = r.degree() - p.degree();
            Polynomial t = new Polynomial(coef, deg);
            q = q.plus(t);
            r = r.minus(t.times(p));
        }//end while
        return new Polynomial[]{ q, r };
    }

    public Polynomial[] div(double coef, int deg) {
        return div(new Polynomial(coef, deg));
    }

    public Polynomial[] div(double n) {
        return div(n, 0);
    }

    public boolean eq(Polynomial p) {
        if (deg != p.degree()) return false;
        for (int i = deg; i >= 0; i--)
            if (coef[i] != p.coeff(i)) return false;
        return true;
    }

    public boolean isZero() {
        for (double c : coef) if (c != 0) return false;
        return true;
    }

    // use Horner's method to compute and return the polynomial evaluated at x
    public double evaluate(double n) {
        double p = 0;
        for (int i = deg; i >= 0; i--)
            p = coef[i] + (n * p);
        return p;
    }

    public Complex evaluate(Complex z) {
        Complex s = new Complex();
        for (int i = 0; i <= deg; i++)
            if (coef[i] != 0)
                s = s.plus(z.pow(i).times(coef[i]));
        return s;
    }

    // return this(p(x)) - compute using Horner's method
    public Polynomial evaluate(Polynomial p) {
        Polynomial q = new Polynomial(0);
        for (int i = deg; i >= 0; i--)
            q = new Polynomial(coef[i], 0).plus(p.times(q));
        return q;
    }

    // differentiate this polynomial and return it
    public Polynomial differentiate() {
        if (deg == 0) return new Polynomial(0);
        Polynomial deriv = new Polynomial(deg - 1);
        for (int i = 0; i < deg; i++)
            deriv.setCoef((i + 1) * coef[i + 1], i);
        return deriv;
    }

    // convert to string representation
    public String toString() {
        DecimalFormat coef_fmt = new DecimalFormat("#.###");
        StringBuilder s = new StringBuilder();
        for (int i = deg; i >= 0; i--)
            if (coef[i] != 0) {
                if (i < deg)
                    s.append(coef[i] < 0 ? " - " : " + ");
                if (coef[i] < 0 && i == deg)
                    s.append("-");
                if (Math.abs(coef[i]) != 1 || i == 0)
                    s.append(coef_fmt.format(Math.abs(coef[i])));
                if (i > 0) {
                    s.append("s");
                    if (i > 1)
                        s.append("^").append(i);
                }
            }
            else if (deg == 0)
                s = new StringBuilder(coef_fmt.format(coef[i]));
        return s.toString();
    }

    // test client
    public static void main(String[] args) {
        Polynomial zero = new Polynomial(0);
        Polynomial p = new Polynomial(1, 0).plus(3, 2).plus(4, 3);   // 4x^3 + 3x^2 + 1
        Polynomial q = new Polynomial(3, 0).plus(1, 1);

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
        System.out.printf("(%s) / (%s): %s, %s", p, q, p.div(q)[0], p.div(q)[1]);
    }

}
