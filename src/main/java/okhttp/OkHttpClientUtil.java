package okhttp;

import okhttp3.*;
import org.apache.commons.collections.MapUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhengyong Date: 2018/8/16 Time: 下午5:16
 */
public class OkHttpClientUtil {

    private static final OkHttpClient client;

    static {

        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(null, cacheSize);
        ConnectionPool connectionPool = new ConnectionPool(50, 5, TimeUnit.SECONDS);

        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor())
                .connectionPool(connectionPool)
                .eventListener(new PrintingEventListener())
                //.cache(cache)
                .build();
    }

    /**
     * 同步GET调用
     *
     * @param url 调用url
     * @return String
     * @throws Exception
     */
    public static String syncGet(String url) throws Exception {
        return synExecute(url, null);
    }

    /**
     * 同步POST调用
     *
     * @param url 调用url
     * @return String
     * @throws Exception
     */
    public static String syncPost(String url, Map<String, String> params) throws Exception {
        return synExecute(url, params);
    }

    /**
     * 异步GET调用
     *
     * @param url 调用url
     * @throws Exception
     */
    public static void asyncGet(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    String result = response.body().string();
                    System.out.println(result);
                } finally {
                    response.close();
                }
            }
        });
    }


    /**
     * 同步调用
     *
     * @param url    调用url
     * @param params post参数
     * @return String
     * @throws Exception
     */
    private static String synExecute(String url, Map<String, String> params) throws Exception {
        Request.Builder rb = new Request.Builder();
        if (MapUtils.isNotEmpty(params)) {
            FormBody.Builder formBody = new FormBody.Builder();
            for (String key : params.keySet()) {
                formBody.add(key, params.get(key));
            }
            rb.post(formBody.build());
        }

        Request request = rb.url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            String result = response.body().string();
            System.out.println(result);
            return result;
        }
    }
}
