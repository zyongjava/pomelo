package httpclient.comfigclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * <p>
 * httpClient 线程池<br/>
 * 请求百度翻译地址 (netstat -an | grep CLOSE_WAIT)
 * </p>
 * Created by zhengyong on 17/1/3.
 */
public class HttpClientTest {

    private final static String URL = "http://fanyi.baidu.com/v2transapi";

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 1000; i++) {
            new Thread() {

                @Override
                public void run() {
                    try {
                        // post请求
                        String postResult = HttpConfigClient.post(URL, preparePostParams());
                        System.out.println(String.format("post result = %s", postResult));
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }

        // get请求
        String getResult = HttpConfigClient.get(URL, prepareGetParams());
        System.out.println(String.format("get result = %s", getResult));

    }

    /***
     * 准备post业务参数
     * 
     * @return List
     */
    private static List<NameValuePair> preparePostParams() {
        List<NameValuePair> parameterList = new ArrayList<>();
        parameterList.add(new BasicNameValuePair("from", "en"));
        parameterList.add(new BasicNameValuePair("to", "zh"));
        parameterList.add(new BasicNameValuePair("query", "hello"));
        parameterList.add(new BasicNameValuePair("transtype", "translang"));
        parameterList.add(new BasicNameValuePair("simple_means_flag", "3"));
        return parameterList;
    }

    /***
     * 准备get业务参数
     *
     * @return map
     */
    private static Map<String, Object> prepareGetParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("from", "en");
        params.put("to", "zh");
        params.put("query", "hello");
        params.put("transtype", "translang");
        params.put("simple_means_flag", "3");
        return params;
    }
}
