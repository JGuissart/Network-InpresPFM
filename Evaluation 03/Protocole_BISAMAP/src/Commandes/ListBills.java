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
public class ListBills implements Serializable
{
    private String Requete;
    private byte[] Signature;

    public ListBills(String Requete, byte[] Signature)
    {
        this.Requete = Requete;
        this.Signature = Signature;
    }

    /**
     * @return the Requete
     */
    public String getRequete() {
        return Requete;
    }

    /**
     * @return the Signature
     */
    public byte[] getSignature() {
        return Signature;
    }
}
