package org.game.api.struct;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 账号
 * @author jzy
 */
@Document(collection = "account")
public class Account {
    /**
     * 唯一id
     */
    @Id
    private long id;

    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
