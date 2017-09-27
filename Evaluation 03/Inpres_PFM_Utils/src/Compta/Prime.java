/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compta;

import java.io.Serializable;

/**
 *
 * @author Julien
 */
public class Prime implements Serializable
{
    private String idPrime;
    private Double Montant;
    private String DateOctroi;
    private String Motif;

    public Prime(String idPrime, Double Montant, String DateOctroi, String Motif) {
        this.idPrime = idPrime;
        this.Montant = Montant;
        this.DateOctroi = DateOctroi;
        this.Motif = Motif;
    }

    /**
     * @return the idPrime
     */
    public String getIdPrime() {
        return idPrime;
    }

    /**
     * @param idPrime the idPrime to set
     */
    public void setIdPrime(String idPrime) {
        this.idPrime = idPrime;
    }

    /**
     * @return the Montant
     */
    public Double getMontant() {
        return Montant;
    }

    /**
     * @param Montant the Montant to set
     */
    public void setMontant(Double Montant) {
        this.Montant = Montant;
    }

    /**
     * @return the DateOctroi
     */
    public String getDateOctroi() {
        return DateOctroi;
    }

    /**
     * @param DateOctroi the DateOctroi to set
     */
    public void setDateOctroi(String DateOctroi) {
        this.DateOctroi = DateOctroi;
    }

    /**
     * @return the Motif
     */
    public String getMotif() {
        return Motif;
    }

    /**
     * @param Motif the Motif to set
     */
    public void setMotif(String Motif) {
        this.Motif = Motif;
    }
}
