/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hrkas
 */
@ManagedBean
@SessionScoped
public class LayoutBean {

    /**
     * Creates a new instance of LayoutBean
     */
    private boolean sidebarCollapsed = false;
    
    public LayoutBean() {
    }

    /**
     * @return the sidebarCollapsed
     */
    public boolean isSidebarCollapsed() {
        return sidebarCollapsed;
    }

    /**
     * @param sidebarCollapsed the sidebarCollapsed to set
     */
    public void setSidebarCollapsed(boolean sidebarCollapsed) {
        this.sidebarCollapsed = sidebarCollapsed;
    }
    
    public void toggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
    }
}
