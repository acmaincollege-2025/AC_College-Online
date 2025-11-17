/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.CourseDAO;
import dao.ProgramDAO;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import model.Course;
import model.Program;
import util.ExcelUtil;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class CourseBean implements Serializable {

    /**
     * Creates a new instance of CourseBean
     */
    private Course course = new Course();
    private List<Course> courseList;
    private List<Program> programList;
    private Part file;
    private Integer selectedProgramId;
    private List<Course> filteredCourses;
    private List<String> curriculumYearList;
    private String selectedCurrYear;
    private final CourseDAO courseDAO = new CourseDAO();
    private final ProgramDAO programDAO = new ProgramDAO();

    public CourseBean() {
    }

    /**
     * @return the selectedProgramId
     */
    public Integer getSelectedProgramId() {
        return selectedProgramId;
    }

    /**
     * @param selectedProgramId the selectedProgramId to set
     */
    public void setSelectedProgramId(Integer selectedProgramId) {
        this.selectedProgramId = selectedProgramId;
    }

    /**
     * @return the filteredCourses
     */
    public List<Course> getFilteredCourses() {
        return filteredCourses;
    }

    /**
     * @param filteredCourses the filteredCourses to set
     */
    public void setFilteredCourses(List<Course> filteredCourses) {
        this.filteredCourses = filteredCourses;
    }

    /**
     * @return the course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * @param course the course to set
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * @return the file
     */
    public Part getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(Part file) {
        this.file = file;
    }

    /**
     * @param courseList the courseList to set
     */
    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    /**
     * @return the programList
     */
    public List<Program> getProgramList() {
        if (programList == null) {
            programList = programDAO.getAllPrograms(); // must exist
        }
        return programList;
    }

    /**
     * @param programList the programList to set
     */
    public void setProgramList(List<Program> programList) {
        this.programList = programList;
    }

    public List<Course> getCourseList() {
        if (courseList == null) {
            setCourseList(courseDAO.listCourses());
        }
        return courseList;
    }
    private List<Course> availableCoursesForStudent;

    public List<Course> getAvailableCoursesForStudent() {
//        CourseDAO courseDAO = new CourseDAO();
        if (availableCoursesForStudent == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            StudentBean studentBean = context.getApplication().evaluateExpressionGet(context, "#{studentBean}", StudentBean.class);

            if (studentBean != null && studentBean.getStudent() != null) {
                String studNo = studentBean.getStudent().getStudno();
                availableCoursesForStudent = courseDAO.getAvailableCoursesForStudent(studNo);
            }
        }
        return availableCoursesForStudent;
    }

    /**
     * @return the curriculumYearList
     */
    public List<String> getCurriculumYearList() {
        if (curriculumYearList == null) {
            curriculumYearList = courseDAO.getDistinctCurrYears();
        }
        return curriculumYearList;
    }

    /**
     * @param curriculumYearList the curriculumYearList to set
     */
    public void setCurriculumYearList(List<String> curriculumYearList) {
        this.curriculumYearList = curriculumYearList;
    }

    /**
     * @return the selectedCurrYear
     */
    public String getSelectedCurrYear() {
        return selectedCurrYear;
    }

    /**
     * @param selectedCurrYear the selectedCurrYear to set
     */
    public void setSelectedCurrYear(String selectedCurrYear) {
        this.selectedCurrYear = selectedCurrYear;
    }

    public void saveCourse() {
        boolean success = courseDAO.addCourse(course);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Course added successfully!", null));
            course = new Course(); // Reset form
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to add course.", null));
        }
        setCourse(new Course());
        setCourseList(null); // refresh list
    }

    public void editCourse(Course c) {
        this.setCourse(c);
    }

//    public void deleteCourse(int id) {
//        courseDAO.deleteCourse(id);
//        setCourseList(null);
//    }
    public void clearForm() {
        setCourse(new Course());
    }

    public void uploadCourses() {
        try (InputStream input = getFile().getInputStream()) {
            List<Course> courses = ExcelUtil.parseCourses(input); // You'll implement this utility
            boolean success = courseDAO.addCoursesFromExcel(courses);

            if (success) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("Courses uploaded successfully"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to upload courses", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), null));
        }
    }

    public void searchCoursesByProgram() {
        if (getSelectedProgramId() != null) {
            setFilteredCourses(courseDAO.getCoursesByProgram(getSelectedProgramId()));
        } else {
            setFilteredCourses(courseDAO.getAllCourses());
        }
    }

    public void onRowEdit(org.primefaces.event.RowEditEvent<Course> event) {
        Course edited = event.getObject();
        boolean success = courseDAO.updateCourse(edited);
        FacesMessage msg = success
                ? new FacesMessage("Course updated successfully")
                : new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update course", null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void deleteCourse(int id) {
        boolean success = courseDAO.deleteCourse(id);
        FacesMessage msg = success
                ? new FacesMessage("Course deleted")
                : new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delete failed", null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        searchCoursesByProgram(); // Refresh list
    }

    public void searchCoursesByFilters() {
        filteredCourses = courseDAO.getCoursesByProgramAndCurrYear(selectedProgramId, selectedCurrYear);
    }

}
