/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.EnrollmentDAO;
import dao.StudentCredentialsDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class StudentSignUpBean implements Serializable{

    /**
     * Creates a new instance of StudentSignUpBean
     */
    private String studentNo;
    private String password;
    private String confirmPassword;

    private final StudentCredentialsDAO credentialsDAO = new StudentCredentialsDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    public StudentSignUpBean() {
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

    // Main Sign-Up Method
    public String register() {
        FacesContext context = FacesContext.getCurrentInstance();

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Passwords do not match.", null));
            return null;
        }

        // Check if student already has credentials
        if (credentialsDAO.isStudentRegistered(getStudentNo())) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "An account already exists for this student number.", null));
            return null;
        }

        // Check if student is enrolled and approved
        if (!enrollmentDAO.isStudentCurrentlyEnrolled(studentNo)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Student is not currently enrolled or enrollment is not approved.", null));
            return null;
        }

        // Save credentials
        boolean success = credentialsDAO.registerStudentCredentials(getStudentNo(), getPassword());
        if (success) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Registration successful. You may now log in.", null));
            clearForm();
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Registration failed. Please try again.", null));
        }

        return null;
    }

    public void clearForm() {
        setStudentNo(null);
        setPassword(null);
        setConfirmPassword(null);
       
        
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(StudentSignUpBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
   public String cancelRegister(){
       return "index.xhtml?faces-redirect=true";
   }
}
