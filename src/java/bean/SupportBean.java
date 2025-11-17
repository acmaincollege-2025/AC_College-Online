/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.SupportTicketDAO;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.SupportTicket;

/**
 *
 * @author hrkas
 */
@ManagedBean
@SessionScoped
public class SupportBean {

    /**
     * Creates a new instance of SupportBean
     */
    private SupportTicket ticket = new SupportTicket();
    private List<SupportTicket> myTickets;
    private final SupportTicketDAO dao = new SupportTicketDAO();
    
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

        
    public SupportBean() {
    }

    /**
     * @return the ticket
     */
    public SupportTicket getTicket() {
        return ticket;
    }

    /**
     * @param ticket the ticket to set
     */
    public void setTicket(SupportTicket ticket) {
        this.ticket = ticket;
    }
    
    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public String submitTicket() {
        getTicket().setStudentId(loginBean.getUserId());
        if (dao.submitTicket(getTicket())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Ticket submitted successfully!"));
            setTicket(new SupportTicket()); // Reset
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to submit ticket.", null));
        }
        return null;
    }

    public List<SupportTicket> getMyTickets() {
        if (myTickets == null) {
            myTickets = dao.getTicketsByStudentId(loginBean.getUserId());
        }
        return myTickets;
    }

    // For Admin View
    public List<SupportTicket> getAllTickets() {
        return dao.getAllTickets();
    }

    public void respondToTicket(SupportTicket selected, String response) {
        dao.respondToTicket(selected.getTicketId(), response, loginBean.getFullname());
    }
}
