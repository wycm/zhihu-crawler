package com.github.wycm.proxy.site.ip89;


import com.github.wycm.common.Proxy;
import com.github.wycm.proxy.ProxyListPageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.github.wycm.common.util.Constants.TIME_INTERVAL;


/**
 * http://www.89ip.cn/
 */
public class Ip89ProxyListPageParser implements ProxyListPageParser {
    @Override
    public List<Proxy> parse(String content) {
        List<Proxy> proxyList = new ArrayList<>();
        if (content == null || content.equals("")){
            return proxyList;
        }
        Document document = Jsoup.parse(content);
        Elements elements = document.select("table.layui-table tbody tr");
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            String isAnonymous = "";
            if(!anonymousFlag || isAnonymous.contains("匿")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
