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
    private String academicYear;
    private String semester;
    private boolean enrolledOnline;
    private boolean paymentAcknowledged;
    private int programId;
    private int totalUnits;
    private double unitFee;
    private double totalAmount;
    private double libraryFee;
    private double insuranceFee;
    private double medicalDentalFee;
    private double basicFee;
    private List<BasicFee> basicFees;
    private List<OtherFeeType> otherFees;
    
    public Assessment() {
    }

    public Assessment(int assessmentId, int enrollmentId, String studentId, String programName, String studentName,
            boolean enrolledOnline, boolean paymentAcknowledged, int programId, int totalUnits, double unitFee, double 
                    totalAmount, double libraryFee, double insuranceFee, double medicalDentalFee) {
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
        this.libraryFee = libraryFee;
        this.insuranceFee = insuranceFee;
        this.medicalDentalFee = medicalDentalFee;
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
     * @return the libraryFee
     */
    public double getLibraryFee() {
        return libraryFee;
    }

    /**
     * @param libraryFee the libraryFee to set
     */
    public void setLibraryFee(double libraryFee) {
        this.libraryFee = libraryFee;
    }

    /**
     * @return the insuranceFee
     */
    public double getInsuranceFee() {
        return insuranceFee;
    }

    /**
     * @param insuranceFee the insuranceFee to set
     */
    public void setInsuranceFee(double insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    /**
     * @return the medicalDentalFee
     */
    public double getMedicalDentalFee() {
        return medicalDentalFee;
    }

    /**
     * @param medicalDentalFee the medicalDentalFee to set
     */
    public void setMedicalDentalFee(double medicalDentalFee) {
        this.medicalDentalFee = medicalDentalFee;
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
     * @return the academicYear
     */
    public String getAcademicYear() {
        return academicYear;
    }

    /**
     * @param academicYear the academicYear to set
     */
    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    /**
     * @return the semester
     */
    public String getSemester() {
        return semester;
    }

    /**
     * @param semester the semester to set
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * @return the basicFee
     */
    public double getBasicFee() {
        return basicFee;
    }

    /**
     * @param basicFee the basicFee to set
     */
    public void setBasicFee(double basicFee) {
        this.basicFee = basicFee;
    }

    /**
     * @return the basicFees
     */
    public List<BasicFee> getBasicFees() {
        return basicFees;
    }

    /**
     * @param basicFees the basicFees to set
     */
    public void setBasicFees(List<BasicFee> basicFees) {
        this.basicFees = basicFees;
    }

   
    
}
