//package org.jzy.game.gate.tcp.server;
//
//import com.jzy.javalib.network.io.handler.Handler;
//import org.jzy.game.gate.service.GateManager;
//import org.jzy.game.proto.MessageId;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * 游戏服注册到网关
// *
// * @author jzy
// */
//@Handler(mid = MessageId.MID.ServerRegisterUpdateReq_VALUE, msg = ServerRegisterUpdateRequest.class)
//public class ServerRegisterUpdateReqHandler extends TcpHandler {
//    public static final Logger LOGGER = LoggerFactory.getLogger(ServerRegisterUpdateReqHandler.class);
//
//    @Override
//    public void run() {
//        var request = (ServerRegisterUpdateRequest) getMessage();
//        ServerInfo serverInfo = request.getServerInfo();
//        GameServerInfo gameServerInfo = GateManager.getInstance().getGameTcpService().getGameServers().get(serverInfo.getId());
//        if (gameServerInfo == null) {
//            //TODO 设置channel属性，channel关闭移除服务器信息
//            gameServerInfo = new GameServerInfo();
//            gameServerInfo.setChannel(this.channel);
//            gameServerInfo.setId(serverInfo.getId());
//            GateManager.getInstance().getGameTcpService().getGameServers().put(serverInfo.getId(), gameServerInfo);
//            LOGGER.info("服务器：{}-{}-{} 连接网关成功", serverInfo.getId(), serverInfo.getName(), serverInfo.getIp());
//        }
//        gameServerInfo.setChannel(this.channel);
//        gameServerInfo.setId(serverInfo.getId());
//        gameServerInfo.setIp(serverInfo.getIp());
//        gameServerInfo.setVersion(serverInfo.getVersion());
//        gameServerInfo.setServerState(serverInfo.getState());
//        gameServerInfo.setOnline(serverInfo.getOnline());
//        ServerRegisterUpdateResponse.Builder builder = ServerRegisterUpdateResponse.newBuilder();
//        ServerInfo.Builder replayServerInfo = ServerInfo.newBuilder();
//        replayServerInfo.setId(GateManager.getInstance().getGateConfig().getId());
//        builder.setServerInfo(replayServerInfo);
//        sendInnerMsg(MIDMessage.MID.ServerRegisterUpdateRes_VALUE, builder.build());
//    }
//}
