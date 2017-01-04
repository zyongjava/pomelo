package elasticsearch;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.SortParseElement;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by pomelo on 16/11/22.
 */
public class Elasticsearch {

    private static TransportClient client   = null;

    /**
     * es index
     */
    private static final String    ES_INDEX = "database_test";
    /**
     * es type
     */
    private static final String    ES_TYPE  = "user";

    public static void main(String[] args) throws Exception {

        createClient();

        String _id = createIndex();

        getDataResponse(_id);

        updateData(_id);

        getDataResponse(_id);

        QueryBuilder builder = QueryBuilders.matchAllQuery();
        // QueryBuilder builder = QueryBuilders.termQuery("username", "pomelo");
        queryByScroll(ES_INDEX, builder);

        deleteDataResponse(_id);

        shutdown();
    }

    public static void createClient() throws Exception {
        if (client == null) {
            synchronized (Elasticsearch.class) {
                if (client != null) {
                    return;
                }
                Settings settings = Settings.settingsBuilder().put("cluster.name", "myCluster").build();
                client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),
                                                                                                                                 9300));
            }
        }
    }

    /**
     * 创建es库并导入数据
     * 
     * @return _id
     */
    public static String createIndex() {
        String json = "{" + "\"username\":\"pomelo\"," + "\"postDate\":\"" + new Date().toLocaleString() + "\","
                      + "\"message\":\"create\"" + "}";

        IndexResponse response = client.prepareIndex(ES_INDEX, ES_TYPE).setSource(json).get();

        System.out.println(String.format("create index response: %s", response.toString()));

        return response.getId();
    }

    /**
     * 根据_id获取数据
     * 
     * @param _id 唯一标识
     * @return GetResponse
     */
    public static GetResponse getDataResponse(String _id) {
        GetResponse response = client.prepareGet(ES_INDEX, ES_TYPE, _id).get();
        System.out.println(String.format("get data response: %s", JSON.toJSONString(response.getSource())));
        return response;
    }

    /**
     * 根据查询条件查询结果集(最大1000条)
     * 
     * @param queryBuilder 查询条件
     * @return List
     */
    public static List<String> queryDataList(QueryBuilder queryBuilder) {
        SearchResponse sResponse = client.prepareSearch(ES_INDEX).setTypes(ES_TYPE).setQuery(queryBuilder).setSize(1000).execute().actionGet();
        SearchHits hits = sResponse.getHits();

        List<String> list = new ArrayList<>();
        SearchHit[] hitArray = hits.hits();
        for (SearchHit hit : hitArray) {
            Map<String, Object> map = hit.getSource();

            String username = (String) map.get("username");
            String postDate = (String) map.get("postDate");
            String message = (String) map.get("message");

            StringBuilder br = new StringBuilder();
            br.append(username).append("_").append(message).append("_").append(postDate);
            list.add(br.toString());
        }

        System.out.println(String.format("query data count=%s, list : %s", list.size(), JSON.toJSONString(list)));

        return list;
    }

    /**
     * 分片轮询查询
     * 
     * @param index 索引
     * @param queryBuilder 查询条件
     * @return list
     */
    public static List<String> queryByScroll(String index, QueryBuilder queryBuilder) {

        // 100 hits per shard will be returned for each scroll
        SearchResponse scrollResp = client.prepareSearch(index).addSort(SortParseElement.DOC_FIELD_NAME,
                                                                        SortOrder.ASC).setScroll(new TimeValue(60000)).setQuery(queryBuilder).setSize(100).execute().actionGet();
        List<String> list = new ArrayList<>();
        // Scroll until no hits are returned
        while (true) {

            for (SearchHit hit : scrollResp.getHits().getHits()) {
                // Handle the hit...
                Map<String, Object> map = hit.getSource();
                list.add(JSON.toJSONString(map));
                System.out.println(String.format("scroll query data list : %s", JSON.toJSONString(map)));
            }
            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
            // Break condition: No hits are returned
            if (scrollResp.getHits().getHits().length == 0) {
                break;
            }
        }
        return list;
    }

    /**
     * 根据_id删除数据
     * 
     * @param _id 唯一标识
     * @return DeleteResponse
     */
    public static DeleteResponse deleteDataResponse(String _id) {
        DeleteResponse response = client.prepareDelete(ES_INDEX, ES_TYPE, _id).get();
        System.out.println(String.format("delete data response: %s", JSON.toJSONString(response)));
        return response;
    }

    /**
     * 根据_id跟下数据
     * 
     * @param _id 唯一标识
     * @throws Exception
     */
    public static void updateData(String _id) throws Exception {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(ES_INDEX);
        updateRequest.type(ES_TYPE);
        updateRequest.id(_id);

        String json = "{" + "\"username\":\"lisi\"," + "\"postDate\":\"" + new Date().toLocaleString() + "\","
                      + "\"message\":\"update\"" + "}";

        updateRequest.doc(json);

        client.update(updateRequest).get();
    }

    private static void shutdown() {
        if (client != null) {
            client.close();
        }
    }

}
