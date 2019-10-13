FROM oracle/graalvm-ce:19.2.0.1
WORKDIR /opt/graalvm
RUN gu install native-image
ENTRYPOINT ["native-image"]
