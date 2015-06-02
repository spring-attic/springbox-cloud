#!/usr/bin/env bash

CATALOG_APP=springbox-catalog
REVIEWS_APP=springbox-reviews
RECOMMENDATIONS_APP=springbox-recommendations
API_GW_APP=springbox-api-gateway
AUTH_APP=springbox-auth-server

if (cf a | grep ^$CATALOG_APP); then
    cf d -f -r $CATALOG_APP
fi

if (cf a | grep ^$REVIEWS_APP); then
    cf d -f -r $REVIEWS_APP
fi

if (cf a | grep ^$RECOMMENDATIONS_APP); then
    cf d -f -r $RECOMMENDATIONS_APP
fi

if (cf a | grep ^$API_GW_APP); then
    cf d -f -r $API_GW_APP
fi

if (cf a | grep ^$AUTH_APP); then
    cf d -f -r $AUTH_APP
fi

REGISTRY_SERVICE=springbox-registry
CONFIG_SERVICE=springbox-config

MYSQL_SERVICE=springbox-mysql
MONGODB_SERVICE=springbox-mongodb
NEO4J_SERVICE=springbox-neo4j
RABBITMQ_SERVICE=springbox-rabbitmq

SSO_SERVICE=sso

if (cf s | grep ^$CONFIG_SERVICE); then
    cf ds -f $CONFIG_SERVICE
fi

if (cf s | grep ^$REGISTRY_SERVICE); then
    cf ds -f $REGISTRY_SERVICE
fi

if (cf s | grep ^$MYSQL_SERVICE); then
    cf ds -f $MYSQL_SERVICE
fi

if (cf s | grep ^$NEO4J_SERVICE); then
    cf ds -f $NEO4J_SERVICE
fi

if (cf s | grep ^$MONGODB_SERVICE); then
    cf ds -f $MONGODB_SERVICE
fi

if (cf s | grep ^$RABBITMQ_SERVICE); then
    cf ds -f $RABBITMQ_SERVICE
fi

if (cf s | grep ^$SSO_SERVICE); then
    cf ds -f $SSO_SERVICE
fi