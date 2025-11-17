/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.EnrollmentPeriodDAO;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.EnrollmentPeriod;

/**
 *
 * @author hrkas
 */
@ManagedBean
@SessionScoped
public class EnrollmentPeriodBean {

    /**
     * Creates a new instance of EnrollmentPeriodBean
     */
    private EnrollmentPeriod currentPeriod = new EnrollmentPeriod();
    private final EnrollmentPeriodDAO dao = new EnrollmentPeriodDAO();

    private String academicYear;
    private String semester;
    private boolean enrollmentOpen;

    public EnrollmentPeriodBean() {
    }

    /**
     * @return the currentPeriod
     */
    public EnrollmentPeriod getCurrentPeriod() {
        return currentPeriod;
    }

    /**
     * @param currentPeriod the currentPeriod to set
     */
    public void setCurrentPeriod(EnrollmentPeriod currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    /**
     * @return the academicYear
     */
    public String getAcademicYear() {
        return academicYear;
    }

    /**
     * @param academicYear the academicYear to set
     */
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    /**
     * @return the semester
     */
    public String getSemester() {
        return semester;
    }

    /**
     * @param semester the semester to set
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * @return the enrollmentOpen
     */
    public boolean isEnrollmentOpen() {
        return enrollmentOpen;
    }

    /**
     * @param enrollmentOpen the enrollmentOpen to set
     */
    public void setEnrollmentOpen(boolean enrollmentOpen) {
        this.enrollmentOpen = enrollmentOpen;
    }

    @PostConstruct
    public void init() {
        currentPeriod = new EnrollmentPeriodDAO().getCurrentPeriod();
        setEnrollmentOpen(dao.openEnrollment(getAcademicYear(), getSemester()));
    }

    public void openEnrollment() {
        if (dao.openEnrollment(getAcademicYear(), getSemester())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Enrollment opened."));
            init();
        }
    }

    public void closeEnrollment() {
        if (getCurrentPeriod() != null && dao.closeEnrollment(getCurrentPeriod().getId())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Enrollment closed."));
            init();
        }
    }

}
