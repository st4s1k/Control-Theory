

public class TransferFunction {
    public final Polynomial A;
    public final Polynomial B;

    public TransferFunction(Polynomial b, Polynomial a) {
        this.B = new Polynomial(b);
        this.A = new Polynomial(a);
    }

    public TransferFunction(TransferFunction h) {
        B = new Polynomial(h.B);
        A = new Polynomial(h.A);
    }

//  OPERATIONS

//  Sum

//   B     b     B a + b A
//  --- + --- = -----------
//   A     a        A a

    public TransferFunction plus(TransferFunction h) {
        if (this.A.equals(h.A))
            return new TransferFunction(
                    this.B.plus(h.B),
                    this.A
            );
        return new TransferFunction(
                this.B.times(h.A).plus(h.B.times(this.A)),
                this.A.times(h.A)
        ).simplify();
    }

    public TransferFunction plus(Polynomial p) {
        return this.plus(new TransferFunction(
                p,
                Polynomial.term(1, 0)
        ));
    }

    public TransferFunction plus(double d) {
        return this.plus(Polynomial.term(d, 0));
    }


//  Difference

//   B     b     B a - b A
//  --- - --- = -----------
//   A     a        A a

    public TransferFunction minus(TransferFunction h) {
        if (this.A.equals(h.A))
            return new TransferFunction(
                    this.B.minus(h.B),
                    this.A
            );
        return new TransferFunction(
                this.B.times(h.A).minus(h.B.times(this.A)),
                this.A.times(h.A)
        ).simplify();
    }

    public TransferFunction minus(Polynomial p) {
        return this.minus(new TransferFunction(
                p,
                Polynomial.term(1, 0)
        ));
    }

    public TransferFunction minus(double d) {
        return this.minus(Polynomial.term(d, 0));
    }


//  Multiplication

//   B     b     B b
//  --- * --- = -----
//   A     a     A a

    public TransferFunction times(TransferFunction h) {
        if (this.A.equals(h.B)) return new TransferFunction(this.B, h.A);
        if (this.B.equals(h.A)) return new TransferFunction(h.B, this.A);
        return new TransferFunction(
                this.B.times(h.B),
                this.A.times(h.A)
        ).simplify();
    }

    public TransferFunction times(Polynomial p) {
        return this.times(
                new TransferFunction(
                        p,
                        Polynomial.term(1, 0)
                )
        );
    }

    public TransferFunction times(double d) {
        return this.times(Polynomial.term(d, 0));
    }


//  Division

//   B     b     B     a     B a
//  --- : --- = --- * --- = -----
//   A     a     A     b     A b

    public TransferFunction div(TransferFunction h) throws DivisionByZeroException {
        if (h.B.isZero())
            throw new DivisionByZeroException(
                    "The divisor-transfer-function's numerator is zero.",
                    this.toString(),
                    h.toString()
            );
        if (this.A.equals(h.A)) return new TransferFunction(this.B, h.B);
        if (this.B.equals(h.B)) return new TransferFunction(h.A, this.A);
        return new TransferFunction(
                this.B.times(h.A),
                this.A.times(h.B)
        ).simplify();
    }

    public TransferFunction div(Polynomial p) throws DivisionByZeroException {
        return this.div(
                new TransferFunction(
                        p,
                        Polynomial.term(1, 0)
                )
        );
    }

    public TransferFunction div(double d) throws DivisionByZeroException {
        return this.div(Polynomial.term(d, 0));
    }


//  Normalization

    public TransferFunction norm() throws DivisionByZeroException {
        return new TransferFunction(
                B.div(A.coeff()),
                A.div(A.coeff())
        );
    }

//  Simplification

    public TransferFunction simplify() {
        if (A.degree() > 0 &&
                B.degree() > 0 &&
                A.coeff(0) == 0 &&
                B.coeff(0) == 0 &&
                !A.isZero() &&
                !B.isZero()) {
            Polynomial p = (A.degree() < B.degree() ? A : B);
            for (int i = 1; i <= p.degree(); i++)
                if (p.coeff(i) != 0) {
                    try {
                        return new TransferFunction(
                                B.div(Polynomial.term(1, i))[0],
                                A.div(Polynomial.term(1, i))[0]
                        );
                    } catch (DivisionByZeroException e) {
                        return this;
                    }
                }
        }
        return this;
    }

//  PID

    public static TransferFunction proportionalGain(double kp) {
        return new TransferFunction(
                Polynomial.term(kp, 0),
                Polynomial.term(1, 0)
        );
    }

    public static TransferFunction integralGain(double ki) {
        return new TransferFunction(
                Polynomial.term(ki, 0),
                Polynomial.term(1, 1)
        );
    }

    public static TransferFunction derivativeGain(double kd) {
        return new TransferFunction(
                Polynomial.term(kd, 1),
                Polynomial.term(1, 0)
        );
    }

    public static TransferFunction serial(TransferFunction tf0,
                                          TransferFunction ... transferFunctions) {
        TransferFunction product = new TransferFunction(tf0);
        for (TransferFunction tf: transferFunctions) { product.times(tf); }
        return product;
    }

    public static TransferFunction parallel(TransferFunction tf0,
                                            TransferFunction ... transferFunctions) {
        TransferFunction sum = new TransferFunction(tf0);
        for (TransferFunction tf: transferFunctions) { sum.plus(tf); }
        return sum;
    }

    public TransferFunction PID(double kp, double ki, double kd) {
        return new TransferFunction(parallel(
                        proportionalGain(kp),
                        integralGain(ki),
                        derivativeGain(kd)
                ).times(this)
        );
    }


//  Closed loop

//                        H_open(s)
//  H_closed(s) = ---------------------------
//                1 + H_feedback(s)*H_open(s)

    public TransferFunction feedback(TransferFunction hFeedback) throws DivisionByZeroException {
        return new TransferFunction(
                this.div(
                        this.times(hFeedback).plus(1)
                )
        );
    }

//                  H_open(s)
//  H_closed(s) = -------------
//                1 + H_open(s)

    public TransferFunction feedback() throws DivisionByZeroException {
        return new TransferFunction(
                this.div(
                        this.plus(1)
                )
        );
    }

    public String toString() {
        return "( " + B + " ) / ( " + A + " ) ";
    }

    public Complex evaluate(Complex s) throws DivisionByZeroException {
        return new Complex(B.evaluate(s).div(A.evaluate(s)));
    }
}