osdatime=`date +"%Y%m%d_%H%M%S"`
currentPath=$(cd `dirname $0`; pwd)
cd "$currentPath"

fim.path=$currentPath"/../conf/fim.yaml"
export fim.path


java -jar $currentPath"/../X-fim-bootstrap-1.0.jar"
