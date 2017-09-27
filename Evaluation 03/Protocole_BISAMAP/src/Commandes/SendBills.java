/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commandes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Julien
 */
public class SendBills implements Serializable
{
    private ArrayList<String> ListIdFacture;
    private byte[] Signature;
    
    public SendBills(ArrayList<String> ListIdFacture, byte[] Signature)
    {
        this.ListIdFacture = ListIdFacture;
        this.Signature = Signature;
    }

    /**
     * @return the ListIdFacture
     */
    public ArrayList<String> getListIdFacture() {
        return ListIdFacture;
    }

    /**
     * @return the Signature
     */
    public byte[] getSignature() {
        return Signature;
    }
}
