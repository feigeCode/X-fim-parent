osdatime=`date +"%Y%m%d_%H%M%S"`
currentPath=$(cd `dirname $0`; pwd)
cd "$currentPath"

fim.path=$currentPath"/../conf/fim.yaml"
log4j.configurationFile=$currentPath"/../conf/log4j2.xml"
export fim.path
export log4j.configurationFile

java -jar $currentPath"/../fim-bootstrap-1.0.jar"
