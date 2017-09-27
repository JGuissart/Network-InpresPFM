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
public class ValidateSalSigned implements Serializable
{
    private ValidateSal validateSal;
    private byte[] HMAC;
    private byte[] Signature;
    
    public ValidateSalSigned(ValidateSal validateSal, byte[] HMAC, byte[] Signature)
    {
        this.validateSal = validateSal;
        this.HMAC = HMAC;
        this.Signature = Signature;
    }

    /**
     * @return the computeSal
     */
    public ValidateSal getValidateSal() {
        return validateSal;
    }

    /**
     * @return the HMAC
     */
    public byte[] getHMAC() {
        return HMAC;
    }

    /**
     * @return the Signature
     */
    public byte[] getSignature() {
        return Signature;
    }
}
