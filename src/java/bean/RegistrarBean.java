/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.EnrollmentDAO;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Enrollment;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class RegistrarBean implements Serializable {

    /**
     * Creates a new instance of RegistrarBean
     */
    private List<Enrollment> pendingEnrollments;
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    public RegistrarBean() {
    }

    /**
     * @return the pendingEnrollments
     */
    public List<Enrollment> getPendingEnrollments() {
        return pendingEnrollments;
    }

    /**
     * @param pendingEnrollments the pendingEnrollments to set
     */
    public void setPendingEnrollments(List<Enrollment> pendingEnrollments) {
        this.pendingEnrollments = pendingEnrollments;
    }

    @PostConstruct
    public void init() {
        loadPendingEnrollments();
    }

    public void loadPendingEnrollments() {
        setPendingEnrollments(enrollmentDAO.getPendingEnrollments());
    }

    public void approve(Enrollment enrollment) {
        boolean success = enrollmentDAO.approveEnrollment(enrollment.getEnrollment_id());
        boolean sc = new EnrollmentDAO().updateCourseProgressStatus(enrollment.getStudno(), enrollment.getCourseCode(), "Completed");

        if (success) {
            if (sc) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Enrollment approved."));
            }

            loadPendingEnrollments(); // refresh
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Approval failed", null));
        }
    }

}
