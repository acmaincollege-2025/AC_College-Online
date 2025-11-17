/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.StudentProgressDAO;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import model.CourseProgress;
import model.Student;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class StudentGradeBean {

    /**
     * Creates a new instance of StudentGradeBean
     */
    private Student student;
    private List<CourseProgress> courseProgressList;

    public StudentGradeBean() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        String loggedStudNo = (String) ec.getSessionMap().get("username"); // Assuming username = studNo

        StudentProgressDAO dao = new StudentProgressDAO();
        Map<Student, List<CourseProgress>> map = dao.getStudentProgressWithGrades(null); // or filter by program

        for (Student s : map.keySet()) {
            if (s.getStudno().equals(loggedStudNo)) {
                this.student = s;
                this.courseProgressList = map.get(s);
                break;
            }
        }
    }

    /**
     * @return the student
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @param student the student to set
     */
    public void setStudent(Student student) {
        this.student = student;
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

}
