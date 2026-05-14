#!/usr/bin/env bash
set -euo pipefail

if [ $# -lt 2 ]; then
	echo "Usage: $0 <siteExternalReferenceCode> <sitePageExternalReferenceCode> [name] [externalReferenceCode]" >&2
	exit 1
fi

SITE_ERC="$1"
SITE_PAGE_ERC="$2"
NAME="${3:-}"
VERSION_ERC="${4:-}"

HOST="${LIFERAY_HOST:-http://localhost:8080}"
USER="${LIFERAY_USER:-test@liferay.com}"
PASSWORD="${LIFERAY_PASSWORD:-test}"

BODY=$(
	jq --null-input \
		--arg name "${NAME}" \
		--arg externalReferenceCode "${VERSION_ERC}" \
		'{} + (if $name != "" then {name: $name} else {} end)
		    + (if $externalReferenceCode != "" then {externalReferenceCode: $externalReferenceCode} else {} end)'
)

curl \
	--fail \
	--silent \
	--show-error \
	--user "${USER}:${PASSWORD}" \
	--header "Content-Type: application/json" \
	--data "${BODY}" \
	--request POST \
	--url "${HOST}/o/headless-admin-site/v1.0/sites/${SITE_ERC}/site-pages/${SITE_PAGE_ERC}/page-specification-versions" \
	| jq '.'
