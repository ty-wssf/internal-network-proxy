package network.proxy.tunnel.server;

import org.noear.socketd.transport.core.Entity;
import org.noear.socketd.transport.core.Session;

import java.util.function.Consumer;

/**
 * 通道服务
 *
 * @author wyl
 * @date 2024年02月14日 16:09
 */
public interface TunnelServer {

    /**
     * 添加本地服务端口和代理局域网信息的映射关系
     */
    TunnelServer addPortMapping(PortMapping portMapping);

    /**
     * 添加本地服务端口和代理局域网信息的映射关系
     */
    TunnelServer removeMappingByPort(Integer port);

    /**
     * 获取映射信息
     */
    PortMapping getMappingByPort(Integer port);

    TunnelServer addClientInfo(ClientInfo clientInfo);

    TunnelServer removeClientInfo(String clientId);

    ClientInfo getClientInfo(String clientId);

    TunnelServer addVisitorWriteCallback(String visitorId, Consumer<byte[]> func);

    TunnelServer removeVisitorWriteCallback(String visitorId);

    /**
     * 启动
     */
    TunnelServer start(int port) throws Exception;


    /**
     * 停止
     */
    void stop();

    Session getSession(Entity entity);

}
