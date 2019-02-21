package com.github.wycm.proxy.site.cnproxy;


import com.github.wycm.common.Proxy;
import com.github.wycm.common.util.Constants;
import com.github.wycm.proxy.ProxyListPageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class CnProxyListPageParser implements ProxyListPageParser {
    @Override
    public List<Proxy> parse(String hmtl) {
        Document document = Jsoup.parse(hmtl);
        Elements elements = document.select("table[id=tablekit-table-1] tbody");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(1)").first().text();
            String port  = element.select("td:eq(2)").first().text();
            if(!anonymousFlag){
                proxyList.add(new Proxy(ip, Integer.valueOf(port), Constants.TIME_INTERVAL));
            }
        }
        return proxyList;
    }
}
