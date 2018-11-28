package valhallaBench;

import java.util.concurrent.*;
import org.openjdk.jmh.annotations.*;

@Fork(1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations=3, time=3)
@Measurement(iterations=3, time=8)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
public class Multiply {

  @Param({"100"})
  public int size;

  private void populate(Complex[][] m) {
    int size = m.length;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        m[i][j] = new Complex(i, j);
      }
    }
  }

  private Complex[][] A;
  private Complex[][] B;
  

  @Setup
  public void prepare() {
    A = new Complex[size][size];
    B = new Complex[size][size];
    populate(A);
    populate(B);
  }


  @Benchmark
  public Complex[][] multiply() {
    int size = A.length;
    Complex[][] R = new Complex[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        Complex s = new Complex(0, 0);
        for (int k = 0; k < size; k++) {
          s = s.add(A[i][k].mul(B[k][j]));
	      }
	      R[i][j] = s;
      }
    }
    return R;
  }
}
