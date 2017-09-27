/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.io.Serializable;

/**
 *
 * @author adrie
 */
public class Produits implements Serializable
{
    private int idProduit;
    private String nom;
    private String description;
    private int quantite;
    private float prix;

    public Produits()
    {
        
    }

    public Produits(int idProduit, String nom, String description, int quantite, float prix)
    {
        this.idProduit = idProduit;
        this.nom = nom;
        this.description = description;
        this.quantite = quantite;
        this.prix = prix;
    }
    
    public int getIdProduit()
    {
        return idProduit;
    }

    public void setIdProduit(int idProduit)
    {
        this.idProduit = idProduit;
    }

    public String getNom()
    {
        return nom;
    }

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getQuantite()
    {
        return quantite;
    }

    public void setQuantite(int quantite)
    {
        this.quantite = quantite;
    }

    public float getPrix()
    {
        return prix;
    }

    public void setPrix(float prix)
    {
        this.prix = prix;
    }

    public float getPrixTotal()
    {
        return prix*quantite;
    }          
}
