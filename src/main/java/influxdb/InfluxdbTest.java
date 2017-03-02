package influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengyong on 17/3/2.
 */
public class InfluxdbTest {

    private static final String database                 = "influxdb-test";

    private static String       DEFAULT_RETENTION_POLICY = "default";

    /**
     * @param args
     */
    public static void main(String[] args) {

        InfluxDB influxDB = new InfluxdbBuilder("http://10.57.17.82:8086", "user", "pass").build();
        influxDB.createDatabase(database);

        Map<String, String> map = new HashMap<>();
        map.put("name", "zhangsan");
        Point.Builder builder = Point.measurement("measurementKey").tag(map).addField("fieldKey", "fieldValue");

        // create retention policy
        influxDB.query(new Query("CREATE RETENTION POLICY \"" + DEFAULT_RETENTION_POLICY + "\" ON \"" + database
                                 + "\" DURATION 30d REPLICATION 1 DEFAULT", database));

        influxDB.write(database, DEFAULT_RETENTION_POLICY, builder.build());

        // select * FROM "measurementKey"
    }

}
