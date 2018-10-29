public class xsproject {
    public static void main(String[] args) {
        TransferFunction H = new TransferFunction(
                new Polynomial(1d,2).plus(2d,3),
                new Polynomial(1d,2).plus(2d,3));
        TransferFunction H_norm = new TransferFunction(H.norm());
        System.out.println("     H(s) = " + H);
        System.out.println("H.norm(s) = " + H.norm());
        System.out.println("H_norm(s) = " + H_norm);
//        Complex s = new Complex(1.0, 1.0);
//        Complex z = new Complex(-1.0, 1.0);
//
//        System.out.print("s\t\t\t");
//        System.out.print(s.toString(3) + "\t");
//        System.out.print(String.format("%.3f", s.getRadius()) + "\t");
//        System.out.print(String.format("%.3f", s.getAngle()) + " rad\t");
//        System.out.println(Math.toDegrees(s.getAngle()) + " deg");
//
//        System.out.print("z\t\t\t");
//        System.out.print(z.toString(3) + "\t");
//        System.out.print(String.format("%.3f", z.getRadius()) + "\t");
//        System.out.print(String.format("%.3f", z.getAngle()) + " rad\t");
//        System.out.println(Math.toDegrees(z.getAngle()) + " deg");
//
//        System.out.print("s.plus(z)\t");
//        System.out.print(s.plus(z).toString(3) + "\t");
//        System.out.print(String.format("%.3f", s.plus(z).getRadius()) + "\t");
//        System.out.print(String.format("%.3f", s.plus(z).getAngle()) + " rad\t");
//        System.out.println(Math.toDegrees(s.plus(z).getAngle()) + " deg");
//
//        System.out.print("z.plus(s)\t");
//        System.out.print(z.plus(s).toString(3) + "\t");
//        System.out.print(String.format("%.3f", z.plus(s).getRadius()) + "\t");
//        System.out.print(String.format("%.3f", z.plus(s).getAngle()) + " rad\t");
//        System.out.println(Math.toDegrees(z.plus(s).getAngle()) + " deg");
//
//        System.out.print("s.minus(z)\t");
//        System.out.print(s.minus(z).toString(3) + "\t");
//        System.out.print(String.format("%.3f", s.minus(z).getRadius()) + "\t");
//        System.out.print(String.format("%.3f", s.minus(z).getAngle()) + " rad\t");
//        System.out.println(Math.toDegrees(s.minus(z).getAngle()) + " deg");
//
//        System.out.print("z.minus(s)\t");
//        System.out.print(z.minus(s).toString(3) + "\t");
//        System.out.print(String.format("%.3f", z.minus(s).getRadius()) + "\t");
//        System.out.print(String.format("%.3f", z.minus(s).getAngle()) + " rad\t");
//        System.out.println(Math.toDegrees(z.minus(s).getAngle()) + " deg");
//
//        System.out.print("s.times(z)\t");
//        System.out.print(s.times(z).toString(3) + "\t");
//        System.out.print(String.format("%.3f", s.times(z).getRadius()) + "\t");
//        System.out.print(String.format("%.3f", s.times(z).getAngle()) + " rad\t");
//        System.out.println(Math.toDegrees(s.times(z).getAngle()) + " deg");
//
//        System.out.print("z.times(s)\t");
//        System.out.print(z.times(s).toString(3) + "\t");
//        System.out.print(String.format("%.3f", z.times(s).getRadius()) + "\t");
//        System.out.print(String.format("%.3f", z.times(s).getAngle()) + " rad\t");
//        System.out.println(Math.toDegrees(z.times(s).getAngle()) + " deg");
//
//        System.out.print("s.div(z)\t");
//        System.out.print(s.div(z).toString(3) + "\t");
//        System.out.print(String.format("%.3f", s.div(z).getRadius()) + "\t");
//        System.out.print(String.format("%.3f", s.div(z).getAngle()) + " rad\t");
//        System.out.println(Math.toDegrees(s.div(z).getAngle()) + " deg");
//
//        System.out.print("z.div(s)\t");
//        System.out.print(z.div(s).toString(3) + "\t");
//        System.out.print(String.format("%.3f", z.div(s).getRadius()) + "\t");
//        System.out.print(String.format("%.3f", z.div(s).getAngle()) + " rad\t");
//        System.out.println(Math.toDegrees(z.div(s).getAngle()) + " deg");
    }
}
