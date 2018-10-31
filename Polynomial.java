// credit: https://gist.github.com/derlin/40545e447fffe7599d26d0a435d9b113

import java.text.DecimalFormat;

public class Polynomial{
    private Double[] coef;  // coefficients
    private int deg;     // degree of polynomial (0 for the zero polynomial)

    public Polynomial() {
        coef = new Double[1];
        coef[0] = 0d;
        deg = 0;
    }

    public Polynomial(Double coef, int deg) {
        this.coef = new Double[deg + 1];
        for (int i = 0; i < deg; i++) this.coef[i] = 0d;
        this.coef[deg] = coef;
        this.deg = deg;
    }

    public Polynomial(int deg) {
        coef = new Double[deg + 1];
        for (int i = 0; i <= deg; i++) coef[i] = 0d;
        this.deg = deg;
    }

    public Polynomial(Double ... args) {
        coef = new Double[args.length];
        System.arraycopy(args, 0, coef, 0, args.length);
        deg = args.length - 1;
    }

    public Polynomial(Polynomial p) {
        coef = new Double[p.coef.length];
        System.arraycopy(p.coef, 0, coef, 0, p.coef.length);
        deg = p.degree();
    }

    // return the degree of this polynomial (0 for the zero polynomial)
    public int degree() {
        int d = 0;
        for (int i = 0; i < coef.length; i++)
            if (!coef[i].equals(0d)) d = i;
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

    public Polynomial plus(Double coef, int deg) {
        return plus(new Polynomial(coef, deg));
    }

    public Polynomial plus(Double d) {
        return plus(d, 0);
    }

    public Polynomial plus(int N) {
        return plus((double)N);
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

    public Polynomial minus(Double a, int b) {
        return minus(new Polynomial(a, b));
    }

    public Polynomial minus(Double d) {
        return minus(d, 0);
    }

    public Polynomial minus(int N) {
        return minus((double)N);
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

    public Polynomial times(Double a, int b) {
        return times(new Polynomial(a, b));
    }

    public Polynomial times(Double c) {
        return times(new Polynomial(c));
    }

    public Polynomial times(int N) {
        return times((double)N);
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
            Double coef = r.coeff() / b.coeff();
            int deg = r.degree() - b.degree();
            Polynomial t = new Polynomial(coef, deg);
            q = q.plus(t);
            r = r.minus(t.times(b));
        }//end while
        return new Polynomial[]{ q, r };
    }

    public Polynomial[] div(Double a, int b) {
        return div(new Polynomial(a, b));
    }

    public Polynomial[] div(Double c) {
        return div(new Polynomial(c));
    }

    public Polynomial[] div(int N) {
        return div((double)N);
    }

    // return a(b(x)) - compute using Horner's method
    public Polynomial compose(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0);
        for (int i = a.deg; i >= 0; i--) {
            Polynomial term = new Polynomial(a.coef[i], 0);
            c = term.plus(b.times(c));
        }
        return c;
    }

    // get the coefficient for the highest degree
    public Double coeff() {return coeff(degree()); }


    // get the coefficient for degree d
    public Double coeff(int degree) {
        if (degree > this.degree()) throw new RuntimeException("bad degree");
        return coef[degree];
    }

    // do a and b represent the same polynomial?
    public boolean eq(Polynomial b) {
        Polynomial a = this;
        if (a.deg != b.deg) return false;
        for (int i = a.deg; i >= 0; i--)
            if (!a.coef[i].equals(b.coef[i])) return false;
        return true;
    }


    // test whether or not this polynomial is zero
    public boolean isZero() {
        for (Double i : coef) if (!i.equals(0d)) return false;
        return true;
    }


    // use Horner's method to compute and return the polynomial evaluated at x
    public Double evaluate(Double d) {
        Double p = 0d;
        for (int i = deg; i >= 0; i--)
            p = coef[i] + (d * p);
        return p;
    }

    public Complex evaluate(Complex z) {
        Complex s = new Complex();
        for (int i = 0; i <= deg; i++)
            if (!coef[i].equals(0d))
                s = s.plus(z.pow(i).times(coef[i]));
        return s;
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
            if (!coef[i].equals(0d)) {
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
                s = new StringBuilder(coef[i].toString());
        return s.toString();
    }


    // test client
    public static void main(String[] args) {
        Polynomial zero = new Polynomial(0);
        Polynomial p = new Polynomial(4d, 3).plus(3d, 2).plus(1d);   // 4x^3 + 3x^2 + 1
        Polynomial q = new Polynomial(1d, 1).plus(3d, 0);

        Polynomial r = p.plus(q);
        Polynomial s = p.times(q);
        Polynomial t = p.compose(q);

        System.out.println("zero(x)     = " + zero);
        System.out.println("p(x)        = " + p);
        System.out.println("q(x)        = " + q);
        System.out.println("p(x) + q(x) = " + r);
        System.out.println("p(x) * q(x) = " + s);
        System.out.println("p(q(x))     = " + t);
        System.out.println("0 - p(x)    = " + zero.minus(p));
        System.out.println("p(3)        = " + p.evaluate(3d));
        System.out.println("p'(x)       = " + p.differentiate());
        System.out.println("p''(x)      = " + p.differentiate().differentiate());
        System.out.printf("(%s) / (%s): %s, %s", p, q, p.div(q)[0], p.div(q)[1]);
    }

}