
package network.proxy.app;


import org.noear.solon.Solon;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Filter;
import org.noear.solon.core.handle.FilterChain;

public class NetworkProxyApplication {

    public static void main(String[] args) {
        Solon.start(NetworkProxyApplication.class, args, solonApp -> {
            solonApp.filter((ctx, chain) -> {
                if ("/".equals(ctx.pathNew())) { //ContextPathFilter 就是类似原理实现的
                    ctx.pathNew("/index.html");
                }
                chain.doFilter(ctx);
            });

        });
    }

}
