public class xsproject {
    public static void main(String[] args) {
        TransferFunction H = new TransferFunction(
                new Polynomial(1d,2).plus(2d,3),
                new Polynomial(1d,2).plus(2d,3));

        TransferFunction H_norm = new TransferFunction(H.norm());

        System.out.println("     H(s) = " + H);
        System.out.println("H.norm(s) = " + H.norm());
        System.out.println("H_norm(s) = " + H_norm);
    }
}
