package ua.ukrpost.service;

import java.net.UnknownHostException;
import java.util.List;

import ua.ukrpost.entity.Address;

public interface TestService {

    void postData(String text) throws UnknownHostException;

    String getData() throws UnknownHostException;
}
