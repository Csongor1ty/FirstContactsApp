package hu.alkfejl.dao;

import hu.alkfejl.config.ContactConfiguration;
import hu.alkfejl.model.Contact;
import hu.alkfejl.model.Phone;
import hu.alkfejl.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContactDAOImpl implements ContactDAO{

    private static final String SELECT_ALL_CONTACTS = "SELECT * FROM CONTACT";
    private static final String SELECT_ALL_CONTACTS_BY_USER = "SELECT * FROM CONTACT WHERE user_id = ?";
    private static final String INSERT_CONTACT = "INSERT INTO CONTACT (name, email, address, dateOfBirth, company, position, user_id) VALUES (?,?,?,?,?,?,?)";
    private static final String UPDATE_CONTACT = "UPDATE CONTACT SET name=?, email = ?, address = ?, dateOfBirth=?, company=?, position = ? WHERE id=?";
    private static final String DELETE_CONTACT = "DELETE FROM CONTACT WHERE id = ?";
    private String connectionURL;
    private PhoneDAO phoneDAO = new PhoneDAOImpl();
    private UserDAO userDAO = UserDAOImpl.getInstance();

    private static ContactDAO instance;

    private ContactDAOImpl(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connectionURL = ContactConfiguration.getValue("db.url");
    }

    public static ContactDAO getInstance() {
        if(instance == null){
            instance = new ContactDAOImpl();
        }
        return instance;
    }

    @Override
    public List<Contact> findAll() {
        return findAll(null);
    }

    @Override
    public List<Contact> findAll(User user) {
        List<Contact> result = new ArrayList<>();

        try(Connection c = DriverManager.getConnection(connectionURL);
        ){
            ResultSet rs;
            if(user == null){
                Statement stmt = c.createStatement();
                rs = stmt.executeQuery(SELECT_ALL_CONTACTS);
            }
            else{
                PreparedStatement stmt = c.prepareStatement(SELECT_ALL_CONTACTS_BY_USER);
                stmt.setInt(1, user.getId());
                rs = stmt.executeQuery();
            }

            while(rs.next()){
                result.add(extractContactFromResultSet(rs));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    @Override
    public Contact save(Contact contact) {
        try(Connection c = DriverManager.getConnection(connectionURL);
            PreparedStatement stmt = contact.getId() <= 0 ? c.prepareStatement(INSERT_CONTACT, Statement.RETURN_GENERATED_KEYS) : c.prepareStatement(UPDATE_CONTACT)
        ){
            if(contact.getId() > 0){ // UPDATE
                stmt.setInt(7, contact.getId());
            }
            else{
                if(contact.getUser() != null){
                    stmt.setInt(7, contact.getUser().getId());
                }
            }

            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getEmail());
            stmt.setString(3, contact.getAddress());
            stmt.setString(4, contact.getDateOfBirth().toString());
            stmt.setString(5, contact.getCompany());
            stmt.setString(6, contact.getPosition());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0){
                return null;
            }

            if(contact.getId() <= 0){ // INSERT
                ResultSet genKeys = stmt.getGeneratedKeys();
                if(genKeys.next()){
                    contact.setId(genKeys.getInt(1));
                }
            }

            List<Phone> oldPhones = phoneDAO.findAllByContactId(contact.getId());

            List<Phone> phones = new ArrayList<>();
            for(Phone phone : contact.getPhones()){
                phones.add(phoneDAO.save(phone, contact.getId()));
            }
            oldPhones.forEach(phone -> {
                if(!phones.contains(phone)){
                    phoneDAO.delete(phone);
                }
            });

            contact.setPhones(phones);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return contact;
    }

    @Override
    public void delete(Contact contact) {
        delete(contact.getId());
    }

    @Override
    public void delete(int contactId) {
        try(Connection c = DriverManager.getConnection(connectionURL);
            PreparedStatement stmt = c.prepareStatement(DELETE_CONTACT);
        ) {
            phoneDAO.deleteAll(contactId);
            stmt.setInt(1, contactId);
            stmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Contact findById(int contactId) {
        try (Connection conn = DriverManager.getConnection(connectionURL);
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM CONTACT WHERE id = ?")
        ) {
            pst.setInt(1, contactId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return extractContactFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    Contact extractContactFromResultSet(ResultSet rs){
        try {
            Contact contact = new Contact();
            contact.setId(rs.getInt("id"));
            contact.setName(rs.getString("name"));
            contact.setEmail(rs.getString("email"));
            contact.setAddress(rs.getString("address"));
            Date date = Date.valueOf(rs.getString("dateOfBirth"));
            contact.setDateOfBirth(date == null ? LocalDate.now() : date.toLocalDate());
            contact.setCompany(rs.getString("company"));
            contact.setPosition(rs.getString("position"));
            contact.setPhones(phoneDAO.findAllByContactId(contact.getId()));
            contact.setUser(userDAO.getUserById(rs.getInt("user_id")));
            return contact;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
