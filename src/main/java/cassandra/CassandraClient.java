package cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.google.common.base.Preconditions;

/**
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
    private String         keyspace;

    /**
     * server ip
     */
    private String         serverIp;

    public  CassandraClient(String keyspace, String serverIp) {
        this.keyspace = keyspace;
        this.serverIp = serverIp;
        createClient();
    }

    /**
     * 创建cassandra客户端
     */
    public void createClient() {

        Preconditions.checkNotNull(keyspace, "keyspace must be not null.");
        Preconditions.checkNotNull(serverIp, "serverIp must be not null.");

        if (session == null) {
            synchronized (this) {
                cluster = Cluster.builder().addContactPoint(serverIp).build(); // (1)
                session = cluster.connect(keyspace); // (2)
            }
        }

    }

    public ResultSet query(String sql) {
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
