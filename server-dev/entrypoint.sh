# ensure you have env-vars set
# if appid or secret doesn't exist
echo $WX_APPID $WX_SECRET
if [[ -z $WX_APPID || -z $WX_SECRET ]]
then
    echo "[FATAL] Environment variables not set!"
    exit 1
fi
# copy artifacts to host
cp -r /app/target/* /target
# wait mysql connection for 30s, if it fails then stop
./wait-for-it.sh -s -t 30 $MYSQL_ADDRESS -- java -jar /app/target/${TARGET_JAR}