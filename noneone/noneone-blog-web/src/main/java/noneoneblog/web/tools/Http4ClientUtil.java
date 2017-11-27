
package noneoneblog.web.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类Http4ClientUtil.java的实现描述：TODO 类实现描述
 * 
 * @author leisure
 */
public class Http4ClientUtil {
	
	private static Logger log = LoggerFactory.getLogger(Http4ClientUtil.class);
    private final static String        DEFAULT_CHARSET = "UTF-8";

    static final int                   timeOut         = 60 * 1000;

    private static CloseableHttpClient httpClient      = null;

    private final static Object        syncLock        = new Object();

    private static SSLContext          ctx             = null;

    private static HostnameVerifier    verifier        = null;

    private static class DefaultTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }

    static {

        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());

            ctx.getClientSessionContext().setSessionTimeout(15);
            ctx.getClientSessionContext().setSessionCacheSize(1000);

        } catch (Exception e) {

        }

        verifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

    }

    private static void initConfig(HttpRequestBase httpRequestBase) {
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
                .setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
        httpRequestBase.setConfig(requestConfig);
    }

    /**
     * 获取HttpClient对象
     * 
     * @param url:http://dev.mp.zhongan.com/
     * @return
     */
    public static CloseableHttpClient getHttpClient(String url) {
        String hostname = url.split("/")[2];
        int port = 80;
        if (hostname.contains(":")) {
            String[] arr = hostname.split(":");
            hostname = arr[0];
            port = Integer.parseInt(arr[1]);
        }
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
                    httpClient = createHttpClient(200, 40, 100, hostname, port);
                }
            }
        }
        return httpClient;
    }

    private static LayeredConnectionSocketFactory getSslFactory() {
        return new SSLConnectionSocketFactory(ctx, verifier);
    }

    /**
     * 创建HttpClient对象
     * 
     * @return
     */
    public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, String hostname,
                                                       int port) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf).register("https", getSslFactory()).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                log.info("exception:{}", exception);
                log.info("retry:{}", executionCount);
                if (executionCount >= 3) {// 如果已经重试了3次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return true;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler).build();

        return httpClient;
    }

    /**
     * POST请求URL获取内容
     * 
     * @param url
     * @param :json格式
     * @return
     * @author SHANHY
     * @throws IOException
     * @create 2015年12月18日
     */
    public static String postJson(String url, String params) throws IOException {
        return post(url, params, ContentType.APPLICATION_JSON);
    }


   
    
    
    
    public static String postForm(String url, String params) {
        return post(url, params, ContentType.APPLICATION_FORM_URLENCODED);
    }

    /**
     * 微信退款接口-获取带ssl认证的httpclient
     * 
     * @param certPath：p12证书地址
     * @param mchId ：证书密钥,默认为商户ID
     * @return
     */
    private static CloseableHttpClient getCertHtppClient(String certPath, String mchId) {
        try {
            // 证书密码（默认为商户ID）
            String password = mchId;
            // 实例化密钥库
            KeyStore ks = KeyStore.getInstance("PKCS12");

            // 获得密钥库文件流
            InputStream fis = Http4ClientUtil.class.getResourceAsStream(certPath);
            // 加载密钥库
            ks.load(fis, password.toCharArray());
            // 关闭密钥库文件流
            fis.close();

            // 实例化密钥库
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            // 初始化密钥工厂
            kmf.init(ks, password.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");

            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
            // 获取SSLSocketFactory对象
            SSLConnectionSocketFactory sslSocketFactoy = new SSLConnectionSocketFactory(sslContext);

            Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslSocketFactoy).build();

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
            return httpClient;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    public static String postWxCert(String url, String params, String certPath, String mchId) {
        CloseableHttpResponse response = null;
        try {
            HttpPost httppost = new HttpPost(url);
            initConfig(httppost);
            log.info("url:{}", url);
            HttpEntity httpEntity = new StringEntity(params, ContentType.APPLICATION_JSON);
            httppost.setEntity(httpEntity);
            response = getCertHtppClient(certPath, mchId).execute(httppost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            log.info("result:{}", result);
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return null;
    }

    public static String postXml(String url, String xmlStr, ContentType type,String charset) {
        log.info("请求参数:{}", xmlStr);
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Content-Type", type.getMimeType());  
        initConfig(httppost);
        CloseableHttpResponse response = null;
        try {
            log.info("url:{}", url);
            HttpEntity httpEntity = new StringEntity(xmlStr, charset);
            httppost.setEntity(httpEntity);
            response = getHttpClient(url).execute(httppost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static String postXml(String url, String xmlStr,String charset) {
        log.info("请求参数:{}", xmlStr);
        HttpPost httppost = new HttpPost(url);
        initConfig(httppost);
        CloseableHttpResponse response = null;
        try {
            log.info("url:{}", url);
            HttpEntity httpEntity = new StringEntity(xmlStr, charset);
            httppost.setEntity(httpEntity);
            response = getHttpClient(url).execute(httppost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
  
    public static String postXml(String url, String xmlStr) {
        return postXml(url, xmlStr, ContentType.APPLICATION_XML,"utf-8");
    }
    
    public static String postTextXml(String url, String xmlStr) {
        return postXml(url, xmlStr, ContentType.TEXT_XML,"utf-8");
    }




    

    private static String post(String url, String params, ContentType contentType) {
        HttpPost httppost = new HttpPost(url);
        initConfig(httppost);
        CloseableHttpResponse response = null;
        try {
            log.info("url:{}, params: {}", url, params);

            HttpEntity httpEntity = new StringEntity(params, contentType);
            httppost.setEntity(httpEntity);
            response = getHttpClient(url).execute(httppost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }
    
   
    
    /**
     * 发送 POST 请求（HTTP），K-V形式
     * 
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPostByMap(String apiUrl, Map<String, Object> params) {
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = getHttpClient(apiUrl).execute(httpPost, HttpClientContext.create());
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

}
