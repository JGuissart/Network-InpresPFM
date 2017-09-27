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
public class ListWaitingSigned implements Serializable
{
    private ListWaiting listWaiting;
    private byte[] Signature;
    
    public ListWaitingSigned(ListWaiting listWaiting, byte[] Signature)
    {
        this.listWaiting = listWaiting;
        this.Signature = Signature;
    }

    /**
     * @return the listWaiting
     */
    public ListWaiting getListWaiting() {
        return listWaiting;
    }

    /**
     * @return the Signature
     */
    public byte[] getSignature() {
        return Signature;
    }
    
    
}
