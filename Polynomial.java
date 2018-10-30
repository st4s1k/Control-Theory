// credit: https://gist.github.com/derlin/40545e447fffe7599d26d0a435d9b113

public class Polynomial{
    private Double[] coef;  // coefficients
    private int deg;     // degree of polynomial (0 for the zero polynomial)

    public Polynomial(Double coef, int deg) {
        this.coef = new Double[deg + 1];
        for (int i = 0; i < deg; i++) this.coef[i] = 0d;
        this.coef[deg] = coef;
        this.deg = deg;
    }

    public Polynomial(int degree) {
        coef = new Double[degree + 1];
        for (int i = 0; i <= degree; i++) coef[i] = 0d;
        deg = degree;
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
            if (coef[i] != 0) d = i;
        return d;
    }


    // return c = a + b
    public Polynomial plus(Polynomial p) {
        Polynomial c = new Polynomial(0d, Math.max(deg, p.deg));
        for (int i = 0; i <= deg; i++) c.coef[i] += coef[i];
        for (int i = 0; i <= p.deg; i++) c.coef[i] += p.coef[i];
        c.deg = c.degree();
        return c;
    }

    public Polynomial plus(Double coef, int deg) {
        return this.plus(new Polynomial(coef, deg));
    }


    // return (a - b)
    public Polynomial minus(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0d, Math.max(a.deg, b.deg));
        for (int i = 0; i <= a.deg; i++) c.coef[i] += a.coef[i];
        for (int i = 0; i <= b.deg; i++) c.coef[i] -= b.coef[i];
        c.deg = c.degree();
        return c;
    }

    public Polynomial minus(Double a, int b) {
        return this.minus(new Polynomial(a, b));
    }

    // return (a * b)
    public Polynomial times(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0d, a.deg + b.deg);
        for (int i = 0; i <= a.deg; i++)
            for (int j = 0; j <= b.deg; j++)
                c.coef[i + j] += (a.coef[i] * b.coef[j]);
        c.deg = c.degree();
        return c;
    }

    public Polynomial times(Double a, int b) {
        return this.times(new Polynomial(a, b));
    }

    public Polynomial times(Double c) {
        return this.times(new Polynomial(c));
    }


    // get the coefficient for the highest degree
    public Double coeff() {return coeff(degree()); }


    // get the coefficient for degree d
    public Double coeff(int degree) {
        if (degree > this.degree()) throw new RuntimeException("bad degree");
        return coef[degree];
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
        Polynomial q = new Polynomial(0d, 0);
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
        return this.div(new Polynomial(a, b));
    }

    public Polynomial[] div(Double c) {
        return this.div(new Polynomial(c));
    }

    // return a(b(x)) - compute using Horner's method
    public Polynomial compose(Polynomial b) {
        Polynomial a = this;
        Polynomial c = new Polynomial(0d, 0);
        for (int i = a.deg; i >= 0; i--) {
            Polynomial term = new Polynomial(a.coef[i], 0);
            c = term.plus(b.times(c));
        }
        return c;
    }


    // do a and b represent the same polynomial?
    public boolean eq(Polynomial b) {
        Polynomial a = this;
        if (a.deg != b.deg) return false;
        for (int i = a.deg; i >= 0; i--)
            if (a.coef[i] != b.coef[i]) return false;
        return true;
    }


    // test wether or not this polynomial is zero
    public boolean isZero() {
        for (Double i : coef) {
            if (i != 0) return false;
        }//end for
        return true;
    }


    // use Horner's method to compute and return the polynomial evaluated at x
    public Double evaluate(int x) {
        double p = 0d;
        for (int i = deg; i >= 0; i--)
            p = coef[i] + (x * p);
        return p;
    }


    // differentiate this polynomial and return it
    public Polynomial differentiate() {
        if (deg == 0) return new Polynomial(0d, 0);
        Polynomial deriv = new Polynomial(0d, deg - 1);
        deriv.deg = deg - 1;
        for (int i = 0; i < deg; i++)
            deriv.coef[i] = (i + 1) * coef[i + 1];
        return deriv;
    }


    // convert to string representation
    public String toString() {
        if (deg == 0) return "" + coef[0];
        if (deg == 1) return coef[1] + "s + " + coef[0];
        String s = coef[deg] + "s^" + deg;
        for (int i = deg - 1; i >= 0; i--) {
            if (coef[i] == 0) {
                continue;
            }
            else if (coef[i] > 0) {
                s = s + " + " + (coef[i]);
            }
            else if (coef[i] < 0) s = s + " - " + (-coef[i]);
            if (i == 1) {
                s = s + "s";
            }
            else if (i > 1) s = s + "s^" + i;
        }
        return s;
    }


    // test client
    public static void main(String[] args) {
        Polynomial zero = new Polynomial(0d, 0);

        Polynomial p1 = new Polynomial(1d, 3);
        Polynomial p2 = new Polynomial(2d, 2);
        Polynomial p3 = new Polynomial(4d, 0);
        Polynomial p4 = new Polynomial(0d, 1);
        Polynomial p = p1.plus(p2).plus(p3).plus(p4);   // 4x^3 + 3x^2 + 1

        Polynomial q1 = new Polynomial(1d, 1);
        Polynomial q2 = new Polynomial(3d, 0);
        Polynomial q = q1.plus(q2);                     // 3x^2 + 5


        Polynomial r = p.plus(q);
        Polynomial s = p.times(q);
        Polynomial t = p.compose(q);

        System.out.println("zero(x) =     " + zero);
        System.out.println("p(x) =        " + p);
        System.out.println("q(x) =        " + q);
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