/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.StudentCredentialsDAO;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.StudentCredential;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class StudentCredentialsBean implements Serializable {

    /**
     * Creates a new instance of StudentCredentialsBean
     */
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    private StudentCredentialsDAO credentialsDAO = new StudentCredentialsDAO();

    public StudentCredentialsBean() {
    }

    public String changePassword() {
        FacesContext context = FacesContext.getCurrentInstance();
        LoginBean loginBean = (LoginBean) context.getExternalContext()
                .getSessionMap().get("loginBean");

        if (loginBean == null || loginBean.getStudentNo() == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "User not logged in.", null));
            return null;
        }

        String studentNo = loginBean.getStudentNo();

        // Fetch current credentials from DB
        StudentCredential cred = getCredentialsDAO().authenticate(studentNo, getCurrentPassword());

        if (cred == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Current password is incorrect.", null));
            return null;
        }

        if (!newPassword.equals(confirmPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "New passwords do not match.", null));
            return null;
        }

        boolean updated = getCredentialsDAO().changePassword(studentNo, getNewPassword());
        if (updated) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Password changed successfully.", null));
            clearFields();
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Password change failed.", null));
        }

        return null;
    }

    private void clearFields() {
        setCurrentPassword(null);
        setNewPassword(null);
        setConfirmPassword(null);
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
     * @return the credentialsDAO
     */
    public StudentCredentialsDAO getCredentialsDAO() {
        return credentialsDAO;
    }

    /**
     * @param credentialsDAO the credentialsDAO to set
     */
    public void setCredentialsDAO(StudentCredentialsDAO credentialsDAO) {
        this.credentialsDAO = credentialsDAO;
    }
    
}
