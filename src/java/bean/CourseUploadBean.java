/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.CourseDAO;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Course;
import org.primefaces.model.file.UploadedFile;
import util.ExcelUtil;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class CourseUploadBean implements Serializable{

    /**
     * Creates a new instance of CourseUploadBean
     */
    private UploadedFile uploadedFile;
    private CourseDAO courseDAO = new CourseDAO(); // Inject if you're using CDI
    
    public CourseUploadBean() {
    }

    /**
     * @return the uploadedFile
     */
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    /**
     * @param uploadedFile the uploadedFile to set
     */
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    public void handleFileUpload() {
        try {
            if (getUploadedFile() != null) {
                InputStream input = getUploadedFile().getInputStream();
                List<Course> courseList = ExcelUtil.parseCourses(input);

                int successCount = 0;
                successCount = courseList.stream().filter((c) -> (courseDAO.addCourse(c))).map((_item) -> 1).reduce(successCount, Integer::sum);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Upload successful: " + successCount + " courses added.", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "No file selected.", null));
            }
        } catch (Exception e) {
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error processing file: " + e.getMessage(), null));
        }
    }
}
