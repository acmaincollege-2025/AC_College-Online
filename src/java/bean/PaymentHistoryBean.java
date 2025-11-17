/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.PaymentDAO;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Payment;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class PaymentHistoryBean {

    /**
     * Creates a new instance of PaymentHistoryBean
     */
    private List<Payment> allPayments;

    private PaymentDAO paymentDAO = new PaymentDAO();

    public PaymentHistoryBean() {
    }

    /**
     * @return the allPayments
     */
    public List<Payment> getAllPayments() {
        return allPayments;
    }

    /**
     * @param allPayments the allPayments to set
     */
    public void setAllPayments(List<Payment> allPayments) {
        this.allPayments = allPayments;
    }
    
    @PostConstruct
    public void init() {
        setAllPayments(paymentDAO.findAllPayments()); // Fetch all payments
    }
}
