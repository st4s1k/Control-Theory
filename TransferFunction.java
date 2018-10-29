public class TransferFunction {
    private Polynomial A;
    private Polynomial B;

    public TransferFunction() {
        A = null;
        B = null;
    }

    public TransferFunction(Polynomial a, Polynomial b) {
        A = new Polynomial(a);
        B = new Polynomial(b);
    }

    // GETTERS

    public Polynomial getA(){
        return A;
    }

    public Polynomial getB(){
        return B;
    }

    // SETTERS

    void setA(Polynomial a){
        A = a;
    }

    void setB(Polynomial b){
        B = b;
    }

    // OPERATIONS

    public TransferFunction Sum(TransferFunction h) {
        return new TransferFunction(
                A.times(h.getA()),
                B.times(h.getA()).plus(A.times(h.getB()))
        );
    }

    public TransferFunction Normalize(){
        return new TransferFunction(
                A.div(A.coeff()),
                B.div(A.coeff()));
    }
}