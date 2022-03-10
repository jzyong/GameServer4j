# docker服务器本地运行

## 前置条件
1.本地windows安装docker运行环境<br>
2.默认端口需求<br>

|  端口   | 描述  |  端口   | 描述  |  端口   | 描述  |
|  ----  | ----  |  ----  | ----  |  ----  | ----  |
| 7000  | 登陆服1 rpc | 7001  | 登陆服2 rpc |
| 7010  | 网关服1 客户端 | 7012  | 网关服2 客户端 |
| 7011  | 网关服1 游戏 |7013  | 网关服2 游戏 |
| 7020  | 后台1服 http | 7021  | 后台2服 http | 7022  | 后台nginx代理 |
| 7030  | 游戏服1 rpc | 7031  | 游戏服2 rpc |
| 2181  | zookeeper |
| 9092  | kafka     |
| 9090  | nginx http |
| 16379  | redis |
| 27017  | mongodb |

3.修改/resources/filter目录下配置文件ip地址

4.打包项目<br>

    mvn clean package -Pjzy -DskipTests

## Zookeeper 
**安装zookeeper：**

       rem docker run --privileged=true -d --name zookeeper -p 2181:2181 zookeeper:latest
双击 zookeeper_run.cmd启动 zookeeper

## Mongodb
安装配置参考：https://www.runoob.com/docker/docker-install-mongodb.html

**安装mongodb：**

    docker run -itd --name mongo -p 27017:27017 mongo --auth

执行如下命令设置mongodb信息：    
    
    docker exec -it mongo mongo admin
    #创建账号
    db.createUser({ user:'admin',pwd:'123456',roles:[ { role:'userAdminAnyDatabase', db: 'admin'},"readWriteAnyDatabase"]});
    db.auth('admin', '123456')

双击mongo_run.cmd 运行mongodb

## Redis
**安装redis:**

    docker pull redis
    docker run --name redis -p 16379:6379  -d redis:latest redis-server --appendonly yes
    docker exec -it 4a20 bash
    cd /usr/local/bin/
    ./redis-cli
    info
    
## kafka
    docker pull wurstmeister/kafka
    docker run -d --name kafka -p 9092:9092 --link zookeeper --env KAFKA_ZOOKEEPER_CONNECT=192.168.0.2:2181 --env KAFKA_ADVERTISED_HOST_NAME=192.168.0.2 --env KAFKA_ADVERTISED_PORT=9092 wurstmeister/kafka
    docker exec -it kafka /bin/sh
    cd /opt/kafka_2.13-2.6.0/bin
    
## nginx
    docker pull nginx:latest
    docker run -d --name=nginx nginx
    #docker cp [容器id]:/etc/nginx D:\soft\nginx\config
    docker cp 3c103:/etc/nginx D:\soft\nginx\config
    docker run -d --name nginx -p 9090:80 -p 7022:7022 -v D:\soft\nginx\config\nginx:/etc/nginx nginx
    # http://127.0.0.1:9090/

[manage http代理配置文件](https://github.com/jzyong/mmo-server/blob/master/mmo-res/docker/local/backup/nginx/conf.d/manage.conf)
       

## java进程启动
**login运行：**

 mmo_login_start_jzy.cmd <br>
 mmo_login_start_jzy2.cmd

**gate运行：**

 mmo_gate_start_jzy.cmd <br>
 mmo_gate_start_jzy2.cmd

**manage运行：**

 mmo_manage_start_jzy.cmd <br>
 mmo_manage_start_jzy2.cmd

**game运行：**

 mmo_game_start_jzy.cmd <br>
 mmo_game_start_jzy2.cmd











