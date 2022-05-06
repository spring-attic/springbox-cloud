#!/bin/bash

usage() {
    echo "Error: missing argument"
    echo
    echo "Usage: $0 [OAUTH_SERVER_HOSTNAME] [OAUTH_SERVER_PORT] [RECOMMENDATIONS_HOSTNAME] [RECOMMENDATIONS_PORT]"
    echo "   eg. $0 localhost 9999 localhost 8082"
    exit 1
}

header() {
    echo "================================================================================"
    echo $@ | sed  -e :a -e 's/^.\{1,77\}$/ & /;ta'
    echo "================================================================================"
 
}

OAUTH_SERVER_HOSTNAME="$1"
if [ "$OAUTH_SERVER_HOSTNAME" == "" ] ; then
	usage
fi

OAUTH_SERVER_PORT="$2"
  if [ "$OAUTH_SERVER_PORT" == "" ] ; then
  	usage
  fi

RECOMMENDATIONS_HOSTNAME="$3"
  if [ "$RECOMMENDATIONS_HOSTNAME" == "" ] ; then
	usage  
  fi
RECOMMENDATIONS_PORT="$4"
    if [ "$RECOMMENDATIONS_PORT" == "" ] ; then
          usage
    fi

header "Getting access token from server..."
TOKEN=$(curl -X POST -d "grant_type=password&username=mstine&password=secret" http://${OAUTH_SERVER_HOSTNAME}:${OAUTH_SERVER_PORT}/uaa/oauth/token -H "Authorization: Basic YWNtZTphY21lc2VjcmV0" | jq -r '.access_token')
echo "access_token: ${TOKEN}"

header "Loading People..."
./loadPeople.sh ${TOKEN} ${RECOMMENDATIONS_HOSTNAME} ${RECOMMENDATIONS_PORT}
header "Loading Movies..."
./loadMovies.sh ${TOKEN} ${RECOMMENDATIONS_HOSTNAME} ${RECOMMENDATIONS_PORT}
header "Loading Likes..."
./loadLikes.sh ${TOKEN} ${RECOMMENDATIONS_HOSTNAME} ${RECOMMENDATIONS_PORT}
