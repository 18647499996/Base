#!/usr/bin/env bash
properties="./gradle.properties"
versionCode=$(awk -F= '/VERSION_CODE=/{print $2}' "$properties")
versionName=$(awk -F= '/VERSION_NAME=/{print $2}' "$properties")
buildType=$1
changeLog=$2
filePath=''
echo $buildType
if [[ $buildType == 'debug' ]];then
    echo '测试版打包开始'
    ./gradlew assembleDebug
    filePath=./app/build/outputs/apk/debug/app-debug.apk
elif [[ $buildType == 'release' ]];then
    echo '正式版打包开始'
    ./gradlew assembleRelease
    filePath=./app/build/outputs/apk/release/app-release.apk
elif [[ $buildType == 'send' ]];then
    echo '送测打包开始'
    ./gradlew assembleSenddebug
    filePath=./app/build/outputs/apk/senddebug/app-senddebug.apk
elif [[ $buildType == 'pre' ]];then
    echo 'Pre环境包开始'
    ./gradlew assemblePredebug
    filePath=./app/build/outputs/apk/predebug/app-predebug.apk
else
    echo '请输入sh fir.sh debug xxx更新日志 或者 sh fir.sh release xxx更新日志'
    fi

echo "-------------获取签名-------------"

result=$(curl -X "POST" "http://api.bq04.com/apps" \
  -H "Content-Type: application/json" \
  -d "{\"type\":\"android\", \"bundle_id\":\"com.liudonghan.component\", \"api_token\":\"4f59fd73869ee3c594ef7abcf3aa1226\"}");
binary=$(echo $result |awk -F '"binary"' '{print $2}');
key=$(echo $binary |awk -F '"key"' '{print $2}'|awk -F '"' '{print $2}');
token=$(echo $binary |awk -F '"token"' '{print $2}'|awk -F '"' '{print $2}');
name="Base"

echo "-------------上传-------------"
result=$(curl  -F "key=$key"              \
    -F "token=$token"             \
    -F "file=@$filePath"             \
    -F "x:name=$name"             \
    -F "x:version=$versionName"         \
    -F "x:build=$versionCode"               \
    -F "x:changelog=$changeLog"       \
    http://upload.qiniu.com);
uploadStatus=$(echo $result |awk -F '"is_completed":' '{print $2}'|awk -F '}' '{print $1}');
if [ "$uploadStatus" = "true" ]; then
echo "上传失败!"
else
echo "上传成功!"
fi
