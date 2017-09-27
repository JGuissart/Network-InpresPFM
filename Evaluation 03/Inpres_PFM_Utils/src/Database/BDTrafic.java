/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import AccessBD.BeanBDAccess;
import Generators.Generator;
import Queries.QueryInsert;
import Queries.QuerySelect;
import Queries.QueryUpdate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Julien
 */
public class BDTrafic
{
    /* SELECT DANS LA TABLE PARC */
    
    public static ArrayList<String> SelectEmplacement(BeanBDAccess bean, String numeroReservation, ArrayList<String> listIdContainers) throws SQLException
    {
        ArrayList<String> listEmplacements = new ArrayList<>();
        Boolean bCorrespondance = true;
        
        QuerySelect qsContainers = new QuerySelect();
        qsContainers.AddFrom("Container");
        qsContainers.AddSelect("idContainer");
        qsContainers.AddWhere("NumeroReservation = '" + numeroReservation + "'");
        
        ResultSet rsContainers = bean.Select(qsContainers);
        
        if(!rsContainers.first())
            listEmplacements.add("ERREUR#Numéro de réservation inconnu");
        else
        {
            rsContainers.last(); // On se positionne sur le dernier tuple
            
            if(rsContainers.getRow() != listIdContainers.size()) // getRow renvoi le numéro du tuple
                listEmplacements.add("ERREUR#Le nombre de containers passé en paramètres ne correspond pas au nombre de containers de la réservation");
            else
            {
                rsContainers.beforeFirst();
                
                while(rsContainers.next())
                {
                    if(listIdContainers.contains(rsContainers.getString(1)))
                        bCorrespondance = true;
                    else
                    {
                        bCorrespondance = false;
                        break;
                    }
                }
                rsContainers.close();
                
                if(bCorrespondance)
                {
                    QuerySelect qsEmplacements = new QuerySelect();
                    qsEmplacements.AddFrom("Parc");
                    qsEmplacements.AddSelect("Coordonnees");
                    qsEmplacements.AddWhere("EtatEmplacement = 1 LIMIT " + listIdContainers.size());
                    
                    ResultSet rsEmplacement = bean.Select(qsEmplacements);
                    
                    while(rsEmplacement.next())
                        listEmplacements.add(rsEmplacement.getString("Coordonnees"));
                    rsEmplacement.close();
                }
                else
                    listEmplacements.add("ERREUR#Problème de correspondance des containers.");
            }
        }
        
        return listEmplacements;
    }
    
    /* MISE A JOUR DE LA TABLE PARC */
    
    public static ArrayList<String> UpdateParcForReservation(BeanBDAccess bean, String dateReservation, ArrayList<String> listIdContainers) throws SQLException
    {
        ArrayList<String> listEmplacements = new ArrayList<>();
        /* On récupère x emplacements libres pour les réserver */
        
        QuerySelect qs = new QuerySelect();
        qs.AddFrom("Parc");
        qs.AddSelect("Coordonnees");
        qs.AddWhere("EtatEmplacement = 0");
        
        ResultSet rs = bean.Select(qs);
        
        if(!rs.first())
            listEmplacements.add("ERREUR#Pas d'emplacement libre.");
        else
        {
            rs.last(); // On se positionne sur le dernier tuple
            
            if(rs.getRow() < listIdContainers.size()) // getRow renvoi le numéro du tuple
                listEmplacements.add("ERREUR#Pas assez d'emplacements libres");
            else
            {
                rs.beforeFirst();
                
                int j = 0;
                while(j < listIdContainers.size() && rs.next())
                {
                    listEmplacements.add(rs.getString("Coordonnees"));
                    j++;
                }
                rs.close();
                
                for(int i = 0; i < listEmplacements.size(); i++)
                {
                    QueryUpdate quParc = new QueryUpdate();
                    quParc.setTable("Parc");
                    quParc.AddValue("EtatEmplacement", "1");
                    quParc.AddValue("DateReservation", "'" + dateReservation + "'");
                    quParc.AddWhere("Coordonnees = '" + listEmplacements.get(i) + "'");
                    bean.Update(quParc);
                }
                bean.Commit();
            }
        }
        return listEmplacements;
    }
    
    public static void UpdateParcForArrival(BeanBDAccess bean, String destination, String dateArrivee, ArrayList<String> listIdContainers, ArrayList<Double> listPoids, ArrayList<String> listCoordonnees) throws SQLException
    {
        for(int i = 0; i < listIdContainers.size(); i++)
        {
            QueryUpdate quParc = new QueryUpdate();
            quParc.setTable("Parc");
            quParc.AddValue("EtatEmplacement", "2");
            quParc.AddValue("Destination", "'" + destination + "'");
            quParc.AddValue("DateArrivee", "'" + dateArrivee + "'");
            quParc.AddValue("IdContainer", "'" + listIdContainers.get(i) + "'");
            quParc.AddValue("Poids", listPoids.get(i).toString());
            quParc.AddWhere("Coordonnees = '" + listCoordonnees.get(i) + "'");
            bean.Update(quParc);
        }
        bean.Commit();
    }
    
    /* MISE A JOUR DE LA TABLE CONTAINER */
    
    public static void PrefixeContainer(BeanBDAccess bean, ArrayList<String> listIdContainers, String idSociete, String idTransporteur) throws SQLException
    {
        QuerySelect qsPrefixe = new QuerySelect();
        ResultSet rs = null;
        
        if(!idSociete.isEmpty() && idTransporteur.isEmpty()) // Dans Application_Trafic --> Requête INPUT_LORRY
        {
            qsPrefixe.AddFrom("Societe");
            qsPrefixe.AddSelect("Prefixe");
            qsPrefixe.AddWhere("idSociete = '" + idSociete + "'");
            rs = bean.Select(qsPrefixe);
            rs.first();
            String strPrefixe = rs.getString("Prefixe");

            for(int i = 0; i < listIdContainers.size(); i++)
                listIdContainers.set(i, strPrefixe + "-" + listIdContainers.get(i));
        }
        else
        {
            if(!idTransporteur.isEmpty() && idSociete.isEmpty()) // Dans Application_Trafic --> Requête INPUT_LORRY_WITHOUT_RESERVATION
            {
                qsPrefixe.AddFrom("Societe INNER JOIN Transporteur USING (idSociete)");
                qsPrefixe.AddSelect("Prefixe");
                qsPrefixe.AddWhere("Transporteur.idTransporteur = '" + idTransporteur + "'");
                rs = bean.Select(qsPrefixe);
                rs.first();
                String strPrefixe = rs.getString("Prefixe");

                for(int i = 0; i < listIdContainers.size(); i++)
                    listIdContainers.set(i, strPrefixe + "-" + listIdContainers.get(i));
            }
        }
        rs.close();
        
        System.out.println("Containers préfixés : ");
        for(int i = 0; i < listIdContainers.size(); i++)
            System.out.println(listIdContainers.get(i));
    }
    
    public static String InsertContainerForILWR(BeanBDAccess bean, ArrayList<String> listIdContainers, String idTransporteur, String destination) throws SQLException
    {
        if(!idTransporteur.isEmpty())
        {
            String strNumeroReservation = Generator.GenerateNumeroReservation();
            
            QuerySelect qs = new QuerySelect();
            qs.AddFrom("Transporteur");
            qs.AddSelect("idSociete");
            qs.AddWhere("idTransporteur = '" + idTransporteur + "'");
            ResultSet rs = bean.Select(qs);
            if(!rs.first())
                System.err.println("idTransporteur inconnu ...");
            else
            {
                String strIdSociete = rs.getString("idSociete");
                System.out.println("strIdSociete = " + strIdSociete);

                for(int i = 0; i < listIdContainers.size(); i++)
                {
                    QueryInsert qiContainer = new QueryInsert();
                    qiContainer.setTable("Container");
                    qiContainer.AddValue("IdSociete", "'" + strIdSociete + "'");
                    qiContainer.AddValue("NumeroReservation", "'" + strNumeroReservation + "'");
                    qiContainer.AddValue("Destination", "'" + destination + "'");
                    qiContainer.AddValue("IdContainer", "'" + listIdContainers.get(i) + "'");
                    bean.Insert(qiContainer);
                }
            }
            bean.Commit();
            
            return strNumeroReservation;
        }
        
        return "";
    }
    
    /* MISE A JOUR DE LA TABLE MOUVEMENT */
    
    public static void InsertMouvement(BeanBDAccess bean, String dateArrivee, ArrayList<Double> listPoids, ArrayList<String> listIdContainers, String idTransporteurIn, String destination) throws SQLException
    {
        String strIdMouvement = Generator.GenerateIdMouvement(); // On génère l'identifiant du mouvement (un identifiant pour une liste de container)
        for(int i = 0; i < listIdContainers.size(); i++)
        {
            QueryInsert qiMouvement = new QueryInsert();
            qiMouvement.setTable("Mouvement");
            qiMouvement.AddValue("IdMouvement", "'" + strIdMouvement + "'");
            qiMouvement.AddValue("DateArrivee", "'" + dateArrivee + "'");
            qiMouvement.AddValue("Destination", "'" + destination + "'");
            qiMouvement.AddValue("IdTransporteurIn", "'" + idTransporteurIn + "'");
            qiMouvement.AddValue("IdContainer", "'" + listIdContainers.get(i) + "'");
            qiMouvement.AddValue("Poids", listPoids.get(i).toString());
            bean.Insert(qiMouvement);
        }
        bean.Commit();
    }
}
