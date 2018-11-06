import java.text.DecimalFormat;

public class Complex {
    private double Re;
    private double Im;

    public Complex(){
        Re = 0;
        Im = 0;
    }

    public Complex(Complex z){
        Re = z.getRe();
        Im = z.getIm();
    }

    public Complex(double Re){
        this.Re = Re;
        Im = 0;
    }

    public Complex(double Re, double Im){
        this.Re = Re;
        this.Im = Im;
    }
    public double getIm() {
        return Im;
    }

    public double getRe() {
        return Re;
    }

    public void setRe(double Re) {
        this.Re = Re;
    }

    public void setIm(double Im) {
        this.Im = Im;
    }

    // Polar form

    public double abs(){
        return Math.sqrt(Re * Re + Im * Im);
    }

    public double phase() {
        return Math.atan2(Im, Re);
    }

    //  Arithmetics

    public Complex plus(Complex z) {
        return new Complex(Re + z.Re, Im + z.Im);
    }

    public Complex plus(double Re) {
        return new Complex(this.Re + Re, Im);
    }

    public Complex minus(Complex z) {
        return new Complex(Re - z.Re, Im - z.Im);
    }

    public Complex minus(double Re) {
        return new Complex(this.Re - Re, Im);
    }

    public Complex times(Complex z) {
        double re = Re * z.Re - Im * z.Im;
        double im = Re * z.Im + Im * z.Re;
        return new Complex(re, im);
    }

    public Complex times(double d) {
        return new Complex(Re * d, Im * d);
    }

    public Complex div(Complex z) {
        if (z.Re == 0 && z.Im == 0) throw new RuntimeException("Division by Zero!");
        double re = (Re * z.Re + Im * z.Im) / (z.Re * z.Re + z.Im * z.Im);
        double im = (-Re * z.Im + Im * z.Re) / (z.Re * z.Re + z.Im * z.Im);
        return new Complex(re, im);
    }

    public Complex div(double Re) {
        if (Re == 0) return null;
        return new Complex(this.Re / Re, Im / Re);
    }

    public Complex pow(double exponent) {
        double magnitude = Math.pow(abs(), exponent);
        double phase = exponent * phase();
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
