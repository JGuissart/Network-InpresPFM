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
public class RecPay implements Serializable
{
    private String IdFacture;
    private double Montant;
    private String NumeroCompteBancaire;

    public RecPay(String IdFacture, double Montant, String NumeroCompteBancaire) {
        this.IdFacture = IdFacture;
        this.Montant = Montant;
        this.NumeroCompteBancaire = NumeroCompteBancaire;
    }

    /**
     * @return the IdFacture
     */
    public String getIdFacture() {
        return IdFacture;
    }

    /**
     * @return the Montant
     */
    public double getMontant() {
        return Montant;
    }

    /**
     * @return the NumeroCompteBancaire
     */
    public String getNumeroCompteBancaire() {
        return NumeroCompteBancaire;
    }
}
