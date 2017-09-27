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
public class ListWaiting implements Serializable
{
    private String TypeListe;
    private String idSociete;
    
    public ListWaiting(String TypeListe)
    {
        this.TypeListe = TypeListe;
        this.idSociete = null;
    }
    
    public ListWaiting(String TypeListe, String idSociete)
    {
        this.TypeListe = TypeListe;
        this.idSociete = idSociete;
    }

    /**
     * @return the TypeListe
     */
    public String getTypeListe() {
        return TypeListe;
    }

    /**
     * @return the idSociete
     */
    public String getIdSociete() {
        return idSociete;
    }
}
