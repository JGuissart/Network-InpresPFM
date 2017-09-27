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
public class Reservations implements Serializable
{
    private String dateReservation;
    private int nbrReservation;

    public Reservations(String dateReservation, int nbrReservation)
    {
        this.dateReservation = dateReservation;
        this.nbrReservation = nbrReservation;
    }

    public String getDateReservation()
    {
        return dateReservation;
    }

    public void setDateReservation(String date)
    {
        this.dateReservation = date;
    }

    public int getNbrReservation()
    {
        return nbrReservation;
    }

    public void setNbrReservation(int nbrReservation)
    {
        this.nbrReservation = nbrReservation;
    }


}
