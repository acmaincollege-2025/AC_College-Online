/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author hrkas
 */
@ManagedBean
@SessionScoped
public class NavigationBean {

    /**
     * Creates a new instance of NavigationBean
     */
    private String role;
    private MenuModel menuModel;

    public NavigationBean() {
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the menuModel
     */
    public MenuModel getMenuModel() {
        return menuModel;
    }

    /**
     * @param menuModel the menuModel to set
     */
    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    /**
     * Dynamically return the dashboard page based on role.
     *
     * @return
     */
    @PostConstruct
    public void init() {
        setMenuModel(new DefaultMenuModel());

        // Get role from session
        FacesContext facesContext = FacesContext.getCurrentInstance();
        LoginBean loginBean = (LoginBean) facesContext.getExternalContext()
                .getSessionMap().get("loginBean");

        if (loginBean != null && loginBean.getUserRole() != null) {
            String role = loginBean.getUserRole();

            switch (role) {
                case "registrar":
                    addItem("Dashboard", "/Registrar/dashboard.xhtml");
                    addItem("Manage Courses", "/Registrar/course.xhtml");
                    addItem("Student Records", "/Registrar/students.xhtml");
                    break;
                case "dean":
                    addItem("Dashboard", "/Dean/dashboard.xhtml");
                    addItem("Program Statistics", "/Dean/statistics.xhtml");
                    addItem("Student Progress", "/Dean/progress.xhtml");
                    break;
                case "cashier":
                    addItem("Dashboard", "/Cashier/dashboard.xhtml");
                    addItem("Assessment", "/Cashier/assessment.xhtml");
                    addItem("Payments", "/Cashier/payment.xhtml");
                    break;
                case "admission":
                    addItem("Dashboard", "/Admission/dashboard.xhtml");
                    addItem("Enrollments", "/Admission/enrollment.xhtml");
                    addItem("Student List", "/Admission/student_list.xhtml");
                    break;
                case "student":
                    addItem("Dashboard", "/Student/dashboard.xhtml");
                    addItem("My Enrollment", "/Student/enrollment_status.xhtml");
                    addItem("Payment History", "/Student/payment_history.xhtml");
                    break;
                default:
                    break;
            }
        }
    }

    private void addItem(String label, String outcome) {
        DefaultMenuItem item = DefaultMenuItem.builder()
                .value(label)
                .outcome(outcome)
                .icon("pi pi-angle-right")
                .build();
        getMenuModel().getElements().add(item);
    }

    public String resolveDashboard() {
        String nav = "index.xhtml?faces-redirect=true";
        try {
            switch (getRole()) {
                case "registrar":
                    nav = "/Registrar/dashboard.xhtml?faces-redirect=true";
                    return nav;
                case "dean":
                    nav = "/Dean/dashboard.xhtml?faces-redirect=true";
                    return nav;
                case "cashier":
                    nav = "/Cashier/dashboard.xhtml?faces-redirect=true";
                    return nav;
                case "admission":
                    nav = "/Admission/dashboard.xhtml?faces-redirect=true";
                    return nav;
                case "student":
                    nav = "/Student/dashboard.xhtml?faces-redirect=true";
                    return nav;
                case "admin":
                    nav = "/Admin/admin.xhtml?faces-redirect=true";
                    return nav;
                default:
                    nav = "/index.xhtml?faces-redirect=true";
                    return nav;
            }

        } catch (NullPointerException ex) {
            System.out.println(ex);

        }
        return nav;
    }

    /**
     * Common logout method
     *
     * @return
     */
    public String logout() {
        setRole(null);
        return "/login.xhtml?faces-redirect=true";
    }
}
