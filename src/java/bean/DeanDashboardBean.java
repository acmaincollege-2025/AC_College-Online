/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.DashboardDAO;
import dao.DeanDashboardDAO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import model.Course;
import model.EnrollmentStats;
import model.Program;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class DeanDashboardBean implements Serializable {

    /**
     * Creates a new instance of DeanDashboardBean
     */
    private List<EnrollmentStats> enrollmentStats;
    private List<Program> programList;
    private List<Course> topCourses;
    private String selectedYear;
    private String selectedSemester;
    private String selectedProgram;
    private int selectedProgramId;
    private List<String> years;
    private BarChartModel enrollmentChart;
    private BarChartModel topCoursesChart;
    private PieChartModel genderDistribution;
    private BarChartModel departmentBreakdown;
    private BarChartModel programStats;
    private BarChartModel yearStats;
    private StreamedContent file;
//    private List<EnrollmentStat> enrollmentStats;

    public DeanDashboardBean() {
    }

    /**
     * @return the topCourses
     */
    public List<Course> getTopCourses() {
        return topCourses;
    }

    /**
     * @param topCourses the topCourses to set
     */
    public void setTopCourses(List<Course> topCourses) {
        this.topCourses = topCourses;
    }

    /**
     * @return the selectedProgram
     */
    public String getSelectedProgram() {
        return selectedProgram;
    }

    /**
     * @param selectedProgram the selectedProgram to set
     */
    public void setSelectedProgram(String selectedProgram) {
        this.selectedProgram = selectedProgram;
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

    /**
     * @return the topCoursesChart
     */
    public BarChartModel getTopCoursesChart() {
        return topCoursesChart;
    }

    /**
     * @param topCoursesChart the topCoursesChart to set
     */
    public void setTopCoursesChart(BarChartModel topCoursesChart) {
        this.topCoursesChart = topCoursesChart;
    }

    /**
     * @return the genderDistribution
     */
    public PieChartModel getGenderDistribution() {
        return genderDistribution;
    }

    /**
     * @param genderDistribution the genderDistribution to set
     */
    public void setGenderDistribution(PieChartModel genderDistribution) {
        this.genderDistribution = genderDistribution;
    }

    /**
     * @return the departmentBreakdown
     */
    public BarChartModel getDepartmentBreakdown() {
        return departmentBreakdown;
    }

    /**
     * @param departmentBreakdown the departmentBreakdown to set
     */
    public void setDepartmentBreakdown(BarChartModel departmentBreakdown) {
        this.departmentBreakdown = departmentBreakdown;
    }

    /**
     * @return the programStats
     */
    public BarChartModel getProgramStats() {
        return programStats;
    }

    /**
     * @param programStats the programStats to set
     */
    public void setProgramStats(BarChartModel programStats) {
        this.programStats = programStats;
    }

    /**
     * @return the yearStats
     */
    public BarChartModel getYearStats() {
        return yearStats;
    }

    /**
     * @param yearStats the yearStats to set
     */
    public void setYearStats(BarChartModel yearStats) {
        this.yearStats = yearStats;
    }

    /**
     * @return the file
     */
    public StreamedContent getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(StreamedContent file) {
        this.file = file;
    }

    @PostConstruct
    public void init() {
        DashboardDAO dao = new DashboardDAO();
        setProgramList(dao.getAllPrograms());
        setYears(dao.getAvailableYears());
        setEnrollmentStats(new ArrayList<>());

        loadGenderDistribution();
        loadDepartmentBreakdown();
        loadProgramStats();
        loadYearStats();
    }

    public void filterStats() {
        setEnrollmentStats(new DashboardDAO().getEnrollmentStats(getSelectedYear(), getSelectedSemester(), getSelectedProgramId()));

        if (getEnrollmentStats() == null || getEnrollmentStats().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "No records found for the selected filters.", ""));
            setEnrollmentChart(null);
            return;
        }

        BarChartModel model = new BarChartModel();
        ChartSeries series = new ChartSeries();
        series.setLabel("Enrolled Students");

        getEnrollmentStats().forEach((stat) -> {
            series.set(stat.getCourseTitle(), stat.getEnrolledCount());
        });

        model.addSeries(series);
        model.setTitle("Enrollment Statistics");
        model.setLegendPosition("ne");
        model.setAnimate(true);
        model.setShowPointLabels(true);

        setEnrollmentChart(model);

        List<EnrollmentStats> topCourse = new DashboardDAO().getTopCourses(getSelectedYear(), getSelectedSemester(), getSelectedProgramId());
        BarChartModel topModel = new BarChartModel();
        ChartSeries topSeries = new ChartSeries();
        topSeries.setLabel("Top 5 Courses");

        topCourse.forEach((stat) -> {
            topSeries.set(stat.getCourseTitle(), stat.getEnrolledCount());
        });
        topModel.addSeries(topSeries);
        topModel.setTitle("Top 5 Most Enrolled Courses");
        topModel.setLegendPosition("ne");
        topModel.setAnimate(true);
        topModel.setShowPointLabels(true);

        setTopCoursesChart(topModel);

    }

    private void loadGenderDistribution() {
        DeanDashboardDAO dao = new DeanDashboardDAO();
        Map<String, Integer> genderStats = dao.getGenderDistribution();

        PieChartModel model = new PieChartModel();
        genderStats.entrySet().forEach((entry) -> {
            model.set(entry.getKey(), entry.getValue());
        });

        model.setTitle("Gender Distribution");
        model.setLegendPosition("e");
        model.setShowDataLabels(true);
        model.setShadow(false);

        this.setGenderDistribution(model);
    }

    private void loadDepartmentBreakdown() {
        DeanDashboardDAO dao = new DeanDashboardDAO();
        Map<String, Integer> deptStats = dao.getDepartmentStats();

        BarChartModel model = new BarChartModel();
        ChartSeries series = new ChartSeries();
        series.setLabel("Students");

        deptStats.entrySet().forEach((entry) -> {
            series.set(entry.getKey(), entry.getValue());
        });

        model.addSeries(series);
        model.setTitle("Department Breakdown");
        model.setLegendPosition("ne");
        model.setAnimate(true);
        model.setShowPointLabels(true);

        this.setDepartmentBreakdown(model);
    }

    private void loadProgramStats() {
        Map<String, Integer> data = new DeanDashboardDAO().getProgramStats();
        ChartSeries series = new ChartSeries();
        series.setLabel("Students");

        data.entrySet().forEach((entry) -> {
            series.set(entry.getKey(), entry.getValue());
        });

        BarChartModel model = new BarChartModel();
        model.addSeries(series);
        model.setTitle("Program-wise Enrollment");
        model.setLegendPosition("ne");
        model.setAnimate(true);
        model.setShowPointLabels(true);

        setProgramStats(model);
    }

    private void loadYearStats() {
        Map<String, Integer> data = new DeanDashboardDAO().getYearStats();
        ChartSeries series = new ChartSeries();
        series.setLabel("Students");

        data.entrySet().forEach((entry) -> {
            series.set(entry.getKey(), entry.getValue());
        });

        BarChartModel model = new BarChartModel();
        model.addSeries(series);
        model.setTitle("Enrollment by Academic Year");
        model.setLegendPosition("ne");
        model.setAnimate(true);
        model.setShowPointLabels(true);

        setYearStats(model);
    }

    public void loadTopCourses() {
        setTopCourses(new ArrayList<>());

        String sql = "SELECT c.course_code, c.course_title, COUNT(e.student_id) AS enrolledCount "
                + "FROM enrollment e "
                + "JOIN course c ON c.course_id = e.course_id "
                + "WHERE e.program_id = ? AND e.year = ? AND e.semester = ? "
                + "GROUP BY c.course_code, c.course_title "
                + "ORDER BY enrolledCount DESC";

        try (Connection con = DatabaseUtil.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, getSelectedProgram());
            ps.setString(2, selectedYear);
            ps.setString(3, selectedSemester);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setCourseCode(rs.getString("course_code"));
                c.setCourseTitle(rs.getString("course_title"));
                c.setEnrolledCount(rs.getInt("enrolledCount")); // Add this field to Course.java
                getTopCourses().add(c);
            }

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public void exportToPDF() {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);

            // Title
            Paragraph title = new Paragraph("Dean Enrollment Dashboard Report", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(10f);
            document.add(title);

            // Filters Info
            document.add(new Paragraph("Academic Year: " + selectedYear));
            document.add(new Paragraph("Semester: " + selectedSemester));
            document.add(new Paragraph("Program: " + selectedProgram));
            document.add(Chunk.NEWLINE);

            // Enrollment Stats Table
            if (enrollmentStats != null && !enrollmentStats.isEmpty()) {
                document.add(new Paragraph("Enrollment Statistics", titleFont));
                PdfPTable table = new PdfPTable(2);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);

                PdfPCell header1 = new PdfPCell(new Phrase("Course Title", headerFont));
                header1.setBackgroundColor(BaseColor.DARK_GRAY);
                table.addCell(header1);

                PdfPCell header2 = new PdfPCell(new Phrase("Enrolled Count", headerFont));
                header2.setBackgroundColor(BaseColor.DARK_GRAY);
                table.addCell(header2);

                for (EnrollmentStats stat : enrollmentStats) {
                    table.addCell(new PdfPCell(new Phrase(stat.getCourseTitle(), cellFont)));
                    table.addCell(new PdfPCell(new Phrase(String.valueOf(stat.getEnrolledCount()), cellFont)));
                }

                document.add(table);
                document.add(Chunk.NEWLINE);
            }

            // Top 5 Courses Table
            if (topCourses != null && !topCourses.isEmpty()) {
                document.add(new Paragraph("Top 5 Most Enrolled Courses", titleFont));
                PdfPTable topTable = new PdfPTable(3);
                topTable.setWidthPercentage(100);
                topTable.setSpacingBefore(10f);

                PdfPCell h1 = new PdfPCell(new Phrase("Course Code", headerFont));
                h1.setBackgroundColor(BaseColor.DARK_GRAY);
                topTable.addCell(h1);

                PdfPCell h2 = new PdfPCell(new Phrase("Course Title", headerFont));
                h2.setBackgroundColor(BaseColor.DARK_GRAY);
                topTable.addCell(h2);

                PdfPCell h3 = new PdfPCell(new Phrase("Enrolled", headerFont));
                h3.setBackgroundColor(BaseColor.DARK_GRAY);
                topTable.addCell(h3);

                for (Course course : topCourses) {
                    topTable.addCell(new PdfPCell(new Phrase(course.getCourseCode(), cellFont)));
                    topTable.addCell(new PdfPCell(new Phrase(course.getCourseTitle(), cellFont)));
                    topTable.addCell(new PdfPCell(new Phrase(String.valueOf(course.getEnrolledCount()), cellFont)));
                }

                document.add(topTable);
            }

            document.close();

            // Prepare response for download
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=\"Dean_Dashboard_Report.pdf\"");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            context.responseComplete();

        } catch (DocumentException | IOException ex) {
            ex.getMessage();
        }
    }

    

    public String getProgramNameByCode(String code) {
        for (Program p : programList) {
            if (p.getProgram_code().equals(code)) {
                return p.getProgram_name();
            }
        }
        return "";
    }

}
/*
public void exportStatisticsToPDF() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            // Add Title
            document.add(new Paragraph("Enrollment Statistics"));
            document.add(new Paragraph("Year: " + selectedYear + " | Semester: " + selectedSemester));
            document.add(new Paragraph("Program: " + getProgramNameByCode(String.format("%s", selectedProgramId))));
            document.add(Chunk.NEWLINE);

            // Chart Placeholder (Optional: Export as image from JSF if implemented)
            document.add(new Paragraph("Chart view is available on the dashboard."));

            // Add Table
            PdfPTable table = new PdfPTable(3); // 3 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell h1 = new PdfPCell(new Phrase("Course Code", headFont));
            PdfPCell h2 = new PdfPCell(new Phrase("Course Title", headFont));
            PdfPCell h3 = new PdfPCell(new Phrase("Enrolled Count", headFont));
            table.addCell(h1);
            table.addCell(h2);
            table.addCell(h3);

            for (EnrollmentStat stat : enrollmentStats) {
                table.addCell(stat.getCourseCode());
                table.addCell(stat.getCourseTitle());
                table.addCell(String.valueOf(stat.getEnrolledCount()));
            }

            document.add(table);
            document.close();

            InputStream stream = new ByteArrayInputStream(out.toByteArray());
            setFile(DefaultStreamedContent.builder()
                    .name("Enrollment-Report.pdf")
                    .contentType("application/pdf")
                    .stream(() -> stream)
                    .build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/