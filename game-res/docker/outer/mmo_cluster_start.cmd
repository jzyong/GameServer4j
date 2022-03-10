call mmo_cluster_push


docker -H tcp://192.168.0.1:2375 pull 192.168.0.1:5000/outer-test/mmo-cluster:releases
docker -H tcp://192.168.0.1:2375 stop mmo-cluster
docker -H tcp://192.168.0.1:2375 rm mmo-cluster
docker -H tcp://192.168.0.1:2375 run -p 40003:40003 -p 40002:40002 --name mmo-cluster --privileged=true -v /data/game/mmo-cluster:/usr/src/mmo-cluster/logs 192.168.0.1:5000/outer-test/mmo-cluster:release