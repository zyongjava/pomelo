package livy;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 */
public class HttpConfigClient {
    
    public static String postLivy(String postUrl, String json) throws IOException {
        CloseableHttpClient livyClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(postUrl);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = livyClient.execute(httpPost);
        return handleResponse(response);
    }

    public static String getLivy(String getUrl, Map<String, Object> parametersMap) throws IOException {
        CloseableHttpClient livyClient = HttpClients.createDefault();
        String url = buildUri(getUrl, parametersMap).toString();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = livyClient.execute(httpGet);
        return handleResponse(response);
    }

    public static String deleteLivy(String deleteUrl) throws IOException {
        CloseableHttpClient livyClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(deleteUrl);
        CloseableHttpResponse response = livyClient.execute(httpDelete);
        return handleResponse(response);
    }

    /***
     * 拼接get请求调用的URL
     *
     * @param url 调用请求
     * @param parametersMap 业务数据，作为参数
     * @return 完成url
     */
    private static URI buildUri(String url, Map<String, Object> parametersMap) {
        if (MapUtils.isEmpty(parametersMap)) {
            return URI.create(url);
        }
        ArrayList list = new ArrayList(parametersMap.size());
        Iterator iterator = parametersMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            list.add(entry.getKey().toString().trim() + "=" + entry.getValue().toString().trim());
        }
        return list.isEmpty() ? URI.create(url) : URI.create(url + "?" + StringUtils.join(list, "&"));
    }

    /**
     * 获取结果
     *
     * @param response 响应
     * @return 返回结果
     * @throws IOException
     */
    private static String handleResponse(CloseableHttpResponse response) throws IOException {
        HttpEntity resultEntity = response.getEntity();
        return EntityUtils.toString(resultEntity);
    }
}