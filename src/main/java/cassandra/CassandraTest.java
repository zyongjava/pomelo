package cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

/**
 * Created by zhengyong on 16/12/12.
 */
public class CassandraTest {

    public static void main(String[] args) {

        CassandraClient cassandraClient = new CassandraClient("demodb","127.0.0.1");
        ResultSet rs = cassandraClient.query("select * from users");

        Row row = rs.one();
        System.out.println("row=" + row.toString());
        System.out.println("user_name=" + row.getString("user_name"));
        System.out.println("password=" + row.getString("password"));
        System.out.println("gender=" + row.getString("gender"));

        cassandraClient.close();
    }
}
