package org.jzy.game.gate.client;

import com.google.protobuf.Message;
import com.jzy.javalib.network.io.message.MsgUtil;
import com.jzy.javalib.network.netty.config.NettyClientConfig;
import com.jzy.javalib.network.netty.tcp.TcpClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import org.jzy.game.proto.MID;

import java.text.MessageFormat;
import java.util.List;

/**
 * 用户客户端
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public class UserClient {

    /**
     * 玩家ID
     */
    private long playerId;

    /**
     * Tcp客户端
     */
    private TcpClient tcpClient;

    private String ip;
    private int port;

    private IMessageDistribute messageDistribute;

    /**
     * 消息ID
     */
    private MID mid;

    public UserClient() {

    }

    public UserClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        tcpClient = new TcpClient();
        NettyClientConfig nettyClientConfig = new NettyClientConfig();
        nettyClientConfig.setIp(ip);
        nettyClientConfig.setPort(port);
        tcpClient.setNettyClientConfig(nettyClientConfig);


        tcpClient.setChannelInitializer(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("Codec", new UserMessageCodec());
                ch.pipeline().addLast("MessageHandler", new UserClientHandler());
            }
        });
        tcpClient.init();
        tcpClient.start();
    }

    /**
     * 发送消息
     *
     * @param mid
     * @param message
     */
    public void sendMsg(MID mid, Message message) {
        if (tcpClient.getChannel() == null) {
            System.out.println("网络连接未建立");
            return;
        }
        this.mid = mid;
        tcpClient.getChannel().writeAndFlush(message);
    }


    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public IMessageDistribute getMessageDistribute() {
        return messageDistribute;
    }

    public void setMessageDistribute(IMessageDistribute messageDistribute) {
        this.messageDistribute = messageDistribute;
    }

    /**
     * 解析与Gate服连接的消息
     * <br>消息长度4+消息id4+客户端已收到最小序号4+消息序号4+protobuf消息体
     *
     * @author JiangZhiYong
     */
    public class UserMessageCodec extends ByteToMessageCodec<Object> {
        /**
         * 消息头长度，除去消息长度
         */
        private static final int HEADER_EXCLUDE_LENGTH = 12;

        /**
         * 请求序号
         */
        private int requestSequence = 0;

        @Override
        protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
            if (msg instanceof Message) {
                Message message = (Message) msg;
                byte[] bytes = message.toByteArray();
                out.writeInt(Integer.reverseBytes(HEADER_EXCLUDE_LENGTH + bytes.length));
                out.writeInt(Integer.reverseBytes(UserClient.this.mid.getNumber()));
                out.writeLong(Integer.reverseBytes(requestSequence));
                out.writeInt(Integer.reverseBytes(++requestSequence));
                out.writeBytes(bytes);
                System.err.println(MessageFormat.format("send message id {0} sequence {1} length {2}",mid,requestSequence,bytes.length));
            } else {
                System.out.println("消息类型不支持");
            }

        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            MsgUtil.decode(ctx, in, out);
        }
    }

    /**
     * 处理cluster返回消息 <br>
     * 待修改
     *
     * @author JiangZhiYong
     */
    public class UserClientHandler extends ChannelInboundHandlerAdapter {


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            System.out.println(MessageFormat.format("网络异常：{0}", cause));
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            UserClient.this.tcpClient.setChannel(ctx.channel());
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            try {
                int msgId = byteBuf.readIntLE();
                byteBuf.readIntLE();
                int reqSequence = byteBuf.readIntLE();
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                System.out.println(MessageFormat.format("收到消息：{0}-{1} 序列号{2}", msgId, MID.forNumber(msgId), reqSequence));
                if (messageDistribute != null) {
                    messageDistribute.handMessage(msgId, bytes);
                }
            } catch (Exception e) {
                System.out.println(MessageFormat.format("消息处理:{0}", e));
            } finally {
                byteBuf.release();
            }
        }
    }

    /**
     * 消息分发器
     */
    public interface IMessageDistribute {

        void handMessage(int messageId, byte[] bytes);
    }

}
