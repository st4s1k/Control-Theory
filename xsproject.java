import javax.swing.*;

public class xsproject extends JFrame {
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

        TransferFunction h = new TransferFunction(
                new Polynomial(9d),
                new Polynomial(0d, 1d, 15d, 50d)
        );
        TransferFunction H_norm = new TransferFunction(h.norm());

        System.out.println("                  H(s) = " + h);
        System.out.println("              H.PID(s) = " + h.PID(1d, 1d, 1d));
        System.out.println("     H.PID.feedback(s) = " + h.PID(1d, 1d, 1d).feedback());
        System.out.println("             H.norm(s) = " + h.norm());
        System.out.println("         H.norm.PID(s) = " + h.norm().PID(1d, 1d, 1d));
        System.out.println("H.norm.PID.feedback(s) = " + h.norm().PID(1d, 1d, 1d).feedback());

        StdDraw.setCanvasSize(500, 500);
        StdDraw.setPenRadius(0.003);
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        StdDraw.line(0, 0.5, 1, 0.5);
        StdDraw.line(0.5, 0, 0.5, 1);

        TransferFunction H = h.PID(1d, 1d, 1d).feedback();

        Complex[] z = H.evaluate();

        for (int i = 0; i < z.length - 1; i++) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.line(0.5 + z[i].getRe(),
                         0.5 + z[i].getIm(),
                         0.5 + z[i + 1].getRe(),
                    0.5 + z[i + 1].getIm());
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(0.5 + 0.15*Math.abs(z[i].phase()),
                         0.5 + z[i].abs(),
                         0.5 + 0.15*Math.abs(z[i + 1].phase()),
                         0.5 + z[i + 1].abs());
            System.out.println(String.format("z1.phase()= %.3f\tz1.abs()= %.3f", z[i].phase(), z[i].abs()));
        }
    }
}
