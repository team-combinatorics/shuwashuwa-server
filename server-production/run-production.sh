#!/bin/bash

# [12f23eddde]

# https://unix.stackexchange.com/questions/507450/echo-with-obfuscation
mask() {
        local n=3
        [[ ${#1} -le 5 ]] && n=$(( ${#1} - 3 ))
        local a="${1:0:${#1}-n}"
        local b="${1:${#1}-n}"
        printf "%s%s\n" "${a//?/*}" "$b"
}

usage(){
    cat << USAGE >&2
Usage:
    ./run-production.sh [-hcu] [wx.appid] [wx.secrets]
    If you already export appid and secret from shell, it would ignore arguments
    If no args specified, it would ask you via interactive commands
    -h | help      Display help message
    -c | convert   Convert all files in current folder from crlf to lf
USAGE
    exit
}

convert(){
    for filename in $PWD/*
    do
        if [[ -f $filename ]]; then
            echo "crlf->lf" $filename
            sed -i 's/\r//g' $filename
        fi
    done
    echo "crlf->lf" .env
    sed -i 's/\r//g' .env
    exit
}

# print help message
if [[ $1 = "-h" || $1 = "help" ]]; then
    usage
elif [[ $1 = "-c" || $1 = "convert" ]]; then
    convert
elif [[ $1 = "-u" || $1 = "update" ]]; then
    update
fi

# Load config
set -o allexport
source .env
set +o allexport

echo "[Shuwashuwa] Setting up Development Server ..."

# Load secrets
if [[ -f .secrets.env ]]; then
  echo "[Shuwashuwa] Loading secrets from .secrets.env"
  set -o allexport
  source .secrets.env
  set +o allexport
  echo wx.appid=$(mask $WX_APPID) wx.appsecret=$(mask $WX_SECRET)
fi

# check target
if ! [[ -f $TARGET_PATH ]] ; then
    echo "[FATAL] Target Jar dosen't exist"
    exit 2
fi

# if appid or secret doesn't exist
if [[ -z $WX_APPID || -z $WX_SECRET ]]; then
    # load appid from shell
    if [[ $# = 2 ]]; then
        export WX_APPID=$1
        export WX_SECRET=$2
        echo wx.appid=$(mask $WX_APPID) wx.appsecret=$(mask $WX_SECRET)
    else
        echo -n "wx.appid="
        read appid
        export WX_APPID=$appid
        echo -n "wx.appsecret="
        read appsecret
        export WX_SECRET=$appsecret
    fi
fi

# generate token secret at runtime
export TOKEN_SECRET=$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c10)
echo token_secret=$(mask $TOKEN_SECRET)

# run docker
docker-compose up --build -d
