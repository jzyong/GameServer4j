package org.mmo.engine.server;

import io.netty.channel.Channel;
import org.mmo.engine.thread.concurrent.ConcurrentCoverSetBlockingQueue;
import org.mmo.engine.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 服务器信息
 */
public class ServerInfo implements Serializable{
    private static final long serialVersionUID = 1L;

    private final static Logger log = LoggerFactory.getLogger(ServerInfo.class);

    // 服务器ID
    private int id;
    // 归属服务器ID，即被合并到哪个服
    private int belongId;
    // 服务器名称
    protected String name;
    // 服务器信
    private String content;
    // 地址
    private String ip;
    // 外网地址
    private String wwwip;
    // 端口
    private int port;
    // 当前状态 -1表示维护，1表示流畅，2表示拥挤；3表示爆满
    private int serverState = 0;
    // 版本号
    private String version;
    //http端口
    private int httpPort;
    //最大用户人数
    private int maxUserCount;
    //准备开服时间
    private String openTime;
    //维护时间
    private String maintainTime;


    // 在线人数
    private int online;

    protected transient final ConcurrentCoverSetBlockingQueue<Channel> allChannels = new ConcurrentCoverSetBlockingQueue<>();//
    public ServerInfo() {
    }


    
    /**
     * 是否已达到最大限制人数
     *
     * @return
     */
    public boolean isFull() {
        return getOnline() >= getMaxUserCount();
    }

    public String getHttpUrl(String content) {
        StringBuilder sb = new StringBuilder("http://").append(getIp()).append(":").append(getHttpPort()).append("/").append(content);
        return sb.toString().trim();
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setOpenTimeStr(String openTime) {
        this.openTime = openTime;
    }

//    //是否在准备开服时间内
//    public boolean isOpenTime() {
//        if (getOpenTime() != null && TimeUtil.currentTimeMillis() < DateTimeUtil.parseTimestamp(getOpenTime())) {
//            return true;
//        }
//        return false;
//    }
//
//    public boolean isMaintainTime() {
//        if (getMaintainTime() != null && TimeUtil.currentTimeMillis() < DateTimeUtil.parseTimestamp(getMaintainTime())) {
//            return true;
//        }
//        return this.gamestate <= ServerState.MAINTAIN.getState();
//    }

    @Override
    public String toString() {
        return JsonUtil.toJSONString(this);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

//    public int getGamestate() {
//        if (isOpenTime()) {//等待开服状态永远显示0
//            return 0;
//        }
//        return gamestate;
//    }


    public String getVersion() {
        return version;
    }

    public int getServerState() {
		return serverState;
	}



	public void setServerState(int serverState) {
		this.serverState = serverState;
	}



	public void setVersion(String version) {
        this.version = version;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }


    public String getMaintainTime() {
        return maintainTime;
    }

    public void setMaintainTime(String maintainTime) {
        this.maintainTime = maintainTime;
    }

    public void setMaintainTimeStr(String maintainTime) {
        this.maintainTime = maintainTime;
    }


//    public String getMaintainTimeString() {
//        if (getMaintainTime() != null && TimeUtil.currentTimeMillis() < DateTimeUtil.parseTimestamp(getMaintainTime())) {
//            return getMaintainTime();
//        }
//        return null;
//    }
//
//    public String getOpenTimeString() {
//        if (getOpenTime() != null && TimeUtil.currentTimeMillis() < DateTimeUtil.parseTimestamp(getOpenTime())) {
//            return getOpenTime();
//        }
//        return null;
//    }
//
//    public Channel getActiveChannel() {
//        if (activeChannel == null || !activeChannel.isActive()) {
//            activeChannel = getMostIdleChannel();
//        }
//        return activeChannel;
//    }

    public int getMaxUserCount() {
        return maxUserCount;
    }

    public void setMaxUserCount(int maxUserCount) {
        this.maxUserCount = maxUserCount;
    }

//    public void sendMsg(Object msg) {
//        Channel se = getActiveChannel();
//        if (se != null) {
//            if(msg instanceof Message) {
//                MsgUtil.sendInnerMsg(se, (Message)msg, getId());
//            }else {
//                MsgUtil.sendInnerMsg(se, msg);
//            }
//           
//        } else {
//            log.warn("服务器:{}-{} {}:{}连接会话为空,消息{}发送失败",this.getId(), name ,this.getIp(),this.port,msg.getClass().getSimpleName());
//        }
//    }

    public String getWwwip() {
        return wwwip;
    }

    public void setWwwip(String wwwip) {
        this.wwwip = wwwip;
    }

    public int getBelongId() {
		return belongId;
	}

	public void setBelongId(int belongId) {
		this.belongId = belongId;
	}


}
