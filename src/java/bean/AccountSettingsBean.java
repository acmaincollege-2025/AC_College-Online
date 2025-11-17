/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.UserDAO;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class AccountSettingsBean implements Serializable {

    /**
     * Creates a new instance of AccountSettingsBean
     */
    private int userId;
    private String firstName;
    private String lastName;
    private String username;

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    private boolean editUsername;
    private boolean editFirstName;
    private boolean editLastName;
    private boolean hasEditSelection;
    private boolean updateSuccess;

    private User user;
    private final UserDAO userDAO = new UserDAO();

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    public AccountSettingsBean() {
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
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * @return the currentPassword
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * @param currentPassword the currentPassword to set
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @param confirmPassword the confirmPassword to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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
     * @return the loginBean
     */
    public LoginBean getLoginBean() {
        return loginBean;
    }

    /**
     * @param loginBean the loginBean to set
     */
    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    /**
     * @return the editUsername
     */
    public boolean isEditUsername() {
        return editUsername;
    }

    /**
     * @param editUsername the editUsername to set
     */
    public void setEditUsername(boolean editUsername) {
        this.editUsername = editUsername;
    }

    /**
     * @return the editFirstName
     */
    public boolean isEditFirstName() {
        return editFirstName;
    }

    /**
     * @param editFirstName the editFirstName to set
     */
    public void setEditFirstName(boolean editFirstName) {
        this.editFirstName = editFirstName;
    }

    /**
     * @return the editLastName
     */
    public boolean isEditLastName() {
        return editLastName;
    }

    /**
     * @param editLastName the editLastName to set
     */
    public void setEditLastName(boolean editLastName) {
        this.editLastName = editLastName;
    }

    /**
     * @return the hasEditSelection
     */
    public boolean isHasEditSelection() {
        return hasEditSelection;
    }

    /**
     * @param hasEditSelection the hasEditSelection to set
     */
    public void setHasEditSelection(boolean hasEditSelection) {
        this.hasEditSelection = hasEditSelection;
    }

    /**
     * @return the updateSuccess
     */
    public boolean isUpdateSuccess() {
        return updateSuccess;
    }

    /**
     * @param updateSuccess the updateSuccess to set
     */
    public void setUpdateSuccess(boolean updateSuccess) {
        this.updateSuccess = updateSuccess;
    }

    @PostConstruct
    public void init() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);

        if (session != null) {
            Object uid = session.getAttribute("userId");
            if (uid != null) {
                this.userId = (int) uid;
                loadUserData();
            }
        }
    }

    public void loadUserData() {
        UserDAO dao = new UserDAO();
        User user = dao.getUserById(userId);
        if (user != null) {
            this.username = user.getUsername();
            this.firstName = user.getFirstname();
            this.lastName = user.getLastname();
        }
    }

    public void prepare() {
        setUser((User) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("user"));
        if (getUser() != null) {
            this.setFirstName(getUser().getFirstname());
            this.setLastName(getUser().getLastname());
            this.setUsername(getUser().getUsername());
        }
    }

    public void updateInfo() {
        try {
            if (!hasEditSelection) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "No fields selected for update.", null));
                return;
            }

            userId = loginBean.getUserId();
            user = userDAO.getUserById(userId);

            if (user == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found.", null));
                return;
            }

            if (editUsername) {
                user.setUsername(username);
            }
            if (editFirstName) {
                user.setFirstname(firstName);
            }
            if (editLastName) {
                user.setLastname(lastName);
            }

            boolean success = userDAO.updateInfo(user);
            if (success) {
                setUpdateSuccess(true);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Account info updated successfully.", null));
            } else {
                setUpdateSuccess(false);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update account info.", null));
            }

        } catch (Exception ex) {
            setUpdateSuccess(false);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating account info.", null));
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    public void changePassword() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            // Validate matching
            if (!newPassword.equals(confirmPassword)) {
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "New password and confirmation do not match.", null));
                return;
            }

            // Validate strength (at least 8 chars, one uppercase, one digit)
            if (!newPassword.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$")) {
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Password must be at least 8 characters long, include one uppercase letter and one number.", null));
                return;
            }

            User user = userDAO.getUserById(userId);

            if (user == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found.", null));
                return;
            }

            // Check current password
            if (!user.getPassword().equals(currentPassword)) {
                context.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Current password is incorrect.", null));
                return;
            }

            // All checks passed, update password
            user.setPassword(newPassword);
            userDAO.updateInfo(user);

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully!", null));

            // Reset fields
            currentPassword = "";
            newPassword = "";
            confirmPassword = "";

        } catch (Exception ex) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error changing password.", null));
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    public void updateEditState() {
        hasEditSelection = editUsername || editFirstName || editLastName;
    }

    public void resetForm() {
        editUsername = false;
        editFirstName = false;
        editLastName = false;
        hasEditSelection = false;
        currentPassword = "";
        newPassword = "";
        confirmPassword = "";
    }

}
