FROM java:8-jre

ENV VERTICLE_FILE target/Runner.jar
ENV CONF_FOLDER src
# Set the location of the verticles
ENV VERTICLE_HOME /opt/verticles

EXPOSE 8086

COPY $VERTICLE_FILE $VERTICLE_HOME/
ADD $CONF_FOLDER $VERTICLE_HOME/src/

WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]

CMD ["java -jar Runner.jar -conf ./src/config/env.json"  ]
