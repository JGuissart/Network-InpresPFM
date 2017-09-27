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
public class RecPayAuth implements Serializable
{
    private RecPay recPay;
    private byte[] HMAC;

    public RecPayAuth(RecPay recPay, byte[] HMAC) {
        this.recPay = recPay;
        this.HMAC = HMAC;
    }

    /**
     * @return the recPay
     */
    public RecPay getRecPay() {
        return recPay;
    }

    /**
     * @return the HMAC
     */
    public byte[] getHMAC() {
        return HMAC;
    }
}
