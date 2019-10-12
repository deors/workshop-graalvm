package graalvm.reflection;

public class Bar {

    public Bar() {
        super();
    }

    public String printBar() {
        return "always is Bar";
    }

    public static void main(String... args) {
        Bar bar = new Bar();
        for (int n = 0; n < 10; n++) {
            System.out.println(bar.printBar());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
}
