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
        System.out.println("              H.PID(s) = " + h.PID(0.0056, 0, 0.056));
        System.out.println("     H.PID.feedback(s) = " + h.PID(0.0056, 0, 0.056).feedback());
        System.out.println("             H.norm(s) = " + h.norm());
        System.out.println("         H.norm.PID(s) = " + h.norm().PID(0.0056, 0, 0.056));
        System.out.println("H.norm.PID.feedback(s) = " + h.norm().PID(0.0056, 0, 0.056).feedback());

        StdDraw.setCanvasSize(500, 500);
        StdDraw.setPenRadius(0.0015);
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        StdDraw.line(0, 0.5, 1, 0.5);
        StdDraw.line(0.5, 0, 0.5, 1);

        // P
        TransferFunction GMS_P = h.PID(0.00213827, 0, 0).feedback();
        TransferFunction Poisk2_P = h.PID(0.0021379206585, 0, 0).feedback();
        TransferFunction NelderMid_P = h.PID(0.0026134411111, 0, 0).feedback();
        // PD
        TransferFunction GMS_PD = h.PID(0.0056, 0, 0.056).feedback();
        TransferFunction Poisk2_PD = h.PID(0.0056019221085, 0, 0.0559302598738).feedback();
        TransferFunction NelderMid_PD = h.PID(0.0068444444445, 0, 0.0684444444446).feedback();

        Complex[] z = h.evaluate();

        Complex[] GMS_P_Data = GMS_P.evaluate();
        Complex[] Poisk2_P_Data = Poisk2_P.evaluate();
        Complex[] NelderMid_P_Data = NelderMid_P.evaluate();

        Complex[] GMS_PD_Data = GMS_PD.evaluate();
        Complex[] Poisk2_PD_Data = Poisk2_PD.evaluate();
        Complex[] NelderMid_PD_Data = NelderMid_PD.evaluate();

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

        double tx = 0.0;
        double ty = 0.5;
        double k = Math.max(
                Math.abs(tx),
                Math.abs(ty)
        );
        double scale = 0.95*k/Math.max(
                Math.max(
                        Math.abs(maxRe),
                        Math.abs(minRe)
                ),
                Math.max(
                        Math.abs(maxIm),
                        Math.abs(minIm)
                )
        );



        for (int i = 0; i < z.length - 1; i++) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.line(ty + scale*z[i].getRe(),
                    ty + scale*z[i].getIm(),
                    ty + scale*z[i + 1].getRe(),
                    ty + scale*z[i + 1].getIm());

            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(ty + scale*GMS_P_Data[i].getRe(),
                    ty + scale*GMS_P_Data[i].getIm(),
                    ty + scale*GMS_P_Data[i + 1].getRe(),
                    ty + scale*GMS_P_Data[i + 1].getIm());

            StdDraw.setPenColor(StdDraw.GREEN);
            StdDraw.line(ty + scale*Poisk2_P_Data[i].getRe(),
                    ty + scale*Poisk2_P_Data[i].getIm(),
                    ty + scale*Poisk2_P_Data[i + 1].getRe(),
                    ty + scale*Poisk2_P_Data[i + 1].getIm());

            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(ty + scale*NelderMid_P_Data[i].getRe(),
                    ty + scale*NelderMid_P_Data[i].getIm(),
                    ty + scale*NelderMid_P_Data[i + 1].getRe(),
                    ty + scale*NelderMid_P_Data[i + 1].getIm());

            StdDraw.setPenColor(StdDraw.MAGENTA);
            StdDraw.line(ty + scale*GMS_PD_Data[i].getRe(),
                    ty + scale*GMS_PD_Data[i].getIm(),
                    ty + scale*GMS_PD_Data[i + 1].getRe(),
                    ty + scale*GMS_PD_Data[i + 1].getIm());

            StdDraw.setPenColor(StdDraw.YELLOW);
            StdDraw.line(ty + scale*Poisk2_PD_Data[i].getRe(),
                    ty + scale*Poisk2_PD_Data[i].getIm(),
                    ty + scale*Poisk2_PD_Data[i + 1].getRe(),
                    ty + scale*Poisk2_PD_Data[i + 1].getIm());

            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            StdDraw.line(ty + scale*NelderMid_PD_Data[i].getRe(),
                    ty + scale*NelderMid_PD_Data[i].getIm(),
                    ty + scale*NelderMid_PD_Data[i + 1].getRe(),
                    ty + scale*NelderMid_PD_Data[i + 1].getIm());
        }
    }
}
