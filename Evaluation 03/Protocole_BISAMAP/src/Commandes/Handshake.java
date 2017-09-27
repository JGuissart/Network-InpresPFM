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
public class Handshake implements Serializable
{
    private byte[] CleSymetriqueChiffrement;
    private byte[] CleSymetriqueHMAC;
    private String Fonction;
    
    public Handshake(byte[] CleSymetriqueChiffrement, byte[] CleSymetriqueHMAC, String Fonction)
    {
        this.CleSymetriqueChiffrement = CleSymetriqueChiffrement;
        this.CleSymetriqueHMAC = CleSymetriqueHMAC;
        this.Fonction = Fonction;
    }

    /**
     * @return the CleSymetriqueChiffrement
     */
    public byte[] getCleSymetriqueChiffrement() {
        return CleSymetriqueChiffrement;
    }

    /**
     * @return the CleSymetriqueHMAC
     */
    public byte[] getCleSymetriqueHMAC() {
        return CleSymetriqueHMAC;
    }

    /**
     * @return the Fonction
     */
    public String getFonction() {
        return Fonction;
    }
}
