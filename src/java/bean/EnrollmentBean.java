/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.AssessmentDAO;
import dao.EnrollmentDAO;
import dao.PaymentDAO;
import dao.StudentDAO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import model.Assessment;
import model.Course;
import model.Enrollment;
import model.Payment;
import model.Student;
import org.primefaces.event.SelectEvent;
import util.EmailUtil;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class EnrollmentBean {

    /**
     * Creates a new instance of EnrollmentBean
     */
    private Enrollment enrollment = new Enrollment();

    private List<Enrollment> enrollments;
    private List<Student> students;
    private List<Course> enrolledCourses;
    private List<Student> studentOptions;
    private List<Student> filteredStudents;

    private int selectedStudentId;
    private String selectedStudno;
    private String selectedSemester;
    private String selectedAcademicYear;
    private String searchKeyword;
    private String searchBy = "lastname";
    private String searchType;
    private Student selectedStudent;

    private EnrollmentDAO enrollmentDAO;
    private AssessmentDAO assessmentDAO;
    private PaymentDAO paymentDAO;
    private Enrollment selectedEnrollment;
    private Assessment assessment; // with all related fee fields
    private Payment payment;       // with payment_reference_number, etc.
//    private List<Student> filteredStudents;

    public EnrollmentBean() {
        loadEnrollments();
        loadStudents();
    }

    /**
     * @return the studentOptions
     */
    public List<Student> getStudentOptions() {
        return studentOptions;
    }

    /**
     * @param studentOptions the studentOptions to set
     */
    public void setStudentOptions(List<Student> studentOptions) {
        this.studentOptions = studentOptions;
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
     * @return the enrollments
     */
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    /**
     * @param enrollments the enrollments to set
     */
    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    /**
     * @return the students
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /**
     * @return the enrolledCourses
     */
    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    /**
     * @param enrolledCourses the enrolledCourses to set
     */
    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    /**
     * @return the selectedStudno
     */
    public String getSelectedStudno() {
        return selectedStudno;
    }

    /**
     * @param selectedStudno the selectedStudno to set
     */
    public void setSelectedStudno(String selectedStudno) {
        this.selectedStudno = selectedStudno;
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
     * @return the selectedAcademicYear
     */
    public String getSelectedAcademicYear() {
        return selectedAcademicYear;
    }

    /**
     * @param selectedAcademicYear the selectedAcademicYear to set
     */
    public void setSelectedAcademicYear(String selectedAcademicYear) {
        this.selectedAcademicYear = selectedAcademicYear;
    }

    /**
     * @return the filteredStudents
     */
    public List<Student> getFilteredStudents() {
        return filteredStudents;
    }

    /**
     * @param filteredStudents the filteredStudents to set
     */
    public void setFilteredStudents(List<Student> filteredStudents) {
        this.filteredStudents = filteredStudents;
    }

    /**
     * @return the selectedStudentId
     */
    public int getSelectedStudentId() {
        return selectedStudentId;
    }

    /**
     * @param selectedStudentId the selectedStudentId to set
     */
    public void setSelectedStudentId(int selectedStudentId) {
        this.selectedStudentId = selectedStudentId;
    }

    /**
     * @return the searchKeyword
     */
    public String getSearchKeyword() {
        return searchKeyword;
    }

    /**
     * @param searchKeyword the searchKeyword to set
     */
    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    /**
     * @return the searchBy
     */
    public String getSearchBy() {
        return searchBy;
    }

    /**
     * @param searchBy the searchBy to set
     */
    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    /**
     * @return the searchType
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * @param searchType the searchType to set
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * @return the selectedStudent
     */
    public Student getSelectedStudent() {
        return selectedStudent;
    }

    /**
     * @param selectedStudent the selectedStudent to set
     */
    public void setSelectedStudent(Student selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public void loadEnrollments() {
        setEnrollments(new EnrollmentDAO().listAllEnrollments());
    }

    public void loadStudents() {
        setStudents(new StudentDAO().listAllStudents());
    }

    /**
     * @return the selectedEnrollment
     */
    public Enrollment getSelectedEnrollment() {
        return selectedEnrollment;
    }

    /**
     * @param selectedEnrollment the selectedEnrollment to set
     */
    public void setSelectedEnrollment(Enrollment selectedEnrollment) {
        this.selectedEnrollment = selectedEnrollment;
    }

    /**
     * @return the assessment
     */
    public Assessment getAssessment() {
        return assessment;
    }

    /**
     * @param assessment the assessment to set
     */
    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    /**
     * @return the payment
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

//    public void loadEnrollmentCourses(int enrollmentId) {
//        setEnrolledCourses(new EnrollmentDAO().getEnrolledCourses(enrollmentId));
//    }
    @PostConstruct
    public void init() {
        studentOptions = new StudentDAO().listAllStudents();
        enrollments = new EnrollmentDAO().listAllEnrollments();
    }

    public void enrollStudent() {
        enrollment.setStudent(new Student(getSelectedStudentId()));
        enrollment.setSemester(selectedSemester);
        enrollment.setAcademic_year(selectedAcademicYear);
        enrollment.setStatus("Pending");
        int yearLevel = new EnrollmentDAO().determineYearLevel(new Student().getStudno());
        enrollment.setYearLevel(yearLevel);
        boolean success = new EnrollmentDAO().createEnrollment(enrollment);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Student enrolled successfully!"));
            enrollments = new EnrollmentDAO().listAllEnrollments();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enrollment failed", null));
        }

        enrollment = new Enrollment();
    }

    public void approveEnrollment(Enrollment e) {
        if (new EnrollmentDAO().updateStatus(e.getEnrollment_id(), "Enrolled")) {
            enrollments = new EnrollmentDAO().listAllEnrollments();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Enrollment approved."));
            Student student = new Student();
            EmailUtil.sendEmail(student.getEmail(),
                    "Enrollment Approved",
                    "Hi " + student.getFirstname() + ",\n\nYour enrollment has been approved.\n\nRegards,\nCollege Admin");

        }
    }

    public void cancelEnrollment(Enrollment e) {
        if (new EnrollmentDAO().updateStatus(e.getEnrollment_id(), "Cancelled")) {
            enrollments = new EnrollmentDAO().listAllEnrollments();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Enrollment cancelled."));
        }
    }

    public void loadEnrollmentCourses(int enrollmentId) {
        enrolledCourses = new EnrollmentDAO().getEnrolledCourses(enrollmentId);
    }

    public void searchStudents() {
        StudentDAO studentDAO = new StudentDAO();
        if ("lastname".equalsIgnoreCase(getSearchBy())) {
            setFilteredStudents(studentDAO.findByLastName(getSearchKeyword()));
        } else {
            setFilteredStudents(studentDAO.findByStudno(getSearchKeyword()));
        }
    }

    public void onStudentSelect(SelectEvent<Student> event) {
        setSelectedStudent(event.getObject());
        enrollment.setStudent(getSelectedStudent());  // Bind to enrollment
    }
    // Enroll from admin panel

    public void enrollStudentFromAdmin() {
        if (selectedStudent != null) {
            enrollment.setStudno(selectedStudent.getStudno());
            enrollmentDAO.createEnrollment(enrollment);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Enrollment successful for " + selectedStudent.getLastname()));
            reset();
        }
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student successfully enrolled."));

        // Optional: reset form data
        selectedStudent = null;
        enrollment = new Enrollment();
    }

// Enroll self-service
    public void enrollSelf() {
        Student loggedIn = (Student) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("currentStudent");

        if (loggedIn != null) {
            enrollment.setStudno(loggedIn.getStudno());
            enrollmentDAO.createEnrollment(enrollment);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("You have been enrolled successfully."));
            reset();
        }
    }

    public void enrollStudentByStudent() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();

        try {
            // Example: Get current logged-in student's student number from session
            String studno = (String) extContext.getSessionMap().get("studentStudno");

            if (studno == null || studno.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Session error", "Student is not logged in."));
                return;
            }

            enrollment.setStudno(studno);
            enrollment.setStatus("Pending"); // default status for student-initiated enrollment

            boolean isDuplicate = enrollmentDAO.isAlreadyEnrolled(studno, enrollment.getAcademic_year(), enrollment.getSemester());

            if (isDuplicate) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Already Enrolled", "You have already enrolled for this term."));
                return;
            }

            boolean success = enrollmentDAO.addEnrollment(enrollment);
            if (success) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Enrollment successful", null));
                enrollment = new Enrollment(); // reset form
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enrollment failed", null));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + ex.getMessage(), null));
            System.out.println("Exception: " + ex);
        }
    }
    private List<Enrollment> studentEnrollments;

    public List<Enrollment> getStudentEnrollments() {
        if (studentEnrollments == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            StudentBean studentBean = context.getApplication().evaluateExpressionGet(context, "#{studentBean}", StudentBean.class);

            if (studentBean != null && studentBean.getStudent() != null) {
                String studNo = studentBean.getStudent().getStudno();
                studentEnrollments = enrollmentDAO.getEnrollmentsByStudentNo(studNo);
            }
        }
        return studentEnrollments;
    }

    public void reset() {
        enrollment = new Enrollment();       // reset enrollment fields
        selectedStudent = null;             // clear selected student
        searchKeyword = null;               // clear search input
        filteredStudents = new ArrayList<>(); // clear search results
    }

    public String viewConfirmation(Enrollment enrollment) {
        this.selectedEnrollment = enrollment;

        // Optionally load full assessment and payment info if needed
        this.selectedEnrollment.setAssessment(assessmentDAO.getAssessmentByEnrollmentId(enrollment.getEnrollment_id()));
        this.selectedEnrollment.setPayment(paymentDAO.getPaymentByEnrollmentId(enrollment.getEnrollment_id()));

        return "enrollment_confirmation?faces-redirect=true";
    }

}
