package graalvm.reflection;

public class Foo {

    public Foo() {
        super();
    }

    public String printFooOrBar() {
        Long now = System.currentTimeMillis();
        return Long.remainderUnsigned(now, 2L) == 0 ? "sometimes is Foo" : "sometimes is Bar";
    }

    public static void main(String... args) {
        Foo foo = new Foo();
        for (int n = 0; n < 10; n++) {
            System.out.println(foo.printFooOrBar());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
}
