# copy artifacts to host
cp -r /app/target/* /target
# wait mysql connection for 30s
./wait-for-it.sh -t 30 $MYSQL_ADDRESS
# run jar
java -jar /app/target/${TARGET_JAR}