package ua.ukrpost.dao;

import org.hibernate.Criteria;
import ua.ukrpost.entity.Address;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static java.lang.String.format;

@Repository
public class AddressDaoImpl implements AddressDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public AddressDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Address> getAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Address.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
    }
}
