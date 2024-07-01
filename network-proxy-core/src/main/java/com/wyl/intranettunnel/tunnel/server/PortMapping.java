package com.wyl.intranettunnel.tunnel.server;

/**
 * @author wyl
 * @date 2024年02月17日 14:19
 */
public class PortMapping {

    /**
     * 客户端id
     */
    private String clientId;
    /**
     * 服务端端口
     */
    private Integer serverPort;
    /**
     * 客户端ip
     */
    private String clientIp;
    /**
     * 客户端端口
     */
    private Integer clientPort;
    /**
     * 协议
     */
    private String protocol;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
