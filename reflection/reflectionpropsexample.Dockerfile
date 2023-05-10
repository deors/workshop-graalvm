FROM debian:10.13-slim
ADD graalvm.reflection.reflectionpropsexample /opt/appexec
RUN chmod +x /opt/appexec
CMD ["/opt/appexec"]
