.PHONY: all build run docs

build:
	./gradlew build --build-cache

run:
	./gradlew run --console=plain -q

jar:
	./gradlew clean build jar

docs:
	doxygen ./documentation.config
