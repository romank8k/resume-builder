FROM almalinux/9-base:9.5

RUN dnf install -y --setopt=tsflags=nodocs \
        java-17-openjdk && \
    dnf clean all -y --enablerepo='*'

ENV JAVA_HOME=/usr/lib/jvm/jre-17-openjdk

ENV RESUME_GENERATOR_HOME=/usr/local/opt/resume-generator
RUN mkdir -p $RESUME_GENERATOR_HOME
WORKDIR $RESUME_GENERATOR_HOME

COPY entrypoint.sh /usr/local/bin/entrypoint.sh

COPY maven/fonts/ ./fonts/
COPY maven/lib/ ./lib/
COPY maven/config/ ./
COPY maven/*.jar ./

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["resume-generator"]
