# GraalVM

Examples of use cases for GraalVM

Adapted from Chris Seaton's workshop in Oracle Code One 2018, with new examples and use cases.

## Examples

All these examples assume that GraalVM is in path and `JAVA_HOME` is set.

### Basic examples

#### Counting words in a file with Java Streams and Lambdas

```
cd topten
javac TopTen.java
time java TopTen quick-brown-fox.txt
time java TopTen alice-in-wonderland.txt
time java TopTen war-and-peace.txt
```

#### Perform heavy mathematical computations

```
cd value-types
mvn package
java -jar target/benchmarks.jar -prof gc
```

### Native image examples

#### Configure native image add-on component

```
gu install native-image
```

#### Counting words as a native image

```
cd topten
native-image TopTen
otool -L topten
ll topten
time ./topten quick-brown-fox.txt
time ./topten alice-in-wonderland.txt
time ./topten war-and-peace.txt
```

#### Using reflection and config files

```
cd reflection
mvn compile
java -cp target/classes graalvm.reflection.ReflectionPropsExample
native-image -cp target/classes graalvm.reflection.ReflectionPropsExample
```

The native binary creation will fail as there is reflection in the code and a fall-back executable is created which requires the JDK to run.

To simplify the extra configuration required, let's analyze the code with the native image agent and obtain the necessary configuration to create the native binary executable.

```
java -agentlib:native-image-agent=config-output-dir=target/native-image -cp target/classes graalvm.reflection.ReflectionPropsExample
ll target/native-image
cat target/native-image/reflect-config.json
cat target/native-image/resource-config.json
native-image -H:ReflectionConfigurationFiles=target/native-image/reflect-config.json -H:ResourceConfigurationFiles=target/native-image/resource-config.json -cp target/classes graalvm.reflection.ReflectionPropsExample
./graalvm.reflection.reflectionpropsexample
```

Native binaries have minimum dependencies and are compact in size.

```
otool -L graalvm.reflection.reflectionpropsexample
ll graalvm.reflection.reflectionpropsexample
```

The following example works without any extra configuration because the reflection operations are "effectively final" and thus, known to the ahead of time compiler.

```
java -cp target/classes graalvm.reflection.ReflectionExample
native-image -cp target/classes graalvm.reflection.ReflectionExample
./graalvm.reflection.reflectionexample
```

#### Creating a Docker image

This example requires a working Docker machine and `docker` executable in path.

To build a container image with the native binary, we must have into consideration that native binaries are not cross-platform.

```
docker build -t reflectionpropsexample -f reflectionpropsexample.Dockerfile .
docker images | grep reflectionpropsexample
docker run -it --rm reflectionpropsexample
```

The above command will fail when working on macOS, because it is a macOS native binary running inside a (Linux) container. To create a viable container image it is required to create a Linux binary image, and for that we must use a GraalVM container image with the native-image tool, and generate the binary inside the container.

```
cd reflection
docker run -it --rm --mount type=bind,source=$PWD,target=/opt/graalvm ghcr.io/graalvm/native-image "--static" "-H:ReflectionConfigurationFiles=/opt/graalvm/target/native-image/reflect-config.json" "-H:ResourceConfigurationFiles=/opt/graalvm/target/native-image/resource-config.json" "-cp" "/opt/graalvm/target/classes" "graalvm.reflection.ReflectionPropsExample"
./graalvm.reflection.reflectionpropsexample
```

As expected, the running the newly generate binary fails on non-Linux machines, because it is a Linux native binary running in macOS. Now it is possible to containerize it, and it will run perfectly as a container.

```
docker build -t reflectionpropsexample -f reflectionpropsexample.Dockerfile .
docker images | grep reflectionpropsexample
docker run -it --rm reflectionpropsexample
```

### Non-JVM language examples

#### Configure experimental add-on components and rebuild native images

```
gu install nodejs && node --version
gu install ruby && ruby --version
gu install python && graalpython --version
gu install R && R --version
gu install wasm && wasm --version
gu rebuild-images polyglot libpolyglot js llvm python ruby R
```

#### Running a Node.js+Express simple web service

```
cd hello-express
npm install
node hello-express.js
```

#### Running a simple Ruby program

```
cd hello-ruby
ruby hello-ruby.rb
```

#### Running a simple Python program

```
cd hello-python
graalpython hello-python.py
```

### Interoperability examples

#### Running Java code from Ruby and Python

```
cd interop
ruby --jvm ruby-java.rb
graalpython --jvm python-java.py
```

#### Running JavaScript code from Ruby

```
cd interop
ruby --jvm --polyglot ruby-javascript.rb
```

#### Running a Node.js+Express service which returns a 3D plot done by R code

```
cd cloudplot
npm install
node --jvm --polyglot cloudplot.js
```
