import lombok.Data;

import java.text.DecimalFormat;

@Data
public class Complex {
    private double re;
    private double im;

    public Complex(){
        re = 0;
        im = 0;
    }

    public Complex(Complex z){
        re = z.getRe();
        im = z.getIm();
    }

    public Complex(double re){
        this.re = re;
        im = 0;
    }

    public Complex(double re, double im){
        this.re = re;
        this.im = im;
    }

    // Polar form

    public double abs(){
        return Math.sqrt(re * re + im * im);
    }

    public double phase() {
        return Math.atan2(im, re);
    }

    //  Arithmetic

    public Complex plus(Complex z) {
        return new Complex(re + z.re, im + z.im);
    }

    public Complex plus(double re) {
        return new Complex(this.re + re, im);
    }

    public Complex minus(Complex z) {
        return new Complex(re - z.re, im - z.im);
    }

    public Complex minus(double re) {
        return new Complex(this.re - re, im);
    }

    public Complex times(Complex z) {
        return new Complex(
            this.re * z.re - this.im * z.im,
            this.re * z.im + this.im * z.re
        );
    }

    public Complex times(double d) {
        return new Complex(re * d, im * d);
    }

    public Complex div(Complex z) throws DivisionByZeroException {
        if (z.abs() == 0) {
            throw new DivisionByZeroException(
                    "Division of complex number by zero.",
                    this.toString(),
                    Double.toString(z.abs())
            );
        }
        return new Complex(
                (this.re * z.re + im * z.im) / z.abs(),
                (-this.re * z.im + this.im * z.re) / z.abs()
        );
    }

    public Complex div(double k) throws DivisionByZeroException {
        if (k == 0) {
            throw new DivisionByZeroException(
                    "Division of complex number by zero.",
                    this.toString(),
                    Double.toString(k)
            );
        }
        return new Complex(this.re / k, im / k);
    }

    public Complex pow(double exponent) {
        double magnitude = Math.pow(abs(), exponent);
        double phase = exponent * phase();
        return new Complex(
                magnitude * Math.cos(phase),
                magnitude * Math.sin(phase)
        );
    }

    public String toString() {
        DecimalFormat valFormat = new DecimalFormat("#.###");
        StringBuilder s = new StringBuilder();
        s.append(valFormat.format(re));
        if (im != 0d) {
            s.append(im < 0 ? " - " : " + ");
            s.append("i");
            s.append(valFormat.format(Math.abs(im)));
        }
        return s.toString();
    }
}
