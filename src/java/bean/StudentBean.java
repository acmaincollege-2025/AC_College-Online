/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ProgramDAO;
import dao.StudentDAO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import model.Program;
import model.Student;
import model.User;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author hrkas
 */
@ManagedBean
@SessionScoped
public class StudentBean implements Serializable {

    /**
     * Creates a new instance of StudentBean
     */
    private Student student = new Student();
    private List<Student> studentList;
    private List<Program> programList;

    private Part file; // for photo upload

    private StudentDAO studentDAO = new StudentDAO();
    private ProgramDAO programDAO = new ProgramDAO();

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    public StudentBean() {
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

    @PostConstruct
    public void init() {
        student = new Student();
        loadStudents();
        setProgramList(programDAO.getAllPrograms());
        setStudentList(studentDAO.listAllStudents());
    }

    public void saveStudent() {
        boolean isDuplicate = studentDAO.isSrcodeOrStudnoDuplicate(student.getSrcode(), student.getStudno(), student.getSid());

        if (isDuplicate) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Duplicate SR Code or Student Number", "Please use a unique SR Code and Student Number."));
            return;
        }

        boolean success;
        if ((Integer) student.getSid() == null) {
            success = studentDAO.addStudent(student);
        } else {
            success = studentDAO.updateStudent(student);
        }

        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Student saved successfully", null));
            clearForm();
            loadStudents();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error saving student", null));
        }
    }

    public void deleteStudentConfirmed() {
        if (student != null && studentDAO.deleteStudent(student.getStudno())) {
            showMessage("Student deleted.");
            studentList = studentDAO.listAllStudents();
            student = new Student();
        } else {
            showMessage("Deletion failed.");
        }
    }

    public void confirmDelete(Student s) {
        this.student = s;
    }

    private void showMessage(String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    public String editStudent(Student s) {
        this.setStudent(s);
        return "student-registration?faces-redirect=true";
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            String filename = event.getFile().getFileName();
            String uploadDir = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRealPath("/resources/uploads/students");

            // Ensure the upload directory exists
            Path folder = Paths.get(uploadDir);
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }

            // Save the uploaded file
            Path filePath = folder.resolve(filename);
            try (InputStream input = event.getFile().getInputStream()) {
                Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Save the filename in the student object
            student.setPhoto(filename);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Photo uploaded: " + filename));
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload failed", null));
        }
    }

    public void generateSrCode() {
        try {
            String newCode = studentDAO.generateNewSrCode();
            student.setSrcode(newCode);
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error generating SR Code: " + e.getMessage(), null));
        }
    }

    public Student getLoggedInStudent() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);

        if (session != null) {
            return (Student) session.getAttribute("loggedInStudent");
        }
        return null;
    }

    public void generateSRCode() {
        this.student.setSrcode(studentDAO.generateNewSrCode());
    }

    public void clearForm() {
        student = new Student(); // resets the form-bound student
    }

    public void loadStudents() {
        studentList = studentDAO.listAllStudents(); // reloads the list
    }

}
//    public String saveStudent() {
//        try {
//            if (getFile() != null) {
//                String fileName = Paths.get(getFile().getSubmittedFileName()).getFileName().toString();
//                String uploadPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + File.separator + "uploads";
//                File uploadDir = new File(uploadPath);
//                if (!uploadDir.exists()) {
//                    uploadDir.mkdirs();
//                }
//
//                File fileToSave = new File(uploadDir, fileName);
//                try (InputStream input = getFile().getInputStream()) {
//                    Files.copy(input, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                    getStudent().setPhoto("uploads/" + fileName); // save relative path
//                }
//            }
//
//            if (getStudent().getSid() == 0) {
//                studentDAO.addStudent(getStudent());
//            } else {
//                studentDAO.updateStudent(getStudent());
//            }
//
//            setStudent(new Student()); // reset form
//            setStudentList(studentDAO.listAllStudents());
//            return "student-registration?faces-redirect=true";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
