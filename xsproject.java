public class xsproject {
    public static void main(String[] args) {
        TransferFunction H = new TransferFunction(
                new Polynomial(9d),
                new Polynomial(0d, 1d, 15d, 50d)
        );
        TransferFunction H_norm = new TransferFunction(H.norm());

//        System.out.println("                  H(s) = " + H);
//        System.out.println("              H.PID(s) = " + H.PID(1d, 1d, 1d));
//        System.out.println("     H.PID.feedback(s) = " + H.PID(1d, 1d, 1d).feedback());
//        System.out.println("             H.norm(s) = " + H.norm());
//        System.out.println("         H.norm.PID(s) = " + H.norm().PID(1d, 1d, 1d));
//        System.out.println("H.norm.PID.feedback(s) = " + H.norm().PID(1d, 1d, 1d).feedback());
//        System.out.println("                     Z = " + new Polynomial(H.getA()).evaluate(new Complex(1d, 1d)));


        Polynomial P = new Polynomial(H.getA());
        Complex z;
        Complex z_last = new Complex(0d, 0d);

        int width = 500;
        int height = 500;

        StdDraw.setCanvasSize(width, height);
        StdDraw.filledRectangle(0, 0, 1, 1);
        StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.YELLOW);

        for (int i = 0; i < 1000; i++) {
            z = P.evaluate(new Complex(1d, (double) i / 10d));
            System.out.println(z.plus(new Complex((double)width/2, (double)height/2)));
            StdDraw.line(width/2 + z_last.getRe(), height/2 + z_last.getIm(),width/2 +  z.getRe(), height/2 + z.getIm());
            z_last = z;
        }
    }
}
