package hu.alkfejl.dao;

import hu.alkfejl.config.ContactConfiguration;
import hu.alkfejl.model.Contact;
import hu.alkfejl.model.Phone;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PhoneDAOImpl implements PhoneDAO{

    private static final String SELECT_PHONES_BY_CONTACT_ID = "SELECT * FROM PHONE WHERE contact_id=?";
    private static final String INSERT_PHONE = "INSERT INTO PHONE (number, phoneType, contact_id) VALUES (?,?,?)";
    private static final String UPDATE_PHONE = "UPDATE PHONE SET number = ?, phoneType = ? WHERE id = ?";
    private static final String DELETE_PHONE = "DELETE FROM PHONE WHERE id = ?";
    private String connectionUrl;

    public PhoneDAOImpl() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.connectionUrl = ContactConfiguration.getValue("db.url");
    }

    @Override
    public List<Phone> findAllByContactId(Contact contact) {
        return findAllByContactId(contact.getId());
    }

    @Override
    public List<Phone> findAllByContactId(int contactId) {
        List<Phone> result = new ArrayList<>();

        try(Connection c = DriverManager.getConnection(connectionUrl);
            PreparedStatement statement = c.prepareStatement(SELECT_PHONES_BY_CONTACT_ID)){

            statement.setInt(1, contactId);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Phone phone = extractPhoneFromResultSet(rs);
                result.add(phone);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return result;
    }

    @Override
    public Phone save(Phone phone, int contactId) {
        try(Connection c = DriverManager.getConnection(connectionUrl);
            PreparedStatement stmt = phone.getId() <= 0 ? c.prepareStatement(INSERT_PHONE, Statement.RETURN_GENERATED_KEYS) : c.prepareStatement(UPDATE_PHONE)
        ){
            if(phone.getId() > 0){ // UPDATE
                stmt.setInt(3, phone.getId());
            }else{ //INSERT
                stmt.setInt(3, contactId);
            }

            stmt.setString(1, phone.getNumber());
            stmt.setInt(2, phone.getPhoneType().ordinal());

            int affectedRows = stmt.executeUpdate();
            if(affectedRows == 0){
                return null;
            }

            if(phone.getId() <= 0){ // INSERT
                ResultSet genKeys = stmt.getGeneratedKeys();
                if(genKeys.next()){
                    phone.setId(genKeys.getInt(1));
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return phone;
    }

    @Override
    public void delete(Phone phone) {
        try(Connection c = DriverManager.getConnection(connectionUrl);
            PreparedStatement stmt = c.prepareStatement(DELETE_PHONE);
        ) {
            stmt.setInt(1, phone.getId());
            stmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteAll(List<Phone> phones) {
        for(Phone p : phones){
            delete(p);
        }
    }

    @Override
    public void deleteAll(int contactId) {
        findAllByContactId(contactId).forEach(this::delete);
    }

    @Override
    public Phone findById(int phoneId) {
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM PHONE WHERE id = ?")
        ) {
            pst.setInt(1, phoneId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return extractPhoneFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Phone extractPhoneFromResultSet(ResultSet rs) {
        try {
            Phone phone = new Phone();
            phone.setId(rs.getInt("id"));
            phone.setNumber(rs.getString("number"));
            int ordinalValue = rs.getInt("phoneType");

            Optional<Phone.PhoneType> pt = Arrays.stream(Phone.PhoneType.values()).filter(phoneType -> phoneType.ordinal() == ordinalValue).findAny();
            phone.setPhoneType(pt.orElse(Phone.PhoneType.UNKNOWN));
            return phone;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
