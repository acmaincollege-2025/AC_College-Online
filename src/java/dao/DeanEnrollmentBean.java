/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.EnrollmentRecord;
import model.Program;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class DeanEnrollmentBean {

    /**
     * Creates a new instance of DeanEnrollmentBean
     */
    private String selectedProgramId;
    private String selectedYear;
    private String selectedSemester;

    private List<Program> programList;
    private List<String> years;
    private List<EnrollmentRecord> enrollmentList;

    public DeanEnrollmentBean() {
    }

    /**
     * @return the selectedProgramId
     */
    public String getSelectedProgramId() {
        return selectedProgramId;
    }

    /**
     * @param selectedProgramId the selectedProgramId to set
     */
    public void setSelectedProgramId(String selectedProgramId) {
        this.selectedProgramId = selectedProgramId;
    }

    /**
     * @return the selectedYear
     */
    public String getSelectedYear() {
        return selectedYear;
    }

    /**
     * @param selectedYear the selectedYear to set
     */
    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }

    /**
     * @return the selectedSemester
     */
    public String getSelectedSemester() {
        return selectedSemester;
    }

    /**
     * @param selectedSemester the selectedSemester to set
     */
    public void setSelectedSemester(String selectedSemester) {
        this.selectedSemester = selectedSemester;
    }

    /**
     * @return the programList
     */
    public List<Program> getProgramList() {
        return programList;
    }

    /**
     * @param programList the programList to set
     */
    public void setProgramList(List<Program> programList) {
        this.programList = programList;
    }

    /**
     * @return the years
     */
    public List<String> getYears() {
        return years;
    }

    /**
     * @param years the years to set
     */
    public void setYears(List<String> years) {
        this.years = years;
    }

    /**
     * @return the enrollmentList
     */
    public List<EnrollmentRecord> getEnrollmentList() {
        return enrollmentList;
    }

    /**
     * @param enrollmentList the enrollmentList to set
     */
    public void setEnrollmentList(List<EnrollmentRecord> enrollmentList) {
        this.enrollmentList = enrollmentList;
    }

    @PostConstruct
    public void init() {
        ProgramDAO dao = new ProgramDAO();
        programList = dao.getAllPrograms();

        setYears(Arrays.asList("2022", "2023", "2024", "2025"));
    }

    public void loadEnrollmentList() {
        setEnrollmentList(EnrollmentDAO.getEnrollmentList(getSelectedProgramId(), getSelectedYear(), getSelectedSemester()));
    }
}
