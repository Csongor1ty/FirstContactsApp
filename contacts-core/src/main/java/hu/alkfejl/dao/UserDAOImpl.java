package hu.alkfejl.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import hu.alkfejl.config.ContactConfiguration;
import hu.alkfejl.model.User;

import java.sql.*;

public class UserDAOImpl implements UserDAO{

    private static final String INSERT_USER = "INSERT INTO USER (username, email, profilePic, description, password) VALUES (?,?,?,?,?)";
    private static final String UPDATE_USER = "UPDATE USER SET username=?, email=?, profilePic=?, description=? WHERE id=?";
    private static UserDAOImpl instance;
    private static final String DB_CONN_STR = ContactConfiguration.getValue("db.url");

    public static UserDAOImpl getInstance() {
        if (instance == null) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            instance = new UserDAOImpl();
        }
        return instance;
    }

    private UserDAOImpl() {
    }

    @Override
    public User getUserById(int id) {
        try (Connection conn = DriverManager.getConnection(DB_CONN_STR);
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM USER WHERE id = ?")
        ) {
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt(1));
                u.setUsername(rs.getString(2));
                u.setEmail(rs.getString(3));
                u.setPassword(rs.getString(4));
                return u;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    public User save(User user) {

        try (Connection conn = DriverManager.getConnection(DB_CONN_STR);
             PreparedStatement pst = user.getId() <= 0 ? conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS) : conn.prepareStatement(UPDATE_USER)
        ) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getProfilePic());
            pst.setString(4, user.getDescription());

            if(user.getId() > 0) { // UPDATE
                pst.setInt(5, user.getId());
            }
            else{ // INSERT
                String hashedPwd = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
                pst.setString(5, hashedPwd);
            }

            int affectedRows = pst.executeUpdate();
            if(affectedRows == 0){
                return null;
            }

            if(user.getId() <= 0){ // INSERT
                ResultSet genKeys = pst.getGeneratedKeys();
                if(genKeys.next()){
                    user.setId(genKeys.getInt(1));
                }
            }

            user.setPassword("");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;

    }

    @Override
    public User login(String username, String password) {

        try (Connection conn = DriverManager.getConnection(DB_CONN_STR);
             PreparedStatement pst = conn.prepareStatement("SELECT * FROM USER WHERE username = ?")
        ) {
            pst.setString(1, username);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {

                String dbPass = rs.getString("password");

                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), dbPass);
                if(result.verified){
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setId(rs.getInt("id"));
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
