#!/usr/bin/env bash

set -euo pipefail

latest_tag="$(git tag --list 'v*' --sort=-version:refname | head -n 1)"

if [[ -n "$latest_tag" ]]; then
    base_version="${latest_tag#v}"
    commit_range="${latest_tag}..HEAD"
else
    base_version="0.0.0"
    commit_range="HEAD"
fi

if [[ ! "$base_version" =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)$ ]]; then
    echo "유효하지 않은 기존 버전 태그: v${base_version}" >&2
    exit 1
fi

major="${BASH_REMATCH[1]}"
minor="${BASH_REMATCH[2]}"
patch="${BASH_REMATCH[3]}"
commit_messages="$(git log "$commit_range" --format='%s%n%b')"

if [[ -z "${commit_messages//[[:space:]]/}" ]]; then
    echo "새 버전을 생성할 커밋이 없습니다." >&2
    exit 1
fi

if grep -Eqi 'BREAKING CHANGE|BREAKING-CHANGE|^[a-z]+(\([^)]*\))?!:' <<< "$commit_messages"; then
    ((major += 1))
    minor=0
    patch=0
elif grep -Eqi '^feat(\([^)]*\))?:' <<< "$commit_messages"; then
    ((minor += 1))
    patch=0
else
    ((patch += 1))
fi

echo "v${major}.${minor}.${patch}"
