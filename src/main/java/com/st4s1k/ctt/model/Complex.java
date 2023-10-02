package com.st4s1k.ctt.model;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.text.DecimalFormat;

@Value
@AllArgsConstructor
public class Complex {

    double re;
    double im;

    public Complex() {
        re = 0.0;
        im = 0.0;
    }

    public Complex(Complex z) {
        re = z.getRe();
        im = z.getIm();
    }

    // Polar form

    public double abs() {
        return Math.sqrt(re * re + im * im);
    }

    public double phase() {
        return Math.atan2(im, re);
    }

    //  Arithmetics

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

    public Complex div(Complex z) {
        if (z.re == 0 && z.im == 0) {
            throw new ArithmeticException("Division by zero");
        }
        double div = z.re * z.re + z.im * z.im;
        return new Complex(
                (this.re * z.re + this.im * z.im) / div,
                (-this.re * z.im + this.im * z.re) / div
        );
    }

    public Complex div(double re) {
        if (re == 0) {
            return null;
        }
        return new Complex(this.re / re, im / re);
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
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        StringBuilder s = new StringBuilder();
        s.append(decimalFormat.format(re));
        if (im != 0d) {
            s.append(im < 0 ? " - " : " + ");
            s.append(decimalFormat.format(Math.abs(im)));
            s.append("i");
        }
        return s.toString();
    }
}
