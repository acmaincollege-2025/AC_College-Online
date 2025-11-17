/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.AssessmentDAO;
import dao.PaymentDAO;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import model.Payment;
import model.Student;
import service.ReceiptPDFGenerator;
import util.EmailUtil;

/**
 *
 * @author hrkas
 */
@ManagedBean
@RequestScoped
public class PaymentBean {

    /**
     * Creates a new instance of PaymentBean
     */
    private Payment payment = new Payment();
    private List<Payment> studentPayments;
    private List<Payment> pendingPayments;
    private List<Payment> paymentHistoryForStudent;
    private Payment receiptData;
    private Payment selectedPayment;

    private PaymentDAO paymentDAO = new PaymentDAO();
    private AssessmentDAO assessmentDAO;
    private Student currentStudent;

    public PaymentBean() {
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

    /**
     * @return the studentPayments
     */
    public List<Payment> getStudentPayments() {
        return studentPayments;
    }

    /**
     * @param studentPayments the studentPayments to set
     */
    public void setStudentPayments(List<Payment> studentPayments) {
        this.studentPayments = studentPayments;
    }

    /**
     * @return the pendingPayments
     */
    public List<Payment> getPendingPayments() {
        return pendingPayments;
    }

    /**
     * @param pendingPayments the pendingPayments to set
     */
    public void setPendingPayments(List<Payment> pendingPayments) {
        this.pendingPayments = pendingPayments;
    }

    /**
     * @return the receiptData
     */
    public Payment getReceiptData() {
        return receiptData;
    }

    /**
     * @param receiptData the receiptData to set
     */
    public void setReceiptData(Payment receiptData) {
        this.receiptData = receiptData;
    }

    /**
     * @return the selectedPayment
     */
    public Payment getSelectedPayment() {
        return selectedPayment;
    }

    /**
     * @param selectedPayment the selectedPayment to set
     */
    public void setSelectedPayment(Payment selectedPayment) {
        this.selectedPayment = selectedPayment;
    }

    /**
     * @return the paymentHistoryForStudent
     */
    public List<Payment> getPaymentHistoryForStudent() {
        return paymentHistoryForStudent;
    }

    /**
     * @param paymentHistoryForStudent the paymentHistoryForStudent to set
     */
    public void setPaymentHistoryForStudent(List<Payment> paymentHistoryForStudent) {
        this.paymentHistoryForStudent = paymentHistoryForStudent;
    }

    @PostConstruct
    public void init() {
        // Example: Load payments for logged-in student
        String studentNo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("studentNo");
        if (studentNo != null) {
            setStudentPayments(paymentDAO.findPaymentsByStudent(studentNo));
        }
        currentStudent = getLoggedInStudent();
        if (currentStudent != null) {
            setPaymentHistoryForStudent(paymentDAO.getPaymentsByStudentId(currentStudent.getStudno()));
        }
    }

    public String showReceipt(Payment payment) {
        this.setReceiptData(payment);
        return "receipt?faces-redirect=true";
    }
    // Called by cashier to load pending enrollments

    public void loadPendingEnrollmentsForCashier() {
        setPendingPayments(paymentDAO.findPendingEnrollments()); // You'll create this method
    }

// Cashier submits on-site payment
    public void confirmOnsitePayment() {
        Student student = new Student();
        if (getSelectedPayment() != null) {
            getSelectedPayment().setPaymentType("On-Site");
            getSelectedPayment().setPaymentReferenceNumber("ONSITE-" + System.currentTimeMillis());
            getSelectedPayment().setPaymentDate(new Date());

            boolean success = paymentDAO.savePayment(getSelectedPayment());
            if (success) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("On-site payment recorded successfully."));
                loadPendingEnrollmentsForCashier();
                EmailUtil.sendEmail(student.getEmail(),
                        "Payment Verified",
                        "Hi " + student.getFirstname() + ",\n\nWe’ve verified your payment. You're now officially enrolled.\n\nThank you!");

            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment failed.", null));
            }
        }
    }

    public void downloadPDF() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

        try {
            ReceiptPDFGenerator.generateReceipt(receiptData, response);
            context.responseComplete();
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    public void submitPayment() {
        // Set payment date
        payment.setPaymentDate(new Date());

        // Retrieve the student’s total assessed fee (you must implement this logic)
        double totalFee = assessmentDAO.findTotalFeeByEnrollmentId(payment.getEnrollmentId());

        if ("Installment".equalsIgnoreCase(payment.getPaymentMode())) {
            double minPayment = totalFee * 0.10;

            if (payment.getAmountPaid() < minPayment) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Installment payments must be at least 10% of the total fee (₱" + minPayment + ")", null));
                return;
            }
        }

        boolean success = paymentDAO.savePayment(payment);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Payment recorded successfully."));
            studentPayments = paymentDAO.findPaymentsByStudent(payment.getStudentNo());
            payment = new Payment(); // Reset form
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment failed.", null));
        }
    }

    private Student getLoggedInStudent() {
        return (Student) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("student");
    }

}
//    public void submitPayment() {
//        getPayment().setPaymentDate(new Date());
//        boolean success = paymentDAO.savePayment(getPayment());
//        if (success) {
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage("Payment recorded successfully."));
//            setStudentPayments(paymentDAO.findPaymentsByStudent(getPayment().getStudentNo()));
//            setPayment(new Payment());
//        } else {
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Payment failed.", null));
//        }
//    }
