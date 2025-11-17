/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import model.Student;
import model.User;
import util.DatabaseUtil;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author hrkas
 */
public class UserDAO {

    public boolean addUser(User user) {

//        System.out.println("Test and Verify");
//        System.out.println("Username: " + user.getUsername());
//        System.out.println("Password: " + user.getPassword());
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
//        System.out.println("Hash Password: " + hashedPassword);
        String sql = "INSERT INTO users (username, password, lastname, firstname, role, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, hashedPassword);
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getLastname());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.getMessage();
        }

        return false;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, status = ?, lastname = ?, firstname = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            ps.setString(3, user.getRole());
            ps.setString(4, user.getStatus());
            ps.setString(5, user.getLastname());
            ps.setString(6, user.getFirstname());
            ps.setInt(7, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Update Error: " + ex);
        }
        return false;
    }

    public boolean changePassword(User user) {
        String sql = "UPDATE users SET  password = ?  WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            ps.setInt(2, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Update Error: " + ex);
        }
        return false;
    }

    public boolean changeStudentPassword(User user) {
        String sql = "UPDATE users SET  password = ?  WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            ps.setString(2, user.getUsername());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Update Error: " + ex);
        }
        return false;
    }

    public boolean updateInfo(User user) {
        String sql = "UPDATE users SET username = ?,  lastname = ?, firstname = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getLastname());
            ps.setString(3, user.getFirstname());
            ps.setInt(4, user.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Update Error: " + ex);
        }
        return false;
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public User getUserById(int userId) {
        User user = null;
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                user.setRole(rs.getString("role"));
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return user;
    }

    public List<User> listAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return list;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("role"), rs.getString("status"), rs.getString("lastname"), rs.getString("firstname"));
//        user.setId(rs.getInt("id"));
//        user.setUsername(rs.getString("username"));
//        user.setPassword(rs.getString("password"));
//        user.setRole(rs.getString("role"));
//        user.setStatus(rs.getString("status"));
        return user;
    }

    public void addStudentUser(Student student) {
        Date legacyDate = student.getDob();
        LocalDate localDate = legacyDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        String defaultPassword = student.getLastname().toLowerCase() + String.format("yyyy", year);
//        String hashedPassword = BCrypt.hashpw(defaultPassword, BCrypt.gensalt());
        User user = new User();
        user.setUsername(student.getStudno());
        user.setPassword(defaultPassword); // Consider hashing or letting them change later
        user.setRole("student");
        user.setStatus("active");
        user.setLastname(student.getLastname());
        user.setFirstname(student.getFirstname());

        new UserDAO().addUser(user);
    }

    public boolean updateUserWithoutPassword(User user) {
        String sql = "UPDATE users SET username = ?, role = ?, status = ?, lastname = ?, firstname = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getRole());
            ps.setString(3, user.getStatus());
            ps.setString(4, user.getLastname());
            ps.setString(5, user.getFirstname());
            ps.setInt(6, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return false;
    }

    public String getUserPasswordById(int id) {
        String sql = "SELECT password FROM users WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException ex) {
            System.out.println("Password Fetch Error: " + ex);
        }
        return "";
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                list.add(user);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return list;
    }

    public User authenticate(String username, String password) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        String sql = "SELECT * FROM users WHERE username = ?";
        String userRole;
        String fullname;
        User user = new User();
        try {
            Connection conn = DatabaseUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, username);
//            ps.setString(2, password); // If hashed, use a hash utility
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role"));
                    user.setFirstname(rs.getString("firstname"));
                    user.setLastname(rs.getString("lastname"));
                    // Set other fields as needed
                    return user;
                }
                fullname = user.getFirstname() + " " + user.getLastname();
                userRole = user.getRole();
                if (BCrypt.checkpw(password, user.getPassword())) {
                    // Store session values only after successful password check
                    HttpSession session = (HttpSession) ec.getSession(true);
                    session.setAttribute("username", username);
                    session.setAttribute("role", userRole);
                    session.setAttribute("fullname", fullname);

                    ec.getSessionMap().put("username", username);
                    ec.getSessionMap().put("role", userRole);
                    ec.getSessionMap().put("fullname", fullname);

                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid password", ""));
                    return null;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return user;
    }

    public LocalDate getLastDpaAckDate(int userId) {
        String sql = "SELECT last_dpa_ack_date FROM user WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getDate("last_dpa_ack_date") != null) {
                return rs.getDate("last_dpa_ack_date").toLocalDate();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void updateLastDpaAckDate(int userId) {
        String sql = "UPDATE user SET last_dpa_ack_date = CURDATE() WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

}
