package com.github.wycm.proxy.site.ihuan;


import com.github.wycm.common.Proxy;
import com.github.wycm.proxy.ProxyListPageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.List;

public class IhuanProxyListPageParser implements ProxyListPageParser {
    @Override
    public List<Proxy> parse(String hmtl) {
        Document document = Jsoup.parse(hmtl);
        List<TextNode> textNodeList = document.select("p.text-left").first().textNodes();
        List<Proxy> proxyList = new ArrayList<>(textNodeList.size());
        textNodeList.forEach(i -> {
            i.toString();
        });
        return proxyList;
    }
}
