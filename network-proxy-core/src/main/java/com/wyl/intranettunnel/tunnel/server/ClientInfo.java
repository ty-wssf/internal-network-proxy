package com.wyl.intranettunnel.tunnel.server;

/**
 * @author wyl
 * @date 2024年02月15日 21:49
 */
public class ClientInfo {

    private String clientId;

    public ClientInfo() {
    }

    public ClientInfo(String clientId) {
        this.clientId = clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

}
