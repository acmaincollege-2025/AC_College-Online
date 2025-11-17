/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.DashboardDAO;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class AdminDashboardBean implements Serializable {

    /**
     * Creates a new instance of AdminDashboardBean
     */
    private int totalEnrolled;
    private int totalPrograms;
    private int pendingAccounts;
    
    public AdminDashboardBean() {
    }

    /**
     * @return the totalEnrolled
     */
    public int getTotalEnrolled() {
        return totalEnrolled;
    }

    /**
     * @param totalEnrolled the totalEnrolled to set
     */
    public void setTotalEnrolled(int totalEnrolled) {
        this.totalEnrolled = totalEnrolled;
    }

    /**
     * @return the totalPrograms
     */
    public int getTotalPrograms() {
        return totalPrograms;
    }

    /**
     * @param totalPrograms the totalPrograms to set
     */
    public void setTotalPrograms(int totalPrograms) {
        this.totalPrograms = totalPrograms;
    }

    /**
     * @return the pendingAccounts
     */
    public int getPendingAccounts() {
        return pendingAccounts;
    }

    /**
     * @param pendingAccounts the pendingAccounts to set
     */
    public void setPendingAccounts(int pendingAccounts) {
        this.pendingAccounts = pendingAccounts;
    }

    @PostConstruct
    public void init() {
        setTotalEnrolled(new DashboardDAO().getTotalEnrolledStudents());
        setTotalPrograms(DashboardDAO.getTotalPrograms());
        setPendingAccounts(DashboardDAO.getPendingUserCount());
    }
}
