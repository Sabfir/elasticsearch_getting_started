package ua.ukrpost.service;

import java.net.UnknownHostException;
import java.util.List;

import ua.ukrpost.entity.Address;

public interface TestService {

    void indexDocument1();

    void updateDocument1();

    void updateDocument1WithScript();

    void fetchDocument1();

    void postData(String text) throws UnknownHostException;

    String getData() throws UnknownHostException;
}
