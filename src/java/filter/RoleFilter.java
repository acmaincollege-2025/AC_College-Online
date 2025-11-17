/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hrkas
 */
public class RoleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String role = (session != null) ? (String) session.getAttribute("userRole") : null;

        if (uri.contains("/registrar/") && !"Registrar".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/unauthorized.xhtml");
            return;
        }

        if (uri.contains("/dean/") && !"Dean".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/unauthorized.xhtml");
            return;
        }

        if (uri.contains("/admission/") && !"Admission".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/unauthorized.xhtml");
            return;
        }

        if (uri.contains("/cashier/") && !"Cashier".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/unauthorized.xhtml");
            return;
        }

        if (uri.contains("/student/") && !"Student".equals(role)) {
            res.sendRedirect(req.getContextPath() + "/unauthorized.xhtml");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //To change body of generated methods, choose Tools | Templates.
    }

}
