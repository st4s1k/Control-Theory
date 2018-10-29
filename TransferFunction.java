public class TransferFunction {
    private Polynomial A;
    private Polynomial B;

    public TransferFunction(Polynomial a, Polynomial b) {
        A = new Polynomial(a);
        B = new Polynomial(b);
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


    // OPERATIONS
    public TransferFunction Sum(TransferFunction h) {
        return new TransferFunction(
                A.times(h.getA()),
                B.times(h.getA()).plus(A.times(h.getB()))
        );
    }

    public TransferFunction norm(){
//        System.out.println("A.coeff() = " + A.coeff());
//        System.out.println("A.div(A.coeff()) = " + A.div(A.coeff()));
//        System.out.println("B.div(A.coeff()) = " + B.div(A.coeff()));
        return new TransferFunction(
                A.div(A.coeff()),
                B.div(A.coeff()));
    }

    public String toString() {
        return "(" + B + ")/(" + A + ")";
    }
}