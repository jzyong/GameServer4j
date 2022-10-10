package org.game.api.service;

import org.game.api.db.config.enity.CShopItem;
import org.game.api.db.config.repository.CShopItemRepository;
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
public class ApiConfigService {
    public static final Logger LOGGER= LoggerFactory.getLogger(ApiConfigService.class);

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
