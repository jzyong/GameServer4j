package org.mmo.engine.io.message;


/**
 * 消息类型
 */
public enum MsgType {
    IDMESSAGE((short) 0, IdMessage.class),
    /**跨服消息*/
    CROSSMESSAGE((short)1, CrossMessage.class),
    ;
    private final short type;
    private final Class<?> msgClass;

    private MsgType(short type, Class<?> msgClass) {
        this.type = type;
        this.msgClass = msgClass;
    }

    public short getType() {
        return type;
    }

    public Class<?> getMsgClass() {
        return msgClass;
    }

}
