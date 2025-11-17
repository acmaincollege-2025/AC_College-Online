/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.COEDAO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.COERequest;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class COEBean {

    /**
     * Creates a new instance of COEBean
     */
    private String studentId;
    private List<COERequest> coeRequests;
    private final COEDAO coeDAO = new COEDAO();

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    public COEBean() {
    }

    /**
     * @return the studentId
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the coeRequests
     */
    public List<COERequest> getCoeRequests() {
        return coeRequests;
    }

    /**
     * @param coeRequests the coeRequests to set
     */
    public void setCoeRequests(List<COERequest> coeRequests) {
        this.coeRequests = coeRequests;
    }

    @PostConstruct
    public void init() {
        if (loginBean != null && loginBean.getLoggedInStudent() != null) {
            setStudentId(loginBean.getLoggedInStudent().getStudno());
            setCoeRequests(coeDAO.getRequestsByStudent(getStudentId()));
        }
    }

    public void requestCOE() {
        boolean success = coeDAO.requestCOE(getStudentId());
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Certificate of Enrollment requested."));
            setCoeRequests(coeDAO.getRequestsByStudent(getStudentId())); // refresh list
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to request COE", null));
        }
    }
}
