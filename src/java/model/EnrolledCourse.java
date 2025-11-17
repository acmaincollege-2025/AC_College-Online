/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author hrkas
 */
public class EnrolledCourse {
    private int enrolled_course_id;
    private int enrollment_id;
    private int course_id;
    private String course_code;
    private String course_description;
    private int units;
    private String grade;
    private String remarks;

    public EnrolledCourse() {
    }

    public EnrolledCourse(int enrolled_course_id, int enrollment_id, int course_id, String course_code, String course_description, int units, String grade, String remarks) {
        this.enrolled_course_id = enrolled_course_id;
        this.enrollment_id = enrollment_id;
        this.course_id = course_id;
        this.course_code = course_code;
        this.course_description = course_description;
        this.units = units;
        this.grade = grade;
        this.remarks = remarks;
    }

    /**
     * @return the enrolled_course_id
     */
    public int getEnrolled_course_id() {
        return enrolled_course_id;
    }

    /**
     * @param enrolled_course_id the enrolled_course_id to set
     */
    public void setEnrolled_course_id(int enrolled_course_id) {
        this.enrolled_course_id = enrolled_course_id;
    }

    /**
     * @return the enrollment_id
     */
    public int getEnrollment_id() {
        return enrollment_id;
    }

    /**
     * @param enrollment_id the enrollment_id to set
     */
    public void setEnrollment_id(int enrollment_id) {
        this.enrollment_id = enrollment_id;
    }

    /**
     * @return the course_id
     */
    public int getCourse_id() {
        return course_id;
    }

    /**
     * @param course_id the course_id to set
     */
    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    /**
     * @return the course_code
     */
    public String getCourse_code() {
        return course_code;
    }

    /**
     * @param course_code the course_code to set
     */
    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    /**
     * @return the course_description
     */
    public String getCourse_description() {
        return course_description;
    }

    /**
     * @param course_description the course_description to set
     */
    public void setCourse_description(String course_description) {
        this.course_description = course_description;
    }

    /**
     * @return the units
     */
    public int getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(int units) {
        this.units = units;
    }

    /**
     * @return the grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    
}
