#!/usr/bin/env sh
set -eu

API_URL="${OPENFGA_API_URL:-http://localhost:8082}"
STORE_NAME="${OPENFGA_STORE_NAME:-school-saas-store}"
MODEL_JSON_FILE="${OPENFGA_MODEL_JSON_FILE:-infra/openfga/model.json}"
TUPLES_FILE="${OPENFGA_TUPLES_FILE:-infra/openfga/tuples.json}"

echo "Using OpenFGA API: ${API_URL}"
echo "Store name: ${STORE_NAME}"

STORE_ID="$(curl -fsS "${API_URL}/stores" \
  | tr '{' '\n' \
  | grep -F "\"name\":\"${STORE_NAME}\"" -B 2 -A 2 \
  | grep -o '"id":"[^"]*"' \
  | head -1 \
  | cut -d'"' -f4 || true)"

if [ -z "${STORE_ID}" ]; then
  echo "Creating store..."
  STORE_ID="$(curl -fsS -X POST "${API_URL}/stores" \
    -H 'Content-Type: application/json' \
    -d "{\"name\":\"${STORE_NAME}\"}" \
    | grep -o '"id":"[^"]*"' \
    | head -1 \
    | cut -d'"' -f4)"
fi

echo "Store id: ${STORE_ID}"
echo "Uploading authorization model from ${MODEL_JSON_FILE}"

if ! MODEL_RESPONSE="$(curl -fsS -X POST "${API_URL}/stores/${STORE_ID}/authorization-models" \
  -H 'Content-Type: application/json' \
  --data-binary @"${MODEL_JSON_FILE}")"; then
  echo "OpenFGA model upload failed."
  echo "The OpenFGA datastore is running and the store exists, but the JSON model still needs validation."
  echo "Inspect infra/openfga/model.json and the OpenFGA logs, then retry this script."
  exit 1
fi

MODEL_ID="$(printf '%s' "${MODEL_RESPONSE}" | grep -o '"authorization_model_id":"[^"]*"' | head -1 | cut -d'"' -f4 || true)"
echo "Authorization model id: ${MODEL_ID}"

TUPLES_JSON="$(cat "${TUPLES_FILE}")"
curl -fsS -X POST "${API_URL}/stores/${STORE_ID}/write" \
  -H 'Content-Type: application/json' \
  -d "{\"writes\":{\"tuple_keys\":${TUPLES_JSON}}}"

echo "OpenFGA setup complete"
