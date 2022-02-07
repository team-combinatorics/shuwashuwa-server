#!/bin/bash

# [12f23eddde]

usage(){
    cat << USAGE >&2
Usage:
    ./run-dev.sh [-hcu] [wx.appid] [wx.secrets]
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
  echo "wx.appid=$WX_APPID wx.appsecret=$WX_SECRET"
fi

# if appid or secret doesn't exist
if [[ -z $WX_APPID || -z $WX_SECRET ]]; then
    # load appid from shell
    if [[ $# = 2 ]]; then
        export WX_APPID=$1
        export WX_SECRET=$2
        echo "wx.appid=$WX_APPID wx.appsecret=$WX_SECRET"
    else
        echo -n "wx.appid="
        read appid
        export WX_APPID=$appid
        echo -n "wx.appsecret="
        read appsecret
        export WX_SECRET=$appsecret
    fi
fi

# run docker
docker-compose up --build -d
