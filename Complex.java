import java.text.DecimalFormat;

public class Complex {
    private Double Re;
    private Double Im;

    public Complex(){
        Re = 0.0;
        Im = 0.0;
    }

    public Complex(Complex z){
        Re = z.Re;
        Im = z.Im;
    }

    public Complex(Double re){
        Re = re;
        Im = 0.0;
    }

    public Complex(Double re, Double im){
        Re = re;
        Im = im;
    }
    public Double getIm() {
        return Im;
    }

    public Double getRe() {
        return Re;
    }

    public void setRe(Double re) {
        Re = re;
    }

    public void setIm(Double im) {
        Im = im;
    }

    // Polar form

    public Double abs(){
        return Math.sqrt(Re * Re + Im * Im);
    }

    public Double phase() {
        return Math.atan2(Im, Re);
    }

    //  Arithmetics

    public Complex plus(Complex z) {
        return new Complex(Re + z.Re, Im + z.Im);
    }

    public Complex plus(Double re) {
        return new Complex(Re + re, Im);
    }

    public Complex plus(int N) {
        return plus((double)N);
    }

    public Complex minus(Complex z) {
        return new Complex(Re - z.Re, Im - z.Im);
    }

    public Complex minus(Double re) {
        return new Complex(Re - re, Im);
    }

    public Complex minus(int N) {
        return minus((double)N);
    }

    public Complex times(Complex z) {
        Double re = Re * z.Re - Im * z.Im;
        Double im = Re * z.Im + Im * z.Re;
        return new Complex(re, im);
    }

    public Complex times(Double re) {
        return new Complex(Re * re, Im * re);
    }

    public Complex times(int N) {
        return times((double)N);
    }

    public Complex div(Complex z) {
        if (z.Re == 0 && z.Im == 0) return null;
        Double re = (Re * z.Re + Im * z.Im) / (z.Re * z.Re + z.Im * z.Im);
        Double im = (-Re * z.Im + Im * z.Re) / (z.Re * z.Re + z.Im * z.Im);
        return new Complex(re, im);
    }

    public Complex div(Double re) {
        if (re == 0) return null;
        return new Complex(Re / re, Im / re);
    }

    public Complex div(int N) {
        return div((double)N);
    }

    public Complex pow(Double N) {
        Double R = Math.pow(abs(), N);
        Double P = N*phase();
        Double re = R * Math.cos(P);
        Double im = R * Math.sin(P);
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
