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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class UserProfileBean implements Serializable {

    /**
     * Creates a new instance of UserProfileBean
     */
    private User currentUser;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    private final UserDAO userDAO = new UserDAO();

    public UserProfileBean() {
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

    @PostConstruct
    public void init() {
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
        if (username != null) {
            currentUser = userDAO.getUserByUsername(username);
        } else {
            currentUser = new User(); // fallback if not logged in
        }
    }

    public void updateProfile() {
        if (userDAO.updateInfo(currentUser)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Profile updated successfully"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update failed", ""));
        }
    }

    public void changePassword() {
        String existingHash = userDAO.getUserPasswordById(currentUser.getId());

        if (!BCrypt.checkpw(currentPassword, existingHash)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Current password is incorrect", ""));
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match", ""));
            return;
        }

        currentUser.setPassword(newPassword);
        if (userDAO.changePassword(currentUser)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Password changed successfully"));
            currentPassword = newPassword = confirmPassword = null;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update password", ""));
        }
    }

    public String goBackToDashboard() {
        String role = currentUser.getRole();

        switch (role) {
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
