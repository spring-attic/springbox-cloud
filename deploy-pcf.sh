#!/usr/bin/env bash

REGISTRY_SERVICE=springbox-registry
CONFIG_SERVICE=springbox-config

MYSQL_SERVICE=springbox-mysql
MONGODB_SERVICE=springbox-mongodb
NEO4J_SERVICE=springbox-neo4j
RABBITMQ_SERVICE=springbox-rabbitmq

SSO_SERVICE=sso

if ! (cf s | grep ^$CONFIG_SERVICE); then
    cf cs p-config-server standard $CONFIG_SERVICE
fi

if ! (cf s | grep ^$REGISTRY_SERVICE); then
    cf cs p-service-registry standard $REGISTRY_SERVICE
fi

if ! (cf s | grep ^$MYSQL_SERVICE); then
    cf cs p-mysql 100mb-dev $MYSQL_SERVICE
fi

if ! (cf s | grep ^$NEO4J_SERVICE); then
    cf cs p-neo4j development $NEO4J_SERVICE
fi

if ! (cf s | grep ^$MONGODB_SERVICE); then
    cf cs p-mongodb development $MONGODB_SERVICE
fi

if ! (cf s | grep ^$RABBITMQ_SERVICE); then
    cf cs p-rabbitmq standard $RABBITMQ_SERVICE
fi

cf push springbox-auth-server -p ./springbox-auth-server/target/springbox-auth-server-0.0.1-SNAPSHOT.jar --no-manifest

if ! (cf s | grep ^$SSO_SERVICE); then
    DOMAINS_INFO="$(cf domains)"
    DOMAINS_REGEX="([^[[:space:]]*)[[:blank:]]*shared"
    [[ "$DOMAINS_INFO" =~ $DOMAINS_REGEX ]]

    APP_DOMAIN="${BASH_REMATCH[1]}"
    SSO_URI="http://springbox-auth-server.$APP_DOMAIN"
    cf cups $SSO_SERVICE -p '{"userInfoUri":"'"$SSO_URI"'/uaa/user", "authorizationUri":"'"$SSO_URI"'/uaa/authorize", "clientId":"acme", "clientSecret":"acmesecret"}'
fi

cf push