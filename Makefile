#makefile
APPLICATION_NAME := dock-transfer-banking
PORT := 8080
PROFILE := docker

docker-compose-up: docker-compose-down
	docker-compose -f docker-compose-test.yaml up

docker-compose-down:
	docker-compose -f docker-compose-test.yaml down -v

build-docker:
	docker build --build-arg SPRING_PROFILES_ACTIVE=$(PROFILE) -f Dockerfile -t $(APPLICATION_NAME):latest .
	docker build --build-arg SPRING_PROFILES_ACTIVE=$(PROFILE) -f Dockerfile -t $(APPLICATION_NAME):$(shell echo $(shell mvn help:evaluate -Dexpression=project.version -q -DforceStdout) | perl -pe 's/-SNAPSHOT//') .

run-build-docker:	build-docker
	docker run -e SPRING_PROFILE=$(PROFILE) -p $(PORT):$(PORT) -t $(APPLICATION_NAME):latest

run-docker:
	docker run -e SPRING_PROFILE=$(PROFILE) -p $(PORT):$(PORT) -t $(APPLICATION_NAME):latest
