#!/usr/bin/env bash
# Prova que os .java da Fase 2 são idênticos aos da Fase 1 (SHA-256).
# Sai com 0 só se todos forem iguais e nenhum faltar ou sobrar.
set -uo pipefail

cd "$(dirname "$0")/.."
ORIGEM="../fase-01-refatoracao/src"
DESTINO="src/main/java"

if command -v shasum >/dev/null 2>&1; then
  hash_de() { shasum -a 256 "$1" | cut -d' ' -f1; }
else
  hash_de() { sha256sum "$1" | cut -d' ' -f1; }
fi

listar() { (cd "$1" && find . -name '*.java' | sed 's|^\./||' | sort); }

total=0
problemas=0
while IFS= read -r arquivo; do
  total=$((total + 1))
  a="ausente"; b="ausente"
  [ -f "$ORIGEM/$arquivo" ] && a="$(hash_de "$ORIGEM/$arquivo")"
  [ -f "$DESTINO/$arquivo" ] && b="$(hash_de "$DESTINO/$arquivo")"
  if [ "$a" = "ausente" ]; then
    estado="SÓ NA FASE 2"
  elif [ "$b" = "ausente" ]; then
    estado="SÓ NA FASE 1"
  elif [ "$a" = "$b" ]; then
    estado="OK"
  else
    estado="DIFERENTE"
  fi
  [ "$estado" = "OK" ] || problemas=$((problemas + 1))
  printf '%s  %s  %s  %s\n' "$arquivo" "${a:0:12}" "${b:0:12}" "$estado"
done < <( { listar "$ORIGEM"; listar "$DESTINO"; } | sort -u )

echo
if [ "$problemas" -eq 0 ]; then
  echo "$total arquivos comparados: todos idênticos"
  exit 0
else
  echo "$total arquivos comparados: $problemas com problema"
  exit 1
fi
