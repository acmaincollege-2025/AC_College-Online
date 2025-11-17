/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.PaymentDAO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Payment;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class CashierPaymentHistoryBean {

    /**
     * Creates a new instance of CashierPaymentHistoryBean
     */
    private Date startDate;
    private Date endDate;
    private String selectedProgram;
    private String cashierName;
    private List<Payment> filteredPayments;

    private PaymentDAO paymentDAO = new PaymentDAO();

    public CashierPaymentHistoryBean() {
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the selectedProgram
     */
    public String getSelectedProgram() {
        return selectedProgram;
    }

    /**
     * @param selectedProgram the selectedProgram to set
     */
    public void setSelectedProgram(String selectedProgram) {
        this.selectedProgram = selectedProgram;
    }

    /**
     * @return the cashierName
     */
    public String getCashierName() {
        return cashierName;
    }

    /**
     * @param cashierName the cashierName to set
     */
    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    /**
     * @return the filteredPayments
     */
    public List<Payment> getFilteredPayments() {
        return filteredPayments;
    }

    /**
     * @param filteredPayments the filteredPayments to set
     */
    public void setFilteredPayments(List<Payment> filteredPayments) {
        this.filteredPayments = filteredPayments;
    }

    public void init() {
        setFilteredPayments(paymentDAO.findPaymentsWithFilters(null, null, null, null));
    }

    public void filterPayments() {
        setFilteredPayments(paymentDAO.findPaymentsWithFilters(getStartDate(), getEndDate(), getSelectedProgram(), getCashierName()));
    }

    public double getTotalAmount() {
        return filteredPayments.stream()
                .mapToDouble(Payment::getAmountPaid) // assuming amountPaid is double or Double
                .sum();
    }

}
