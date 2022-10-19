package org.jzy.game.common.constant;

/**
 * kafka 客户端ID
 * @author jzy
 * @mail 359135103@qq.com
 */
public enum  KafkaClientId {

    Api("com.jzy.game.api");


    private String name;

    KafkaClientId(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
