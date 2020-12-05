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
time java TopTen war-and-peace.txt
```

#### Perform heavy mathematical computations

```
cd value-types
mvn package
java -jar target/benchmarks.jar -prof gc
```

### Native image examples

#### Counting words as a native image

```
cd topten
time java TopTen quick-brown-fox.txt
native-image TopTen
otool -L topten
ll topten
time ./topten quick-brown-fox.txt
```

#### Using reflection and config files

```
cd reflection
mvn compile
java -cp target/classes graalvm.reflection.ReflectionPropsExample
native-image -cp target/classes graalvm.reflection.ReflectionPropsExample
```

The native binary creation will fail as there is reflection in the code and a fall-back executable which requires the JDK is created instead.

Let's analyze the code with the native image agent and obtain the necessary configuration to create the native binary executable.

```
java -agentlib:native-image-agent=config-output-dir=target/native-image -cp target/classes graalvm.reflection.ReflectionPropsExample
ll target/native-image
cat target/native-image/reflect-config.json
cat target/native-image/resource-config.json
native-image -H:ReflectionConfigurationFiles=target/native-image/reflect-config.json -H:ResourceConfigurationFiles=target/native-image/resource-config.json -cp target/classes graalvm.reflection.ReflectionPropsExample
otool -L graalvm.reflection.reflectionpropsexample
ll graalvm.reflection.reflectionpropsexample
./graalvm.reflection.ReflectionPropsExample
```

This example works without extra configuration because the reflection operations are "effective final" and thus, known to the ahead of time compiler.

```
native-image -cp target/classes graalvm.reflection.ReflectionExample
```

#### Creating a Docker image

This example requires a working Docker machine and `docker` executable in path.

```
cd reflection
docker build -t graalvm-native-image -f graalvm-native-image.Dockerfile .
docker images | grep graalvm
docker run -it --rm --mount type=bind,source=$PWD,target=/opt/graalvm graalvm-native-image "--static" "-H:ReflectionConfigurationFiles=target/native-image/reflect-config.json" "-H:ResourceConfigurationFiles=target/native-image/resource-config.json" "-cp" "target/classes" "graalvm.reflection.ReflectionPropsExample"
./graalvm.reflection.reflectionpropsexample
```

The above command will fail on non-Linux machines, because it is a Linux native binary.

```
docker build -t reflectionpropsexample -f reflectionpropsexample.Dockerfile .
docker images | grep reflectionpropsexample
docker run -it --rm reflectionpropsexample
```

### Non-JVM language examples

#### Running a Node.js+Express simple web service

```
cd hello-express
npm install
node hello-express.js
```

### Interoperability examples

#### Running Java code from Ruby and Python

```
cd interop
ruby --jvm ruby-java.rb
graalpython --jvm python-java.py
```

#### Running a Node.js+Express service which returns a 3D plot done by R code

```
cd cloudplot
npm install
node --jvm --polyglot cloudplot.js
```
