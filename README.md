
## Intro
&emsp;&emsp;Using Java, netty, zookeeper, spring boot and mongo, redis tools to development of game hot update distributed framework.
Clients and gateways use TCP custom protocols, Intranet message forwarding use GRPC forwarding, 
all stateless services can be horizontally extended, and stateful services can be horizontally extended through partition, state binding and other rules.
The basic architecture of the project is shown below:


![Architecture diagram](https://raw.githubusercontent.com/jzyong/mmo-server/master/mmo-res/img/mmo%E6%9C%8D%E5%8A%A1%E5%99%A8.png) 




## Module
1. Logical scripts that end with the project module at the end of scripts as the corresponding project can be hot-updated. [docker run](https://github.com/jzyong/mmo-server/blob/master/mmo-res/docker/local/DockerLocalRun.md)  
* [game-engine](https://github.com/jzyong/mmo-server/blob/master/mmo-engine/README.md) Framework underlying core logic, network communication, threading models, common tools classes  
* [game-common](https://github.com/jzyong/mmo-server/blob/master/mmo-common/README.md) Common logic code  
* [game-message](https://github.com/jzyong/mmo-server/blob/master/mmo-message/README.md) Protobuf message,grpc service  
* [game-gate](https://github.com/jzyong/mmo-server/blob/master/mmo-gate/README.md) Message routing 
* [game-api](https://github.com/jzyong/mmo-server/blob/master/mmo-login/README.md) Login authentication ,charge verify
* [game-game](https://github.com/jzyong/mmo-server/blob/master/mmo-game/README.md) Game Demo 
* [game-manage](https://github.com/jzyong/mmo-server/blob/master/mmo-manage/README.md) Web background management, GM, etc
* [game-res](https://github.com/jzyong/mmo-server/blob/master/mmo-res/README.md) Server resource files, Docker scripts, documents, etc
* game-world: World service demo, pause  

2. Abandoned module  
* game-db: akka+spring Cross-server data sharing using Demo  
* game-bill： kotlin+spring+netty+gradle billing use demo  
* game-cluster: Netty tcp,http demo 
* game-log： Log storage operations, Akka Stream operations log Demo



## Technology select
1. spring-boot 
2. mongodb 
3. maven 
4. netty 
5. grpc 
6. redis 
7. zookeeper
8. kafka
  
  
  
[中文文档](https://blog.csdn.net/jzhiy/category_10634655.html)  
**QQ Communication group:** 143469012

