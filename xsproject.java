public class xsproject {
    public static void main(String[] args) {
        TransferFunction H = new TransferFunction(
                new Polynomial(9d),
                new Polynomial(0d, 1d, 15d, 50d)
        );
        TransferFunction H_norm = new TransferFunction(H.norm());

        System.out.println("     H(s) = " + H);
        System.out.println("H.norm(s) = " + H.norm());
        System.out.println("H.PID(s) = " + H.PID(1d, 2d, 3d).feedback());
    }
}
