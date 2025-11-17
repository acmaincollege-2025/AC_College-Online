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
public class Program {
    private int program_id;
    private String program_code;
    private String program_name;

    public Program() {
    }

    public Program(int program_id, String program_code, String program_name) {
        this.program_id = program_id;
        this.program_code = program_code;
        this.program_name = program_name;
    }

    public Program(String program_name) {
        this.program_name = program_name;
    }

    /**
     * @return the program_id
     */
    public int getProgram_id() {
        return program_id;
    }

    /**
     * @param program_id the program_id to set
     */
    public void setProgram_id(int program_id) {
        this.program_id = program_id;
    }

    /**
     * @return the program_code
     */
    public String getProgram_code() {
        return program_code;
    }

    /**
     * @param program_code the program_code to set
     */
    public void setProgram_code(String program_code) {
        this.program_code = program_code;
    }

    /**
     * @return the program_name
     */
    public String getProgram_name() {
        return program_name;
    }

    /**
     * @param program_name the program_name to set
     */
    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }
    
    
}
