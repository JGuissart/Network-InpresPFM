/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compta;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Julien
 */
public class Salaire implements Serializable
{
    private String idSalaire;
    private Employe employe;
    private Double MontantBrut;
    private Double RetraitONSS;
    private Double RetraitPrecompte;
    private String Mois;
    private String Annee;
    private ArrayList<Prime> ListPrimes = new ArrayList<>();

    public Salaire(String idSalaire, Employe employe, Double MontantBrut, Double RetraitONSS, Double RetraitPrecompte, String Mois, String Annee) 
    {
        this.idSalaire = idSalaire;
        this.employe = employe;
        this.MontantBrut = MontantBrut;
        this.RetraitONSS = RetraitONSS;
        this.RetraitPrecompte = RetraitPrecompte;
        this.Mois = Mois;
        this.Annee = Annee;
    }
    
    public void AddPrime(Prime p)
    {
        ListPrimes.add(p);
    }

    @Override
    public String toString() {
        return employe.toString();
    }
    
    

    /**
     * @return the idSalaire
     */
    public String getIdSalaire() {
        return idSalaire;
    }

    /**
     * @param idSalaire the idSalaire to set
     */
    public void setIdSalaire(String idSalaire) {
        this.idSalaire = idSalaire;
    }

    /**
     * @return the employe
     */
    public Employe getEmploye() {
        return employe;
    }

    /**
     * @param employe the employe to set
     */
    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    /**
     * @return the MontantBrut
     */
    public Double getMontantBrut() {
        return MontantBrut;
    }

    /**
     * @param MontantBrut the MontantBrut to set
     */
    public void setMontantBrut(Double MontantBrut) {
        this.MontantBrut = MontantBrut;
    }

    /**
     * @return the RetraitONSS
     */
    public Double getRetraitONSS() {
        return RetraitONSS;
    }

    /**
     * @param RetraitONSS the RetraitONSS to set
     */
    public void setRetraitONSS(Double RetraitONSS) {
        this.RetraitONSS = RetraitONSS;
    }

    /**
     * @return the RetraitPrecompte
     */
    public Double getRetraitPrecompte() {
        return RetraitPrecompte;
    }

    /**
     * @param RetraitPrecompte the RetraitPrecompte to set
     */
    public void setRetraitPrecompte(Double RetraitPrecompte) {
        this.RetraitPrecompte = RetraitPrecompte;
    }

    /**
     * @return the Mois
     */
    public String getMois() {
        return Mois;
    }

    /**
     * @param Mois the Mois to set
     */
    public void setMois(String Mois) {
        this.Mois = Mois;
    }

    /**
     * @return the Annee
     */
    public String getAnnee() {
        return Annee;
    }

    /**
     * @param Annee the Annee to set
     */
    public void setAnnee(String Annee) {
        this.Annee = Annee;
    }

    /**
     * @return the ListPrimes
     */
    public ArrayList<Prime> getListPrimes() {
        return ListPrimes;
    }

    /**
     * @param ListPrimes the ListPrimes to set
     */
    public void setListPrimes(ArrayList<Prime> ListPrimes) {
        this.ListPrimes = ListPrimes;
    }
}
