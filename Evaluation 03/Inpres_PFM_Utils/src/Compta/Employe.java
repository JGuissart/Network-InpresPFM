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
public class Employe implements Serializable
{
    private String Login;
    private String Nom;
    private String Prenom;
    private String Fonction;

    public Employe(String Login, String Nom, String Prenom, String Fonction) 
    {
        this.Login = Login;
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.Fonction = Fonction;
    }

    @Override
    public String toString() {
        return Prenom + " " + Nom;
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

    /**
     * @return the Nom
     */
    public String getNom() {
        return Nom;
    }

    /**
     * @param Nom the Nom to set
     */
    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    /**
     * @return the Prenom
     */
    public String getPrenom() {
        return Prenom;
    }

    /**
     * @param Prenom the Prenom to set
     */
    public void setPrenom(String Prenom) {
        this.Prenom = Prenom;
    }

    /**
     * @return the Fonction
     */
    public String getFonction() {
        return Fonction;
    }

    /**
     * @param Fonction the Fonction to set
     */
    public void setFonction(String Fonction) {
        this.Fonction = Fonction;
    }
}
