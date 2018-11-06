public class CTTmain {

    public static void main(String[] args) {

        TransferFunction h = new TransferFunction(
                new Polynomial(9, 0),
                new Polynomial(0, 0).plus(1, 1).plus(15, 2).plus(50, 3)
        );

        System.out.println("                  H(s) = " + h);
        System.out.println("              H.PID(s) = " + h.PID(0.0056, 0, 0.056));
        System.out.println("     H.PID.feedback(s) = " + h.PID(0.0056, 0, 0.056).feedback());
        System.out.println("             H.norm(s) = " + h.norm());
        System.out.println("         H.norm.PID(s) = " + h.norm().PID(0.0056, 0, 0.056));
        System.out.println("H.norm.PID.feedback(s) = " + h.norm().PID(0.0056, 0, 0.056).feedback());

        int N = 1000;
        Complex[] z = new Complex[N + 1];
        double omega = 0;
        double max_omega = 10;
        double omega_step = max_omega/N;

        for (int i = 0; i <= N; i++) {
            z[i] = h.PID(0.0056, 0, 0.056).feedback().evaluate(0, omega);
//            z[i] = h.PID(1, 1, 1).evaluate(0, omega);
            omega += omega_step;
        }

        double maxRe = z[0].getRe();
        double maxIm = z[0].getIm();
        double minRe = z[0].getRe();
        double minIm = z[0].getIm();
        double maxAbs = z[0].abs();
        double maxPhase = Math.abs(z[0].phase());

        for (Complex iz: z)
        {
            if (iz.getRe() > maxRe)
                maxRe = iz.getRe();
            if (iz.getRe() < minRe)
                minRe = iz.getRe();
            if (iz.getIm() > maxIm)
                maxIm = iz.getIm();
            if (iz.getIm() < minIm)
                minIm = iz.getIm();
            if (iz.abs() > maxAbs)
                maxAbs = iz.abs();
            if (Math.abs(iz.phase()) > maxPhase)
                maxPhase = Math.abs(iz.phase());
        }

        double tx = 0.5;
        double ty = 0.5;
        double scale = 0.95*0.5/Math.max(
                Math.max(
                        Math.abs(maxRe),
                        Math.abs(minRe)
                        ),
                Math.max(
                        Math.abs(maxIm),
                        Math.abs(minIm)
                )
        );

        StdDraw.enableDoubleBuffering();

        StdDraw.setCanvasSize(500, 500);
        StdDraw.setPenRadius(0.0015);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(0, 0, 1, 1);
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.line(0, 0.5, 1, 0.5);
        StdDraw.line(0.5, 0, 0.5, 1);

        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.text(0.55, 0.975, "Im");
        StdDraw.text(0.95, 0.525, "Re");
        for (int i = 0; i < z.length - 1; i++) {
            StdDraw.line(
                    tx + scale * z[i].getRe(),
                    ty + scale * z[i].getIm(),
                    tx + scale * z[i + 1].getRe(),
                    ty + scale * z[i + 1].getIm()
            );
        }

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.text(0.65, 0.975, "(Amplitude)");
        StdDraw.text(0.87, 0.525, "(Phase)");
        for (int i = 0; i < z.length - 1; i++) {
            StdDraw.line(
                    tx + Math.abs(z[i].phase())*0.5/maxPhase,
                    ty + z[i].abs()*0.5/maxAbs,
                    tx + Math.abs(z[i + 1].phase())*0.5/maxPhase,
                    ty + z[i + 1].abs()*0.5/maxAbs
            );
        }

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.25, 0.95, h.PID(0.0056, 0, 0.056).feedback().getB().toString());
        StdDraw.text(0.25, 0.9, h.PID(0.0056, 0, 0.056).feedback().getA().toString());
        StdDraw.text(0.25, 0.94, "________________________");


        StdDraw.show();
    }
}
