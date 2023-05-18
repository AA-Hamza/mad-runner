.PHONY: all build run

build:
	gradle build --build-cache

run:
	gradle run --console=plain -q
