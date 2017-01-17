package httpclient.comfigclient;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 * <p>
 * 自定义 httpClient-4.3.6.jar
 * </p>
 * Created by zhengyong on 17/1/3.
 */
public class HttpConfigFactory {

    private static final Logger                                             logger                   = LoggerFactory.getLogger(HttpConfigFactory.class);

    /**
     * 最大客户端数
     */
    private static final int                                                MAX_CLIENT_COUNT         = 100;

    private static final String                                             DEFAULT_CLIENT_KEY       = "default";

    /**
     * 连接超时时间
     */
    private static final int                                                CONNECTION_TIME_OUT      = 10 * 1000;

    /**
     * socket超时时间
     */
    private static final int                                                SOCKET_TIME_OUT          = 5 * 1000;

    /**
     * 重连次数
     */
    private static final int                                                CONN_RETRY_MAX_TIMES     = 3;

    /**
     * 每个路由的最大连接数
     */
    private static final int                                                MAX_CONNECTION_PER_ROUTE = 2000;

    /**
     * 总最大连接数
     */
    private static final int                                                MAX_CONNECTION_TOTAL     = 2000;

    /**
     * 默认存活时间2分钟
     */
    private static final long                                               KEEP_ALIVE_DURATION      = 2 * 60 * 1000;

    /**
     * 全局的clientMap,key为业务场景标识 同一个业务场景共享一个httpClient 有唯一的default httpClient
     */
    private static final ConcurrentMap<String, CloseableHttpClient>         clientMap                = new ConcurrentHashMap<>(MAX_CLIENT_COUNT);

    private static final ConcurrentMap<String, HttpClientConnectionManager> connMgrMap               = new ConcurrentHashMap<>(MAX_CLIENT_COUNT);

    /**
     * 清除无用连接线程
     */
    static {
        IdleConnectionMonitorThread idle = new IdleConnectionMonitorThread();
        idle.start();
    }

    // http client instance (HttpClient实现应该是线程安全的。 建议将此httpclient相同实例重复用于多个请求执行)
    public static CloseableHttpClient defaultInstance() {
        return getInstance(DEFAULT_CLIENT_KEY, ConfigParamWrapper.defaultWrapper());
    }

    public static CloseableHttpClient getInstance(String key) {
        return getInstance(key, null);
    }

    public static CloseableHttpClient getInstance(String key, final ConfigParamWrapper wrapper) {
        CloseableHttpClient httpclient = clientMap.get(key);
        if (httpclient != null) {
            return httpclient;
        }

        ConfigParamWrapper.checkParamWrapper(wrapper);

        synchronized (clientMap) {
            httpclient = clientMap.get(key);
            if (httpclient != null) {
                return httpclient;
            }
            // 创建httpclient连接池
            PoolingHttpClientConnectionManager poolConnManager = new PoolingHttpClientConnectionManager();
            // 设置连接池最大数量
            poolConnManager.setMaxTotal(wrapper.getMaxConnTotal());
            // 设置单个路由最大连接数量
            poolConnManager.setDefaultMaxPerRoute(wrapper.getMaxConnPerRoute());

            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(wrapper.getSocketTimeout()).build();
            poolConnManager.setDefaultSocketConfig(socketConfig);

            // 长连接策略
            ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy() {

                @Override
                public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                    long keepAlive = super.getKeepAliveDuration(response, context);
                    if (keepAlive == -1) {
                        // Keep connections alive 5 seconds if a keep-alive value
                        // has not be explicitly set by the server
                        keepAlive = wrapper.getKeepAliveDuration();
                    }
                    return keepAlive;
                }

            };

            // 重试策略
            HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {

                public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                    if (executionCount >= wrapper.getRetryTimes()) {
                        // Do not retry if over max retry count
                        return false;
                    }
                    if (exception instanceof InterruptedIOException) {
                        // Timeout
                        return false;
                    }
                    if (exception instanceof UnknownHostException) {
                        // Unknown host
                        return false;
                    }
                    if (exception instanceof ConnectTimeoutException) {
                        // Connection refused
                        return false;
                    }
                    if (exception instanceof SSLException) {
                        // SSL handshake exception
                        return false;
                    }
                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                    if (idempotent) {
                        // Retry if the request is considered idempotent
                        return true;
                    }
                    return false;
                }
            };

            // 超时配置
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(wrapper.getSocketTimeout()).setConnectTimeout(wrapper.getConnTimeout()).setCookieSpec(CookieSpecs.BEST_MATCH).build();

            httpclient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(poolConnManager).setDefaultRequestConfig(requestConfig).setKeepAliveStrategy(keepAliveStrat).setRetryHandler(retryHandler).build();

            clientMap.put(key, httpclient);
            connMgrMap.put(key, poolConnManager);
        }

        return httpclient;
    }

    public static void close(CloseableHttpClient httpclient) throws IOException {
        if (httpclient != null) {
            httpclient.close();
        }
    }

    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }

            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }

            });
        } catch (GeneralSecurityException e) {
            logger.error("ssl connect socket create failed:", e);
        }
        return sslsf;
    }

    static class ConfigParamWrapper {

        private Integer maxConnPerRoute;
        private Integer maxConnTotal;
        private Integer connTimeout;
        private Integer socketTimeout;
        private Integer retryTimes;
        private Long    keepAliveDuration;

        public ConfigParamWrapper(){

        }

        public ConfigParamWrapper(Integer maxConnPerRoute, Integer maxConnTotal, Integer connTimeout,
                                  Integer socketTimeout, Integer retryTimes, Long keepAliveDuration){

            this.maxConnPerRoute = maxConnPerRoute;
            this.maxConnTotal = maxConnTotal;
            this.connTimeout = connTimeout;
            this.socketTimeout = socketTimeout;
            this.retryTimes = retryTimes;
            this.keepAliveDuration = keepAliveDuration;
        }

        public static ConfigParamWrapper defaultWrapper() {
            return new ConfigParamWrapper(MAX_CONNECTION_PER_ROUTE, MAX_CONNECTION_TOTAL, CONNECTION_TIME_OUT,
                                          SOCKET_TIME_OUT, CONN_RETRY_MAX_TIMES, KEEP_ALIVE_DURATION);
        }

        /**
         * 给未设置值的属性赋予默认值
         *
         * @param wrapper 自定义配置对象
         */
        public static void checkParamWrapper(ConfigParamWrapper wrapper) {
            if (wrapper == null) {
                defaultWrapper();
                return;
            }
            if (wrapper.getMaxConnPerRoute() == null) {
                wrapper.setMaxConnPerRoute(MAX_CONNECTION_PER_ROUTE);
            }
            if (wrapper.getMaxConnTotal() == null) {
                wrapper.setMaxConnTotal(MAX_CONNECTION_TOTAL);
            }
            if (wrapper.getConnTimeout() == null) {
                wrapper.setConnTimeout(CONNECTION_TIME_OUT);
            }
            if (wrapper.getSocketTimeout() == null) {
                wrapper.setSocketTimeout(SOCKET_TIME_OUT);
            }
            if (wrapper.getKeepAliveDuration() == null) {
                wrapper.setKeepAliveDuration(KEEP_ALIVE_DURATION);
            }
            if (wrapper.getRetryTimes() == null) {
                wrapper.setRetryTimes(CONN_RETRY_MAX_TIMES);
            }

        }

        public Integer getMaxConnPerRoute() {
            return maxConnPerRoute;
        }

        public void setMaxConnPerRoute(Integer maxConnPerRoute) {
            this.maxConnPerRoute = maxConnPerRoute;
        }

        public Integer getMaxConnTotal() {
            return maxConnTotal;
        }

        public void setMaxConnTotal(Integer maxConnTotal) {
            this.maxConnTotal = maxConnTotal;
        }

        public Integer getConnTimeout() {
            return connTimeout;
        }

        public void setConnTimeout(Integer connTimeout) {
            this.connTimeout = connTimeout;
        }

        public Integer getSocketTimeout() {
            return socketTimeout;
        }

        public void setSocketTimeout(Integer socketTimeout) {
            this.socketTimeout = socketTimeout;
        }

        public Integer getRetryTimes() {
            return retryTimes;
        }

        public void setRetryTimes(Integer retryTimes) {
            this.retryTimes = retryTimes;
        }

        public Long getKeepAliveDuration() {
            return keepAliveDuration;
        }

        public void setKeepAliveDuration(Long keepAliveDuration) {
            this.keepAliveDuration = keepAliveDuration;
        }

    }

    static class IdleConnectionMonitorThread extends Thread {

        private volatile boolean shutdown;

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        for (Map.Entry<String, HttpClientConnectionManager> entry : connMgrMap.entrySet()) {
                            if (entry.getValue() != null && entry.getValue() instanceof HttpClientConnectionManager) {

                                HttpClientConnectionManager connMgr = entry.getValue();

                                // Close expired connections
                                connMgr.closeExpiredConnections();
                                // Optionally, close connections
                                // that have been idle longer than 60 sec
                                connMgr.closeIdleConnections(1, TimeUnit.MINUTES);

                            }
                        }

                    }
                }
            } catch (InterruptedException ex) {
                // terminate
            }
        }

        public void shutdown() {
            shutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }

    }
}
