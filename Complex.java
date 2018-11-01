import java.text.DecimalFormat;

public class Complex {
    private Double Re;
    private Double Im;

    public Complex(){
        this.Re = 0d;
        this.Im = 0d;
    }

    public Complex(Complex z){
        this.Re = z.Re;
        this.Im = z.Im;
    }

    public Complex(Double Re){
        this.Re = Re;
        this.Im = 0d;
    }

    public Complex(Double Re, Double Im){
        this.Re = Re;
        this.Im = Im;
    }
    public Double getIm() {
        return this.Im;
    }

    public Double getRe() {
        return this.Re;
    }

    public void setRe(Double Re) {
        this.Re = Re;
    }

    public void setIm(Double Im) {
        this.Im = Im;
    }

    // Polar form

    public Double abs(){
        return Math.sqrt(this.Re * this.Re + this.Im * this.Im);
    }

    public Double phase() {
        return Math.atan2(this.Im, this.Re);
    }

    //  Arithmetics

    public Complex plus(Complex z) {
        return new Complex(this.Re + z.Re, this.Im + z.Im);
    }

    public Complex plus(Double Re) {
        return new Complex(this.Re + Re, this.Im);
    }

    public Complex plus(int N) {
        return plus((double)N);
    }

    public Complex minus(Complex z) {
        return new Complex(this.Re - z.Re, this.Im - z.Im);
    }

    public Complex minus(Double Re) {
        return new Complex(this.Re - Re, this.Im);
    }

    public Complex minus(int N) {
        return minus((double)N);
    }

    public Complex times(Complex z) {
        Double Re = this.Re * z.Re - this.Im * z.Im;
        Double Im = this.Re * z.Im + this.Im * z.Re;
        return new Complex(Re, Im);
    }

    public Complex times(Double d) {
        return new Complex(this.Re * d, this.Im * d);
    }

    public Complex times(int N) {
        return times((double)N);
    }

    public Complex div(Complex z) {
        if (z.Re == 0 && z.Im == 0) return null;
        Double Re = (this.Re * z.Re + this.Im * z.Im) / (z.Re * z.Re + z.Im * z.Im);
        Double Im = (-this.Re * z.Im + this.Im * z.Re) / (z.Re * z.Re + z.Im * z.Im);
        return new Complex(Re, Im);
    }

    public Complex div(Double Re) {
        if (Re.equals(0d)) return null;
        return new Complex(this.Re / Re, this.Im / Re);
    }

    public Complex div(int Re) {
        return div((double)Re);
    }

    public Complex pow(Double exponent) {
        Double magnitude = Math.pow(abs(), exponent);
        Double phase = exponent*phase();
        Double re = magnitude * Math.cos(phase);
        Double im = magnitude * Math.sin(phase);
        return new Complex(re, im);
    }

    public Complex pow(int N) {
        return pow((double)N);
    }

    public String toString() {
        DecimalFormat val_fmt = new DecimalFormat("#.###");
        StringBuilder s = new StringBuilder();
        s.append(val_fmt.format(Re));
        if (!Im.equals(0d)) {
            s.append(Im < 0 ? " - " : " + ");
            s.append(val_fmt.format(Math.abs(Im)));
            s.append("i");
        }
        return s.toString();
    }
}
