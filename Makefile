clean:
	gradle clean
shadowJar:
	gradle shadowJar
jpackage:
	jpackage --type rpm --verbose --input build/libs --dest build/libs --name Vifeco --main-jar vifeco-0.1.0.jar --main-class org.laeq.Launcher --module-path /home/david/.sdkman/candidates/java/15.0.2.fx-librca/jmods --add-modules java.base,java.logging,javafx.controls,javafx.web,javafx.graphics,javafx.media,javafx.fxml
install:
	sudo rpm -ivh build/libs/vifeco-1.0-1.x86_64.rpm
remove:
	sudo rpm -e vifeco-1.0-1.x86_64

all: clean shadowJar jpackage install