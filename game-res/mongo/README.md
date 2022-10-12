# Mongo



## Install
参考：https://www.runoob.com/docker/docker-install-mongodb.html  
https://hub.docker.com/_/mongo?tab=description  

```shell script
# 1. 运行镜像
docker run -itd --name mongo-3.6.23 -p 27017:27017 mongo:3.6.23 --auth
docker run -itd --name mongo -p 27017:27017 mongo:latest --auth

# 2. 进入容器创建账号
$ docker exec -it mongo-3.6.23 mongo admin
$ docker exec -it mongo mongo admin
# 创建一个名为 admin，密码为 123456 的用户。
db.createUser({ user:'admin',pwd:'123456',roles:[ { role:'userAdminAnyDatabase', db: 'admin'},"readWriteAnyDatabase"]});
# 尝试使用上面创建的用户信息进行连接。
db.auth('admin', '123456')

# 3. 连接地址
mongodb://admin:123456@127.0.0.1:27017/?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&ssl=false
```


### TODO 
* MongoDB 升级到4.0，兼容AWS DocumentDB
