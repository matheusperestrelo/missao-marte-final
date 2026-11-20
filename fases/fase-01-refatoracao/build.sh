#!/usr/bin/env bash
# Compila a Fase 1 para out/. Com o argumento "run", executa o jogo em seguida.
set -euo pipefail

cd "$(dirname "$0")"

rm -rf out
mkdir -p out
find src -name '*.java' -print0 | xargs -0 javac --release 17 -encoding UTF-8 -d out

if [ "${1:-}" = "run" ]; then
  java -cp out missaomarte.Main
fi
