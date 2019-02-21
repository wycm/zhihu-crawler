package com.github.wycm.common.util;

import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.*;
import org.asynchttpclient.cookie.CookieStore;
import org.asynchttpclient.cookie.ThreadSafeCookieStore;
import org.asynchttpclient.proxy.ProxyServer;
import org.asynchttpclient.uri.Uri;
import redis.clients.jedis.JedisPool;

import javax.net.ssl.SSLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.function.Predicate;

/**
 * Created by wycm on 2018/10/28.
 */
@Slf4j
public class SimpleHttpClient {
    private AsyncHttpClient asyncHttpClient;
    /**
     * 限制同一网站并发请求数
     * k:domain
     */
    public final static Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    private final static int MAX_CONCURRENT_COUNT = 3;

    protected JedisPool jedisPool = null;

    private int timeout = 10000;

    public SimpleHttpClient(JedisPool jedisPool){
        this(500, 2000, jedisPool);
    }

    public SimpleHttpClient(int maxConnectionsPerHost, int maxConnections, JedisPool jedisPool){
        this(new ThreadSafeCookieStore(), maxConnectionsPerHost, maxConnections, jedisPool);
    }
    public SimpleHttpClient(CookieStore cookieStore, int maxConnectionsPerHost, int maxConnections, JedisPool jedisPool){
        SslContext sslCtx = null;
        try {
            sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } catch (SSLException e) {
            e.printStackTrace();
        }
        DefaultAsyncHttpClientConfig.Builder builder = Dsl.config()
                .setConnectTimeout(timeout)
                .setHandshakeTimeout(timeout)
                .setReadTimeout(timeout)
                .setRequestTimeout(timeout)
                .setShutdownTimeout(timeout)
                .setSslSessionTimeout(timeout)
                .setPooledConnectionIdleTimeout(timeout)
                .setMaxConnectionsPerHost(maxConnectionsPerHost)
                .setMaxConnections(maxConnections)
                .setSslContext(sslCtx)
                .setMaxRedirects(3)
                .setFollowRedirect(true);
        if (cookieStore != null){
            builder.setCookieStore(cookieStore);
        }
        asyncHttpClient = new DefaultAsyncHttpClient(builder.build());
        this.jedisPool = jedisPool;
    }
    public String get(String url) throws ExecutionException, InterruptedException {
        Request request = new RequestBuilder()
                .setUrl(url)
                .setHeader("user-agent", Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)])
                .build();
        return execute(request);
    }

    public String get(String url, ProxyServer proxyServer) throws ExecutionException, InterruptedException {
        Request request = new RequestBuilder()
                .setUrl(url)
                .setProxyServer(proxyServer)
                .setHeader("user-agent", Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)])
                .build();
        return execute(request);
    }

    public String execute(Request request) throws ExecutionException, InterruptedException {
        return executeRequest(request).getResponseBody();
    }

    public Response getResponse(String url, ProxyServer proxyServer) throws ExecutionException, InterruptedException {
//        proxyServer = new ProxyServer.Builder("127.0.0.1", 8888).build();
        Request request = new RequestBuilder()
                .setUrl(url)
                .setProxyServer(proxyServer)
                .setHeader("user-agent", Constants.userAgentArray[new Random().nextInt(Constants.userAgentArray.length)])
                .build();
        return executeRequest(request);
    }
    public Response getResponse(String url, ProxyServer proxyServer, String userAgent) throws ExecutionException, InterruptedException {
//        proxyServer = new ProxyServer.Builder("127.0.0.1", 8888).build();
        RequestBuilder builder = new RequestBuilder();
        builder.resetCookies();
        Request request = builder
                .setUrl(url)
                .setProxyServer(proxyServer)
                .setHeader("user-agent", userAgent)
                .build();
        return executeRequest(request);
    }
    public Response getResponse(String url, ProxyServer proxyServer, String userAgent, Map<String, String> headers) throws ExecutionException, InterruptedException {
//        proxyServer = new ProxyServer.Builder("127.0.0.1", 8888).build();
        RequestBuilder builder = new RequestBuilder();
        builder.resetCookies();
        builder.setUrl(url)
                .setProxyServer(proxyServer)
                .setHeader("user-agent", userAgent);
        headers.forEach(builder::setHeader);
        return executeRequest(builder.build());
    }

    public Response executeRequest(Request request) throws InterruptedException, ExecutionException {
        Response res = null;

        String domain = PatternUtil.group(request.getUrl(), "//.*?([^\\.]+)\\.(com|net|org|info|coop|int|co\\.uk|org\\.uk|ac\\.uk|uk|cn)", 1);
        if (domain.contains("zhihu") || domain.contains("baidu")){
            res = asyncHttpClient.executeRequest(request).get();
            return res;
        }
        semaphoreMap.putIfAbsent(domain, new Semaphore(MAX_CONCURRENT_COUNT));
        try {
            semaphoreMap.get(domain).acquire();
            res = asyncHttpClient.executeRequest(request).get();
        } finally {
            semaphoreMap.get(domain).release();
        }
        return res;
    }

    public static class IgnoreCookieStore implements CookieStore{
        List<Cookie> empty = Collections.emptyList();

        @Override
        public void add(Uri uri, Cookie cookie) {

        }

        @Override
        public List<Cookie> get(Uri uri) {
            return empty;
        }

        @Override
        public List<Cookie> getAll() {
            return empty;
        }

        @Override
        public boolean remove(Predicate<Cookie> predicate) {
            return true;
        }

        @Override
        public boolean clear() {
            return true;
        }
    }
}
