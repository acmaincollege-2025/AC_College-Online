/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.DashboardDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartOptions;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class AdmissionDashboardBean implements Serializable {

    /**
     * Creates a new instance of AdmissionDashboardBean
     */
    private int totalApplicants;
    private int pendingVerifications;
    private BarChartModel programChart;

    public AdmissionDashboardBean() {
    }

    /**
     * @return the totalApplicants
     */
    public int getTotalApplicants() {
        return totalApplicants;
    }

    /**
     * @param totalApplicants the totalApplicants to set
     */
    public void setTotalApplicants(int totalApplicants) {
        this.totalApplicants = totalApplicants;
    }

    /**
     * @return the pendingVerifications
     */
    public int getPendingVerifications() {
        return pendingVerifications;
    }

    /**
     * @param pendingVerifications the pendingVerifications to set
     */
    public void setPendingVerifications(int pendingVerifications) {
        this.pendingVerifications = pendingVerifications;
    }

    /**
     * @return the programChart
     */
    public BarChartModel getProgramChart() {
        return programChart;
    }

    /**
     * @param programChart the programChart to set
     */
    public void setProgramChart(BarChartModel programChart) {
        this.programChart = programChart;
    }

    @PostConstruct
    public void init() {
        DashboardDAO dao = new DashboardDAO();
        setTotalApplicants(dao.getTotalApplicants());
        setPendingVerifications(dao.getPendingVerifications());
        createBarModel(dao.getApplicantsByProgram());
    }

    private void createBarModel(Map<String, Integer> data) {
        programChart = new BarChartModel();
        ChartData chartData = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Applicants");
        barDataSet.setData(new ArrayList<>(data.values()));
        barDataSet.setBackgroundColor("rgba(153, 0, 0, 0.8)");

        chartData.addChartDataSet(barDataSet);
        chartData.setLabels(new ArrayList<>(data.keySet()));

//        programChart.setData(chartData);
//        programChart.setOptions(new BarChartOptions());
    }

    private void loadProgramChart(Map<String, Integer> data) {
        BarChartModel barModel = new BarChartModel();
        ChartSeries series = new ChartSeries();
        series.setLabel("Applicants");

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            series.set(entry.getKey(), entry.getValue());
        }

        barModel.addSeries(series);
        barModel.setTitle("Applicants by Program");
        barModel.setLegendPosition("ne");
        barModel.setAnimate(true);
        barModel.setShowPointLabels(true);
        barModel.getAxis(AxisType.Y).setLabel("No. of Applicants");
        barModel.getAxis(AxisType.Y).setMin(0); // optional
        barModel.getAxis(AxisType.Y).setMax(20); // adjust as needed

        this.programChart = barModel;
    }

}
