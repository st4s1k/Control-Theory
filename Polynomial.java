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

    public Polynomial(double ... args) {
        coef = new double[args.length];
        System.arraycopy(args, 0, coef, 0, args.length);
        deg = args.length - 1;
    }

    public Polynomial(Polynomial p) {
        coef = new double[p.coef.length];
        System.arraycopy(p.coef, 0, coef, 0, p.coef.length);
        deg = p.degree();
    }

    // return the degree of this polynomial (0 for the zero polynomial)
    public int degree() {
        int d = 0;
        for (int i = 0; i < coef.length; i++)
            if (coef[i] != 0) d = i;
        return d;
    }


    // return c = a + b
    public Polynomial plus(Polynomial p) {
        Polynomial c = new Polynomial(Math.max(deg, p.deg));
        for (int i = 0; i <= deg; i++) c.coef[i] += coef[i];
        for (int i = 0; i <= p.deg; i++) c.coef[i] += p.coef[i];
        c.deg = c.degree();
        return c;
    }

    public Polynomial plus(double coef, int deg) {
        return plus(new Polynomial(coef, deg));
    }

    public Polynomial plus(double d) {
        return plus(d, 0);
    }

    // return (a - b)
    public Polynomial minus(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] -= b.coef[i];
        c.deg = c.degree();
        return c;
    }

    public Polynomial minus(double a, int b) {
        return minus(new Polynomial(a, b));
    }

    public Polynomial minus(double d) {
        return minus(d, 0);
    }

    // return (a * b)
    public Polynomial times(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(a.deg + b.deg);
        for (int i = 0; i <= a.deg; i++)
            for (int j = 0; j <= b.deg; j++)
                c.coef[i + j] += (a.coef[i] * b.coef[j]);
        c.deg = c.degree();
        return c;
    }

    public Polynomial times(double a, int b) {
        return times(new Polynomial(a, b));
    }

    public Polynomial times(double c) {
        return times(new Polynomial(c));
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
    public Polynomial[] div(Polynomial b) {
        Polynomial q = new Polynomial(0);
        Polynomial r = new Polynomial(this);
        while(!r.isZero() && r.degree() >= b.degree()) {
            double coef = r.coeff() / b.coeff();
            int deg = r.degree() - b.degree();
            Polynomial t = new Polynomial(coef, deg);
            q = q.plus(t);
            r = r.minus(t.times(b));
        }//end while
        return new Polynomial[]{ q, r };
    }

    public Polynomial[] div(double a, int b) {
        return div(new Polynomial(a, b));
    }

    public Polynomial[] div(double c) {
        return div(new Polynomial(c));
    }


    // get the coefficient for the highest degree
    public double coeff() {return coeff(degree()); }


    // get the coefficient for degree d
    public double coeff(int degree) {
        if (degree > this.degree()) throw new RuntimeException("bad degree");
        return coef[degree];
    }

    // do a and b represent the same polynomial?
    public boolean eq(Polynomial b) {
        Polynomial a = this;
        if (a.deg != b.deg) return false;
        for (int i = a.deg; i >= 0; i--)
            if (a.coef[i] != b.coef[i]) return false;
        return true;
    }


    // test whether or not this polynomial is zero
    public boolean isZero() {
        for (double i : coef) if (i != 0) return false;
        return true;
    }

    // use Horner's method to compute and return the polynomial evaluated at x
    public double evaluate(double d) {
        double p = 0;
        for (int i = deg; i >= 0; i--)
            p = coef[i] + (d * p);
        return p;
    }

    public Complex evaluate(Complex z) {
        Complex s = new Complex();
        for (int i = 0; i <= deg; i++)
            if (coef[i] != 0)
                s = s.plus(z.pow(i).times(coef[i]));
        return s;
    }

    // return a(b(x)) - compute using Horner's method
    public Polynomial evaluate(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0);
        for (int i = a.deg; i >= 0; i--) {
            Polynomial term = new Polynomial(a.coef[i], 0);
            c = term.plus(b.times(c));
        }
        return c;
    }

    // differentiate this polynomial and return it
    public Polynomial differentiate() {
        if (deg == 0) return new Polynomial(0);
        Polynomial deriv = new Polynomial(deg - 1);
        deriv.deg = deg - 1;
        for (int i = 0; i < deg; i++)
            deriv.coef[i] = (i + 1) * coef[i + 1];
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
        Polynomial p = new Polynomial(4, 3).plus(3, 2).plus(1);   // 4x^3 + 3x^2 + 1
        Polynomial q = new Polynomial(1, 1).plus(3, 0);

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