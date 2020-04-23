package okhttp;

import java.util.HashMap;
import java.util.Map;

/**
 * okhttp3 test
 *
 * @author: zhengyong Date: 2018/8/16 Time: 下午4:24
 */
public class OkHttpTest {

    public static void main(String[] args) throws Exception {

        // 同步GET请求
        OkHttpClientUtil.syncGet("https://www.baidu.com/");
        OkHttpClientUtil.syncGet("https://www.baidu.com/");

        // 同步POST请求
        Map<String, String> params = new HashMap<>();
        params.put("search", "Jurassic Park");
       // OkHttpClientUtil.syncPost("https://en.wikipedia.org/wiki/Main_Page", null);

        // 异步GET请求
        //OkHttpClientUtil.asyncGet("https://publicobject.com/helloworld.txt");
    }
}
