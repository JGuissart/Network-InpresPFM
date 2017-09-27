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
public class Facture implements Serializable
{
    private String idFacture;
    private String idSociete;
    private String idMouvement;
    private String Mois;
    private String Annee;
    private String Destination;
    private double MontantTotalHTVA;
    private double MontantTotalTVA;
    private boolean Validee;
    private String Login;
    private boolean Envoyee;
    private String TypeEnvoi;
    private boolean Payee;
    private ArrayList<String> ListContainers;
    
    public Facture(String idFacture, String idSociete, String idMouvement, String Mois, String Annee, String Destination, double MontantTotalHTVA, double MontantTotalTVA)
    {
        this.idFacture = idFacture;
        this.idSociete = idSociete;
        this.idMouvement = idMouvement;
        this.Mois = Mois;
        this.Annee = Annee;
        this.Destination = Destination;
        this.MontantTotalHTVA = MontantTotalHTVA;
        this.MontantTotalTVA = MontantTotalTVA;
        this.ListContainers = new ArrayList<>();
    }

    public Facture(String idFacture, String idSociete, String idMouvement, String Mois, String Annee, String Destination, double MontantTotalHTVA, double MontantTotalTVA, boolean Validee, String Login, boolean Envoyee, String TypeEnvoi, boolean Payee)
    {
        this.idFacture = idFacture;
        this.idSociete = idSociete;
        this.idMouvement = idMouvement;
        this.Mois = Mois;
        this.Annee = Annee;
        this.Destination = Destination;
        this.MontantTotalHTVA = MontantTotalHTVA;
        this.MontantTotalTVA = MontantTotalTVA;
        this.Validee = Validee;
        this.Login = Login;
        this.Envoyee = Envoyee;
        this.TypeEnvoi = TypeEnvoi;
        this.Payee = Payee;
        this.ListContainers = new ArrayList<>();
    }
    
    public void AddContainer(String idContainer)
    {
        ListContainers.add(idContainer);
    }

    /**
     * @return the idFacture
     */
    public String getIdFacture() {
        return idFacture;
    }

    /**
     * @param idFacture the idFacture to set
     */
    public void setIdFacture(String idFacture) {
        this.idFacture = idFacture;
    }

    /**
     * @return the idSociete
     */
    public String getIdSociete() {
        return idSociete;
    }

    /**
     * @param idSociete the idSociete to set
     */
    public void setIdSociete(String idSociete) {
        this.idSociete = idSociete;
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
     * @return the MontantTotalHTVA
     */
    public double getMontantTotalHTVA() {
        return MontantTotalHTVA;
    }

    /**
     * @param MontantTotalHTVA the MontantTotalHTVA to set
     */
    public void setMontantTotalHTVA(double MontantTotalHTVA) {
        this.MontantTotalHTVA = MontantTotalHTVA;
    }

    /**
     * @return the MontantTotalTVA
     */
    public double getMontantTotalTVA() {
        return MontantTotalTVA;
    }

    /**
     * @param MontantTotalTVA the MontantTotalTVA to set
     */
    public void setMontantTotalTVA(double MontantTotalTVA) {
        this.MontantTotalTVA = MontantTotalTVA;
    }

    /**
     * @return the Validee
     */
    public boolean isValidee() {
        return Validee;
    }

    /**
     * @param Validee the Validee to set
     */
    public void setValidee(boolean Validee) {
        this.Validee = Validee;
    }

    /**
     * @return the Envoyee
     */
    public boolean isEnvoyee() {
        return Envoyee;
    }

    /**
     * @param Envoyee the Envoyee to set
     */
    public void setEnvoyee(boolean Envoyee) {
        this.Envoyee = Envoyee;
    }

    /**
     * @return the TypeEnvoi
     */
    public String getTypeEnvoi() {
        return TypeEnvoi;
    }

    /**
     * @param TypeEnvoi the TypeEnvoi to set
     */
    public void setTypeEnvoi(String TypeEnvoi) {
        this.TypeEnvoi = TypeEnvoi;
    }

    /**
     * @return the Payee
     */
    public boolean isPayee() {
        return Payee;
    }

    /**
     * @param Payee the Payee to set
     */
    public void setPayee(boolean Payee) {
        this.Payee = Payee;
    }

    /**
     * @return the ListContainers
     */
    public ArrayList<String> getListContainers() {
        return ListContainers;
    }

    /**
     * @param ListContainers the ListContainers to set
     */
    public void setListContainers(ArrayList<String> ListContainers) {
        this.ListContainers = ListContainers;
    }

    /**
     * @return the idMouvement
     */
    public String getIdMouvement() {
        return idMouvement;
    }

    /**
     * @param idMouvement the idMouvement to set
     */
    public void setIdMouvement(String idMouvement) {
        this.idMouvement = idMouvement;
    }

    /**
     * @return the Destination
     */
    public String getDestination() {
        return Destination;
    }

    /**
     * @param Destination the Destination to set
     */
    public void setDestination(String Destination) {
        this.Destination = Destination;
    }

    /**
     * @return the Login
     */
    public String getLogin() {
        return Login;
    }

    /**
     * @param Login the Login to set
     */
    public void setLogin(String Login) {
        this.Login = Login;
    }
}
