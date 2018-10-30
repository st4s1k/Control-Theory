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
        return new TransferFunction(
                B.times(a).plus(b.times(A)),
                A.times(a)
        );
    }

    public TransferFunction plus(Polynomial p) {
        Polynomial a = p;
        Polynomial b = new Polynomial(1d);
        return new TransferFunction(
                B.times(a).plus(b.times(A)),
                A.times(a)
        );
    }

    public TransferFunction plus(Double d) {
        Polynomial a = new Polynomial(d);
        Polynomial b = new Polynomial(1d);
        return new TransferFunction(
                B.times(a).plus(b.times(A)),
                A.times(a)
        );
    }


//  Difference

//   B     b     B a - b A
//  --- - --- = -----------
//   A     a        A a

    public TransferFunction minus(TransferFunction h) {
        Polynomial a = h.getA();
        Polynomial b = h.getB();
        return new TransferFunction(
                B.times(a).minus(b.times(A)),
                A.times(a)
        );
    }

    public TransferFunction minus(Polynomial p) {
        Polynomial a = p;
        Polynomial b = new Polynomial(1d);
        return new TransferFunction(
                B.times(a).minus(b.times(A)),
                A.times(a)
        );
    }

    public TransferFunction minus(Double d) {
        Polynomial a = new Polynomial(d);
        Polynomial b = new Polynomial(1d);
        return new TransferFunction(
                B.times(a).minus(b.times(A)),
                A.times(a)
        );
    }


//  Multiplication

//   B     b     B b
//  --- * --- = -----
//   A     a     A a

    public TransferFunction times(TransferFunction h) {
        Polynomial a = h.getA();
        Polynomial b = h.getB();
        return new TransferFunction(
                B.times(b),
                A.times(a)
        );
    }

    public TransferFunction times(Polynomial p) {
        return this.times(new TransferFunction(p, new Polynomial(1d)));
    }


//  Division

//   B     b     B     a     B a
//  --- : --- = --- * --- = -----
//   A     a     A     b     A b

    public TransferFunction div(TransferFunction h) {
        Polynomial a = h.getA();
        Polynomial b = h.getB();
        return new TransferFunction(
                B.times(a),
                A.times(b)
        );
    }


//  Normalization

    public TransferFunction norm() {
        return new TransferFunction(
                B.div(A.coeff())[0],
                A.div(A.coeff())[0]
        );
    }


//  PID

    public TransferFunction PID(double kp, double ki, double kd) {
        Polynomial p = new Polynomial(kp);
        Polynomial i = new Polynomial(ki).div(1d, 1)[0];
        Polynomial d = new Polynomial(kd).times(1d, 1);
        return this.times(p.plus(i).plus(d));
    }


//  Closed loop

//               Hd(s)
//  Hc(s) = --------------
//          1 + Hf(s)Hd(s)

    public TransferFunction feedback(TransferFunction hf) {
        TransferFunction hd = this;
        return new TransferFunction(hd.div(hd.times(hf).plus(1d)));
    }

    public TransferFunction feedback() {
        TransferFunction hd = this;
        return new TransferFunction(hd.div(hd.plus(1d)));
    }

    public String toString() {
        return "( " + B + " ) / ( " + A + " ) ";
    }
}