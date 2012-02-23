#! /usr/bin/env sh

BASEDIR=$(dirname $0)

source "$BASEDIR/../env"

until python "$BASEDIR/vision.py" $@; do
    echo "Vision crashed :( Respawning.." >&2
    sleep 1
done
