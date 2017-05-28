package ua.ukrpost.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.transaction.Transactional;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ukrpost.dao.AddressDao;

@Service
public class TestServiceImpl implements TestService {
    private AddressDao addressDao;
    private static final int index = 1;
    private static int iterator = 1;

    @Autowired
    public TestServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
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
