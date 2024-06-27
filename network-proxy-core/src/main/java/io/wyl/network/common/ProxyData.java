package io.wyl.network.common;

/**
 * @author wyl
 * @date 2024年06月25日 9:47
 */
public class ProxyData {

    private String visitorId;

    private Integer port;

    private byte[] data;


    public ProxyData(String visitorId, Integer port, byte[] data) {
        this.visitorId = visitorId;
        this.data = data;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public Integer getPort() {
        return port;
    }

    public byte[] getData() {
        return data;
    }

}
