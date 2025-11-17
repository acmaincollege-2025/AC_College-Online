/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.EmailSettingsDAO;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.EmailSettings;
import util.DatabaseUtil;

/**
 *
 * @author hrkas
 */
@ManagedBean
@ViewScoped
public class EmailSettingsBean {

    /**
     * Creates a new instance of EmailSettingsBean
     */
    private EmailSettings settings = new EmailSettings();

    public EmailSettingsBean() {
    }

    /**
     * @return the settings
     */
    public EmailSettings getSettings() {
        return settings;
    }

    /**
     * @param settings the settings to set
     */
    public void setSettings(EmailSettings settings) {
        this.settings = settings;
    }

    @PostConstruct
    public void init() {
        try {
            setSettings(new EmailSettingsDAO(DatabaseUtil.getConnection()).loadSettings());
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error loading settings"));
        }
    }

    public void save() {
        try {
            new EmailSettingsDAO(DatabaseUtil.getConnection()).saveSettings(settings);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Settings saved.", null));
        } catch (SQLException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Save failed.", null));
        }
    }
}
