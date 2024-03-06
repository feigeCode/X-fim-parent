set "ROOT_BIN=%~dp0"
set "fim.path=%ROOT_BIN%..\conf\fim.yaml"
set "log4j.configurationFile=%ROOT_BIN%..\conf\log4j2.xml"
java -jar ../fim-bootstrap-1.0.jar
