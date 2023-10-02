package com.st4s1k.ctt;

import com.st4s1k.ctt.model.Complex;
import com.st4s1k.ctt.model.Polynomial;
import com.st4s1k.ctt.model.TransferFunction;

import static com.st4s1k.ctt.util.StdDraw.BLACK;
import static com.st4s1k.ctt.util.StdDraw.GRAY;
import static com.st4s1k.ctt.util.StdDraw.GREEN;
import static com.st4s1k.ctt.util.StdDraw.WHITE;
import static com.st4s1k.ctt.util.StdDraw.YELLOW;
import static com.st4s1k.ctt.util.StdDraw.enableDoubleBuffering;
import static com.st4s1k.ctt.util.StdDraw.filledRectangle;
import static com.st4s1k.ctt.util.StdDraw.line;
import static com.st4s1k.ctt.util.StdDraw.setCanvasSize;
import static com.st4s1k.ctt.util.StdDraw.setPenColor;
import static com.st4s1k.ctt.util.StdDraw.setPenRadius;
import static com.st4s1k.ctt.util.StdDraw.show;
import static com.st4s1k.ctt.util.StdDraw.text;

public class ControlTheoryToolsetApplication {

    public static void main(String[] args) {
        TransferFunction h = TransferFunction.of(
                Polynomial.of(9, 0),
                Polynomial.of(50, 3).plus(15, 2).plus(1, 1)
        );

        int iterations = 1000;
        Complex[] z = new Complex[iterations + 1];
        double omega = 0;
        double maxOmega = 10;
        double omegaStep = maxOmega / iterations;

        for (int i = 0; i <= iterations; i++) {
            z[i] = h.pid(0.0056, 0, 0.056).feedback().evaluate(0, omega);
            omega += omegaStep;
        }

        double maxRe = z[0].getRe();
        double maxIm = z[0].getIm();
        double minRe = z[0].getRe();
        double minIm = z[0].getIm();
        double maxAbs = z[0].abs();
        double maxPhase = Math.abs(z[0].phase());

        for (Complex iz : z) {
            maxRe = Math.max(maxRe, iz.getRe());
            minRe = Math.min(minRe, iz.getRe());
            maxIm = Math.max(maxIm, iz.getIm());
            minIm = Math.min(minIm, iz.getIm());
            maxAbs = Math.max(maxAbs, iz.abs());
            maxPhase = Math.max(maxPhase, Math.abs(iz.phase()));
        }

        double tx = 0.5;
        double ty = 0.5;
        double scale = 0.95 * 0.5 / Math.max(
                Math.max(
                        Math.abs(maxRe),
                        Math.abs(minRe)
                ),
                Math.max(
                        Math.abs(maxIm),
                        Math.abs(minIm)
                )
        );

        enableDoubleBuffering();

        setCanvasSize(500, 500);
        setPenRadius(0.0015);
        setPenColor(BLACK);
        filledRectangle(0, 0, 1, 1);
        setPenColor(GRAY);
        line(0, 0.5, 1, 0.5);
        line(0.5, 0, 0.5, 1);

        setPenColor(WHITE);
        text(0.55, 0.975, "Im");
        text(0.95, 0.525, "Re");

        setPenColor(YELLOW);
        text(0.87, 0.525, "(Phase)");

        for (int i = 0; i < z.length - 1; i++) {
            double x0 = tx + scale * z[i].getRe();
            double y0 = ty + scale * z[i].getIm();
            double x1 = tx + scale * z[i + 1].getRe();
            double y1 = ty + scale * z[i + 1].getIm();
            line(x0, y0, x1, y1);
        }

        setPenColor(GREEN);
        text(0.65, 0.975, "(Amplitude)");

        for (int i = 0; i < z.length - 1; i++) {
            double x0 = tx + Math.abs(z[i].phase()) * 0.5 / maxPhase;
            double y0 = ty + z[i].abs() * 0.5 / maxAbs;
            double x1 = tx + Math.abs(z[i + 1].phase()) * 0.5 / maxPhase;
            double y1 = ty + z[i + 1].abs() * 0.5 / maxAbs;
            line(x0, y0, x1, y1);
        }

        setPenColor(WHITE);
        text(0.25, 0.95, h.pid(0.0056, 0, 0.056).feedback().getB().toString());
        line(0.02, 0.93, 0.48, 0.93);
        text(0.25, 0.9, h.pid(0.0056, 0, 0.056).feedback().getA().toString());

        show();
    }
}
