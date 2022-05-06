#!/bin/bash

usage() {
    echo "Error: missing argument"
    echo
    echo "Usage: $0 [OAUTH_SERVER_HOSTNAME] [OAUTH_SERVER_PORT] [REVIEWS_HOSTNAME] [REVIEWS_PORT]"
    echo "   eg. $0 localhost 9999 localhost 8081"
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

REVIEWS_HOSTNAME="$3"
  if [ "$REVIEWS_HOSTNAME" == "" ] ; then
	usage
  fi
REVIEWS_PORT="$4"
    if [ "$REVIEWS_PORT" == "" ] ; then
          usage
    fi

header "Getting access token from server..."
TOKEN=$(curl -X POST -d "grant_type=password&username=mstine&password=secret" http://${OAUTH_SERVER_HOSTNAME}:${OAUTH_SERVER_PORT}/uaa/oauth/token -H "Authorization: Basic YWNtZTphY21lc2VjcmV0" | jq -r '.access_token')
echo "access_token: ${TOKEN}"

ROUTE=${REVIEWS_HOSTNAME}:${REVIEWS_PORT}

curl ${ROUTE}/reviews -H "Authorization: Bearer ${TOKEN}" -X POST -d '{"userName":"mstine","mlId":"1","title":"Toy Story (1995)","review":"Great movie!","rating":"5"}' -H "Content-Type: application/json"
curl ${ROUTE}/reviews -H "Authorization: Bearer ${TOKEN}" -X POST -d '{"userName":"mstine","mlId":"2","title":"GoldenEye (1995)","review":"Pretty good...","rating":"3"}' -H "Content-Type: application/json"
curl ${ROUTE}/reviews -H "Authorization: Bearer ${TOKEN}" -X POST -d '{"userName":"starbuxman","mlId":"2","title":"GoldenEye (1995)","review":"BOND BOND BOND!","rating":"5"}' -H "Content-Type: application/json"
curl ${ROUTE}/reviews -H "Authorization: Bearer ${TOKEN}" -X POST -d '{"userName":"starbuxman","mlId":"4","title":"Get Shorty (1995)","review":"Meh","rating":"3" }}' -H "Content-Type: application/json"
curl ${ROUTE}/reviews -H "Authorization: Bearer ${TOKEN}" -X POST -d '{"userName":"starbuxman","mlId":"5","title":"Copycat (1995)","review":"Nicely done!","rating":"4"}' -H "Content-Type: application/json"
curl ${ROUTE}/reviews -H "Authorization: Bearer ${TOKEN}" -X POST -d '{"userName":"littleidea","mlId":"2","title":"GoldenEye (1995)","review":"Good show!","rating":"4"}' -H "Content-Type: application/json"
curl ${ROUTE}/reviews -H "Authorization: Bearer ${TOKEN}" -X POST -d '{"userName":"littleidea","mlId":"3","title":"Four Rooms (1995)","review":"Could have been better...","rating":"3"}' -H "Content-Type: application/json"
curl ${ROUTE}/reviews -H "Authorization: Bearer ${TOKEN}" -X POST -d '{"userName":"littleidea","mlId":"5","title":"Copycat (1995)","review":"Nicely done!","rating":"4"}' -H "Content-Type: application/json"
