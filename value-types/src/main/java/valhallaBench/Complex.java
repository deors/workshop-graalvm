package valhallaBench;


public class Complex {
  private final double re;
  private final double im;
  
  public Complex(double re, double im) {
    this.re = re;
    this.im = im;
  }

  public double re() { return re; }
  public double im() { return im; }
  
  public Complex add(Complex that) {
    return new Complex(this.re + that.re, this.im + that.im);
  }

  public Complex mul(Complex that) {
    return new Complex(this.re * that.re - this.im * that.im,
                       this.re * that.im + this.im * that.re);
  }
}
