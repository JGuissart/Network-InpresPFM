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
public class ValidateBill implements Serializable
{
    private String idFacture;
    private String Comptable;
    
    public ValidateBill(String idFacture, String Comptable)
    {
        this.idFacture = idFacture;
        this.Comptable = Comptable;
    }

    /**
     * @return the idFacture
     */
    public String getIdFacture() {
        return idFacture;
    }

    /**
     * @return the Comptable
     */
    public String getComptable() {
        return Comptable;
    }
}
