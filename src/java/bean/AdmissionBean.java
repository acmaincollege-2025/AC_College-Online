package bean;

import dao.EnrollmentDAO;
import dao.StudentDAO;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Enrollment;
import model.Student;
import util.EmailUtil;

/**
 * AdmissionBean — Handles admission workflow and enrollment generation.
 * Supports approval, rejection, and document requests for applicants.
 *
 * Author: hrkas (refactored)
 */
@ManagedBean
@ViewScoped
public class AdmissionBean implements Serializable {

    private Student student = new Student();
    private List<Student> students;
    private List<Student> pendingStudents;
    private Student selectedStudent;
    private Enrollment enrollment = new Enrollment();
    private String academicYear;
    private String semester;
    private String remarks;

    private final StudentDAO studentDAO = new StudentDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    @PostConstruct
    public void init() {
        loadStudents();
        loadPendingStudents();
    }

    /**
     * Load all students.
     */
    public void loadStudents() {
        students = studentDAO.listAllStudents();
    }

    /**
     * Load students pending for admission.
     */
    /**
     * Load only pending students (e.g., those awaiting enrollment approval)
     */
    public void loadPendingStudents() {
//        StudentDAO studentDAO = new StudentDAO();
        setPendingStudents(studentDAO.listPendingStudents());
    }

    /**
     * Register new student and auto-create enrollment record.
     */
    public void saveStudent() {
        if (studentDAO.addStudent(student)) {
            enrollment.setStudno(student.getStudno());
            enrollment.setAcademic_year(academicYear);
            enrollment.setSemester(semester);
            enrollmentDAO.createEnrollment(enrollment);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Student registered and enrollment initiated.", null));
            student = new Student();
            loadStudents();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error", "Failed to register student."));
        }
    }

    /**
     * Approve student application and generate enrollment record.
     *
     * @param student
     */
    public void approveStudent(Student student) {
        try {
            student.setAdmissionStatus("Approved");
            student.setRemarks(null);
            studentDAO.updateAdmissionStatus(student);

            // Auto-create enrollment
            Enrollment e = new Enrollment();
            e.setStudno(student.getStudno());
            e.setAcademic_year(academicYear);
            e.setSemester(semester);
            e.setYearLevel(enrollmentDAO.determineYearLevel(student.getStudno()));
            enrollmentDAO.createEnrollment(e);

            // Send email (optional)
            EmailUtil.sendEmail(student.getEmail(),
                    "Admission Approved",
                    "Congratulations " + student.getFirstname() + "! Your admission has been approved.");

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Student approved and enrollment created.", null));

            loadPendingStudents();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error approving student: " + e.getMessage(), null));
        }
    }

    /**
     * Reject student application with remarks.
     *
     * @param student
     */
    public void rejectStudent(Student student) {
        try {
            student.setAdmissionStatus("Rejected");
            student.setRemarks(remarks);
            studentDAO.updateAdmissionStatus(student);

            EmailUtil.sendEmail(student.getEmail(),
                    "Admission Rejected",
                    "Dear " + student.getFirstname()
                    + ",\n\nWe regret to inform you that your admission was not approved.\n\nRemarks: "
                    + remarks);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Student application rejected.", null));

            loadPendingStudents();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error rejecting student: " + e.getMessage(), null));
        }
    }

    /**
     * Request additional documents from the student.
     *
     * @param student
     */
    public void requestDocuments(Student student) {
        try {
            student.setAdmissionStatus("Pending Documents");
            student.setRemarks(remarks);
            studentDAO.updateAdmissionStatus(student);

            EmailUtil.sendEmail(student.getEmail(),
                    "Additional Documents Required",
                    "Dear " + student.getFirstname()
                    + ",\n\nPlease provide the following additional documents:\n\n" + remarks);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Requested additional documents from student.", null));

            loadPendingStudents();
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error requesting documents: " + e.getMessage(), null));
        }
    }

    /**
     * Getters & Setters
     *
     *
     * @return
     */
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Student> getPendingStudents() {
        return pendingStudents;
    }

    public void setPendingStudents(List<Student> pendingStudents) {
        this.pendingStudents = pendingStudents;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Student getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(Student selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
