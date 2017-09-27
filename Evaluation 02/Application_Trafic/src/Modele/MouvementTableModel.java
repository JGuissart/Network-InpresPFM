/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Julien
 */
public class MouvementTableModel extends DefaultTableModel
{
    public MouvementTableModel()
    {
        FillNewIdentifiers();
    }
    
    private void FillNewIdentifiers()
    {
        super.columnIdentifiers.add("Mouvement");
        super.columnIdentifiers.add("Date d'arrivée");
        super.columnIdentifiers.add("Date de départ");
        super.columnIdentifiers.add("Poids");
        super.columnIdentifiers.add("Container");
        super.columnIdentifiers.add("Transporteur entrant");
        super.columnIdentifiers.add("Transporteur sortant");
        super.columnIdentifiers.add("Destination");
    }
}
