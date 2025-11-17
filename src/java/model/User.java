/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author hrkas
 */
public class User implements Serializable{
    private int Id;
    private String username;
    private String password;
    private String role;
    private String status;
    private String lastname;
    private String firstname;
    private String fullname;

    public User() {
    }

    public User(int Id, String username, String password, String role, String status) {
        this.Id = Id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public User(int Id, String username, String password, String role, String status, String lastname, String firstname) {
        this.Id = Id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.lastname = lastname;
        this.firstname = firstname;
    }

    public User(int Id, String username, String password, String role, String status, String lastname, String firstname, String fullname) {
        this.Id = Id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.lastname = lastname;
        this.firstname = firstname;
        this.fullname = fullname;
    }

    /**
     * @return the Id
     */
    public int getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(int Id) {
        this.Id = Id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    
}
