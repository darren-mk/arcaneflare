FROM alpine_jdk17_lein
WORKDIR /root/app
COPY . .
EXPOSE 8000
CMD ["lein", "run"]