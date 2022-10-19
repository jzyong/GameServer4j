package org.jzy.game.common.constant;

/**
 * 离线类型
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
public enum OfflineType {
    /**
     * 网络问题
     */
    Network,

    /**
     * 异常
     */
    Exception,

    /**
     * 网络空闲
     */
    Idle,
    /**
     * 服务器踢下线
     */
    KickOut,
    /**
     * 服务器关服
     */
    ServerClose,
    /**
     * 非法请求|攻击
     */
    IllegalRequest,
    /**
     * 重复登录
     */
    RepeatLogin,
    /**
     * 客户端关闭
     */
    ClientClose,
    /**
     * 禁止账号
     */
    ForbidAccount,

}
