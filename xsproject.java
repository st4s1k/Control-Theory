public class xsproject {
    public static void main(String[] args) {
        TransferFunction H = new TransferFunction(
                new Polynomial(9d),
                new Polynomial(0d, 1d, 15d, 50d)
        );
        TransferFunction H_norm = new TransferFunction(H.norm());

        System.out.println("                  H(s) = " + H);
        System.out.println("         H.feedback(s) = " + H.feedback());
        System.out.println("              H.PID(s) = " + H.PID(1d, 1d, 1d));
        System.out.println("     H.PID.feedback(s) = " + H.PID(1d, 1d, 1d).feedback());
        System.out.println("             H.norm(s) = " + H.norm());
        System.out.println("    H.norm.feedback(s) = " + H.norm().feedback());
        System.out.println("         H.norm.PID(s) = " + H.norm().PID(1d, 1d, 1d));
        System.out.println("H.norm.PID.feedback(s) = " + H.norm().PID(1d, 1d, 1d).feedback());
    }
}
