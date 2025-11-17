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
public class BasicFee {
    private int id;
    private String feeType; // e.g., "Library", "Insurance", "Medical/Dental"
    private double amount;

    public BasicFee() {
    }

    public BasicFee(int id, String feeType, double amount) {
        this.id = id;
        this.feeType = feeType;
        this.amount = amount;
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
     * @return the feeType
     */
    public String getFeeType() {
        return feeType;
    }

    /**
     * @param feeType the feeType to set
     */
    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    
}
