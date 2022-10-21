package org.jzy.game.common.constant;

/**
 * 服务器类型枚举
 *
 * @author JiangZhiYong
 * @mail 359135103@qq.com
 */
public enum ServerType {

    NONE(0),
    /**
     * 网关
     */
    GATE(2),
    /**
     * 登录|充值
     */
    Api(3),
    /**
     * 大厅逻辑服
     */
    Hall(4),
    ;

    private final int type;

    private ServerType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ServerType valueOf(int type) {
        for (ServerType t : ServerType.values()) {
            if (t.getType() == type) {
                return t;
            }
        }
        return NONE;
    }
}
