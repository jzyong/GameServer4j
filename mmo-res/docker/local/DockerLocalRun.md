# docker服务器本地运行

##前置条件
1.本地windows安装docker运行环境<br>
2.默认端口需求<br>

|  端口   | 描述  |
|  ----  | ----  |
| 1  | 登陆服 |
| 单元格  | 单元格 |


##Zookeeper 
**安装zookeeper：**

       rem docker run --privileged=true -d --name zookeeper -p 2181:2181 zookeeper:latest
双击 zookeeper_run.cmd启动 zookeeper

##Mongodb
安装配置参考：https://www.runoob.com/docker/docker-install-mongodb.html

**安装mongodb：**

    docker run -itd --name mongo -p 27017:27017 mongo --auth

执行如下命令设置mongodb信息：    
    
    docker exec -it mongo mongo admin
    #创建账号
    db.createUser({ user:'admin',pwd:'123456',roles:[ { role:'userAdminAnyDatabase', db: 'admin'},"readWriteAnyDatabase"]});
    db.auth('admin', '123456')

双击mongo_run.cmd 运行mongodb

##Redis
**安装redis:**

    docker pull redis
    docker run --name redis -p 16379:6379  -d redis:latest redis-server --appendonly yes
    docker exec -it 4a20 bash
    cd /usr/local/bin/
    ./redis-cli
    info
    

















