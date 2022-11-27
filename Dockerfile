FROM ubuntu:latest

RUN apt update && apt install -y openssh-server sudo openjdk-8-jdk maven || true

ARG user=user 
ARG password=password

RUN useradd -rm -d "/home/$user" -s /bin/bash -g root -G sudo -u 1000 "$user"

RUN  echo "$user:$password" | chpasswd

RUN service ssh start

EXPOSE 22

CMD ["/usr/sbin/sshd","-D"]
