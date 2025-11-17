/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author hrkas
 */
public class Payment {

    private int id;
    private int enrollmentId;
    private String studentNo;
    private String paymentType;
    private String paymentReferenceNumber;
    private String programName;
    private String studentName;
//    private String paymentType; // e.g., GCash, Bank, etc.
    private String paymentMode;
    private String receivedBy;
    private double amountPaid;
    private Date paymentDate;

    public Payment() {
    }

    public Payment(int id, int enrollmentId, String studentNo, String paymentType, String paymentReferenceNumber, double amountPaid, Date paymentDate) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.studentNo = studentNo;
        this.paymentType = paymentType;
        this.paymentReferenceNumber = paymentReferenceNumber;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
    }

    public Payment(int id, int enrollmentId, String studentNo, String paymentType, String paymentReferenceNumber, String programName, String studentName, double amountPaid, Date paymentDate) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.studentNo = studentNo;
        this.paymentType = paymentType;
        this.paymentReferenceNumber = paymentReferenceNumber;
        this.programName = programName;
        this.studentName = studentName;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the enrollmentId
     */
    public int getEnrollmentId() {
        return enrollmentId;
    }

    /**
     * @param enrollmentId the enrollmentId to set
     */
    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    /**
     * @return the studentNo
     */
    public String getStudentNo() {
        return studentNo;
    }

    /**
     * @param studentNo the studentNo to set
     */
    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    /**
     * @return the paymentType
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return the paymentReferenceNumber
     */
    public String getPaymentReferenceNumber() {
        return paymentReferenceNumber;
    }

    /**
     * @param paymentReferenceNumber the paymentReferenceNumber to set
     */
    public void setPaymentReferenceNumber(String paymentReferenceNumber) {
        this.paymentReferenceNumber = paymentReferenceNumber;
    }

    /**
     * @return the amountPaid
     */
    public double getAmountPaid() {
        return amountPaid;
    }

    /**
     * @param amountPaid the amountPaid to set
     */
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    /**
     * @return the paymentDate
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate the paymentDate to set
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return the programName
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * @param programName the programName to set
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * @return the studentName
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * @param studentName the studentName to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * @return the paymentMode
     */
    public String getPaymentMode() {
        return paymentMode;
    }

    /**
     * @param paymentMode the paymentMode to set
     */
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    /**
     * @return the receivedBy
     */
    public String getReceivedBy() {
        return receivedBy;
    }

    /**
     * @param receivedBy the receivedBy to set
     */
    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

}
