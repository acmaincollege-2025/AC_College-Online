/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.EnrollmentDAO;
import dao.PaymentDAO;
import dao.StudentDAO;
import dao.UserDAO;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Enrollment;
import model.Payment;
import model.Student;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class CashierApprovalBean {

    /**
     * Creates a new instance of CashierApprovalBean
     */
    private List<Enrollment> pendingEnrollments;
    private Enrollment selectedEnrollment;
    private Payment payment = new Payment();

    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();

    public CashierApprovalBean() {
    }

    /**
     * @return the pendingEnrollments
     */
    public List<Enrollment> getPendingEnrollments() {
        return pendingEnrollments;
    }

    /**
     * @param pendingEnrollments the pendingEnrollments to set
     */
    public void setPendingEnrollments(List<Enrollment> pendingEnrollments) {
        this.pendingEnrollments = pendingEnrollments;
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

    @PostConstruct
    public void init() {
        setPendingEnrollments(enrollmentDAO.findPendingEnrollments());
    }

    public void approveOnlinePayment() {
        if (getSelectedEnrollment() != null) {
            enrollmentDAO.updatePaymentStatus(getSelectedEnrollment().getEnrollment_id(), "verified");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Payment verified successfully."));
            init(); // reload
        }
    }

    public void processOnsitePayment() {
        if (getSelectedEnrollment() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "No enrollment selected.", null));
            return;
        }

        // populate payment
        getPayment().setEnrollmentId(getSelectedEnrollment().getEnrollment_id());
        getPayment().setStudentNo(getSelectedEnrollment().getStudno());
        getPayment().setPaymentDate(new Date());
        getPayment().setPaymentType("On-site");

//        StudentDAO studentDAO = new StudentDAO();
        // Use the selected enrollment studno (not a new Enrollment())
        Student student = new StudentDAO().findByStudentNo(getSelectedEnrollment().getStudno());

        if (student == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Student record not found.", null));
            return;
        }

        if (paymentDAO.savePayment(getPayment())) {
            enrollmentDAO.updatePaymentStatus(getSelectedEnrollment().getEnrollment_id(), "paid");
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("On-site payment recorded successfully."));

            String stType = student.getStudentType();
            if (stType != null && (stType.equalsIgnoreCase("freshman")
                    || stType.equalsIgnoreCase("transferee")
                    || stType.equalsIgnoreCase("returnee"))) {

                try {
                    new UserDAO().addStudentUser(student);
                } catch (Exception ex) {
                    // don't break payment flow; log and notify
                    ex.printStackTrace();
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "User creation failed: " + ex.getMessage(), null));
                }
            }
            init(); // reload view data
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment failed.", null));
        }
    }

}
