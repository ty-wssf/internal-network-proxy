package io.wyl.network.common;

/**
 * @author wyl
 * @date 2024年06月25日 9:47
 */
public class ProxyData {

    private String visitorId;

    private byte[] data;

    public ProxyData(String visitorId, byte[] data) {
        this.visitorId = visitorId;
        this.data = data;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public byte[] getData() {
        return data;
    }

}
