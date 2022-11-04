# 本地运行

&emsp;&emsp;本地demo在windows环境下使用docker-compose允许。

## 前置条件

1. 本地windows安装docker-compose运行环境<br>
2. 默认端口需求<br>
3. JDK14允许环境
4. 下表端口

| 端口    | 描述         | 端口    | 描述            | 端口  | 描述  |
|-------|------------|-------|---------------|-----|-----|
| 7000  | api1 rpc   | 7001  |
| 7020  | 网关1 客户端    | 7021  | 网关1 游戏        |
| 7030  | 大厅1 rpc    |
| 7061  | 后台1 http   |
| 2181  | zookeeper  |
| 9092  | kafka      |
| 9090  | nginx http |
| 16379 | redis      |
| 27017 | mongodb    | 27018 | mongo-express |

## 运行

* [build_image.cmd](build_image.cmd) 打包项目为docker image
* [docker-compose.yml](docker-compose.yml) docker运行配置

```shell
# 1.打开build_image.cmd 修改JAVA_HOME 环境变量
set JAVA_HOME=D:\Program Files\Java\jdk-14.0.1

# 2.运行 build_image.cmd
build_image.cmd

# 3.启动项目
docker-compose up -d

```

* mongo数据库访问： <http://localhost:27018/>  `root` `123456`














