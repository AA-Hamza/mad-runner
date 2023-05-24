.PHONY: all build run docs

build:
	gradle build --build-cache

run:
	gradle run --console=plain -q

jar:
	gradle jar

docs:
	doxygen ./documentation.config
