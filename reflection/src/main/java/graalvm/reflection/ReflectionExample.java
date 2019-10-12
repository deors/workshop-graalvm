package graalvm.reflection;

import java.lang.reflect.Method;

public class ReflectionExample {
    public static void main(String[] args) {
        ReflectionExample example = new ReflectionExample();
        example.printSomething();
    }

    public void printSomething() {
        try {
            Class<?> fooClass = Class.forName("graalvm.reflection.Foo");
            Method printFooOrBarMethod = fooClass.getMethod("printFooOrBar");
            Object fooObj = fooClass.getConstructor().newInstance();

            Class<?> barClass = Class.forName("graalvm.reflection.Bar");
            Method printBarMethod = barClass.getMethod("printBar");
            Object barObj = barClass.getConstructor().newInstance();

            new Thread(() -> {
                try {
                    for (int n = 0; n < 10; n++) {
                        System.out.println(printFooOrBarMethod.invoke(fooObj));
                        Thread.sleep(100);
                    }
                } catch (Exception e) { // catch Exception... don't try this at home!
                    System.out.println("something went wrong during Foo exec");
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    for (int n = 0; n < 10; n++) {
                        System.out.println(printBarMethod.invoke(barObj));
                        Thread.sleep(100);
                    }
                } catch (Exception e) { // catch Exception... don't try this at home!
                    System.out.println("something went wrong during Bar exec");
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) { // catch Exception... don't try this at home!
            System.out.println("something went wrong during init");
            e.printStackTrace();
		}
    }
}
