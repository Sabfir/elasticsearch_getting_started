package ua.ukrpost.service;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.List;

import javax.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.ukrpost.dao.AddressDao;
import ua.ukrpost.entity.Address;

import static ua.ukrpost.util.LogMessageUtil.getAllLogEndpoint;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {
    private AddressDao addressDao;

    @Autowired
    public AddressServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    @Transactional
    public List<Address> getAll() {
        log.info(getAllLogEndpoint(Address.class));
        return addressDao.getAll();
    }
}
