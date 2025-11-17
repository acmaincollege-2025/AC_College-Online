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
public class TuitionFee {
    private int id;
    private double amountPerUnit;

    public TuitionFee() {
    }

    public TuitionFee(int id, double amountPerUnit) {
        this.id = id;
        this.amountPerUnit = amountPerUnit;
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
     * @return the amountPerUnit
     */
    public double getAmountPerUnit() {
        return amountPerUnit;
    }

    /**
     * @param amountPerUnit the amountPerUnit to set
     */
    public void setAmountPerUnit(double amountPerUnit) {
        this.amountPerUnit = amountPerUnit;
    }
    
    
}
