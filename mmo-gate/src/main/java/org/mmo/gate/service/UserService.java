package org.mmo.gate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 用户管理
 * @author jzy
 */
@Service
public class UserService {
    private static final Logger LOGGER= LoggerFactory.getLogger(UserService.class);

    @PostConstruct
    public void  init(){

    }
}
