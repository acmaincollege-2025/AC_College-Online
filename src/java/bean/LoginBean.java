/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.StudentCredentialsDAO;
import dao.StudentDAO;
import dao.UserDAO;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import model.Student;
import model.StudentCredential;
import model.User;
import org.primefaces.PrimeFaces;

/**
 *
 * @author hrkas
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    /**
     * Creates a new instance of LoginBean
     */
    private User user = new User();
    private List<User> userList;
    private boolean editMode = false;
    private String username;
    private String password;
    private String userRole;
    private boolean loggedIn = false;
    private String studentNo;
    private String fullname;
    private Student loggedInStudent;
    private User loggedInUser;
    private final UserDAO userDAO = new UserDAO();
    private String loggedInUserName;
    private String loginType = "general";
    private int userId;
    private boolean agreedToPrivacy;
    private boolean shouldShowDpaNotice = false;

    public LoginBean() {
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the userList
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * @param userList the userList to set
     */
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    /**
     * @return the editMode
     */
    public boolean isEditMode() {
        return editMode;
    }

    /**
     * @param editMode the editMode to set
     */
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    /**
     * @return the userRole
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * @param userRole the userRole to set
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    /**
     * @return the loggedIn
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * @param loggedIn the loggedIn to set
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * @return the studentNo
     */
    public String getStudentNo() {
        return studentNo;
    }

    /**
     * @param studentNo the studentNo to set
     */
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    /**
     * @return the loggedInStudent
     */
    public Student getLoggedInStudent() {
        return loggedInStudent;
    }

    /**
     * @param loggedInStudent the loggedInStudent to set
     */
    public void setLoggedInStudent(Student loggedInStudent) {
        this.loggedInStudent = loggedInStudent;
    }

    /**
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * @return the loggedInUser
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * @param loggedInUser the loggedInUser to set
     */
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    /**
     * @return the loggedInUserName
     */
    public String getLoggedInUserName() {
        return loggedInUserName;
    }

    /**
     * @param loggedInUserName the loggedInUserName to set
     */
    public void setLoggedInUserName(String loggedInUserName) {
        this.loggedInUserName = loggedInUserName;
    }

    /**
     * @return the loginType
     */
    public String getLoginType() {
        return loginType;
    }

    /**
     * @param loginType the loginType to set
     */
    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the agreedToPrivacy
     */
    public boolean isAgreedToPrivacy() {
        return agreedToPrivacy;
    }

    /**
     * @param agreedToPrivacy the agreedToPrivacy to set
     */
    public void setAgreedToPrivacy(boolean agreedToPrivacy) {
        this.agreedToPrivacy = agreedToPrivacy;
    }

    /**
     * @return the shouldShowDpaNotice
     */
    public boolean isShouldShowDpaNotice() {
        return shouldShowDpaNotice;
    }

    /**
     * @param shouldShowDpaNotice the shouldShowDpaNotice to set
     */
    public void setShouldShowDpaNotice(boolean shouldShowDpaNotice) {
        this.shouldShowDpaNotice = shouldShowDpaNotice;
    }

    @PostConstruct
    public void init() {
        setUserList(userDAO.listAllUsers());
    }

//    public void acknowledgeDpaNotice() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
//        if (session != null) {
//            session.setAttribute("lastDpaAcknowledged", LocalDate.now().toString());
//        }
//        FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage(FacesMessage.SEVERITY_INFO, "Thank you for acknowledging the Data Privacy Notice.", null));
//    }
    public boolean shouldShowDpaNotice() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        if (session == null) {
            return true;
        }

        String lastAck = (String) session.getAttribute("lastDpaAcknowledged");
        if (lastAck == null) {
            return true;
        }

        LocalDate lastDate = LocalDate.parse(lastAck, DateTimeFormatter.ISO_LOCAL_DATE);
        return !lastDate.isEqual(LocalDate.now());
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(true);

        try {
            if ("student".equals(getLoginType())) {
                StudentCredentialsDAO studentCredDAO = new StudentCredentialsDAO();
                StudentCredential sc = studentCredDAO.authenticate(username, password);
                if (sc == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials.", null));
                    return null;
                }

                StudentDAO studentDAO = new StudentDAO();
                Student student = studentDAO.findByStudentNo(sc.getStudentNo());

                if (student != null) {
                    setLoggedInStudent(student);
                    setUserRole("Student");
                    setFullname(student.getFirstname() + " " + student.getLastname());
                    setStudentNo(student.getStudno());
                    studentCredDAO.updateLoginTime(sc.getStudentNo());

                    // ✅ Check DPA acknowledgment
                    LocalDate lastAck = studentCredDAO.getLastDpaAckDate(student.getStudno());
                    if (lastAck == null || !lastAck.isEqual(LocalDate.now())) {
                        setShouldShowDpaNotice(true);
                        session.setAttribute("showDpaNotice", true);
                    }

                    session.setAttribute("loggedInStudent", student);
                    ec.getSessionMap().put("loggedInStudent", student);

                    PrimeFaces.current().executeScript("$('#loginModal').modal('hide')");
                    return "student";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Student record not found.", null));
                    return null;
                }
            } else {
                User allUser = userDAO.authenticate(username, password);
                if (allUser == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials.", null));
                    return null;
                }

                fullname = allUser.getFirstname() + " " + allUser.getLastname();
                userId = allUser.getId();
                userRole = allUser.getRole();

                // ✅ Check DPA acknowledgment for staff
                LocalDate lastAck = userDAO.getLastDpaAckDate(allUser.getId());
                if (lastAck == null || !lastAck.isEqual(LocalDate.now())) {
                    setShouldShowDpaNotice(true);
                    session.setAttribute("showDpaNotice", true);
                }

                PrimeFaces.current().executeScript("$('#loginModal').modal('hide')");

                switch (userRole) {
                    case "Administrator":
                        return "admin";
                    case "Registrar":
                        return "registrar";
                    case "Dean":
                        return "dean";
                    case "Admission":
                        return "admission";
                    case "Cashier":
                        return "cashier";
                    default:
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid role", ""));
                        return null;
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed: " + ex.getMessage(), null));
            return null;
        }
    }

    public void acknowledgeDpaNotice() {
        try {
            if ("Student".equals(userRole)) {
                new StudentCredentialsDAO().updateLastDpaAckDate(getStudentNo());
            } else {
                new UserDAO().updateLastDpaAckDate(userId);
            }
            setShouldShowDpaNotice(false);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Thank you for acknowledging the Data Privacy Notice.", null));
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public void saveUser() {
        boolean success;
        if (isEditMode()) {
            success = userDAO.updateUser(getUser());
        } else {
            success = userDAO.addUser(getUser());
        }
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User saved successfully."));
            resetForm();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to save user.", null));
        }
        setUserList(userDAO.listAllUsers());
    }

    public void editUser(User u) {
        this.setUser(u);
        this.setEditMode(true);
    }

    public void deleteUser(int id) {
        if (userDAO.deleteUser(id)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User deleted successfully."));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete user.", null));
        }
        setUserList(userDAO.listAllUsers());
    }

    public void resetForm() {
        this.setUser(new User());
        this.setEditMode(false);
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

    public boolean isRegistrar() {
        return "Registrar".equals(getUserRole());
    }

    public boolean isDean() {
        return "Dean".equals(getUserRole());
    }

    public boolean isCashier() {
        return "Cashier".equals(getUserRole());
    }

    public boolean isAdmission() {
        return "Admission".equals(getUserRole());
    }

    public boolean isStudent() {
        return "Student".equals(getUserRole());
    }

    public String redirectToHome() {
        switch (userRole) {
            case "Registrar":
                return "registrar.xhtml?faces-redirect=true";
            case "Dean":
                return "dean.xhtml?faces-redirect=true";
            case "Admission":
                return "admission.xhtml?faces-redirect=true";
            case "Cashier":
                return "cashier.xhtml?faces-redirect=true";
            case "Student":
                return "student.xhtml?faces-redirect=true";
            default:
                return "login.xhtml?faces-redirect=true";
        }
    }

    public boolean isAdmin() {
        return loggedInUser != null && "Admin".equalsIgnoreCase(loggedInUser.getRole());
    }

    public String getDashboardOutcome() {
        switch (getUserRole()) {
            case "Dean":
                return "/Dean/dashboard.xhtml?faces-redirect=true";
            case "Cashier":
                return "/Cashier/dashboard.xhtml?faces-redirect=true";
            case "Admission":
                return "/Admission/dashboard.xhtml?faces-redirect=true";
            case "Registrar":
                return "/Registrar/dashboard.xhtml?faces-redirect=true";
            case "Student":
                return "/Student/dashboard.xhtml?faces-redirect=true";
            case "Admin":
                return "/Admin/dashboard.xhtml?faces-redirect=true";
            default:
                return "/index.xhtml?faces-redirect=true";
        }
    }

}

/*
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext ec = context.getExternalContext();
        Connection con = null;

        try {
            con = DatabaseUtil.getConnection(); // Use your method to get DB connection
            String sql = "SELECT password, role, firstname, lastname FROM users WHERE username = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                userRole = rs.getString("role");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                setFullname(firstName + " " + lastName);

//                String trimmedInputPassword = user.getPassword() != null ? user.getPassword().trim() : "";
                if (BCrypt.checkpw(password, hashedPassword.trim())) {
                    // Store session values only after successful password check
                    HttpSession session = (HttpSession) ec.getSession(true);
                    session.setAttribute("username", username);
                    session.setAttribute("role", userRole);
                    session.setAttribute("fullname", getFullname());

                    ec.getSessionMap().put("username", username);
                    ec.getSessionMap().put("role", userRole);
                    ec.getSessionMap().put("fullname", getFullname());

                    switch (userRole) {
                        case "Registrar":
                            return "registrar";
                        case "Dean":
                            return "dean";
                        case "Admission":
                            return "admission";
                        case "Cashier":
                            return "cashier";
                        case "Student":
                            // Load full student data
                            StudentDAO studentDAO = new StudentDAO();
                            setLoggedInStudent(studentDAO.findByUsername(username));

                            if (getLoggedInStudent() != null) {
                                session.setAttribute("loggedInStudent", getLoggedInStudent());
                                ec.getSessionMap().put("loggedInStudent", getLoggedInStudent());
                            }
                            return "student";
                        default:
                            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid role", ""));
                            return null;
                    }
                } else {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid password", ""));
                    return null;
                }
            } else {
                System.out.println("User not found in DB: " + username);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found", ""));
                return null;
            }
        } catch (SQLException ex) {
            ex.getMessage();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed: " + ex.getMessage(), ""));
            return null;
        } finally {
            // Inside your LoginBean.java after validating login
            NavigationBean navBean = (NavigationBean) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("navigationBean");

            if (navBean != null) {
                navBean.setRole(this.userRole); // assuming your login bean knows the user role
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ex) {
                    ex.getMessage();
                }
            }
        }

    }
 */
