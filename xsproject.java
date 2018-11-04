import javax.swing.*;

public class xsproject extends JFrame {

    private JFrame mainFrame;

    private xsproject() {
        initUI();
    }

    private void initUI() {
        setTitle("Simple example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        xsproject xs = new xsproject();

        xs.initUI();

        TransferFunction h = new TransferFunction(
                new Polynomial(9d),
                new Polynomial(0d, 1d, 15d, 50d)
        );

        System.out.println("                  H(s) = " + h);
        System.out.println("              H.PID(s) = " + h.PID(0.0056, 0, 0.056));
        System.out.println("     H.PID.feedback(s) = " + h.PID(0.0056, 0, 0.056).feedback());
        System.out.println("             H.norm(s) = " + h.norm());
        System.out.println("         H.norm.PID(s) = " + h.norm().PID(0.0056, 0, 0.056));
        System.out.println("H.norm.PID.feedback(s) = " + h.norm().PID(0.0056, 0, 0.056).feedback());

        StdDraw.setCanvasSize(500, 500);
        StdDraw.setPenRadius(0.0015);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(0, 0, 1, 1);
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.line(0, 0.5, 1, 0.5);
        StdDraw.line(0.5, 0, 0.5, 1);


        int N = 2*1000;
        Complex[] z = new Complex[N + 1];
        double omega = 0;
        double max_omega = 20;

        for (int i = 0; i <= N; i++) {
            z[i] = h.PID(0.0056, 0, 0.056).feedback().evaluate(0.1, omega);
            omega += max_omega/N;
        }

        double maxRe = z[0].getRe();
        double maxIm = z[0].getIm();
        double minRe = z[0].getRe();
        double minIm = z[0].getIm();

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

        StdDraw.setPenColor(StdDraw.YELLOW);
        for (int i = 0; i < z.length - 1; i++) {
            StdDraw.line(tx + scale*z[i].getRe(),
                    ty + scale*z[i].getIm(),
                    tx + scale*z[i + 1].getRe(),
                    ty + scale*z[i + 1].getIm());
        }
    }
}
