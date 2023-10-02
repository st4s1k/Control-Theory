package com.st4s1k.ctt.model;

import lombok.Value;

@Value
public class TransferFunction {

    Polynomial a;
    Polynomial b;

    private TransferFunction(Polynomial b, Polynomial a) {
        this.b = b;
        this.a = a;
    }

    private TransferFunction(TransferFunction h) {
        a = h.a;
        b = h.b;
    }

    public static TransferFunction of(Polynomial b, Polynomial a) {
        return new TransferFunction(b, a);
    }

    /**
     * Sum:
     * <pre>
     *    B     b     B a + b A
     *   --- + --- = -----------
     *    A     a        A a
     * </pre>
     */

    public TransferFunction plus(TransferFunction h) {
        if (a.equals(h.a)) {
            return new TransferFunction(b.plus(h.b), a);
        }
        return new TransferFunction(
                b.times(h.a).plus(h.b.times(a)),
                a.times(h.a)
        ).simplify();
    }

    public TransferFunction plus(Polynomial polynomial) {
        return plus(from(polynomial));
    }

    public TransferFunction plus(double d) {
        return plus(Polynomial.of(d, 0));
    }

    /**
     * Difference:
     * <pre>
     *    B     b     B a - b A
     *   --- - --- = -----------
     *    A     a        A a
     * </pre>
     */

    public TransferFunction minus(TransferFunction h) {
        if (this.a.equals(h.a)) {
            return new TransferFunction(
                    this.b.minus(h.b),
                    this.a
            );
        }
        return new TransferFunction(
                this.b.times(h.a).minus(h.b.times(this.a)),
                this.a.times(h.a)
        ).simplify();
    }

    public TransferFunction minus(Polynomial polynomial) {
        return minus(from(polynomial));
    }

    public TransferFunction minus(double d) {
        return minus(Polynomial.of(d, 0));
    }

    /**
     * Multiplication:
     * <pre>
     *    B     b     B b
     *   --- * --- = -----
     *    A     a     A a
     * </pre>
     */

    public TransferFunction times(TransferFunction h) {
        if (this.a.equals(h.b)) {
            return new TransferFunction(this.b, h.a);
        }
        if (this.b.equals(h.a)) {
            return new TransferFunction(h.b, this.a);
        }
        return new TransferFunction(
                this.b.times(h.b),
                this.a.times(h.a)
        ).simplify();
    }

    public TransferFunction times(Polynomial polynomial) {
        return times(from(polynomial));
    }

    public static TransferFunction from(Polynomial polynomial) {
        return new TransferFunction(polynomial, Polynomial.of(1, 0));
    }

    public TransferFunction times(double coefficient) {
        return times(Polynomial.of(coefficient, 0));
    }

    /**
     * Division:
     * <pre>
     *    B     b     B     a     B a
     *   --- : --- = --- * --- = -----
     *    A     a     A     b     A b
     * </pre>
     */

    public TransferFunction div(TransferFunction h) {
        if (this.a.equals(h.a)) {
            return new TransferFunction(this.b, h.b);
        }
        if (this.b.equals(h.b)) {
            return new TransferFunction(h.a, this.a);
        }
        return new TransferFunction(
                this.b.times(h.a),
                this.a.times(h.b)
        ).simplify();
    }

    public TransferFunction div(Polynomial polynomial) {
        return div(from(polynomial));
    }

    public TransferFunction div(double coefficient) {
        return div(Polynomial.of(coefficient, 0));
    }

    /**
     * Normalization
     */

    public TransferFunction normalize() {
        return new TransferFunction(
                b.div(a.coefficient())[0],
                a.div(a.coefficient())[0]
        );
    }

    /**
     * Simplification
     */

    public TransferFunction simplify() {
        if (a.degree() > 0 &&
                b.degree() > 0 &&
                a.coefficient(0) == 0 &&
                b.coefficient(0) == 0 &&
                a.isNotZero() &&
                b.isNotZero()) {
            Polynomial p = (a.degree() < b.degree() ? a : b);
            for (int i = 0; i <= p.degree(); i++) {
                if (p.coefficient(i) != 0) {
                    return new TransferFunction(
                            b.div(1, i)[0],
                            a.div(1, i)[0]
                    );
                }
            }
        }
        return this;
    }

    /**
     * PID
     */

    public TransferFunction pid(double kp, double ki, double kd) {
        TransferFunction p = from(Polynomial.of(kp, 0));
        TransferFunction i = new TransferFunction(Polynomial.of(ki, 0), Polynomial.of(1, 1));
        TransferFunction d = from(Polynomial.of(kd, 1));
        return times(p.plus(i).plus(d));
    }


    /**
     * Closed loop:
     * <pre>
     *                Hd(s)
     *   Hc(s) = --------------
     *           1 + Hf(s)Hd(s)
     * </pre>
     */

    public TransferFunction feedback(TransferFunction hf) {
        return new TransferFunction(div(times(hf).plus(1)));
    }

    public TransferFunction feedback() {
        return new TransferFunction(div(plus(1)));
    }

    public String toString() {
        return "( " + b + " ) / ( " + a + " ) ";
    }

    public Complex evaluate(double sigma, double omega) {
        Complex s = new Complex(sigma, omega);
        return new Complex(b.evaluate(s).div(a.evaluate(s)));
    }
}