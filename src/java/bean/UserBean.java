/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.UserDAO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.User;

/**
 *
 * @author hrkas
 */
@ManagedBean
@SessionScoped
public class UserBean {

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;
    private User currentUser = new User(); // loaded during login
    private final UserDAO userDAO = new UserDAO();

    private boolean showUpdateDialog = false;
    private boolean changePassword = false;
    private boolean showCreateDialog;
    private boolean showEditDialog;

    private String currentPassword;
    private String confirmPassword;
    private String newPassword;

    private List<User> userList;

    public UserBean() {
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
     * @return the currentUser
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * @param currentUser the currentUser to set
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * @return the showUpdateDialog
     */
    public boolean isShowUpdateDialog() {
        return showUpdateDialog;
    }

    /**
     * @param showUpdateDialog the showUpdateDialog to set
     */
    public void setShowUpdateDialog(boolean showUpdateDialog) {
        this.showUpdateDialog = showUpdateDialog;
    }

    /**
     * @return the changePassword
     */
    public boolean isChangePassword() {
        return changePassword;
    }

    /**
     * @param changePassword the changePassword to set
     */
    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
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
     * @return the showCreateDialog
     */
    public boolean isShowCreateDialog() {
        return showCreateDialog;
    }

    /**
     * @param showCreateDialog the showCreateDialog to set
     */
    public void setShowCreateDialog(boolean showCreateDialog) {
        this.showCreateDialog = showCreateDialog;
    }

    /**
     * @return the showEditDialog
     */
    public boolean isShowEditDialog() {
        return showEditDialog;
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
     * @param showEditDialog the showEditDialog to set
     */
    public void setShowEditDialog(boolean showEditDialog) {
        this.showEditDialog = showEditDialog;
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

    @PostConstruct
    public void init() {
        setUserList(userDAO.getAllUsers());
    }

    public void prepareNewUser() {
        currentUser = new User();
    }

    public void editUser(User user) {
        this.currentUser = user;
    }

    public void saveUser() {
        boolean success;
        if (!currentUser.getPassword().equals(confirmPassword)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password Mismatch", "Passwords do not match.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        if (currentUser.getId() == 0) {
            success = userDAO.addUser(currentUser);
        } else {
            success = userDAO.updateUser(currentUser);
        }
        currentUser = new User();
        confirmPassword = null;
//        showDialog = false;

        FacesMessage msg = new FacesMessage(success ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR,
                success ? "Success" : "Failed", success ? "User saved." : "Operation failed.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        setUserList(userDAO.getAllUsers());
    }

    public void deleteUser(User user) {
        boolean success = userDAO.deleteUser(user.getId());
        FacesMessage msg = new FacesMessage(success ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR,
                success ? "Deleted" : "Failed", success ? "User deleted." : "Delete failed.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        setUserList(userDAO.getAllUsers());
    }

    public void openUpdateDialog() {
        this.showUpdateDialog = true;
        this.changePassword = false;  // reset toggle
        this.confirmPassword = "";    // clear confirm password
    }

    public void closeUpdateDialog() {
        setShowUpdateDialog(false);
    }

    public void updateAccount() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (changePassword) {
            if (!currentUser.getPassword().equals(confirmPassword)) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Password mismatch", "New password and confirm password do not match."));
                return;
            }
            // You can add password strength validation here if needed
        } else {
            // Keep old password from DB
            String existingPassword = new UserDAO().getUserPasswordById(currentUser.getId());
            currentUser.setPassword(existingPassword);
        }

        boolean success = new UserDAO().updateUser(currentUser);

        if (success) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Success", "Your account has been updated successfully."));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error", "Failed to update your account."));
        }

        this.showUpdateDialog = false;  // Hide dialog
    }

    public void openCreateDialog() {
        currentUser = new User();
        confirmPassword = null;
        setShowCreateDialog(true);
    }

    public void openEditDialog(User user) {
        currentUser = user;
        confirmPassword = user.getPassword(); // optional
        setShowEditDialog(true);
    }

    public void updateUser() {
        try {
            if (currentUser != null) {
                // Optional: validate passwords match
                if (!currentUser.getPassword().equals(confirmPassword)) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match.", null));
                    return;
                }

                // Update the user via DAO
                userDAO.updateUser(currentUser);

                // Refresh user list
                userList = userDAO.getAllUsers();

                // Show success message
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "User updated successfully", null));

                // Clear form
                currentUser = new User(); // or null, if you handle it that way
                confirmPassword = null;
            }
        } catch (Exception ex) {
            ex.getMessage();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating user: " + ex.getMessage(), null));
        }
    }

    public String updatePassword() {
        FacesContext context = FacesContext.getCurrentInstance();
//        LoginBean logInBean = new LoginBean();
        String username = getLoginBean().getUsername();
        // Fetch the user from DB based on the username
        User user = userDAO.getUserByUsername(username); // Replace with your DAO

        if (user == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "User not found.", null));
            return null;
        }

        if (!user.getPassword().equals(currentPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Current password is incorrect.", null));
            return null;
        }

        if (!newPassword.equals(confirmPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "New password and confirm password do not match.", null));
            return null;
        }

        // Update the password
        user.setPassword(getNewPassword()); // ideally, hash the password before saving
        boolean success = userDAO.changeStudentPassword(user);

        if (success) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Password updated successfully!", null));
            clearPasswordFields();
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Failed to update password.", null));
        }

        return null;
    }

    private void clearPasswordFields() {
        setCurrentPassword(null);
        setNewPassword(null);
        confirmPassword = null;
    }

}
