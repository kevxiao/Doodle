JFLAGS = -g
JC = javac
JVM= java
FILE=
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Doodle.java \
	DoodleModel.java \
	Sidebar.java \
	Bottombar.java \
	DrawArea.java \
	Constants.java \
	Line.java \
	Stroke.java \
	Observer.java

MAIN = Doodle

default: classes

classes: $(CLASSES:.java=.class)

run: classes
	$(JVM) $(MAIN)

clean:
	$(RM) *.class