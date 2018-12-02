import exceptions.DivisionByZeroException;
import org.apache.commons.math3.complex.Complex;

public class TransferFunction {
    public final MonoVarPoly A;
    public final MonoVarPoly B;

    public TransferFunction(MonoVarPoly b, MonoVarPoly a) {
        this.B = new MonoVarPoly(b).setSym('s');
        this.A = new MonoVarPoly(a).setSym('s');
    }

    public TransferFunction(TransferFunction h) {
        B = new MonoVarPoly(h.B).setSym('s');
        A = new MonoVarPoly(h.A).setSym('s');
    }

//  OPERATIONS

//  Sum

//   B     b     B a + b A
//  --- + --- = -----------
//   A     a        A a

    public TransferFunction add(TransferFunction h) {
        if (this.A.equals(h.A))
            return new TransferFunction(
                    this.B.add(h.B),
                    this.A
            );
        return new TransferFunction(
                this.B.multiply(h.A).add(h.B.multiply(this.A)),
                this.A.multiply(h.A)
        ).simplify();
    }

    public TransferFunction add(MonoVarPoly p) {
        return this.add(new TransferFunction(
                p,
                MonoVarPoly.term(1, 0)
        ));
    }

    public TransferFunction add(double d) {
        return this.add(MonoVarPoly.term(d, 0));
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
                this.B.multiply(h.A).minus(h.B.multiply(this.A)),
                this.A.multiply(h.A)
        ).simplify();
    }

    public TransferFunction minus(MonoVarPoly p) {
        return this.minus(new TransferFunction(
                p,
                MonoVarPoly.term(1, 0)
        ));
    }

    public TransferFunction minus(double d) {
        return this.minus(MonoVarPoly.term(d, 0));
    }


//  Multiplication

//   B     b     B b
//  --- * --- = -----
//   A     a     A a

    public TransferFunction multiply(TransferFunction h) {
        if (this.A.equals(h.B)) return new TransferFunction(this.B, h.A);
        if (this.B.equals(h.A)) return new TransferFunction(h.B, this.A);
        return new TransferFunction(
                this.B.multiply(h.B),
                this.A.multiply(h.A)
        ).simplify();
    }

    public TransferFunction multiply(MonoVarPoly p) {
        return this.multiply(
                new TransferFunction(
                        p,
                        MonoVarPoly.term(1, 0)
                )
        );
    }

    public TransferFunction multiply(double d) {
        return this.multiply(MonoVarPoly.term(d, 0));
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
                this.B.multiply(h.A),
                this.A.multiply(h.B)
        ).simplify();
    }

    public TransferFunction div(MonoVarPoly p) throws DivisionByZeroException {
        return this.div(
                new TransferFunction(
                        p,
                        MonoVarPoly.term(1, 0)
                )
        );
    }

    public TransferFunction div(double d) throws DivisionByZeroException {
        return this.div(MonoVarPoly.term(d, 0));
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
            MonoVarPoly p = (A.degree() < B.degree() ? A : B);
            for (int i = 1; i <= p.degree(); i++)
                if (p.coeff(i) != 0) {
                    try {
                        return new TransferFunction(
                                B.div(MonoVarPoly.term(1, i))[0],
                                A.div(MonoVarPoly.term(1, i))[0]
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
                MonoVarPoly.term(kp, 0),
                MonoVarPoly.term(1, 0)
        );
    }

    public static TransferFunction integralGain(double ki) {
        return new TransferFunction(
                MonoVarPoly.term(ki, 0),
                MonoVarPoly.term(1, 1)
        );
    }

    public static TransferFunction derivativeGain(double kd) {
        return new TransferFunction(
                MonoVarPoly.term(kd, 1),
                MonoVarPoly.term(1, 0)
        );
    }

    public static TransferFunction prod(TransferFunction tf0,
                                          TransferFunction ... transferFunctions) {
        TransferFunction product = new TransferFunction(tf0);
        for (TransferFunction tf: transferFunctions) { product.multiply(tf); }
        return product;
    }

    public static TransferFunction sum(TransferFunction tf0,
                                            TransferFunction ... transferFunctions) {
        TransferFunction sum = new TransferFunction(tf0);
        for (TransferFunction tf: transferFunctions) { sum.add(tf); }
        return sum;
    }

    public TransferFunction PID(double kp, double ki, double kd) {
        return new TransferFunction(sum(
                        proportionalGain(kp),
                        integralGain(ki),
                        derivativeGain(kd)
                ).multiply(this)
        );
    }


//  Closed loop

//                        H_open(s)
//  H_closed(s) = ---------------------------
//                1 + H_feedback(s)*H_open(s)

    public TransferFunction feedback(TransferFunction hFeedback) throws DivisionByZeroException {
        return new TransferFunction(
                this.div(
                        this.multiply(hFeedback).add(1)
                )
        );
    }

//                  H_open(s)
//  H_closed(s) = -------------
//                1 + H_open(s)

    public TransferFunction feedback() throws DivisionByZeroException {
        return new TransferFunction(
                this.div(
                        this.add(1)
                )
        );
    }

    public String toString() {
        return "( " + B + " ) / ( " + A + " ) ";
    }

    public Complex evaluate(Complex s) throws DivisionByZeroException {
        return B.evaluate(s).divide(A.evaluate(s));
    }

    public static void main(String[] args) {

        TransferFunction h = new TransferFunction(
                MonoVarPoly.term(9, 0),
                MonoVarPoly.term(50, 3)
                        .add(MonoVarPoly.term(15, 2))
                        .add(MonoVarPoly.term(1, 1))
        );

//                        9                    9
//         H(s) = ------------------ = -----------------
//                s(10s + 1)(5s + 1)   50s^3 + 15s^2 + s

        System.out.println("H(s)= " + h.A);
        Complex s = new Complex(1, 2);
        System.out.println("s = " + s);
        System.out.println("H(" + s + ") = " + h.A.evaluate(s) + "\n");

        try {
            System.out.println("                  H(s) = " + h);
            System.out.println("              H.PID(s) = " + h.PID(0.0056, 0, 0.056));
            System.out.println("     H.PID.feedback(s) = " + h.PID(0.0056, 0, 0.056).feedback());
            System.out.println("             H.norm(s) = " + h.norm());
            System.out.println("         H.norm.PID(s) = " + h.norm().PID(0.0056, 0, 0.056));
            System.out.println("H.norm.PID.feedback(s) = " + h.norm().PID(0.0056, 0, 0.056).feedback());
        } catch (DivisionByZeroException e) {
            e.printStackTrace();
        }

    }
}