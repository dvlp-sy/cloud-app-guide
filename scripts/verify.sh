#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

echo "[verify] 저장소 구조 확인"
test -f AGENTS.md
test -f harness/goals.md
test -f harness/architecture.md
test -f harness/quality-gates.md
test -f harness/review-checklist.md
test -f harness/workflows/feature.md
test -f harness/workflows/bugfix.md
test -f harness/workflows/review.md
test -f scripts/verify.sh

echo "[verify] Git 공백 오류 확인"
git diff --check

echo "[verify] Gradle 테스트 실행"
./gradlew test --console=plain

echo "[verify] 완료"
