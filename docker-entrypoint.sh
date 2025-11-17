#!/bin/bash
set -euo pipefail

TEMPLATE="/tmp/context-template.xml"
TOMCAT_CONF="/usr/local/tomcat/conf/context.xml"

: "${DB_HOST:?Need to set DB_HOST}"
: "${DB_PORT:?Need to set DB_PORT}"
: "${DB_NAME:?Need to set DB_NAME}"
: "${DB_USER:?Need to set DB_USER}"
: "${DB_PASS:?Need to set DB_PASS}"

if command -v envsubst >/dev/null 2>&1; then
  envsubst < "${TEMPLATE}" > "${TOMCAT_CONF}"
else
  sed -e "s|\${DB_HOST}|${DB_HOST}|g"               -e "s|\${DB_PORT}|${DB_PORT}|g"               -e "s|\${DB_NAME}|${DB_NAME}|g"               -e "s|\${DB_USER}|${DB_USER}|g"               -e "s|\${DB_PASS}|${DB_PASS}|g"               "${TEMPLATE}" > "${TOMCAT_CONF}"
fi

echo "Wrote Tomcat context to ${TOMCAT_CONF}"
exec "$@"
