package hu.alkfejl.dao;

import hu.alkfejl.model.Contact;
import hu.alkfejl.model.User;

import java.util.List;

public interface ContactDAO {

    List<Contact> findAll();
    List<Contact> findAll(User user);

    Contact save(Contact contact);

    void delete(Contact contact);
    void delete(int contactId);

    Contact findById(int contactId);
}
