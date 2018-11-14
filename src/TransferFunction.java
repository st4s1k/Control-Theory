

public class TransferFunction {
    private Polynomial A;
    private Polynomial B;

    public TransferFunction(Polynomial B, Polynomial A) {
        this.B = new Polynomial(B);
        this.A = new Polynomial(A);
    }

    public TransferFunction(TransferFunction h) {
        A = new Polynomial(h.getA());
        B = new Polynomial(h.getB());
    }

    // GETTERS

    public Polynomial getA() { return new Polynomial(A); }

    public Polynomial getB(){
        return new Polynomial(B);
    }


//  OPERATIONS

//  Sum

//   B     b     B a + b A
//  --- + --- = -----------
//   A     a        A a

    public TransferFunction plus(TransferFunction h) {
        Polynomial b = h.getB();
        Polynomial a = h.getA();
        if (A.eq(a))
            return new TransferFunction(
                    B.plus(b),
                    A
            );
        return new TransferFunction(
                B.times(a).plus(b.times(A)),
                A.times(a)
        ).simplify();
    }

    public TransferFunction plus(Polynomial p) {
        return plus(new TransferFunction(
                p,
                new Polynomial(1, 0)
        ));
    }

    public TransferFunction plus(double d) {
        return plus(new Polynomial(d, 0));
    }


//  Difference

//   B     b     B a - b A
//  --- - --- = -----------
//   A     a        A a

    public TransferFunction minus(TransferFunction h) {
        Polynomial a = h.getA();
        Polynomial b = h.getB();
        if (A.eq(a))
            return new TransferFunction(
                    B.minus(b),
                    A
            );
        return new TransferFunction(
                B.times(a).minus(b.times(A)),
                A.times(a)
        ).simplify();
    }

    public TransferFunction minus(Polynomial p) {
        return minus(new TransferFunction(
                p,
                new Polynomial(1, 0)
        ));
    }

    public TransferFunction minus(double d) {
        return minus(new Polynomial(d, 0));
    }


//  Multiplication

//   B     b     B b
//  --- * --- = -----
//   A     a     A a

    public TransferFunction times(TransferFunction h) {
        Polynomial a = h.getA();
        Polynomial b = h.getB();
        if (A.eq(b)) return new TransferFunction(B, a);
        if (B.eq(a)) return new TransferFunction(b, A);
        return new TransferFunction(
                B.times(b),
                A.times(a)
        ).simplify();
    }

    public TransferFunction times(Polynomial p) {
        return times(
                new TransferFunction(
                        p,
                        new Polynomial(1, 0)
                )
        );
    }

    public TransferFunction times(double d) {
        return times(new Polynomial(d, 0));
    }


//  Division

//   B     b     B     a     B a
//  --- : --- = --- * --- = -----
//   A     a     A     b     A b

    public TransferFunction div(TransferFunction h) {
        Polynomial a = h.getA();
        Polynomial b = h.getB();
        if (A.eq(a)) return new TransferFunction(B, b);
        if (B.eq(b)) return new TransferFunction(a, A);
        return new TransferFunction(
                B.times(a),
                A.times(b)
        ).simplify();
    }

    public TransferFunction div(Polynomial p) {
        return div(
                new TransferFunction(
                        p,
                        new Polynomial(1, 0)
                )
        );
    }

    public TransferFunction div(double d) {
        return div(new Polynomial(d, 0));
    }


//  Normalization

    public TransferFunction norm() {
        return new TransferFunction(
                B.div(A.coeff())[0],
                A.div(A.coeff())[0]
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
            for (int i = 0; i <= p.degree(); i++)
                if (p.coeff(i) != 0)
                    return new TransferFunction(
                            B.div(1, i)[0],
                            A.div(1, i)[0]
                    );
        }
        return this;
    }

//  pid

    public TransferFunction pid(double kp, double ki, double kd) {
        TransferFunction p = new TransferFunction(new Polynomial(kp, 0), new Polynomial(1, 0));
        TransferFunction i = new TransferFunction(new Polynomial(ki, 0), new Polynomial(1, 1));
        TransferFunction d = new TransferFunction(new Polynomial(kd, 1), new Polynomial(1, 0));
        return times(p.plus(i).plus(d));
    }


//  Closed loop

//               Hd(s)
//  Hc(s) = --------------
//          1 + Hf(s)Hd(s)

    public TransferFunction feedback(TransferFunction hf) {
        return new TransferFunction(div(times(hf).plus(1)));
    }

    public TransferFunction feedback() {
        return new TransferFunction(div(plus(1)));
    }

    public String toString() {
        return "( " + B + " ) / ( " + A + " ) ";
    }

    public Complex evaluate(double sigma, double omega) throws DivisionByZeroException{
        Complex s = new Complex(sigma, omega);
        return new Complex(B.evaluate(s).div(A.evaluate(s)));
    }
}