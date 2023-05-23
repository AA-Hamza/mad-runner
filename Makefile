.PHONY: all build run docs

build:
	gradle build --build-cache

run:
	gradle run --console=plain -q

docs:
	doxygen ./documentation.config
