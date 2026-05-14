#!/bin/bash
#
# Smoke test for LPD-90199 — pageSpecification as a NestedField-supplied field
# on PageSpecificationVersion.
#
# Verifies:
#   - List endpoint OMITS pageSpecification by default.
#   - List with ?nestedFields=pageSpecification INCLUDES it on each item.
#   - GET-one with ?nestedFields=pageSpecification INCLUDES it.
#   - Transitive ?nestedFields=pageSpecificationVersions.pageSpecification on
#     the SitePage endpoint INCLUDES it on every version.
#
# Env vars (all optional):
#   BASE_URL        default: http://localhost:8080
#   LIFERAY_USER    default: test@liferay.com
#   LIFERAY_PASS    default: test
#   SITE_ERC        default: L_GUEST
#   SITE_PAGE_ERC   default: 3ac76b3a-d6e5-738f-49d7-749fdd540579
#   JQ_FILTER       default: .  (use e.g. '.items | map(.externalReferenceCode)'
#                   to slim output)
#
# Usage:
#   ./scripts/test-page-specification-versions-nested.sh
#   SITE_ERC=mysite ./scripts/test-page-specification-versions-nested.sh

set -u

BASE_URL="${BASE_URL:-http://localhost:8080}"
LIFERAY_USER="${LIFERAY_USER:-test@liferay.com}"
LIFERAY_PASS="${LIFERAY_PASS:-test}"
SITE_ERC="${SITE_ERC:-L_GUEST}"
SITE_PAGE_ERC="${SITE_PAGE_ERC:-3ac76b3a-d6e5-738f-49d7-749fdd540579}"
JQ_FILTER="${JQ_FILTER:-.}"

API_BASE="${BASE_URL}/o/headless-admin-site/v1.0"
VERSIONS_BASE="${API_BASE}/sites/${SITE_ERC}/site-pages/${SITE_PAGE_ERC}/page-specification-versions"
SITE_PAGE_BASE="${API_BASE}/sites/${SITE_ERC}/site-pages/${SITE_PAGE_ERC}"

_pretty() {
	if command -v jq >/dev/null 2>&1; then
		jq "${JQ_FILTER}" 2>/dev/null || cat
	else
		cat
	fi
}

_call() {
	local title="$1"
	local url="$2"
	local expect="$3"

	echo
	echo "═══════════════════════════════════════════════════════════════"
	echo ">>> ${title}"
	echo "Expect: ${expect}"
	echo "GET ${url}"
	echo "───────────────────────────────────────────────────────────────"

	local tmp
	tmp=$(mktemp)
	local status
	status=$(curl \
		--silent \
		--user "${LIFERAY_USER}:${LIFERAY_PASS}" \
		--url "${url}" \
		--output "${tmp}" \
		--write-out '%{http_code}')

	echo "[HTTP ${status}]"
	_pretty < "${tmp}"
	rm -f "${tmp}"
}

_has_page_specification() {
	local url="$1"
	curl --silent --user "${LIFERAY_USER}:${LIFERAY_PASS}" --url "${url}" \
		| jq -r '.items[0].pageSpecification // "ABSENT"' 2>/dev/null
}

echo "Base URL:        ${BASE_URL}"
echo "User:            ${LIFERAY_USER}"
echo "Site ERC:        ${SITE_ERC}"
echo "Site page ERC:   ${SITE_PAGE_ERC}"

# Scenario 1: list without nested fields
_call \
	"1. List versions (no nestedFields)" \
	"${VERSIONS_BASE}" \
	"pageSpecification absent / null in each item"

# Pull the first version's ERC for scenario 3
VERSION_ERC=""
if command -v jq >/dev/null 2>&1; then
	VERSION_ERC=$(curl --silent \
		--user "${LIFERAY_USER}:${LIFERAY_PASS}" \
		--url "${VERSIONS_BASE}" \
		| jq -r '.items[0].externalReferenceCode // empty' 2>/dev/null)
fi

# Scenario 2: list WITH nested field
_call \
	"2. List versions with ?nestedFields=pageSpecification" \
	"${VERSIONS_BASE}?nestedFields=pageSpecification" \
	"pageSpecification populated on each item"

# Scenario 3: get-one WITH nested field
if [ -n "${VERSION_ERC}" ]; then
	_call \
		"3. Get one version (${VERSION_ERC}) with ?nestedFields=pageSpecification" \
		"${VERSIONS_BASE}/${VERSION_ERC}?nestedFields=pageSpecification" \
		"pageSpecification populated"
else
	echo
	echo "(skipping scenario 3 — could not extract a version ERC from scenario 1's response)"
fi

# Scenario 4: transitive nested from site-page
_call \
	"4. Site-page with ?nestedFields=pageSpecificationVersions.pageSpecification" \
	"${SITE_PAGE_BASE}?nestedFields=pageSpecificationVersions.pageSpecification" \
	"pageSpecificationVersions array with pageSpecification on each version"

echo
echo "═══════════════════════════════════════════════════════════════"
echo "Done."
