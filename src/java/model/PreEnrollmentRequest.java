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
public class PreEnrollmentRequest {
    private int id;
    private String studno;
    private String academic_year;
    private String semester;
    private String request_date;
    private String status;
    private String remarks;

    public PreEnrollmentRequest() {
    }

    public PreEnrollmentRequest(int id, String studno, String academic_year, String semester, String request_date, String status, String remarks) {
        this.id = id;
        this.studno = studno;
        this.academic_year = academic_year;
        this.semester = semester;
        this.request_date = request_date;
        this.status = status;
        this.remarks = remarks;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the studno
     */
    public String getStudno() {
        return studno;
    }

    /**
     * @param studno the studno to set
     */
    public void setStudno(String studno) {
        this.studno = studno;
    }

    /**
     * @return the academic_year
     */
    public String getAcademic_year() {
        return academic_year;
    }

    /**
     * @param academic_year the academic_year to set
     */
    public void setAcademic_year(String academic_year) {
        this.academic_year = academic_year;
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
     * @return the request_date
     */
    public String getRequest_date() {
        return request_date;
    }

    /**
     * @param request_date the request_date to set
     */
    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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
