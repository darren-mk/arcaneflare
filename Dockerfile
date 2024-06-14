FROM alpine_jdk17_lein
WORKDIR /app
COPY env /app/env
COPY src /app/src
COPY resources /app/resources
COPY dev-config.edn /app/dev-config.edn
COPY project.clj /app/project.clj
COPY app.json /app/app.json
CMD ["lein", "run"]