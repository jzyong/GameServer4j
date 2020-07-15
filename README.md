# mmo-server
分布式游戏服务器基本框架，持续开发中......



![项目架构图](https://raw.githubusercontent.com/jzyong/mmo-server/master/mmo-res/img/mmo%E6%9C%8D%E5%8A%A1%E5%99%A8.png) 



### 模块说明：
&emsp;&emsp;scripts模块为对应项目的逻辑脚本项目，可热更新  
**mmo-bill：** kotlin+spring+netty+gradle充值使用demo,已从项目中移除  
**mmo-cluster:** 服务器管理中心，可使用zookeeper替换  
**mmo-common:** 通用公共逻辑代码  
**mmo-db:** akka+spring 跨服数据共享使用demo，已从项目移除
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



### 技术选择：
1. spring-boot作为基础框架  
2. mongodb 数据存储
3. maven 项目管理
4. netty 客户端到服务器通信
5. grpc 服务器内部通信调用  
  
  
  
  
**QQ交流群:** 143469012

