#!/usr/bin/env bash
# Gate de qualidade do workflow de specs.
# Mantido pelo usuario. Agentes executam; nao editam.
# Adapta-se as fases: fases/fase-NN-*/ (exceto a fase 0) sao detectadas sozinhas.
set -uo pipefail

cd "$(git rev-parse --show-toplevel)"
RAIZ="$(pwd)"
FASE0="fases/fase-00-console"

falhas=()
TMP="$(mktemp -d "${TMPDIR:-/tmp}/marte-check.XXXXXX")"
trap 'rm -rf "$TMP"' EXIT

etapa() {
  local nome="$1"; shift
  echo "==> $nome"
  if "$@"; then
    echo "    ok"
  else
    echo "    FALHOU"
    falhas+=("$nome")
  fi
}

# ---------- 1. Fase 0 imutavel ----------
fase0_intacta() {
  local ok=0
  if ! git diff --quiet HEAD -- "$FASE0/src"; then
    echo "    $FASE0/src difere do HEAD"; ok=1
  fi
  if [ -n "$(git status --porcelain -- "$FASE0/src")" ]; then
    echo "    $FASE0/src tem alteracoes ou arquivos novos nao commitados"; ok=1
  fi
  if git rev-parse --verify -q main >/dev/null; then
    if ! git diff --quiet main -- "$FASE0/src"; then
      echo "    $FASE0/src difere de main"; ok=1
    fi
  fi
  return $ok
}
etapa "fase 0 imutavel" fase0_intacta

# ---------- JDK 17 (so para fases Gradle) ----------
achar_jdk17() {
  local c v
  for c in "${JAVA_HOME:-}" \
           "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home" \
           "/opt/homebrew/opt/openjdk@17" \
           "/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home" \
           "$(/usr/libexec/java_home -v 17 2>/dev/null || true)"; do
    [ -n "$c" ] && [ -x "$c/bin/java" ] || continue
    v="$("$c/bin/java" -version 2>&1 | head -1)"
    case "$v" in *'"17.'*) echo "$c"; return 0 ;; esac
  done
  return 1
}

# ---------- 2. Fases ----------
compilar_javac() {  # $1 = pasta da fase
  local fase="$1" out="$TMP/out-$(basename "$fase")"
  mkdir -p "$out"
  find "$fase/src" -name '*.java' -print0 | xargs -0 javac --release 17 -encoding UTF-8 -Xlint:all -d "$out"
}

camadas() {  # $1 = pasta da fase
  local fase="$1" ok=0 f
  local ext='(com\.badlogic|gdx|android|androidx)'
  while IFS= read -r f; do
    case "$f" in
      */domain/*)
        grep -nE "^[[:space:]]*import[[:space:]]+(static[[:space:]]+)?([A-Za-z0-9_.]*\.)?((application|console|infrastructure)|$ext)\." "$f" \
          | sed "s|^|    $f: domain importa camada proibida: |" && ok=1 ;;
      */application/*)
        grep -nE "^[[:space:]]*import[[:space:]]+(static[[:space:]]+)?([A-Za-z0-9_.]*\.)?((console|infrastructure)|$ext)\." "$f" \
          | sed "s|^|    $f: application importa camada proibida: |" && ok=1 ;;
      */infrastructure/*)
        grep -nE "^[[:space:]]*import[[:space:]]+(static[[:space:]]+)?([A-Za-z0-9_.]*\.)?$ext\." "$f" \
          | sed "s|^|    $f: infrastructure importa camada proibida: |" && ok=1 ;;
    esac
  done < <(find "$fase" \( -name build -o -name .gradle -o -name out \) -prune -o -name '*.java' -print)
  [ $ok -eq 0 ]
}

gradle_build() {  # $1 = pasta com gradlew
  local jdk
  jdk="$(achar_jdk17)" || {
    echo "    JDK 17 nao encontrado. Instale (brew install openjdk@17) ou exporte JAVA_HOME para um JDK 17."
    return 1
  }
  ( cd "$1" && JAVA_HOME="$jdk" ./gradlew clean build --console=plain )
}

achou_fase=0
for fase in fases/fase-*/; do
  fase="${fase%/}"
  [ "$fase" = "$FASE0" ] && continue
  [ -d "$fase" ] || continue
  achou_fase=1

  gradles=()
  for d in "$fase" "$fase/cliente" "$fase/servidor"; do
    [ -f "$d/gradlew" ] && gradles+=("$d")
  done

  if [ ${#gradles[@]} -gt 0 ]; then
    for d in "${gradles[@]}"; do
      etapa "$d: gradlew clean build" gradle_build "$d"
    done
  elif [ -d "$fase/src" ] && [ -n "$(find "$fase/src" -name '*.java' -print -quit)" ]; then
    etapa "$fase: javac" compilar_javac "$fase"
  else
    echo "==> $fase: sem codigo ainda (nada a compilar)"
  fi

  etapa "$fase: regras de camadas" camadas "$fase"
done

[ $achou_fase -eq 0 ] && echo "==> nenhuma fase alem da 0: apenas a verificacao da fase 0"

echo
if [ ${#falhas[@]} -eq 0 ]; then
  echo "check.sh: tudo passou"
  exit 0
else
  echo "check.sh: falhou em: ${falhas[*]}"
  exit 1
fi
