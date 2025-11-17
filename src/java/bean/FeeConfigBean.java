/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.FeeConfigDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.BasicFee;
import model.OtherFeeType;
import model.TuitionFee;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class FeeConfigBean implements Serializable {

    /**
     * Creates a new instance of FeeConfigBean
     */
    private TuitionFee tuitionFee;
    private List<BasicFee> basicFees;
    private List<OtherFeeType> otherFees;

    private OtherFeeType newOtherFee = new OtherFeeType();
    private OtherFeeType selectedOtherFee;

    private FeeConfigDAO dao = new FeeConfigDAO();

    public FeeConfigBean() {
    }

    /**
     * @return the tuitionFee
     */
    public TuitionFee getTuitionFee() {
        return tuitionFee;
    }

    /**
     * @param tuitionFee the tuitionFee to set
     */
    public void setTuitionFee(TuitionFee tuitionFee) {
        this.tuitionFee = tuitionFee;
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
     * @return the newOtherFee
     */
    public OtherFeeType getNewOtherFee() {
        return newOtherFee;
    }

    /**
     * @param newOtherFee the newOtherFee to set
     */
    public void setNewOtherFee(OtherFeeType newOtherFee) {
        this.newOtherFee = newOtherFee;
    }

    /**
     * @return the selectedOtherFee
     */
    public OtherFeeType getSelectedOtherFee() {
        return selectedOtherFee;
    }

    /**
     * @param selectedOtherFee the selectedOtherFee to set
     */
    public void setSelectedOtherFee(OtherFeeType selectedOtherFee) {
        this.selectedOtherFee = selectedOtherFee;
    }

    @PostConstruct
    public void init() {
        try {
            setTuitionFee(dao.getTuitionFee());
            setBasicFees(dao.getAllBasicFees());
            setOtherFees(dao.getAllOtherFees());
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public void updateTuitionFee() {
        try {
            dao.updateTuitionFee(getTuitionFee().getAmountPerUnit());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Tuition fee updated"));
        } catch (SQLException ex) {
            Logger.getLogger(FeeConfigBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateBasicFee(BasicFee fee) {
        try {
            dao.updateBasicFee(fee.getId(), fee.getAmount());
        } catch (SQLException ex) {
            Logger.getLogger(FeeConfigBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Basic fee updated"));
    }

    public void addOtherFee() {
        try {
            dao.addOtherFee(getNewOtherFee());
            setOtherFees(dao.getAllOtherFees());
            setNewOtherFee(new OtherFeeType());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Other fee added"));
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public void updateOtherFee() {
        try {
            dao.updateOtherFee(getSelectedOtherFee());
            setOtherFees(dao.getAllOtherFees());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Other fee updated"));
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }

    public void deleteOtherFee(int id) {
        try {
            dao.deleteOtherFee(id);
            setOtherFees(dao.getAllOtherFees());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Other fee deleted"));
        } catch (SQLException ex) {
            ex.getMessage();
        }
    }
}
