/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.AuditLogDAO;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.AuditLog;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class AuditLogBean implements Serializable {

    /**
     * Creates a new instance of AuditLogBean
     */
    private List<AuditLog> logs;

    public AuditLogBean() {
    }

    /**
     * @return the logs
     */
    public List<AuditLog> getLogs() {
        return logs;
    }

    /**
     * @param logs the logs to set
     */
    public void setLogs(List<AuditLog> logs) {
        this.logs = logs;
    }

    @PostConstruct
    public void init() {
        setLogs(new AuditLogDAO().getAllLogs());
    }
}
