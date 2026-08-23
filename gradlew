#!/usr/bin/env sh
# Minimal wrapper launcher - Android Studio will regenerate the full wrapper
# on first sync ("Sync Project with Gradle Files"). If you have Gradle 8.7+
# installed locally you can also just run `gradle <task>` directly.
DIR="$(cd "$(dirname "$0")" && pwd)"
exec gradle "$@"
