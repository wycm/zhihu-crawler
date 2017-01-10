package com.crawl.proxy.site.mimiip;


import com.crawl.proxy.ProxyListPageParser;
import com.crawl.proxy.entity.Proxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.crawl.core.util.Constants.TIME_INTERVAL;

public class MimiipProxyListPageParser implements ProxyListPageParser{
    @Override
    public List<Proxy> parse(String hmtl) {
        Document document = Jsoup.parse(hmtl);
        Elements elements = document.select("table[class=list] tr");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (int i = 1; i < elements.size(); i++){
            String isAnonymous = elements.get(i).select("td:eq(3)").first().text();
            if(isAnonymous.equals("高匿")){
                String ip = elements.get(i).select("td:eq(0)").first().text();
                String port  = elements.get(i).select("td:eq(1)").first().text();
                proxyList.add(new Proxy(ip, Integer.valueOf(port), TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
