package com.wyl.intranettunnel.common;

/**
 * 常量
 *
 * @author wyl
 * @date 2024年02月14日 16:38
 */
public interface TunnelConstants {

    /**
     * 元信息：消费回执
     */
    String META_ACK = "ack";
    /**
     * 元信息：客户端id
     */
    String CLIENT_ID = "client.id";
    /**
     * 元信息：访问者id
     */
    String VISITOR_ID = "visitor.id";
    /**
     * 元信息：局域网信息
     */
    String LAN_INFO = "lan.info";
    /**
     * 元信息：连接
     */
    String CONNECT = "connect";
    /**
     * 元信息：断开连接
     */
    String DISCONNECT = "disconnect";
    /**
     * 元信息：传输数据
     */
    String TRANSFER_DATA = "data";
    /**
     * 元信息：协议
     */
    String PROTOCOL = "protocol";
    /**
     * 元信息：TCP协议
     */
    String TCP_PROTOCOL = "tcp";
    /**
     * 元信息：UDP协议
     */
    String UDP_PROTOCOL = "udp";

}
