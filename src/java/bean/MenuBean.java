/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author hrkas
 */
@ManagedBean
@SessionScoped
public class MenuBean {

    /**
     * Creates a new instance of MenuBean
     */
    private MenuModel menuModel;
    private String role; // Default role, you can set this on login

    public MenuBean() {
    }

    /**
     * @return the menuModel
     */
    public MenuModel getMenuModel() {
        return menuModel;
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

    @PostConstruct
    public void init() {
        menuModel = new DefaultMenuModel();

        switch (role) {
            case "registrar":
                createRegistrarMenu();
                break;
            case "admission":
                createAdmissionMenu();
                break;
            case "dean":
                createDeanMenu();
                break;
            case "cashier":
                createCashierMenu();
                break;
        }
    }

    private void createRegistrarMenu() {
        DefaultSubMenu regMenu = DefaultSubMenu.builder().label("Registrar Menu").build();

        regMenu.getElements().add(DefaultMenuItem.builder()
                .value("Dashboard")
                .outcome("registrar")
                .icon("pi pi-chart-line")
                .build());

        regMenu.getElements().add(DefaultMenuItem.builder()
                .value("Student Management")
                .outcome("student")
                .icon("pi pi-users")
                .build());

        regMenu.getElements().add(DefaultMenuItem.builder()
                .value("Enrollment")
                .outcome("enrollment")
                .icon("pi pi-book")
                .build());

        regMenu.getElements().add(DefaultMenuItem.builder()
                .value("Courses")
                .outcome("course")
                .icon("pi pi-list")
                .build());

        regMenu.getElements().add(DefaultMenuItem.builder()
                .value("Programs")
                .outcome("programs")
                .icon("pi pi-folder")
                .build());

        menuModel.getElements().add(regMenu);
    }

    private void createAdmissionMenu() {
        DefaultSubMenu admMenu = DefaultSubMenu.builder().label("Admission Menu").build();

        admMenu.getElements().add(DefaultMenuItem.builder()
                .value("Student Registration")
                .outcome("admission")
                .icon("pi pi-user-plus")
                .build());

        admMenu.getElements().add(DefaultMenuItem.builder()
                .value("Enrollment")
                .outcome("/Admission/admission_enrollment.xhtml")
                .icon("pi pi-book")
                .build());

        menuModel.getElements().add(admMenu);
    }

    private void createDeanMenu() {
        DefaultSubMenu deanMenu = DefaultSubMenu.builder().label("Dean Menu").build();

        deanMenu.getElements().add(DefaultMenuItem.builder()
                .value("Dashboard")
                .outcome("dean")
                .icon("pi pi-home")
                .build());

        deanMenu.getElements().add(DefaultMenuItem.builder()
                .value("Enrollment List")
                .outcome("/Dean/enrollment-list.xhtml")
                .icon("pi pi-table")
                .build());

        menuModel.getElements().add(deanMenu);
    }

    private void createCashierMenu() {
        DefaultSubMenu cashMenu = DefaultSubMenu.builder().label("Cashier Menu").build();

        cashMenu.getElements().add(DefaultMenuItem.builder()
                .value("Dashboard")
                .outcome("cashier")
                .icon("pi pi-wallet")
                .build());

        menuModel.getElements().add(cashMenu);
    }

}
