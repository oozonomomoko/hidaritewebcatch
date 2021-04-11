package hidari.dto;

import org.jsoup.internal.StringUtil;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author 左手掐腰
 * @since 2019/10/11 11:04
 */
public class CatchConfig {

    public static Proxy proxy;

    public static boolean init(String ipPort) {
        if(StringUtil.isBlank(ipPort)){
            proxy = null;
            return true;
        }
        String[] sp = ipPort.split(":");

        if(2 != sp.length) {
            return false;
        }
        try{
            int port = Integer.parseInt(sp[1]);
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(sp[0], port));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
