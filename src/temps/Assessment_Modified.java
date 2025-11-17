/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author hrkas
 */
public class Assessment {

    private int assessmentId;
    private int enrollmentId;
    private String studentId;
    private String programName;
    private String studentName; // from join
    private boolean enrolledOnline;
    private boolean paymentAcknowledged;
    private int programId;
    private int totalUnits;
    private double unitFee;
    private double totalAmount;
    private double libraryFee;
    private double insuranceFee;
    private double medicalDentalFee;

    private List<OtherFeeType> otherFees;

    public Assessment() {
    }

    public Assessment(int assessmentId, int enrollmentId, String studentId, int programId, int totalUnits, double unitFee, double totalAmount, List<OtherFeeType> otherFees) {
        this.assessmentId = assessmentId;
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.programId = programId;
        this.totalUnits = totalUnits;
        this.unitFee = unitFee;
        this.totalAmount = totalAmount;
        this.otherFees = otherFees;
    }

    public Assessment(int assessmentId, int enrollmentId, String studentId, String programName, int programId, int totalUnits, double unitFee, double totalAmount, List<OtherFeeType> otherFees) {
        this.assessmentId = assessmentId;
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.programName = programName;
        this.programId = programId;
        this.totalUnits = totalUnits;
        this.unitFee = unitFee;
        this.totalAmount = totalAmount;
        this.otherFees = otherFees;
    }

    public Assessment(int assessmentId, int enrollmentId, String studentId, String programName, String studentName, boolean enrolledOnline,
            boolean paymentAcknowledged, int programId, int totalUnits, double unitFee, double totalAmount, List<OtherFeeType> otherFees) {
        this.assessmentId = assessmentId;
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.programName = programName;
        this.studentName = studentName;
        this.enrolledOnline = enrolledOnline;
        this.paymentAcknowledged = paymentAcknowledged;
        this.programId = programId;
        this.totalUnits = totalUnits;
        this.unitFee = unitFee;
        this.totalAmount = totalAmount;
        this.otherFees = otherFees;
    }

    /**
     * @return the assessmentId
     */
    public int getAssessmentId() {
        return assessmentId;
    }

    /**
     * @param assessmentId the assessmentId to set
     */
    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
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
     * @return the studentId
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the programId
     */
    public int getProgramId() {
        return programId;
    }

    /**
     * @param programId the programId to set
     */
    public void setProgramId(int programId) {
        this.programId = programId;
    }

    /**
     * @return the totalUnits
     */
    public int getTotalUnits() {
        return totalUnits;
    }

    /**
     * @param totalUnits the totalUnits to set
     */
    public void setTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
    }

    /**
     * @return the unitFee
     */
    public double getUnitFee() {
        return unitFee;
    }

    /**
     * @param unitFee the unitFee to set
     */
    public void setUnitFee(double unitFee) {
        this.unitFee = unitFee;
    }

    /**
     * @return the totalAmount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the otherFees
     */
    public List<OtherFeeType> getOtherFees() {
        return otherFees;
    }

    /**
     * @param otherFees the otherFees to set
     */
    public void setOtherFees(List<OtherFeeType> otherFees) {
        this.otherFees = otherFees;
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
     * @return the enrolledOnline
     */
    public boolean isEnrolledOnline() {
        return enrolledOnline;
    }

    /**
     * @param enrolledOnline the enrolledOnline to set
     */
    public void setEnrolledOnline(boolean enrolledOnline) {
        this.enrolledOnline = enrolledOnline;
    }

    /**
     * @return the paymentAcknowledged
     */
    public boolean isPaymentAcknowledged() {
        return paymentAcknowledged;
    }

    /**
     * @param paymentAcknowledged the paymentAcknowledged to set
     */
    public void setPaymentAcknowledged(boolean paymentAcknowledged) {
        this.paymentAcknowledged = paymentAcknowledged;
    }

}


    public double getLibraryFee() {
        return libraryFee;
    }

    public void setLibraryFee(double libraryFee) {
        this.libraryFee = libraryFee;
    }

    public double getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public double getMedicalDentalFee() {
        return medicalDentalFee;
    }

    public void setMedicalDentalFee(double medicalDentalFee) {
        this.medicalDentalFee = medicalDentalFee;
    }
