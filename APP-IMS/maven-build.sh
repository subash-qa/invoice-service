dir=$(pwd)
echo "$dir"

cd $dir/../APP-NAPID-CONFIG

mvn clean install 

cd $dir/../APP-NAPID-AUTH

mvn clean install 

cd $dir/../APP-NAPID-VENDOR

mvn clean install 


cd $dir/../APP-NAPID-MAIN

mvn clean install 


cd $dir/../APP-NAPID-CRON

mvn clean install 
