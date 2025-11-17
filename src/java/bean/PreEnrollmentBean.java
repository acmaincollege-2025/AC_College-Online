/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.PreEnrollmentDAO;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.PreEnrollmentRequest;
import model.Student;
import util.SessionUtils;
//import org.apache.catalina.manager.util.SessionUtils;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class PreEnrollmentBean implements Serializable{

    /**
     * Creates a new instance of PreEnrollmentBean
     */
    private PreEnrollmentRequest request = new PreEnrollmentRequest();
    private PreEnrollmentDAO dao = new PreEnrollmentDAO();

    private Student currentStudent;
    
    public PreEnrollmentBean() {
    }

    /**
     * @return the request
     */
    public PreEnrollmentRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(PreEnrollmentRequest request) {
        this.request = request;
    }

    /**
     * @return the dao
     */
    public PreEnrollmentDAO getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(PreEnrollmentDAO dao) {
        this.dao = dao;
    }

    /**
     * @return the currentStudent
     */
    public Student getCurrentStudent() {
        return currentStudent;
    }

    /**
     * @param currentStudent the currentStudent to set
     */
    public void setCurrentStudent(Student currentStudent) {
        this.currentStudent = currentStudent;
    }
     @PostConstruct
    public void init() {
        setCurrentStudent(SessionUtils.getLoggedInStudent());
        getRequest().setStudno(getCurrentStudent().getStudno());
    }

    public void submitRequest() {
        boolean submitted = getDao().submitRequest(getRequest());
        if (submitted) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Pre-enrollment request submitted. Please wait for approval."));
            setRequest(new PreEnrollmentRequest()); // reset
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Submission failed", null));
        }
    }
}
