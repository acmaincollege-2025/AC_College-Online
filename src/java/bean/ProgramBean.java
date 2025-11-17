/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ProgramDAO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Program;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class ProgramBean {

    /**
     * Creates a new instance of ProgramBean
     */
    private Program program = new Program();
    private List<Program> programList;

    private ProgramDAO programDAO = new ProgramDAO();

    public ProgramBean() {
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

    @PostConstruct
    public void init() {
        setProgramList(programDAO.getAllPrograms());
    }

//    public String saveProgram() {
//        if (getProgram().getProgram_id() == 0) {
//            programDAO.addProgram(getProgram());
//        } else {
//            programDAO.updateProgram(getProgram());
//        }
//        setProgram(new Program());
//        setProgramList(programDAO.getAllPrograms());
//        return null;
//    }
    public void editProgram(Program p) {
        this.setProgram(p);
    }

    public void deleteProgram(Program p) {
        programDAO.deleteProgram(p.getProgram_code());
        setProgramList(programDAO.getAllPrograms());
    }

    public String saveProgram() {
        boolean success;
        if (program.getProgram_id() == 0) {
            success = programDAO.addProgram(program);
            if (success) {
                showMessage("Program added successfully.");
            }
        } else {
            success = programDAO.updateProgram(program);
            if (success) {
                showMessage("Program updated successfully.");
            }
        }

        if (success) {
            program = new Program();
            programList = programDAO.getAllPrograms();
        } else {
            showMessage("Operation failed. Please check the details.");
        }
        return null;
    }

    public void deleteProgramConfirmed() {
        if (program != null && programDAO.deleteProgram(program.getProgram_code())) {
            showMessage("Program deleted successfully.");
            programList = programDAO.getAllPrograms();
            program = new Program(); // reset
        } else {
            showMessage("Failed to delete the program.");
        }
    }

    public void confirmDelete(Program p) {
        this.program = p;
    }

    private void showMessage(String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }
}
