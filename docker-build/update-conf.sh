#!/bin/bash

# export .env to shell
export $(grep -v '^#' .env | xargs -d '\n')

# substitute *.example
for filename in *.example; do
    envsubst < $filename > $(basename "$filename" .example)
    echo Updated: $(basename "$filename" .example)
done
