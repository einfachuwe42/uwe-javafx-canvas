Bester Stream! https://twitch.tv/einfachuwe42

# Uwe JavaFX Canvas

## Java 17 installieren

### Download
https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-22.0.0.2

### Umgebung Variabeln und Path
```
JAVA_HOME=/home/uwe/graalvm
PATH=PATH;JAVA_HOME/bin
```

## Anwendung bauen

Im Projektordner `gradlew quarkusBuild` ausführen.

## Anwendung starten

`java -jar $PROJECT_DIR/build/quarkus-app/quarkus-run.jar`


```
case R -> restart();
case P -> pause();
case A -> points.add(Point.create(mouseX, mouseY, mouseX, mouseY));
case Y -> addPointWithConstrain(mouseX, mouseY);
case S -> selectPoint(mouseX, mouseY);
case D -> createConstrain();
case Q -> save();
case W -> load();
case C -> clear();
case G -> switchGravity();
case X -> addPointWithConstrainFixLength(mouseX, mouseY);
```


