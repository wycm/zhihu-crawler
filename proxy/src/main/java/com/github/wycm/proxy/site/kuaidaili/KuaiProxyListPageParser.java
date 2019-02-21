package com.github.wycm.proxy.site.kuaidaili;


import com.github.wycm.common.Proxy;
import com.github.wycm.proxy.ProxyListPageParser;
import com.github.wycm.common.util.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class KuaiProxyListPageParser implements ProxyListPageParser {
    @Override
    public List<Proxy> parse(String content) {
        Document document = Jsoup.parse(content);
        Elements elements = document.select("[id=list] tbody tr");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            String isAnonymous = element.select("td:eq(2)").first().text();
            if(!anonymousFlag || isAnonymous.contains("匿")){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), Constants.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
