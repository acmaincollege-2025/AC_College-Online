package model;

import java.util.Date;

/**
 * Represents a student entity with complete personal, academic, and guardian
 * information. Now includes admissionStatus and remarks for admission workflow
 * tracking.
 *
 * Author: hrkas (refactored)
 */
public class Student {

    private int sid;
    private String srcode;
    private String studno;
    private String lastname;
    private String firstname;
    private String middlename;
    private String gender;
    private Date dob;
    private String address;
    private String contactno;
    private String email;
    private int programId;
    private String photo;

    // Guardian and Family Info
    private String guardian;
    private String guardianNumber;
    private String fathername;
    private String fatherOccupation;
    private String mothername;
    private String motherOccupation;
    private String parentAddress;
    private String parentContactNumber;

    // Academic Info
    private String studentType;          // e.g., New, Transferee, Returnee
    private String program_enrolled;     // Program name
    private String year_level;           // 1st, 2nd, 3rd, 4th year
    private double gpa;

    // Admission Workflow
    private String admissionStatus;      // Pending, Approved, Rejected, Pending Documents
    private String remarks;              // Comments for admission office use
    private Date dateRegistered;         // Timestamp when registration was submitted
    private Date dateUpdated;            // Timestamp of last update

    // Constructors
    public Student() {
    }

    public Student(int sid) {
        this.sid = sid;
    }

    public Student(int sid, String srcode, String studno, String lastname, String firstname,
            String middlename, String gender, Date dob, String address, String contactno,
            String email, int programId, String photo, String guardian, String guardianNumber,
            String fathername, String fatherOccupation, String mothername,
            String motherOccupation, String parentAddress, String parentContactNumber,
            String studentType, String admissionStatus) {
        this.sid = sid;
        this.srcode = srcode;
        this.studno = studno;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.contactno = contactno;
        this.email = email;
        this.programId = programId;
        this.photo = photo;
        this.guardian = guardian;
        this.guardianNumber = guardianNumber;
        this.fathername = fathername;
        this.fatherOccupation = fatherOccupation;
        this.mothername = mothername;
        this.motherOccupation = motherOccupation;
        this.parentAddress = parentAddress;
        this.parentContactNumber = parentContactNumber;
        this.studentType = studentType;
        this.admissionStatus = admissionStatus;
    }

    public Student(String studno, String lastname, String firstname, String middlename) {
        this.studno = studno;
        this.lastname = lastname;
        this.firstname = firstname;
        this.middlename = middlename;
    }

    /* ==========================
       Getters and Setters
       ========================== */
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSrcode() {
        return srcode;
    }

    public void setSrcode(String srcode) {
        this.srcode = srcode;
    }

    public String getStudno() {
        return studno;
    }

    public void setStudno(String studno) {
        this.studno = studno;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getGuardianNumber() {
        return guardianNumber;
    }

    public void setGuardianNumber(String guardianNumber) {
        this.guardianNumber = guardianNumber;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public String getMothername() {
        return mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getMotherOccupation() {
        return motherOccupation;
    }

    public void setMotherOccupation(String motherOccupation) {
        this.motherOccupation = motherOccupation;
    }

    public String getParentAddress() {
        return parentAddress;
    }

    public void setParentAddress(String parentAddress) {
        this.parentAddress = parentAddress;
    }

    public String getParentContactNumber() {
        return parentContactNumber;
    }

    public void setParentContactNumber(String parentContactNumber) {
        this.parentContactNumber = parentContactNumber;
    }

    public String getStudentType() {
        return studentType;
    }

    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }

    public String getProgram_enrolled() {
        return program_enrolled;
    }

    public void setProgram_enrolled(String program_enrolled) {
        this.program_enrolled = program_enrolled;
    }

    public String getYear_level() {
        return year_level;
    }

    public void setYear_level(String year_level) {
        this.year_level = year_level;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public String getAdmissionStatus() {
        return admissionStatus;
    }

    public void setAdmissionStatus(String admissionStatus) {
        this.admissionStatus = admissionStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    /* ==========================
       Utility Methods
       ========================== */
    public String getFullName() {
        return String.format("%s, %s %s", lastname, firstname, middlename != null ? middlename : "");
    }

    public boolean isApproved() {
        return "Approved".equalsIgnoreCase(admissionStatus);
    }

    public boolean isPending() {
        return admissionStatus == null || "Pending".equalsIgnoreCase(admissionStatus);
    }

    public boolean isRejected() {
        return "Rejected".equalsIgnoreCase(admissionStatus);
    }
}
