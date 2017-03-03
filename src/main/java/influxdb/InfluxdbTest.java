package influxdb;

import org.influxdb.InfluxDB;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengyong on 17/3/2.
 */
public class InfluxdbTest {

    /**
     * 数据库名称
     */
    private static final String database        = "influxdb-database";

    /**
     * 数据报存策略
     */
    private static String       retentionPolicy = "default";

    public static void main(String[] args) {

        InfluxDB influxDB = new InfluxdbBuilder("http://10.57.17.82:8086", "user", "pass").build();

        InfluxdbService service = new InfluxdbService(database, retentionPolicy, influxDB);

        // 创建数据库
        service.createDatabase();

        // 创建数据保存策略
        service.createRetentionPolicy("30d", 1);

        // 插入数据
        Map<String, String> tags = new HashMap<>();
        tags.put("methodName", "getName");
        Map<String, Object> fields = new HashMap<>();
        fields.put("rt", 200);
        fields.put("tps", 300);
        service.insert("measurementKey", tags, fields);

        System.out.println("finish");

    }

}
