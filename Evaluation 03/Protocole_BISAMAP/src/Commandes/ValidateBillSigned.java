/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commandes;

import java.io.Serializable;

/**
 *
 * @author Julien
 */
public class ValidateBillSigned implements Serializable
{
    private ValidateBill validateBill;
    private byte[] Signature;
    
    public ValidateBillSigned(ValidateBill validateBill, byte[] Signature)
    {
        this.validateBill = validateBill;
        this.Signature = Signature;
    }

    /**
     * @return the validateBill
     */
    public ValidateBill getValidateBill() {
        return validateBill;
    }

    /**
     * @return the Signature
     */
    public byte[] getSignature() {
        return Signature;
    }
}
