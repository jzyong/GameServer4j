# mmo-server


## 简介
&emsp;&emsp;使用java,netty,zookeeper,spring boot,mongodb,redis等工具开发的可热更新分布式游戏服框架。客户端与网关使用TCP自定义协议，内网消息转发使用grpc转发，
所有无状态服务可水平扩展，有状态服务通过分区，状态绑定等规则水平扩展。项目基本架构如下所示


![项目架构图](https://raw.githubusercontent.com/jzyong/mmo-server/master/mmo-res/img/mmo%E6%9C%8D%E5%8A%A1%E5%99%A8.png) 




## 模块说明：
1. scripts模块为对应项目的逻辑脚本项目可热更新。 [docker运行](https://github.com/jzyong/mmo-server/blob/master/mmo-res/docker/local/DockerLocalRun.md)  
**mmo-common:** 通用公共逻辑代码  
**mmo-engine:** 框架底层核心逻辑，网络通信、线程模型、常用工具类等的封装  
**mmo-game:** 具体游戏逻辑demo  
**mmo-gate：** 网关  
**mmo-log：** 日志存储操作  
**mmo-login：** 登录服务器demo  
**mmo-manage:** 服务器后台管理、gm等demo  
**mmo-message：** protobuf消息定义  
**mmo-res：** 服务器资源文件，docker脚本，文档等  
**mmo-scene：** mmo战斗场景demo，暂停  
**mmo-world：** 世界服demo，暂停  
2. 废弃模块  
**mmo-db:** akka+spring 跨服数据共享使用demo  
**mmo-bill：** kotlin+spring+netty+gradle充值使用demo  
**mmo-cluster:** 服务器管理中心  



## 技术选择：
1. spring-boot作为基础框架  
2. mongodb 数据存储
3. maven 项目管理
4. netty 客户端到服务器通信
5. grpc 服务器内部通信调用  
6. redis 数据缓存
7. zookeeper 集群配置管理
  
  
  
  
**QQ交流群:** 143469012

