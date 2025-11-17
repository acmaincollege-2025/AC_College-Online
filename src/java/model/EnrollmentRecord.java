/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author hrkas
 */
public class EnrollmentRecord {
    private Student student;
    private Program program;

    public EnrollmentRecord() {
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
     * @return the program
     */
    public Program getProgram() {
        return program;
    }

    /**
     * @param program the program to set
     */
    public void setProgram(Program program) {
        this.program = program;
    }

    public void setStudent(String sno, String lastname, String firstname, String middlename) {
        student.setStudno(sno);
        student.setLastname(lastname);
        student.setFirstname(firstname);
        student.setMiddlename(middlename);
    }
    
    
}
