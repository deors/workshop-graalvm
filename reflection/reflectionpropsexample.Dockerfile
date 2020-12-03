FROM alpine:3.10.2
COPY graalvm.reflection.reflectionpropsexample /opt/docker/exec
RUN chmod +x /opt/docker/exec
CMD ["/opt/docker/exec"]
