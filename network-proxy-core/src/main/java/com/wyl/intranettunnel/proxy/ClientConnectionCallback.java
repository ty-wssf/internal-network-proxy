package com.wyl.intranettunnel.proxy;

import java.io.IOException;

/**
 * @author wyl
 * @date 2024年02月18日 11:19
 */
public interface ClientConnectionCallback {
    void onConnectSuccess() throws IOException;
    void onConnectFailure(Throwable e) throws IOException;
}
