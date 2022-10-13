package org.jzy.game.gate.service;

import org.apache.curator.x.discovery.ServiceInstance;
import org.jzy.game.common.constant.GlobalProperties;
import org.jzy.game.common.constant.ServiceName;
import org.jzy.game.common.constant.ZKNode;
import org.jzy.game.common.service.AbstractMicroServiceClientService;
import org.jzy.game.common.struct.service.ApiServiceInfo;
import org.jzy.game.common.struct.service.IMicroserviceInfo;
import org.jzy.game.proto.AccountServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Api 客户端
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class ApiClientService extends AbstractMicroServiceClientService<ApiServiceInfo> {

    @Autowired
    private GlobalProperties globalProperties;

    @Override
    public String getServicePath() {
        return ZKNode.ServicePath.getKey(globalProperties.getProfile());
    }

    @Override
    public String getServiceName() {
        return ServiceName.ApiRpc.name();
    }

    @Override
    public ApiServiceInfo buildIMicroserviceInfo(ServiceInstance serviceInstance) {
        String url = serviceInstance.getAddress() + ":" + serviceInstance.getPort();
        return new ApiServiceInfo(serviceInstance.getId(), url, serviceInstance.getName());
    }

    /**
     * 随机一个 客户端
     * <br>线上环境一般不能随机进行，否则可能有并发问题，一般Hash同一用户分配固定的客户端
     *
     * @return
     */
    public AccountServiceGrpc.AccountServiceStub randomAccountServiceStub() {
        ApiServiceInfo apiServiceInfo = randomMicroServiceInfo();
        if (apiServiceInfo == null) {
            return null;
        }
        return apiServiceInfo.getAccountServiceStub();
    }
}
