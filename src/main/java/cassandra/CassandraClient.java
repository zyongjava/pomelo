package cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ProtocolOptions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * cassandra客户端<br/>
 * <p>
 * http://docs.datastax.com/en/developer/java-driver/3.1/manual/
 * </p>
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

    public CassandraClient(){
    }

    public CassandraClient(String keyspace, String serverIp){
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

        Cluster.Builder builder = Cluster.builder().addContactPoint(serverIp);

        // 设置连接池
        PoolingOptions poolingOptions = getPoolingOptions();
        builder.withPoolingOptions(poolingOptions);

        // socket 链接配置
        SocketOptions socketOptions = new SocketOptions().setKeepAlive(true).setReceiveBufferSize(1024
                                                                                                  * 1024).setSendBufferSize(1024
                                                                                                                            * 1024).setConnectTimeoutMillis(5
                                                                                                                                                            * 1000).setReadTimeoutMillis(1000);
        builder.withSocketOptions(socketOptions);

        // 设置压缩方式
        builder.withCompression(ProtocolOptions.Compression.LZ4);

        // 负载策略
        DCAwareRoundRobinPolicy dCAwareRoundRobinPolicy = DCAwareRoundRobinPolicy.builder().withLocalDc("myLocalDC").withUsedHostsPerRemoteDc(2).allowRemoteDCsForLocalConsistencyLevel().build();
        builder.withLoadBalancingPolicy(dCAwareRoundRobinPolicy);

        // 重试策略
        builder.withRetryPolicy(DefaultRetryPolicy.INSTANCE);

        cluster = builder.build();
        session = cluster.connect(keyspace); // (2)

        Preconditions.checkNotNull(session, "session must not be null.");
    }

    /**
     * 构建连接池
     *
     * @return 连接池
     */
    private PoolingOptions getPoolingOptions() {
        PoolingOptions poolingOptions = new PoolingOptions().setCoreConnectionsPerHost(HostDistance.LOCAL,
                                                                                       4).setMaxConnectionsPerHost(HostDistance.LOCAL,
                                                                                                                   10).setCoreConnectionsPerHost(HostDistance.REMOTE,
                                                                                                                                                 2).setMaxConnectionsPerHost(HostDistance.REMOTE,
                                                                                                                                                                             20);
        return poolingOptions;
    }

    /**
     * 执行预处理sql
     *
     * @param sql 数据库语句
     * @param params 占位符值
     * @return 执行结果
     */
    public ResultSet executePreparedSQL(String sql, List<String> params) {

        Preconditions.checkNotNull(session, "session must not be null.");
        Preconditions.checkNotNull(sql, "sql must not be null.");
        Preconditions.checkNotNull(params, "params must not be null.");

        PreparedStatement prepared = session.prepare(sql);
        BoundStatement bound = prepared.bind(params.toArray());
        return session.execute(bound);
    }

    /**
     * 执行sql语句
     *
     * @param sql 数据库语句
     * @return 执行结果
     */
    public ResultSet executeSQL(String sql) {

        Preconditions.checkNotNull(session, "session must not be null.");
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
