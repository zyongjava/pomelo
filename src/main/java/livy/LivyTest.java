package livy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://www.jianshu.com/p/65d3150e8999
 *
 * https://blog.csdn.net/qq_36330643/article/details/76686225?locationNum=1&fps=1
 *
 * Q: requirement failed: Local path /data01/admin/apps/zy/transform/lib/etl.jar cannot be added to user sessions.
 *
 * A: 修改livy.conf： livy.file.local-dir-whitelist = /data01/admin/jars/
 *
 * # List of local directories from where files are allowed to be added to user sessions. By
 # default it's empty, meaning users can only reference remote URIs when starting their
 # sessions.
 *
 *
 * @author: zhengyong Date: 2020/4/17 Time: 9:48 AM
 */
public class LivyTest {

    public static void main(String[] args) throws IOException {
        String url = "http://10.57.17.215:8998/batches";
        List<Integer> sessionIds = getBatches(url);
        deleteBatches(url, sessionIds);
        String batches = postBatches(url);
    }


    public static void deleteBatches(String url, List<Integer> sessionIds) throws IOException {
        for (Integer id : sessionIds) {
            String deleteResult = HttpConfigClient.deleteLivy(String.format("%s/%s", url, id));
            System.out.println(String.format("delete batches id=%s, result: %s", id, deleteResult));
        }
    }


    public static List<Integer> getBatches(String url) throws IOException {
        String sessions = HttpConfigClient.getLivy(url, null);
        JSONObject object = JSON.parseObject(sessions);
        JSONArray array = object.getJSONArray("sessions");
        if (array == null) {
            return null;
        }
        List<Integer> sessionIds = new ArrayList<>(array.size());
        for (Object o : array) {
            JSONObject obj = (JSONObject) o;
            int id = obj.getIntValue("id");
            String name = obj.getString("name");
            String state = obj.getString("state");
            sessionIds.add(id);
            System.out.println(String.format("query session id=%s, name=%s(%s)", id, name, state));
        }
        return sessionIds;
    }

    public static String postBatches(String url) throws IOException {
        LivyEntity livyEntity = new LivyEntity();
        livyEntity.setFile("/home/admin/apps/jars/etl.jar");
        livyEntity.setClassName("cn.etl.DataEtlMain");
        livyEntity.setNumExecutors(3);
        livyEntity.setName("ETL");
        livyEntity.setExecutorMemory("4g");
        livyEntity.setExecutorCores(2);
        livyEntity.setArgs(Lists.newArrayList("params1"));

        //spark参数设置： http://spark.apache.org/docs/latest/configuration.html
        Map<String, Object> conf = new HashMap<>();
        conf.put("spark.master","spark://10.57.17.215:7077");
        conf.put("spark.driver.cores", "2");
        conf.put("spark.driver.memory", "4g");
        livyEntity.setConf(conf);

        String result = HttpConfigClient.postLivy(url, JSON.toJSONString(livyEntity));
        System.out.println("post batches result: " + result);
        return result;
    }
}