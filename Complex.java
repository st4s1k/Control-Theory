import java.text.DecimalFormat;

public class Complex {
    private double Re;
    private double Im;

    public Complex(){
        this.Re = 0d;
        this.Im = 0d;
    }

    public Complex(Complex z){
        this.Re = z.Re;
        this.Im = z.Im;
    }

    public Complex(double Re){
        this.Re = Re;
        this.Im = 0d;
    }

    public Complex(double Re, double Im){
        this.Re = Re;
        this.Im = Im;
    }
    public double getIm() {
        return this.Im;
    }

    public double getRe() {
        return this.Re;
    }

    public void setRe(double Re) {
        this.Re = Re;
    }

    public void setIm(double Im) {
        this.Im = Im;
    }

    // Polar form

    public double abs(){
        return Math.sqrt(this.Re * this.Re + this.Im * this.Im);
    }

    public double phase() {
        return Math.atan2(this.Im, this.Re);
    }

    //  Arithmetics

    public Complex plus(Complex z) {
        return new Complex(this.Re + z.Re, this.Im + z.Im);
    }

    public Complex plus(double Re) {
        return new Complex(this.Re + Re, this.Im);
    }

    public Complex minus(Complex z) {
        return new Complex(this.Re - z.Re, this.Im - z.Im);
    }

    public Complex minus(double Re) {
        return new Complex(this.Re - Re, this.Im);
    }

    public Complex times(Complex z) {
        double Re = this.Re * z.Re - this.Im * z.Im;
        double Im = this.Re * z.Im + this.Im * z.Re;
        return new Complex(Re, Im);
    }

    public Complex times(double d) {
        return new Complex(this.Re * d, this.Im * d);
    }

    public Complex div(Complex z) {
        if (z.Re == 0 && z.Im == 0) return null;
        double Re = (this.Re * z.Re + this.Im * z.Im) / (z.Re * z.Re + z.Im * z.Im);
        double Im = (-this.Re * z.Im + this.Im * z.Re) / (z.Re * z.Re + z.Im * z.Im);
        return new Complex(Re, Im);
    }

    public Complex div(double Re) {
        if (Re == 0) return null;
        return new Complex(this.Re / Re, this.Im / Re);
    }

    public Complex pow(double exponent) {
        double magnitude = Math.pow(abs(), exponent);
        double phase = exponent*phase();
        double re = magnitude * Math.cos(phase);
        double im = magnitude * Math.sin(phase);
        return new Complex(re, im);
    }

    public String toString() {
        DecimalFormat val_fmt = new DecimalFormat("#.###");
        StringBuilder s = new StringBuilder();
        s.append(val_fmt.format(Re));
        if (Im == 0d) {
            s.append(Im < 0 ? " - " : " + ");
            s.append(val_fmt.format(Math.abs(Im)));
            s.append("i");
        }
        return s.toString();
    }
}
