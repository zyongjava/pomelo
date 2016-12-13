package cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

/**
 * Created by zhengyong on 16/12/12.
 */
public class CassandraTest {

    public static void main(String[] args) {

        // 创建客户端
        CassandraClient cassandraClient = new CassandraClient("demodb", "127.0.0.1");

        // 插入数据
        cassandraClient.execute("INSERT INTO users (user_name, password, gender) VALUES ('zhangsan', 'admin', 'male')");

        // 查询数据
        ResultSet rs = cassandraClient.execute("select * from users where user_name = 'zhangsan'");

        // 输出
        Row row = rs.one();
        System.out.println("row=" + row.toString());
        System.out.println("user_name=" + row.getString("user_name"));
        System.out.println("password=" + row.getString("password"));
        System.out.println("gender=" + row.getString("gender"));

        // 关闭
        cassandraClient.close();
    }
}
