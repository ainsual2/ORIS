#!/usr/bin/env bash
set -euo pipefail

# Usage:
#   ./scripts/benchmark_compare.sh /absolute/or/relative/path/to/image.jpg [runs]
# Default runs: 20

if [[ $# -lt 1 ]]; then
  echo "Usage: $0 <image_path> [runs]"
  exit 1
fi

IMAGE_PATH="$1"
RUNS="${2:-20}"
BASE_URL="${BASE_URL:-http://127.0.0.1:8080}"

if [[ ! -f "$IMAGE_PATH" ]]; then
  echo "Image file not found: $IMAGE_PATH"
  exit 1
fi

if ! [[ "$RUNS" =~ ^[0-9]+$ ]] || [[ "$RUNS" -le 0 ]]; then
  echo "Runs must be a positive integer"
  exit 1
fi

check_endpoint() {
  local path="$1"
  local code
  code=$(curl -s -o /dev/null -w "%{http_code}" -F "image=@${IMAGE_PATH}" "${BASE_URL}${path}" || true)
  if [[ "$code" -lt 200 || "$code" -ge 400 ]]; then
    echo "Endpoint ${BASE_URL}${path} returned HTTP ${code}."
    echo "Make sure Java app + Python services + NATS are running."
    exit 1
  fi
}

measure_ms() {
  local path="$1"
  local total=0
  local min=99999999
  local max=0
  local i elapsed

  for ((i=1; i<=RUNS; i++)); do
    elapsed=$(curl -s -o /dev/null -w "%{time_total}" -F "image=@${IMAGE_PATH}" "${BASE_URL}${path}")
    local ms
    ms=$(awk -v t="$elapsed" 'BEGIN { printf "%d", t*1000 }')
    total=$((total + ms))
    (( ms < min )) && min=$ms
    (( ms > max )) && max=$ms
    printf "%s run %02d: %d ms\n" "$path" "$i" "$ms"
  done

  local avg
  avg=$((total / RUNS))
  echo "$avg $min $max"
}

echo "Checking endpoints..."
check_endpoint "/upload"
check_endpoint "/upload2"

echo "Running benchmark: ${RUNS} iterations each"
read -r http_avg http_min http_max < <(measure_ms "/upload")
read -r nats_avg nats_min nats_max < <(measure_ms "/upload2")

echo ""
echo "===== RESULT ====="
echo "HTTP  (/upload)  avg=${http_avg}ms min=${http_min}ms max=${http_max}ms"
echo "NATS  (/upload2) avg=${nats_avg}ms min=${nats_min}ms max=${nats_max}ms"

if (( nats_avg < http_avg )); then
  echo "Winner by average latency: NATS"
elif (( nats_avg > http_avg )); then
  echo "Winner by average latency: HTTP"
else
  echo "Average latency tie"
fi
