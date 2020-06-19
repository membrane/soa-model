FROM ubuntu:xenial

RUN apt-get update && apt-get install -y openjdk-8-jdk-headless curl

RUN  rm -rf /var/lib/apt/lists/* && rm -rf /var/cache/oracle-jdk7-installer

RUN curl -o /maven.tar.gz http://ftp.halifax.rwth-aachen.de/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz && \
  mkdir /maven && \
  cd /maven && \
  tar -xvf /maven.tar.gz && \
  rm /maven.tar.gz

ADD pom.xml /app/
ADD core/pom.xml /app/core/
ADD distribution/pom.xml /app/distribution/
WORKDIR /app

RUN if [ -d .m2 ] ; then mv .m2 /root ; fi

# fake maven run to pre-cache a few maven dependencies
RUN /maven/apache-maven-*/bin/mvn integration-test ; exit 0

ADD . /app

RUN /maven/apache-maven-*/bin/mvn integration-test