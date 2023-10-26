#!/bin/bash
# Entrypoint script for shuwashuwa production server

set -e

mask() {
    # mask-print a token :- say no to accidental leakage!
    # https://unix.stackexchange.com/questions/507450/echo-with-obfuscation
    local n=3
    [[ ${#1} -le 5 ]] && n=$(( ${#1} - 3 ))
    local a="${1:0:${#1}-n}"
    local b="${1:${#1}-n}"
    printf "%s%s\n" "${a//?/*}" "$b"
}

# Check required variables
for var in "WX_APPID" "WX_SECRET"; do
  value="${!var}"
  # Check if the value is empty
  if [ -z "$value" ]; then
    echo "Error: $var is empty"
    exit 1
  else
    echo $var=$(mask $value)
  fi
done

# Generate TOKEN_SECRET if not specified
if [[ -z ${TOKEN_SECRET} ]]; then
    # generate token secret at runtime
    export TOKEN_SECRET=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c10)
else
    echo "Consistent token secret is easy to guess, consider leaving it blank to regenerate on every run."
fi
echo TOKEN_SECRET=$(mask $TOKEN_SECRET)

if [ -f /app/target ]; then
    echo "Just compiled, copying artifacts to host"
    cp -r /app/target/* /target
fi

# Find the latest .jar file in the folder using ls and sort
latest_jar=$(ls -t /target/*.jar 2>/dev/null | head -1)
if [[ -n "$latest_jar" ]]; then
  echo "Using .jar file: $latest_jar"
  echo "Build Timestamp: $(ls -l --time-style=+"%Y-%m-%d %H:%M:%S" "$latest_jar" | awk '{print $6, $7}')"
else
  echo "No .jar files found in the folder /target."
  exit 2
fi

echo "Waiting for MySQL connection from $MYSQL_ADDRESS ..."
./wait-for-it.sh -s -t 30 "$MYSQL_ADDRESS" -- java -jar $latest_jar