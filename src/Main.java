import javax.swing.*;

public class Main extends JFrame {

//    public Main() {
//        super("hello");
//        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        this.add(new JLabel("Hello, world!"));
//        this.pack();
//        this.setVisible(true);
//    }

    public static void main(String[] args) {

        TransferFunction h = new TransferFunction(
                Polynomial.term(9, 0),
                Polynomial.term(50, 3)
                        .plus(Polynomial.term(15, 2))
                        .plus(Polynomial.term(1, 1))
        );

//                        9                    9
//         H(s) = ------------------ = -----------------
//                s(10s + 1)(5s + 1)   50s^3 + 15s^2 + s

        System.out.println("H(s)= " + h.A);
        Complex s = new Complex(1, 2);
        System.out.println("s = " + s);
        System.out.println("H(" + s + ")= " + h.A.evaluate(s) + "\n");

        try {
            System.out.println("                  H(s) = " + h);
            System.out.println("              H.PID(s) = " + h.PID(0.0056, 0, 0.056));
            System.out.println("     H.PID.feedback(s) = " + h.PID(0.0056, 0, 0.056).feedback());
            System.out.println("             H.norm(s) = " + h.norm());
            System.out.println("         H.norm.PID(s) = " + h.norm().PID(0.0056, 0, 0.056));
            System.out.println("H.norm.PID.feedback(s) = " + h.norm().PID(0.0056, 0, 0.056).feedback());
        } catch (DivisionByZeroException e) {
            System.out.println("OOPS =)");
        }

//        int n = 1000;
//        Complex[] z = new Complex[n + 1];
//        double omega = 0;
//        double maxOmega = 10;
//        double omegaStep = maxOmega/n;
//
//        for (int i = 0; i <= n; i++) {
//            try {
//                z[i] = h.PID(0.0056, 0, 0.056).feedback().evaluate(new Complex(0, omega));
////                z[i] = h.PID(0.0056, 0, 0.056).norm().feedback().evaluate(0, omega);
////                z[i] = h.PID(0.0056, 0, 0.056).norm().evaluate(0, omega);
////                z[i] = h.norm().evaluate(0, omega);
////                z[i] = h.feedback().evaluate(0, omega);
////                z[i] = h.evaluate(0, omega);
//            } catch (DivisionByZeroException e) {
//                --i;
//            }
//            omega += omegaStep;
//        }
//
//        new Main();
//
//        Double maxRe = null;
//        Double maxIm = null;
//        Double minRe = null;
//        Double minIm = null;
//        Double maxAbs = null;
//        Double maxPhase = null;
//
//        for (Complex iz: z)
//        {
//            if (iz != null) {
//                if (maxRe == null || iz.getRe() > maxRe) {
//                    maxRe = iz.getRe();
//                }
//                if (minRe == null || iz.getRe() < minRe) {
//                    minRe = iz.getRe();
//                }
//                if (maxIm == null || iz.getIm() > maxIm) {
//                    maxIm = iz.getIm();
//                }
//                if (minIm == null || iz.getIm() < minIm) {
//                    minIm = iz.getIm();
//                }
//                if (maxAbs == null || iz.abs() > maxAbs) {
//                    maxAbs = iz.abs();
//                }
//                if (maxPhase == null || Math.abs(iz.phase()) > maxPhase) {
//                    maxPhase = Math.abs(iz.phase());
//                }
//            }
//        }
//
//        double tx = 0.5;
//        double ty = 0.5;
//        double scale = 0.5/Math.max(
//                Math.max(
//                        Math.abs(maxRe),
//                        Math.abs(minRe)
//                        ),
//                Math.max(
//                        Math.abs(maxIm),
//                        Math.abs(minIm)
//                )
//        );
//
//        StdDraw.enableDoubleBuffering();
//
//        StdDraw.setCanvasSize(500, 500);
//        StdDraw.setPenRadius(0.0015);
//        StdDraw.setPenColor(StdDraw.BLACK);
//        StdDraw.filledRectangle(0, 0, 1, 1);
//        StdDraw.setPenColor(StdDraw.GRAY);
//        StdDraw.line(0, 0.5, 1, 0.5);
//        StdDraw.line(0.5, 0, 0.5, 1);
//
//        StdDraw.setPenColor(StdDraw.YELLOW);
//        StdDraw.text(0.55, 0.975, "Im");
//        StdDraw.text(0.95, 0.525, "Re");
//        for (int i = 0; i < z.length - 1; i++) {
//            if (z[i] != null && z[i + 1] != null)
//                StdDraw.line(
//                        tx + scale * z[i].getRe(),
//                        ty + scale * z[i].getIm(),
//                        tx + scale * z[i + 1].getRe(),
//                        ty + scale * z[i + 1].getIm()
//                );
//        }
//
//        StdDraw.setPenColor(StdDraw.GREEN);
//        StdDraw.text(0.65, 0.975, "(Amplitude)");
//        StdDraw.text(0.87, 0.525, "(Phase)");
//        for (int i = 0; i < z.length - 1; i++) {
//            if (z[i] != null && z[i + 1] != null)
//                StdDraw.line(
//                        tx + Math.abs(z[i].phase())*0.5/maxPhase,
//                        ty + z[i].abs()*0.5/maxAbs,
//                        tx + Math.abs(z[i + 1].phase())*0.5/maxPhase,
//                        ty + z[i + 1].abs()*0.5/maxAbs
//                );
//        }
//
//        StdDraw.setPenColor(StdDraw.WHITE);
//        StdDraw.text(0.25, 0.95, h.PID(0.0056, 0, 0.056).feedback().getB().toString());
//        StdDraw.text(0.25, 0.9, h.PID(0.0056, 0, 0.056).feedback().getA().toString());
//        StdDraw.text(0.25, 0.94, "________________________");
//
//
//        StdDraw.show();
    }
}
