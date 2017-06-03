package ua.ukrpost.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.HashMap;
import java.util.Map;
import javax.transaction.Transactional;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ukrpost.dao.AddressDao;

@Service
public class TestServiceImpl implements TestService {
//    private AddressDao addressDao;
    private static final int index = 1;
    private static int iterator = 1;
    private Map<String, Object> document1 = new HashMap<>();
    private Map<String, Object> partialDoc1 = new HashMap<>();
    private TransportClient client;

    @Autowired
    public TestServiceImpl(AddressDao addressDao) {
//        this.addressDao = addressDao;
        document1.put("screen_name", "d_bharvi");
        document1.put("followers_count", 2000);
        document1.put("create_at", "2015-09-20");
        partialDoc1.put("user_name", "Bharvi Dixit");
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void indexDocument1() {
        IndexResponse response = client.prepareIndex()
                .setIndex("index1").setType("doctype1")
                .setId("1").setSource(document1)
                .execute().actionGet();
        System.out.println("response: " + response);
    }

    @Override
    public void updateDocument1() {
        IndexResponse response = client.prepareIndex()
                .setIndex("index1").setType("doctype1")
                .setId("1").setSource(partialDoc1)
                .execute().actionGet();
        System.out.println("response: " + response);
    }

    @Override
    public void updateDocument1WithScript() {
        // TODO do not work
        String script = "ctx._source.user_name = \"Alberto Paro\"";
        UpdateResponse response = client.prepareUpdate()
                .setIndex("index1").setType("doctype1")
                .setScript(new Script(ScriptType.INLINE, script, "groovy", null))
                .setId("1").execute().actionGet();
        System.out.println("response: " + response);
    }

    @Override
    public void fetchDocument1() {
        GetResponse response = client.prepareGet()
                .setIndex("index1").setType("doctype1")
                .setId("1").execute().actionGet();
        System.out.println("response: " + response);
    }

    @Override
    @Transactional
    public void postData(String text) throws UnknownHostException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

//        String json = "{" +
//                "\"user\":\"Sabfir Oleh\"," +
//                "\"postDate\":\"2013-01-31\"," +
//                "\"message\":\"test message for Elasticsearch\"" +
//                "}";
        String json = "{" +
                "\"user\":\"Sabfir Oleh" + iterator + "\"," +
                "\"postDate\":\"2013-01-31\"," +
                "\"message\":\"" + text + "\"" +
                "}";

//        IndexResponse response = client.prepareIndex("twitter", "tweet", String.valueOf(index++))
//                .setSource(json)
//                .get();
        IndexResponse response = client.prepareIndex("twitter", "tweet", String.valueOf(iterator++))
                .setSource(json)
                .get();

//        // Index name
//        String _index = response.getIndex();
//        // Type name
//        String _type = response.getType();
//        // Document ID (generated or not)
//        String _id = response.getId();
//        // Version (if it's the first time you index this document, you will get: 1)
//        long _version = response.getVersion();
//        // status has stored current instance statement.
//        RestStatus status = response.status();

        client.close();
    }

    @Override
    public String getData() throws UnknownHostException {
//        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
//        SearchResponse searchResponse = client.prepareSearch("music").setTypes("lyrics").execute().actionGet();
//        searchResponse.get


        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        GetResponse response = client.prepareGet("twitter", "tweet", "1").get();
        String json;
        if (response.isExists()) {
            json = response.getSourceAsString();
        } else {
            json = "Can't find!";
        }

        client.close();

        return json;
    }
}
