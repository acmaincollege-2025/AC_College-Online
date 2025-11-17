/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listerner;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hrkas
 */
public class LoginRedirectPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession(false);

        String currentPage = context.getViewRoot().getViewId();
        boolean isLoginPage = currentPage.contains("login.xhtml");

        Object user = (session != null) ? session.getAttribute("user") : null;

        if (!isLoginPage && user == null) {
            try {
                context.getExternalContext().redirect(request.getContextPath() + "/login.xhtml");
            } catch (IOException ex) {
                ex.getMessage();
            }
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    
}
