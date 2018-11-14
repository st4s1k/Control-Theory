import javax.swing.*;

public class CTTmain  extends JFrame {

    public CTTmain() {
        super("hello");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.add(new JLabel("Hello, world!"));
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {

        TransferFunction h = new TransferFunction(
                new Polynomial(9, 0),
//              new Polynomial(1, 1).times(new Polynomial(10, 1).plus(1, 0)).times(new Polynomial(5, 1).plus(1, 0))
                new Polynomial(50, 3).plus(15, 2).plus(1, 1)
        );

//                        9                    9
//         H(s) = ------------------ = -----------------
//                s(10s + 1)(5s + 1)   50s^3 + 15s^2 + s

        System.out.println("                  H(s) = " + h);
        System.out.println("              H.pid(s) = " + h.pid(0.0056, 0, 0.056));
        System.out.println("     H.pid.feedback(s) = " + h.pid(0.0056, 0, 0.056).feedback());
        System.out.println("             H.norm(s) = " + h.norm());
        System.out.println("         H.norm.pid(s) = " + h.norm().pid(0.0056, 0, 0.056));
        System.out.println("H.norm.pid.feedback(s) = " + h.norm().pid(0.0056, 0, 0.056).feedback());

        int N = 1000;
        Complex[] z = new Complex[N + 1];
        double omega = 0;
        double maxOmega = 10;
        double omegaStep = maxOmega/N;

        for (int i = 0; i <= N; i++) {
            try {
                z[i] = h.pid(0.0056, 0, 0.056).feedback().evaluate(0, omega);
//                z[i] = h.pid(0.0056, 0, 0.056).norm().feedback().evaluate(0, omega);
//                z[i] = h.pid(0.0056, 0, 0.056).norm().evaluate(0, omega);
//                z[i] = h.norm().evaluate(0, omega);
//                z[i] = h.feedback().evaluate(0, omega);
//                z[i] = h.evaluate(0, omega);
            } catch (DivisionByZeroException e) {
                z[i] = null;
            }
            omega += omegaStep;
        }

        Double maxRe = null;
        Double maxIm = null;
        Double minRe = null;
        Double minIm = null;
        Double maxAbs = null;
        Double maxPhase = null;

        for (Complex iz: z)
        {
            if (iz != null) {
                if (maxRe == null || iz.getRe() > maxRe)
                    maxRe = iz.getRe();
                if (minRe == null || iz.getRe() < minRe)
                    minRe = iz.getRe();
                if (maxIm == null || iz.getIm() > maxIm)
                    maxIm = iz.getIm();
                if (minIm == null || iz.getIm() < minIm)
                    minIm = iz.getIm();
                if (maxAbs == null || iz.abs() > maxAbs)
                    maxAbs = iz.abs();
                if (maxPhase == null || Math.abs(iz.phase()) > maxPhase)
                    maxPhase = Math.abs(iz.phase());
            }
        }

        new CTTmain();

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
//        StdDraw.text(0.25, 0.95, h.pid(0.0056, 0, 0.056).feedback().getB().toString());
//        StdDraw.text(0.25, 0.9, h.pid(0.0056, 0, 0.056).feedback().getA().toString());
//        StdDraw.text(0.25, 0.94, "________________________");
//
//
//        StdDraw.show();
    }
}
