package org.jzy.game.manage.service.rpc;

import org.apache.curator.x.discovery.ServiceInstance;
import org.jzy.game.common.constant.GlobalProperties;
import org.jzy.game.common.constant.ServiceName;
import org.jzy.game.common.constant.ZKNode;
import org.jzy.game.common.service.AbstractMicroServiceClientService;
import org.jzy.game.common.struct.service.ApiServiceInfo;
import org.jzy.game.common.struct.service.HallServiceInfo;
import org.jzy.game.proto.AccountServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 大厅 客户端
 *
 * @author jzy
 * @mail 359135103@qq.com
 */
@Service
public class HallClientService extends AbstractMicroServiceClientService<HallServiceInfo> {

    @Autowired
    private GlobalProperties globalProperties;

    @Override
    public String getServicePath() {
        return ZKNode.ServicePath.getKey(globalProperties.getProfile());
    }

    @Override
    public String getServiceName() {
        return ServiceName.HallRpc.name();
    }

    @Override
    public HallServiceInfo buildIMicroserviceInfo(ServiceInstance serviceInstance) {
        String url = serviceInstance.getAddress() + ":" + serviceInstance.getPort();
        return new HallServiceInfo(serviceInstance.getId(), url, serviceInstance.getName());
    }

}
