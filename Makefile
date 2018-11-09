all:
	javac -sourcepath src/ -d classes/ src/GUILauncher.java

clean:
	rm -rf classes/*

run:
	java -cp classes/ GUILauncher

test:
	javac -sourcepath src/ -d classes src/CLILauncher.java && java -cp classes/ CLILauncher
