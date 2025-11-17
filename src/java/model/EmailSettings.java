/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author hrkas
 */
public class EmailSettings {
    private int id;
    private String senderEmail;
    private String senderPassword; // You can encrypt this for better security
    private String smtpHost;
    private String smtpPort;

    public EmailSettings() {
    }

    public EmailSettings(int id, String senderEmail, String senderPassword, String smtpHost, String smtpPort) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the senderEmail
     */
    public String getSenderEmail() {
        return senderEmail;
    }

    /**
     * @param senderEmail the senderEmail to set
     */
    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    /**
     * @return the senderPassword
     */
    public String getSenderPassword() {
        return senderPassword;
    }

    /**
     * @param senderPassword the senderPassword to set
     */
    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }

    /**
     * @return the smtpHost
     */
    public String getSmtpHost() {
        return smtpHost;
    }

    /**
     * @param smtpHost the smtpHost to set
     */
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    /**
     * @return the smtpPort
     */
    public String getSmtpPort() {
        return smtpPort;
    }

    /**
     * @param smtpPort the smtpPort to set
     */
    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }
    
    
}
