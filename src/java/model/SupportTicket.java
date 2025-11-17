/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author hrkas
 */
public class SupportTicket {
    private int ticketId;
    private int studentId;
    private String subject;
    private String message;
    private String status;
    private String response;
    private String respondedBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public SupportTicket() {
    }

    public SupportTicket(int ticketId, int studentId, String subject, String message, String status, String response, String respondedBy, Timestamp createdAt, Timestamp updatedAt) {
        this.ticketId = ticketId;
        this.studentId = studentId;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.response = response;
        this.respondedBy = respondedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * @return the ticketId
     */
    public int getTicketId() {
        return ticketId;
    }

    /**
     * @param ticketId the ticketId to set
     */
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * @return the studentId
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @return the respondedBy
     */
    public String getRespondedBy() {
        return respondedBy;
    }

    /**
     * @param respondedBy the respondedBy to set
     */
    public void setRespondedBy(String respondedBy) {
        this.respondedBy = respondedBy;
    }

    /**
     * @return the createdAt
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the updatedAt
     */
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    
}
