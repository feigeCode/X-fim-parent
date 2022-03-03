package com.feige.im.task;

import com.feige.discovery.DiscoveryManager;
import com.feige.discovery.ProviderService;
import com.feige.discovery.pojo.ServerInstance;
import com.feige.im.client.ImClient;
import com.feige.im.handler.MsgListener;
import com.feige.im.handler.client.ClusterClientMsgListener;
import com.feige.im.log.Logger;
import com.feige.im.log.LoggerFactory;
import com.feige.im.route.IRoute;
import com.feige.im.route.RouteManager;
import com.feige.im.service.ImBusinessService;
import com.feige.im.utils.AssertUtil;
import com.feige.im.utils.IpUtil;
import com.feige.im.utils.ScheduledThreadPoolExecutorUtil;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author feige<br />
 * @ClassName: ClusterTask <br/>
 * @Description: 和其他主机建立连接<br/>
 * @date: 2021/11/7 22:48<br/>
 */
public class ClusterTask implements Consumer<Integer> {

    private static final Logger LOG = LoggerFactory.getLogger();
    private static final ScheduledThreadPoolExecutorUtil EXECUTOR_UTIL = ScheduledThreadPoolExecutorUtil.getInstance();

    private final ImBusinessService imBusinessService;

    public ClusterTask(ImBusinessService imBusinessService) {
        this.imBusinessService = imBusinessService;
    }

    @Override
    public void accept(Integer port) {
        InetAddress localAddress = IpUtil.getLocalAddress();
        AssertUtil.notNull(localAddress,"localAddress");
        IRoute iRoutes = RouteManager.getIRoutes();
        ProviderService providerService = DiscoveryManager.getProviderService();
        providerService.subscribe(iRoutes::add);
        try {
            List<ServerInstance> allServerInstances = providerService.getAllServerInstances();
            final CountDownLatch countDownLatch = new CountDownLatch(allServerInstances.size());
            for (ServerInstance serverInstance : allServerInstances) {
                // 去除自己
                if (localAddress.getHostAddress().equals(serverInstance.getIp()) && port.equals(serverInstance.getPort())){
                    continue;
                }
                EXECUTOR_UTIL.execute(() -> {
                    ImClient.connect(serverInstance.getIp(),serverInstance.getPort(),new ClusterClientMsgListener(imBusinessService));
                    countDownLatch.countDown();
                });
            }
            // 等待链接建立完成之后注册自己
            countDownLatch.await();
            EXECUTOR_UTIL.execute(new ImRegistry(localAddress.getHostAddress(),port));
        } catch (Exception e) {
            LOG.error("cluster task fail:",e);
        }
    }
}
