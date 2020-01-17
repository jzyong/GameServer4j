 package org.mmo.engine.io.netty.script;

 import io.netty.buffer.ByteBuf;
 import io.netty.channel.ChannelHandlerContext;
 import io.netty.channel.socket.SocketChannel;
 import org.mmo.engine.script.IBaseScript;
 import org.mmo.engine.script.IInitBaseScript;


 /**
  * netty channel，handler脚本
  * @author JiangZhiYong
  * @date 2018/12/11
  */
 public interface IChannelHandlerScript extends IBaseScript, IInitBaseScript {

     /**
      * 连接是否为黑名单
      * @param ctx
      * @return
      */
     default boolean isBlackList(ChannelHandlerContext ctx) {
         return false;
     }

     /**
      * 进入消息检测
      * @param ctx
      * @param byteBuf
      * @return true 消息正常， false消息异常
      */
     default boolean inBoundMessageCheck(ChannelHandlerContext ctx, ByteBuf byteBuf){
         return true;
     }

     /**
      * 初始化channel
      */
     default void initChannel(SocketChannel ch, Object... objects) {
     }

     /**
      * channel active
      * @param ctx
      */
     default void channelActive(ChannelHandlerContext ctx, Object... objects) {

     }
 }
