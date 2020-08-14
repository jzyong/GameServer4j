package org.mmo.login.service;

import org.mmo.login.db.config.enity.CShopItem;
import org.mmo.login.db.config.repository.CShopItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 配置数据
 * @author jzy
 */
@Service
public class LoginConfigService {
    public static final Logger LOGGER= LoggerFactory.getLogger(LoginConfigService.class);

    @Autowired
    CShopItemRepository shopItemRepository;

    @PostConstruct
    public void init(){
        loadConfig();
    }

    /**
     * 加载配置
     */
    public void loadConfig(){
        Iterable<CShopItem> shopItems = shopItemRepository.findAll();
        shopItems.forEach(it->{
            LOGGER.info("道具信息：{}",it.toString());
        });
    }
}
