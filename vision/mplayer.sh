die () {
    echo >&2 "$@"
    exit 1
}

if [ -z $1 ]; then die "No temp dir specified"; fi

TMPDIR=$1

if [ -d "$TMPDIR" ]; then
   rm -rf "$TMPDIR"
fi

mkdir "$TMPDIR"
 cd "$TMPDIR"

mplayer -tv device=/dev/video0:driver=v4l2:input=2:width=768:height=576:norm=pal tv://1 -zoom -aspect 4:3 -vo jpeg -really-quiet &> /dev/null

rm -rf "$TMPDIR"
