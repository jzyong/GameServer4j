# mmo-server

## Intro
&emsp;&emsp;Using Java, netty, zookeeper, spring boot and mongo, redis tools to development of game hot update distributed framework.
Clients and gateways use TCP custom protocols, Intranet message forwarding USES GRPC forwarding, 
all stateless services can be horizontally extended, and stateful services can be horizontally extended through partition, state binding and other rules.
The basic architecture of the project is shown below:


![Architecture diagram](https://raw.githubusercontent.com/jzyong/mmo-server/master/mmo-res/img/mmo%E6%9C%8D%E5%8A%A1%E5%99%A8.png) 




## Module
1. Logical scripts that end with the project module at the end of scripts as the corresponding project can be hot-updated. [docker run](https://github.com/jzyong/mmo-server/blob/master/mmo-res/docker/local/DockerLocalRun.md)  
* [mmo-engine](https://github.com/jzyong/mmo-server/blob/master/mmo-engine/README.md) Framework underlying core logic, network communication, threading models, common tools classes  
* [mmo-common](https://github.com/jzyong/mmo-server/blob/master/mmo-common/README.md) Common logic code  
* [mmo-message](https://github.com/jzyong/mmo-server/blob/master/mmo-message/README.md) Protobuf message,grpc service  
* [mmo-gate](https://github.com/jzyong/mmo-server/blob/master/mmo-gate/README.md) Message routing 
* [mmo-login](https://github.com/jzyong/mmo-server/blob/master/mmo-login/README.md) Login authentication 
* [mmo-game](https://github.com/jzyong/mmo-server/blob/master/mmo-game/README.md) Game Demo 
* [mmo-manage](https://github.com/jzyong/mmo-server/blob/master/mmo-manage/README.md) Web background management, GM, etc
* [mmo-res](https://github.com/jzyong/mmo-server/blob/master/mmo-res/README.md) Server resource files, Docker scripts, documents, etc
* mmo-scene: Mmo combat scenario Demo, pause  
* mmo-world: World service demo, pause  

2. Abandoned module  
* mmo-db: akka+spring Cross-server data sharing using Demo  
* mmo-bill： kotlin+spring+netty+gradle billing use demo  
* mmo-cluster: Netty tcp,http demo 
* mmo-log： Log storage operations, Akka Stream operations log Demo



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

