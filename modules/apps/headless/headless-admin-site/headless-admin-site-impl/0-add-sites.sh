#!/bin/bash

# =======================================================
# SCRIPT TO CREATE LIFERAY SITES USING THE REST API
# This script requires 'curl' for making requests.
# =======================================================

# -------------------------------------------------------
# 1. ENVIRONMENT CONFIGURATION
# -------------------------------------------------------
LIFERAY_URL="http://localhost:8080"
USERNAME="test@liferay.com"
PASSWORD="test"
API_ENDPOINT="${LIFERAY_URL}/o/headless-admin-site/v1.0/sites"

# Define an array of site definitions in the format: "EXTERNAL_REF_CODE,Site Name"
# Add more definitions here to create new sites.
SITE_DEFINITIONS=(
	"OriginSite,La Maison de Pablo,com.liferay.site.initializer.welcome,site-initializer"
	"TargetSite,La Casa de Pablo,,"
)

# -------------------------------------------------------
# 2. FUNCTION TO CREATE A SITE
# Parameters:
# $1: External Reference Code (unique identifier)
# $2: Site Name (human-readable name)
# -------------------------------------------------------
create_site() {
	local EXTERNAL_REF_CODE="$1"
	local SITE_NAME="$2"
	local TEMPLATE_KEY="$3"
	local TEMPLATE_TYPE="$4"

	echo ""
	echo "⚙️  Attempting to create Site: ${SITE_NAME} (ERC: ${EXTERNAL_REF_CODE})"

	# Build the JSON payload
	JSON_PAYLOAD=$(cat <<EOF
{
	  "active": true,
	  "externalReferenceCode": "${EXTERNAL_REF_CODE}",
	  "manualMembership": true,
	  "membershipRestriction": 0,
	  "name": "${SITE_NAME}",
	  "name_i18n": {
		"en-US": "${SITE_NAME}"
	  },
	  "membershipType": "open",
	  "templateKey": "${TEMPLATE_KEY}",
	  "templateType": "${TEMPLATE_TYPE}"
}
EOF
)

	# Execute the POST request to the Liferay REST API
	# -s: Silent mode
	# -w: Output format after transfer is complete (to get the status code)
	# -X: Specify request command (POST)
	# -u: Defines basic authentication (username:password)
	# -H: Sets the Content-Type as JSON
	# -d: Provides the JSON request body

	HTTP_RESPONSE=$(curl -s -w "\nHTTP_STATUS:%{http_code}\n" -X POST \
		-u "${USERNAME}:${PASSWORD}" \
		-H "Content-Type: application/json" \
		-d "${JSON_PAYLOAD}" \
		"${API_ENDPOINT}")

	# Extract the HTTP status code and response body
	HTTP_STATUS=$(echo "${HTTP_RESPONSE}" | awk '/HTTP_STATUS/{print $2}')
	RESPONSE_BODY=$(echo "${HTTP_RESPONSE}" | sed '/HTTP_STATUS/d')

	if [ "${HTTP_STATUS}" = "200" ] || [ "${HTTP_STATUS}" = "201" ]; then
		echo "✅ Success creating ${SITE_NAME}. Status code: ${HTTP_STATUS}"
	else
		echo "❌ Error creating ${SITE_NAME}. Status code: ${HTTP_STATUS}"
		echo "API Response:"
		echo "${RESPONSE_BODY}"
	fi
}

# -------------------------------------------------------
# 3. MAIN EXECUTION
# -------------------------------------------------------
echo "==============================================="
echo "  STARTING SITE CREATION IN LIFERAY DXP"
echo "==============================================="

# Iterate over the array and parse the definition for ERC and Name
for DEFINITION in "${SITE_DEFINITIONS[@]}"; do
	# Temporarily set IFS (Internal Field Separator) to comma for reading
	IFS=',' read -r EXTERNAL_REF_CODE SITE_NAME TEMPLATE_KEY TEMPLATE_TYPE <<< "$DEFINITION"

	# Basic validation check
	if [ -n "$EXTERNAL_REF_CODE" ] && [ -n "$SITE_NAME" ]; then
		create_site "${EXTERNAL_REF_CODE}" "${SITE_NAME}" "${TEMPLATE_KEY}" "${TEMPLATE_TYPE}"
	else
		echo "⚠️  Skipping invalid definition: ${DEFINITION}"
	fi
done

echo ""
echo "==============================================="
echo "  SITE CREATION PROCESS FINISHED"
echo "==============================================="