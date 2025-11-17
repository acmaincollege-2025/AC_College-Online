/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.CourseDAO;
import dao.EnrollmentDAO;
import dao.ProgramDAO;
import dao.StudentDAO;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import model.Course;
import model.CourseProgress;
import model.Enrollment;
import model.Program;
import model.Student;
import util.SessionUtils;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class StudentPortalBean {

    /**
     * Creates a new instance of StudentPortalBean
     */
    private Enrollment enrollment = new Enrollment();
    private List<Enrollment> myEnrollments;
    private String loggedStudno;

    private final StudentDAO studentDAO = new StudentDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final ProgramDAO programDAO = new ProgramDAO();
    private Student selectedStudent;

    public StudentPortalBean() {
    }

    /**
     * @return the enrollment
     */
    public Enrollment getEnrollment() {
        return enrollment;
    }

    /**
     * @param enrollment the enrollment to set
     */
    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    /**
     * @return the myEnrollments
     */
    public List<Enrollment> getMyEnrollments() {
        return myEnrollments;
    }

    /**
     * @param myEnrollments the myEnrollments to set
     */
    public void setMyEnrollments(List<Enrollment> myEnrollments) {
        this.myEnrollments = myEnrollments;
    }

    /**
     * @return the loggedStudno
     */
    public String getLoggedStudno() {
        return loggedStudno;
    }

    /**
     * @param loggedStudno the loggedStudno to set
     */
    public void setLoggedStudno(String loggedStudno) {
        this.loggedStudno = loggedStudno;
    }

    @PostConstruct
    public void init() {
        // Fetch from session if available
//        setLoggedStudno(FacesContext.getCurrentInstance().getExternalContext()
//                .getSessionMap().get("studno").toString());
//
//        setMyEnrollments(new EnrollmentDAO().listEnrollmentsByStudno(getLoggedStudno()));
    }

    public void submitEnrollment() {
        try {
            Student currentStudent = studentDAO.findByStudentNo(SessionUtils.getStudentNo());

            if (currentStudent == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Student not found in session.", null));
                return;
            }

            // 1. Check for deficiencies
            if (enrollmentDAO.hasDeficiencies(currentStudent.getStudno())) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Enrollment denied", "You have outstanding course deficiencies."));
                return;
            }

            // 2. Create new enrollment record
            Enrollment e = new Enrollment();
            e.setStudno(currentStudent.getStudno());
            e.setAcademic_year(enrollment.getAcademic_year());
            e.setSemester(enrollment.getSemester());
            e.setStatus("Pending Approval");
            e.setPayment_status("pending");
            int enrollmentId = enrollmentDAO.saveEnrollment(e);

            if (enrollmentId <= 0) {
                throw new Exception("Failed to create enrollment record.");
            }

            // 3. Load regular courses for the student's program and semester
            List<Course> regularCourses = courseDAO.findRegularCourses(currentStudent.getProgramId(), enrollment.getSemester());
            if (regularCourses != null && !regularCourses.isEmpty()) {
                for (Course course : regularCourses) {
                    enrollmentDAO.addEnrollmentDetail(enrollmentId, course.getCourseCode(), (int) course.getUnits());
                }
            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Enrollment submitted for approval."));

            // Reload the student’s enrollment list
            this.myEnrollments = enrollmentDAO.findByStudentNo(currentStudent.getStudno());

        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enrollment failed: " + ex.getMessage(), null));
        }
    }

    public void generateProgressPdf() {
        try {
            Program program = new Program();

            List<CourseProgress> courseProgressList = new ArrayList<>();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

            program = programDAO.getProgramById(selectedStudent.getProgramId());

            Document document = new Document();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=student_progress.pdf");
            OutputStream out = response.getOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Student Progress Report"));
            document.add(new Paragraph("Name: " + selectedStudent.getLastname() + ", " + selectedStudent.getFirstname()));
            document.add(new Paragraph("Program: " + program.getProgram_name()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2); // Course and Status
            table.addCell("Course");
            table.addCell("Status");

            courseProgressList.stream().map((cp) -> {
                table.addCell(cp.getCourse().getCourseTitle());
                return cp;
            }).forEachOrdered((cp) -> {
                table.addCell(cp.getStatus());
            });

            document.add(table);
            document.close();
            facesContext.responseComplete();

        } catch (DocumentException | IOException ex) {
            ex.getMessage();
        }
    }
}
