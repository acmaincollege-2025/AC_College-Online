/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.DashboardDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.EnrollmentStats;
import model.Program;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class DashboardBean implements Serializable {

    /**
     * Creates a new instance of DashboardBean
     */
    private List<EnrollmentStats> enrollmentStats;
    private List<Program> programList;
    private String selectedYear;
    private String selectedSemester;
    private int selectedProgramId;

    private List<String> years;

    private BarChartModel enrollmentChart;

    public DashboardBean() {
    }

    /**
     * @return the enrollmentStats
     */
    public List<EnrollmentStats> getEnrollmentStats() {
        return enrollmentStats;
    }

    /**
     * @param enrollmentStats the enrollmentStats to set
     */
    public void setEnrollmentStats(List<EnrollmentStats> enrollmentStats) {
        this.enrollmentStats = enrollmentStats;
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
     * @return the selectedProgramId
     */
    public int getSelectedProgramId() {
        return selectedProgramId;
    }

    /**
     * @param selectedProgramId the selectedProgramId to set
     */
    public void setSelectedProgramId(int selectedProgramId) {
        this.selectedProgramId = selectedProgramId;
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
     * @return the enrollmentChart
     */
    public BarChartModel getEnrollmentChart() {
        return enrollmentChart;
    }

    /**
     * @param enrollmentChart the enrollmentChart to set
     */
    public void setEnrollmentChart(BarChartModel enrollmentChart) {
        this.enrollmentChart = enrollmentChart;
    }

    @PostConstruct
    public void init() {
        setProgramList(new DashboardDAO().getAllPrograms());
        setYears(new DashboardDAO().getAvailableYears());
        setEnrollmentStats(new ArrayList<>());
    }

    public void filterStats() {
        List<EnrollmentStats> result = new DashboardDAO().getEnrollmentStats(getSelectedYear(), getSelectedSemester(), getSelectedProgramId());
        setEnrollmentStats(result);

        if (result == null || result.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "No records found for the selected filters.", ""));
            setEnrollmentChart(null);
            return;
        }

        BarChartModel model = new BarChartModel();
        ChartSeries series = new ChartSeries();
        series.setLabel("Enrolled Students");

        for (EnrollmentStats stat : result) {
            series.set(stat.getCourseTitle(), stat.getEnrolledCount());
        }

        model.addSeries(series);
        model.setTitle("Enrollment Statistics");
        model.setLegendPosition("ne");
        model.setAnimate(true);
        model.setShowPointLabels(true);

        setEnrollmentChart(model);
    }

}
