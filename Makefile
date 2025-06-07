TARGET_DIR=./deploy

.PHONY: prepare
prepare:
	rm -f $(TARGET_DIR)/fun-events-start-*.jar
	@JAR_FILE=$(shell ls ./fun-events-start/fun-events-start-app/target/fun-events-start-app-*.jar | head -n 1); \
	cp "$$JAR_FILE" "$(TARGET_DIR)/fun-events-start-app.jar"
	@JAR_FILE=$(shell ls ./fun-events-start/fun-events-start-supply/target/fun-events-start-supply-*.jar | head -n 1); \
	cp "$$JAR_FILE" "$(TARGET_DIR)/fun-events-start-supply.jar"	
		
build:
	mvn clean install

up:
	docker-compose -f $(TARGET_DIR)/docker-compose.yml up -d	
	
.PHONY: deploy
deploy: prepare up

stop:
	docker-compose -f $(TARGET_DIR)/docker-compose.yml down
