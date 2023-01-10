package hu.alkfejl.dao;

import hu.alkfejl.model.Contact;
import hu.alkfejl.model.Phone;

import java.util.List;

public interface PhoneDAO {

    List<Phone> findAllByContactId(Contact contact);
    List<Phone> findAllByContactId(int contactId);
    Phone save(Phone p, int contactId);
    void delete(Phone p);

    void deleteAll(List<Phone> Phones);
    void deleteAll(int contactId);

    Phone findById(int phoneId);
}
