#!/usr/bin/env bash
set -euo pipefail

if [ $# -lt 3 ]; then
	echo "Usage: $0 <siteExternalReferenceCode> <sitePageExternalReferenceCode> <pageSpecificationVersionExternalReferenceCode>" >&2
	exit 1
fi

SITE_ERC="$1"
SITE_PAGE_ERC="$2"
VERSION_ERC="$3"

HOST="${LIFERAY_HOST:-http://localhost:8080}"
USER="${LIFERAY_USER:-test@liferay.com}"
PASSWORD="${LIFERAY_PASSWORD:-test}"

curl \
	--fail \
	--silent \
	--show-error \
	--user "${USER}:${PASSWORD}" \
	--request POST \
	--url "${HOST}/o/headless-admin-site/v1.0/sites/${SITE_ERC}/site-pages/${SITE_PAGE_ERC}/page-specification-versions/${VERSION_ERC}/restore"
