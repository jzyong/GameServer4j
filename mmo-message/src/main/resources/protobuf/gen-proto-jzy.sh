#java
#protoc --java_out /Users/jiangzhiyong/Project/game/server/mmo-server/mmo-message/src/main/java ./*.proto
#go
protoc --go_out=plugins=grpc:/Users/jiangzhiyong/Project/go/src/github.com/jzy/go-mmo-server/src/message *.proto
#copy proto to go project
cp *.proto /Users/jiangzhiyong/Project/go/src/github.com/jzy/go-mmo-server/src/message/proto