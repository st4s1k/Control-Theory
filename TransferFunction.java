import javax.swing.table.TableRowSorter;

public class TransferFunction {
    private Polynomial A;
    private Polynomial B;

    public TransferFunction(Polynomial b, Polynomial a) {
        B = new Polynomial(b);
        A = new Polynomial(a);
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

    // SETTERS

    void setA(Polynomial a) { A = new Polynomial(a); }

    void setB(Polynomial b){
        B = new Polynomial(b);
    }


//  OPERATIONS

//  Sum

//   B     b     B a + b A
//  --- + --- = -----------
//   A     a        A a

    public TransferFunction plus(TransferFunction h) {
        Polynomial a = h.getA();
        Polynomial b = h.getB();
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
        return this.plus(new TransferFunction(
                p,
                new Polynomial(1d)
        ));
    }

    public TransferFunction plus(Double d) {
        return this.plus(new Polynomial(d));
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
        return this.minus(new TransferFunction(
                p,
                new Polynomial(1d)
        ));
    }

    public TransferFunction minus(Double d) {
        return this.minus(new Polynomial(d));
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
        return this.times(
                new TransferFunction(
                        p,
                        new Polynomial(1d)
                )
        );
    }

    public TransferFunction times(Double d) {
        return this.times(new Polynomial(d));
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
        return this.div(
                new TransferFunction(
                        p,
                        new Polynomial(1d))
        );
    }

    public TransferFunction div(Double d) {
        return this.div(new Polynomial(d));
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
                A.coeff(0).equals(0d) &&
                B.coeff(0).equals(0d) &&
                !A.isZero() &&
                !B.isZero()) {
            Polynomial p = (A.degree() < B.degree() ? A : B);
            for (int i = 0; i <= p.degree(); i++)
                if (!p.coeff(i).equals(0d))
                    return new TransferFunction(
                            B.div(1d, i)[0],
                            A.div(1d, i)[0]
                    );
        }
        return this;
    }

//  PID

    public TransferFunction PID(Double kp, Double ki, Double kd) {
        if (kp.isNaN() || ki.isNaN() || kd.isNaN()) throw new RuntimeException("PID: invalid arguments (kp, ki, kd)");
        TransferFunction p = new TransferFunction(new Polynomial(kp), new Polynomial(1d));
        TransferFunction i = new TransferFunction(new Polynomial(ki), new Polynomial(1d, 1));
        TransferFunction d = new TransferFunction(new Polynomial(kd, 1), new Polynomial(1d));
//        System.out.println("    p= " + p);
//        System.out.println("    i= " + i);
//        System.out.println("    d= " + d);
//        System.out.println("  p+i= " + p.plus(i));
//        System.out.println("  p+d= " + p.plus(d));
//        System.out.println("  i+d= " + i.plus(d));
//        System.out.println("p+i+d= " + p.plus(i).plus(d));
        return this.times(p.plus(i).plus(d));
    }


//  Closed loop

//               Hd(s)
//  Hc(s) = --------------
//          1 + Hf(s)Hd(s)

    public TransferFunction feedback(TransferFunction hf) {
        return new TransferFunction(this.div(this.times(hf).plus(1d)));
    }

    public TransferFunction feedback() {
        return new TransferFunction(this.div(this.plus(1d)));
    }

    public String toString() {
        return "( " + B + " ) / ( " + A + " ) ";
    }
}