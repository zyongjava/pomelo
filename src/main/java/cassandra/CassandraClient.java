package cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.google.common.base.Preconditions;

/**
 * cassandra客户端<br/>
 * <p>
 * Created by zhengyong on 16/12/12.
 */
public class CassandraClient {

    /**
     * cluster
     */
    private static Cluster cluster;

    /**
     * session
     */
    private static Session session;

    /**
     * table name
     */
    private String keyspace;

    /**
     * server ip
     */
    private String serverIp;

    public CassandraClient(String keyspace, String serverIp) {
        this.keyspace = keyspace;
        this.serverIp = serverIp;
        createClient();
    }

    /**
     * 创建cassandra客户端
     */
    public void createClient() {

        Preconditions.checkNotNull(keyspace, "keyspace must not be null.");
        Preconditions.checkNotNull(serverIp, "serverIp must not be null.");

        if (session == null) {
            synchronized (this) {
                cluster = Cluster.builder().addContactPoint(serverIp).build(); // (1)
                session = cluster.connect(keyspace); // (2)
            }
        }
        Preconditions.checkNotNull(session, "session must not be null.");

    }

    /**
     * 执行sql语句
     *
     * @param sql 数据库语句
     * @return 执行结果
     */
    public ResultSet execute(String sql) {

        Preconditions.checkNotNull(sql, "sql must not be null.");

        return session.execute(sql);
    }

    public void close() {

        if (session != null) {
            session.close();
        }

        if (null != cluster) {
            cluster.close();
        }

    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
