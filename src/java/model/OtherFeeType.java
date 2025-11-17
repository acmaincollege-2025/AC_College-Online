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
public class OtherFeeType {
    private int feeTypeId;
    private String feeCode;
    private String descriptions;
    private double amount;

    public OtherFeeType() {
    }

    public OtherFeeType(int feeTypeId, String feeCode, String name, double amount) {
        this.feeTypeId = feeTypeId;
        this.feeCode = feeCode;
        this.descriptions = name;
        this.amount = amount;
    }

    /**
     * @return the feeTypeId
     */
    public int getFeeTypeId() {
        return feeTypeId;
    }

    /**
     * @param feeTypeId the feeTypeId to set
     */
    public void setFeeTypeId(int feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    /**
     * @return the feeCode
     */
    public String getFeeCode() {
        return feeCode;
    }

    /**
     * @param feeCode the feeCode to set
     */
    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    /**
     * @return the descriptions
     */
    public String getDescriptions() {
        return descriptions;
    }

    /**
     * @param descriptions the descriptions to set
     */
    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
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
