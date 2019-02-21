package com.github.wycm.proxy;

import com.github.wycm.proxy.site.cnproxy.CnProxyListPageParser;
import com.github.wycm.proxy.site.ip66.Ip66ProxyListPageParser;
import com.github.wycm.proxy.site.ip89.Ip89ProxyListPageParser;
import com.github.wycm.proxy.site.kuaidaili.KuaiProxyListPageParser;
import com.github.wycm.proxy.site.mimiip.MimiipProxyListPageParser;
import com.github.wycm.proxy.site.qydaili.QydailiProxyListPageParser;
import com.github.wycm.proxy.site.xicidaili.XicidailiProxyListPageParser;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 知乎代理池
 */
@Component
public class ProxyPageProxyPool {


    public static final int MAX_REQS = 3;

    public final static Map<String, Class> proxyMap = new HashMap<>();

    @PostConstruct
    public void init() {
        int pages = 16;
        for(int i = 1; i <= pages; i++){
            proxyMap.put("http://www.xicidaili.com/wt/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/nn/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/wn/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/nt/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("https://www.kuaidaili.com/free/inha/" + i + "/", KuaiProxyListPageParser.class);
            proxyMap.put("http://www.mimiip.com/gngao/" + i, MimiipProxyListPageParser.class);//高匿
            proxyMap.put("http://www.mimiip.com/gnpu/" + i, MimiipProxyListPageParser.class);//普匿
            proxyMap.put("http://www.66ip.cn/" + i + ".html", Ip66ProxyListPageParser.class);
//            for(int j = 1; j < 34; j++){
//                proxyMap.put("http://www.66ip.cn/areaindex_" + j + "/" + i + ".html", Ip66ProxyListPageParser.class);
//            }
            proxyMap.put("http://www.89ip.cn/index_" + i + ".html", Ip89ProxyListPageParser.class);
            proxyMap.put("http://www.qydaili.com/free/?action=china&page=" + i, QydailiProxyListPageParser.class);
        }
//        ThreadPoolUtil.createThreadPool(ThreadPoolUtil.getParserPoolName(XicidailiProxyListPageParser.class), MAX_REQS, MAX_REQS);
//        ThreadPoolUtil.createThreadPool(ThreadPoolUtil.getParserPoolName(KuaiProxyListPageParser.class), MAX_REQS, MAX_REQS);
//        ThreadPoolUtil.createThreadPool(ThreadPoolUtil.getParserPoolName(MimiipProxyListPageParser.class), MAX_REQS, MAX_REQS);
//        ThreadPoolUtil.createThreadPool(ThreadPoolUtil.getParserPoolName(Ip66ProxyListPageParser.class), MAX_REQS, MAX_REQS);

        proxyMap.putIfAbsent("http://cn-proxy.com/", CnProxyListPageParser.class);
    }

}
