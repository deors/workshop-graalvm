package graalvm.reflection;

import java.lang.reflect.Method;
import java.util.Properties;

public class ReflectionPropsExample {
    public static void main(String[] args) {
        ReflectionPropsExample example = new ReflectionPropsExample();
        example.printSomething();
    }

    public void printSomething() {
        try {
            Properties config = new Properties();
            config.load(this.getClass().getResourceAsStream("/config.properties"));

            Class<?> fooClass = Class.forName(config.getProperty("fooClass"));
            Method printFooOrBarMethod = fooClass.getMethod("printFooOrBar");
            Object fooObj = fooClass.getConstructor().newInstance();

            Class<?> barClass = Class.forName(config.getProperty("barClass"));
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
