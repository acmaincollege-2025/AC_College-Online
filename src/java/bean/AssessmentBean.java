/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.AssessmentDAO;
import dao.EnrollmentDAO;
import dao.ProgramDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Assessment;
import model.BasicFee;
import model.EnrollmentCourse;
import model.OtherFeeType;
import model.Program;
import model.Student;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class AssessmentBean implements Serializable {

    private Assessment assessment = new Assessment();
    private Assessment selectedAssessment;
    private List<OtherFeeType> availableFees;
    private List<Assessment> allAssessments;
    private List<Program> programList;
    private double calculatedTotal;
    private double basicFeeTotal;
    private double otherFeeTotal;
    private List<Assessment> cashierAssessmentList;
    private List<EnrollmentCourse> subjectList;

    private AssessmentDAO assessmentDAO = new AssessmentDAO();

    public AssessmentBean() {
        try {
            availableFees = new AssessmentDAO().getDefaultOtherFees();
            assessment.setOtherFees(availableFees);
        } catch (SQLException ex) {
            ex.getMessage();
        }
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
     * @return the availableFees
     */
    public List<OtherFeeType> getAvailableFees() {
        return availableFees;
    }

    /**
     * @param availableFees the availableFees to set
     */
    public void setAvailableFees(List<OtherFeeType> availableFees) {
        this.availableFees = availableFees;
    }

    /**
     * @return the calculatedTotal
     */
    public double getCalculatedTotal() {
        return calculatedTotal;
    }

    /**
     * @param calculatedTotal the calculatedTotal to set
     */
    public void setCalculatedTotal(double calculatedTotal) {
        this.calculatedTotal = calculatedTotal;
    }

    /**
     * @return the allAssessments
     */
    public List<Assessment> getAllAssessments() {
        return allAssessments;
    }

    /**
     * @param allAssessments the allAssessments to set
     */
    public void setAllAssessments(List<Assessment> allAssessments) {
        this.allAssessments = allAssessments;
    }

    /**
     * @return the programList
     */
    public List<Program> getProgramList() {
        if (programList == null) {
            programList = new ProgramDAO().getAllPrograms();
        }
        return programList;
    }

    /**
     * @param programList the programList to set
     */
    public void setProgramList(List<Program> programList) {
        this.programList = programList;
    }

    /**
     * @return the subjectList
     */
    public List<EnrollmentCourse> getSubjectList() {
        return subjectList;
    }

    /**
     * @param subjectList the subjectList to set
     */
    public void setSubjectList(List<EnrollmentCourse> subjectList) {
        this.subjectList = subjectList;
    }

    /**
     * @return the basicFeeTotal
     */
    public double getBasicFeeTotal() {
        List<BasicFee> fees = assessment.getBasicFees();
        if (fees == null) {
            return 0.0;
        }
        return fees.stream()
                .mapToDouble(BasicFee::getAmount)
                .sum();
    }

    /**
     * @param basicFeeTotal the basicFeeTotal to set
     */
    public void setBasicFeeTotal(double basicFeeTotal) {
        this.basicFeeTotal = basicFeeTotal;
    }

    /**
     * @return the otherFeeTotal
     */
    public double getOtherFeeTotal() {
        return otherFeeTotal;
    }

    /**
     * @param otherFeeTotal the otherFeeTotal to set
     */
    public void setOtherFeeTotal(double otherFeeTotal) {
        this.otherFeeTotal = otherFeeTotal;
    }

    /**
     * @return the selectedAssessment
     */
    public Assessment getSelectedAssessment() {
        return selectedAssessment;
    }

    /**
     * @param selectedAssessment the selectedAssessment to set
     */
    public void setSelectedAssessment(Assessment selectedAssessment) {
        this.selectedAssessment = selectedAssessment;
    }

    @PostConstruct
    public void init() {
        try {
            Student loggedInStudent = (Student) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("loggedInStudent");

            if (loggedInStudent != null) {
                assessment.setStudentId(loggedInStudent.getStudno());
                assessment.setStudentName(loggedInStudent.getLastname() + ", " + loggedInStudent.getFirstname() + " " + loggedInStudent.getMiddlename()); // assumes getFullName() in Student
                assessment.setProgramId(loggedInStudent.getProgramId());
                assessment.setProgramName(new ProgramDAO().findById(loggedInStudent.getProgramId()).getProgram_name());
                // You can set year level if available in your Student or Enrollment model
            }

            availableFees = assessmentDAO.getDefaultOtherFees();
            assessment.setOtherFees(availableFees);

            loadAllAssessments();
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public void calculateTotal() {
        double total;
        double tuition = assessment.getUnitFee() * assessment.getTotalUnits();
        double basicFees = 0;
        double otherFees = 0;
//        total = assessment.getOtherFees().stream().map((fee) -> fee.getAmount()).reduce(total, (accumulator, _item) -> accumulator + _item);
        if (assessment.getOtherFees() != null) {
            for (OtherFeeType fee : assessment.getOtherFees()) {
                // Let's assume basic fees are identified by specific codes like "LIB", "MED", "INS"
                if (fee.getFeeCode().equalsIgnoreCase("LIB")
                        || fee.getFeeCode().equalsIgnoreCase("INS")
                        || fee.getFeeCode().equalsIgnoreCase("MED")) {
                    basicFees += fee.getAmount();
                } else {
                    otherFees += fee.getAmount();
                }
            }
        }
        this.setBasicFeeTotal(basicFees);
        this.setOtherFeeTotal(otherFees);
        total = tuition + basicFees + otherFees;
        assessment.setTotalAmount(total);
        this.calculatedTotal = total;
    }

    public void loadEnrolledSubjects() {
        if (assessment != null && assessment.getEnrollmentId() > 0) {
            setSubjectList(new EnrollmentDAO().getEnrolledCoursesByEnrollmentId(assessment.getEnrollmentId()));
        } else {
            setSubjectList(new ArrayList<>());
        }
    }

    public void submitAssessment() {
        try {
            calculateTotal();
            assessmentDAO.saveAssessment(assessment);
            loadAllAssessments();
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public void loadAllAssessments() {
        setAllAssessments(assessmentDAO.findAll());
    }

    public void saveAssessment() {
        boolean success = assessmentDAO.save_Assessment(assessment);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Assessment saved successfully."));
            loadAllAssessments();
            assessment = new Assessment();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to save assessment.", null));
        }
    }

    public void updateAssessment() {
        boolean success = assessmentDAO.updateAssessment(assessment);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Assessment updated successfully."));
            loadAllAssessments();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to update assessment.", null));
        }
    }

    public void deleteAssessment(int id) {
        boolean success = assessmentDAO.deleteAssessment(id);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Assessment deleted successfully."));
            loadAllAssessments();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to delete assessment.", null));
        }
    }

    public void loadCashierAssessmentList() {
        cashierAssessmentList = assessmentDAO.findAllForCashier();
    }

    public void acknowledgePayment(int assessmentId) {
        boolean success = assessmentDAO.markAsAcknowledged(assessmentId);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Payment acknowledged successfully."));
            loadCashierAssessmentList();
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to acknowledge payment.", null));
        }
    }

    public List<Assessment> getCashierAssessmentList() {
        return cashierAssessmentList;
    }

    public void viewAssessmentDetail(Assessment assessment) {
        this.selectedAssessment = assessment;
        // Optionally compute breakdown again if not pre-computed
        double tuition = assessment.getUnitFee() * assessment.getTotalUnits();
        double totalBasicFee = assessment.getBasicFees() != null
                ? assessment.getBasicFees().stream().mapToDouble(BasicFee::getAmount).sum()
                : 0.0;
        double totalOtherFee = assessment.getOtherFees() != null
                ? assessment.getOtherFees().stream().mapToDouble(OtherFeeType::getAmount).sum()
                : 0.0;

        this.basicFeeTotal = totalBasicFee;
        this.otherFeeTotal = totalOtherFee;
        this.calculatedTotal = tuition + basicFeeTotal + otherFeeTotal;

    }

//    public List<Program> getProgramList() {
//        
//    }
}
