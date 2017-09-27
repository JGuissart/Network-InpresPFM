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
public class ComputeSalSigned implements Serializable
{
    private ComputeSal computeSal;
    private byte[] HMAC;
    private byte[] Signature;
    
    public ComputeSalSigned(ComputeSal computeSal, byte[] HMAC, byte[] Signature)
    {
        this.computeSal = computeSal;
        this.HMAC = HMAC;
        this.Signature = Signature;
    }

    /**
     * @return the computeSal
     */
    public ComputeSal getComputeSal() {
        return computeSal;
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
