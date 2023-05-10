package valhallaBench;

public record Complex(double re, double im) {

  public Complex add(Complex that) {
    return new Complex(this.re + that.re, this.im + that.im);
  }

  public Complex mul(Complex that) {
    return new Complex(this.re * that.re - this.im * that.im,
                       this.re * that.im + this.im * that.re);
  }
}
