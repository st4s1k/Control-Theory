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


    // GETTERS
    public Double getIm() {
        return Im;
    }

    public Double getRe() {
        return Re;
    }


    // Radius - Polar coordinates
    public Double getRadius(){
        return Math.sqrt(Re * Re + Im * Im);
    }


    // Angle - Polar coordinates
    public Double getAngle() {
        return Math.atan2(Im, Re);
    }


    // SETTERS
    public void setRe(Double re) {
        Re = re;
    }

    public void setIm(Double im) {
        Im = im;
    }


    // METHODS

    // Sum
    public Complex plus(Complex z) {
        return new Complex(Re + z.Re, Im + z.Im);
    }

    public Complex plus(Double re) {
        return new Complex(Re + re, Im);
    }


    // Difference
    public Complex minus(Complex z) {
        return new Complex(Re - z.Re, Im - z.Im);
    }

    public Complex minus(Double re) {
        return new Complex(Re - re, Im);
    }


    // Product
    public Complex times(Complex z) {
        Double re = Re * z.Re - Im * z.Im;
        Double im = Re * z.Im + Im * z.Re;
        return new Complex(re, im);
    }

    public Complex times(Double re) {
        return new Complex(Re * re, Im * re);
    }


    // Division
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


    // Misc
    public String toString() {
        return (String.format("%.3f", Re) +
                (Im < 0 ? "" : "+") +
                String.format("%.3f", Im) + "i");
    }
}
