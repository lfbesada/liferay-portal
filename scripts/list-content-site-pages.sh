#!/usr/bin/env bash
set -euo pipefail

if [ $# -lt 1 ]; then
	echo "Usage: $0 <siteExternalReferenceCode>" >&2
	exit 1
fi

SITE_ERC="$1"

HOST="${LIFERAY_HOST:-http://localhost:8080}"
USER="${LIFERAY_USER:-test@liferay.com}"
PASSWORD="${LIFERAY_PASSWORD:-test}"

curl \
	--fail \
	--silent \
	--show-error \
	--user "${USER}:${PASSWORD}" \
	--url "${HOST}/o/headless-admin-site/v1.0/sites/${SITE_ERC}/site-pages?pageSize=200&fields=externalReferenceCode,title,type" \
	| jq -r '.items[] | select(.type == "ContentPage") | .externalReferenceCode'
