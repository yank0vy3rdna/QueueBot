#!/bin/bash
config=$(cat src/main/resources/application.properties.dist)
config=${config/db_url/$DATASOURCE_URL}
config=${config/db_username/$DATASOURCE_USERNAME}
config=${config/db_pass/$DATASOURCE_PASS}
config=${config/BotFirstName/$BOT_NAME}
config=${config/TelegramToken/$BOT_TOKEN}
echo "$config" > src/main/resources/application.properties
mvn spring-boot:run