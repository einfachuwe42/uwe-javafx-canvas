#!/bin/bash

PROJECT_DIR=/home/uwe/java/twitch/uwe-javafx-canvas

$PROJECT_DIR/gradlew quarkusBuild
java -jar $PROJECT_DIR/build/quarkus-app/quarkus-run.jar