/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import model.Student;

/**
 *
 * @author hrkas
 */
public class SessionUtils {

    public static HttpSession getSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (HttpSession) context.getExternalContext().getSession(true);
    }

    public static Student getLoggedInStudent() {
        return (Student) getSession().getAttribute("loggedInStudent");
    }

    public static String getLoggedInUsername() {
        return (String) getSession().getAttribute("username");
    }

    public static String getLoggedInRole() {
        return (String) getSession().getAttribute("role");
    }
    public static String getStudentNo(){
        return (String) getSession().getAttribute("studno");
    }
}
