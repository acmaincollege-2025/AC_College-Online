/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.CourseDAO;
import dao.EnrollmentDAO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Course;
import model.EnrollmentCourse;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class RegistrarEnrollmentBean {

    /**
     * Creates a new instance of RegistrarEnrollmentBean
     */
    private int enrollmentId;
    private List<EnrollmentCourse> enrolledCourses;
    private List<Course> availableCourses;
    private int selectedCourseId;

    public RegistrarEnrollmentBean() {
    }

    /**
     * @return the enrollmentId
     */
    public int getEnrollmentId() {
        return enrollmentId;
    }

    /**
     * @param enrollmentId the enrollmentId to set
     */
    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    /**
     * @return the enrolledCourses
     */
    public List<EnrollmentCourse> getEnrolledCourses() {
        return enrolledCourses;
    }

    /**
     * @param enrolledCourses the enrolledCourses to set
     */
    public void setEnrolledCourses(List<EnrollmentCourse> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    /**
     * @return the availableCourses
     */
    public List<Course> getAvailableCourses() {
        return availableCourses;
    }

    /**
     * @param availableCourses the availableCourses to set
     */
    public void setAvailableCourses(List<Course> availableCourses) {
        this.availableCourses = availableCourses;
    }

    /**
     * @return the selectedCourseId
     */
    public int getSelectedCourseId() {
        return selectedCourseId;
    }

    /**
     * @param selectedCourseId the selectedCourseId to set
     */
    public void setSelectedCourseId(int selectedCourseId) {
        this.selectedCourseId = selectedCourseId;
    }

    @PostConstruct
    public void init() {
        // populate enrollmentId from UI or selection
        setEnrolledCourses(new EnrollmentDAO().getEnrolledCoursesByEnrollmentId(getEnrollmentId()));
        setAvailableCourses(new CourseDAO().getAllCourses());
    }

    public void addCourse() {
        if (new EnrollmentDAO().addCourseToEnrollment(getEnrollmentId(), getSelectedCourseId())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Course added."));
            setEnrolledCourses(new EnrollmentDAO().getEnrolledCoursesByEnrollmentId(getEnrollmentId()));
        }
    }

    public void dropCourse(int enrollmentCourseId) {
        if (new EnrollmentDAO().dropCourseFromEnrollment(enrollmentCourseId)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Course dropped."));
            setEnrolledCourses(new EnrollmentDAO().getEnrolledCoursesByEnrollmentId(getEnrollmentId()));
        }
    }


}
