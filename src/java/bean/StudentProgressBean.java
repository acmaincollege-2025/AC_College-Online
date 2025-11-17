/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.CourseProgressDAO;
import dao.EnrollmentDAO;
import dao.ProgramDAO;
import dao.StudentDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import model.CourseProgress;
import model.Program;
import model.Student;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class StudentProgressBean {

    /**
     * Creates a new instance of StudentProgressBean
     */
    private String selectedProgramId;
    private String selectedStudentId;

    private List<Program> programList;
    private List<Student> studentList;
    private List<CourseProgress> courseProgressList;

    private int selectedCourseId;
    private String selectedStatus;

    private String userRole;
    private String searchKeyword;
    private Student selectedStudent;
    private List<Student> searchResults;

    private List<String> statusOptions = Arrays.asList("Enrolled", "In Progress", "Completed");

    public StudentProgressBean() {
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
     * @return the selectedStudentId
     */
    public String getSelectedStudentId() {
        return selectedStudentId;
    }

    /**
     * @param selectedStudentId the selectedStudentId to set
     */
    public void setSelectedStudentId(String selectedStudentId) {
        this.selectedStudentId = selectedStudentId;
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
     * @return the studentList
     */
    public List<Student> getStudentList() {
        return studentList;
    }

    /**
     * @param studentList the studentList to set
     */
    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    /**
     * @return the courseProgressList
     */
    public List<CourseProgress> getCourseProgressList() {
        return courseProgressList;
    }

    /**
     * @param courseProgressList the courseProgressList to set
     */
    public void setCourseProgressList(List<CourseProgress> courseProgressList) {
        this.courseProgressList = courseProgressList;
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

    /**
     * @return the selectedStatus
     */
    public String getSelectedStatus() {
        return selectedStatus;
    }

    /**
     * @param selectedStatus the selectedStatus to set
     */
    public void setSelectedStatus(String selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public List<String> getStatusOptions() {
        return statusOptions;
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
     * @return the searchResults
     */
    public List<Student> getSearchResults() {
        return searchResults;
    }

    /**
     * @param searchResults the searchResults to set
     */
    public void setSearchResults(List<Student> searchResults) {
        this.searchResults = searchResults;
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

    @PostConstruct
    public void init() {
        programList = new ProgramDAO().getAllPrograms();
        studentList = new ArrayList<>();
        courseProgressList = new ArrayList<>();

        // Simulate getting role from session
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        if (session != null) {
            userRole = (String) session.getAttribute("userRole"); // e.g., "Dean" or "Registrar"
        }
    }

    public void loadStudentsByProgram() {
        if (selectedProgramId != null && !selectedProgramId.isEmpty()) {
            studentList = new StudentDAO().getStudentsByProgram(selectedProgramId);
        } else {
            studentList.clear();
        }
    }

    public void loadProgress() {
        if (selectedStudentId != null && !selectedStudentId.isEmpty()) {
            courseProgressList = new EnrollmentDAO().getStudentCourseProgress(selectedStudentId);
        } else {
            courseProgressList.clear();
        }
    }

    // This method assigns courses to a student based on the enrolled program, semester, and year level
    public void assignCoursesToStudent(String studno, int programId, String semester, int yearLevel) {
        List<Integer> courseIds = new EnrollmentDAO().getCourseIdsForAssignment(programId, semester, yearLevel);
        boolean success = new EnrollmentDAO().assignCoursesToStudent(studno, courseIds);

        if (success) {
            new dao.CourseProgressDAO().syncProgressFromEnrollment(studno, courseIds);
        }

    }

    public void updateCourseStatus() {
        if (userRole == null || (!userRole.equals("Dean") && !userRole.equals("Registrar"))) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Access denied. You are not authorized to update course status.", null));
            return;
        }

        if (selectedStudentId != null && selectedCourseId > 0 && selectedStatus != null && !selectedStatus.isEmpty()) {
            boolean success = new EnrollmentDAO().updateStudentCourseStatus(selectedStudentId, selectedCourseId, selectedStatus);

            if (success) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Status updated successfully.", null));
                loadProgress();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update status.", null));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Missing data.", null));
        }
    }

    public void searchStudents() {
        StudentDAO dao = new StudentDAO();
        setSearchResults(dao.searchStudentsByKeyword(getSearchKeyword()));
    }

    public void loadProgressFromSearch(Student s) {
        this.selectedStudentId = s.getStudno();
        loadProgress(); // your existing logic to load course progress
    }

    public void loadProgressFromAutocomplete() {
        if (getSelectedStudent() != null) {
            selectedStudentId = getSelectedStudent().getStudno();
            loadProgress();
        }
    }

    public List<Student> completeStudent(String query) {
        StudentDAO dao = new StudentDAO();
        return dao.searchStudentsByProgramAndKeyword(selectedProgramId, query);
    }

}
