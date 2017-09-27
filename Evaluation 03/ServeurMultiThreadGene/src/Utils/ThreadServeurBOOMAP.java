/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import AccessBD.BeanBDAccess;
import CommunicationBasNiveau.MessageCPP;
import Generators.Generator;
import Queries.QuerySelect;
import Queries.QueryUpdate;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julien
 */
public class ThreadServeurBOOMAP extends Thread
{
    public static String BeanTrafic = "_$BDTRAFIC";
    
    private static final int LOGIN_CONT = 1;
    private static final int GET_XY = 2;
    private static final int SEND_WEIGHT = 3;
    private static final int GET_LIST = 4;
    private static final int SIGNAL_DEP = 5;
    
    private static final int LOGIN_OK = 101;
    private static final int LOGIN_KO  = 102;
    
    private static final int REPONSE_OK  = 201;
    private static final int REPONSE_KO  = 202;
    
    private ServerSocket SSocket = null;
    private Socket CSocket = null;
    private Properties ConteneursProperties = null;
    private Properties PropertiesFile = null;
    private DataInputStream dis;
    private DataOutputStream dos;
    private int Port;
    private ConsoleServeur GUIApplication;
    private Object Param;
    private String LoginServeur;
    private String PasswordServeur;
    private String Separator;
    private String FinChaine;
    private int NombreMaxContainers;
    private String idTransporteurOut;
    
    public ThreadServeurBOOMAP(int Port, ConsoleServeur GUIApplication, Object Param, String Separator, String FinChaine)
    {
        this.Port = Port;
        this.Param = Param;
        this.GUIApplication = GUIApplication;
        this.Separator = Separator;
        this.FinChaine = FinChaine;
        LoadPropertiesFile();
    }
    
    private void LoadPropertiesFile()
    {
        File f = new File("LoginServeurConteneur.properties");
        if(!f.exists())
        {
            OutputStream os = null;
            try
            {
                os = new FileOutputStream(f);
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(ThreadServeurBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Properties propToCreate = new Properties();
            propToCreate.put("LOGIN", "Serveur");
            propToCreate.put("PASSWORD", "Conteneur");
            
            try
            {
                propToCreate.store(os, "LoginServeurConteneur");
                os.flush();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ThreadServeurBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try
        {
            InputStream is = new FileInputStream(f);
            ConteneursProperties = new Properties();
            ConteneursProperties.load(is);
            LoginServeur = ConteneursProperties.getProperty("LOGIN");
            PasswordServeur = ConteneursProperties.getProperty("PASSWORD");
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ThreadServeurBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ThreadServeurBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run()
    {        
        try
        {
            SSocket = new ServerSocket(Port);
            System.out.println("************ Port RES en attente ************");
            
            CSocket = SSocket.accept();
            GUIApplication.TraceEvenements(CSocket.getRemoteSocketAddress().toString() + "#accept#thread serveur");
            dis = new DataInputStream(CSocket.getInputStream());
            dos = new DataOutputStream(CSocket.getOutputStream());
            
            while(true)
            {
                String strRequete = MessageCPP.BoucleReception(dis, '@', FinChaine.charAt(0));
                System.out.println("strRequete =  " + strRequete);
                String[] arrayRequete = strRequete.split("#");

                String strReponse = null;
                System.out.println("Valeur de arrayRequete[0] = " + arrayRequete[0]);
                switch(Integer.parseInt(arrayRequete[0]))
                {
                    case LOGIN_CONT:
                        strReponse = traiteRequeteLoginCont(CSocket, GUIApplication, strRequete);
                    break;
                        
                    case GET_XY:
                        strReponse = traiteRequeteGetXY(CSocket, GUIApplication, Param, strRequete);
                    break;
                           
                    case SEND_WEIGHT:
                        strReponse = traiteRequeteSendWeight(CSocket, GUIApplication, Param, strRequete);
                    break;
                           
                    case GET_LIST:
                        strReponse = traiteRequeteGetList(CSocket, GUIApplication, Param, strRequete);
                    break;
                           
                    case SIGNAL_DEP:
                        strReponse = traiteRequeteSignalDep(CSocket, GUIApplication, Param, strRequete);
                    break;
                }

                System.out.println("strReponse = " + strReponse);
                dos.write(MessageCPP.ConvertirChaineEnBytes(strReponse));
                dos.flush();
                System.out.println("J'ai envoyé " + strReponse);
            }
        }
        catch (IOException e)
        {
            System.err.println("Erreur d'accept ! ? [" + e.getMessage() + "]");
            System.exit(1);
        }
    }
    
    private String traiteRequeteLoginCont(Socket SocketCli, ConsoleServeur cs, String requete)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        System.out.println("Début de traiteRequeteLoginCont : adresse distante = " + adresseDistante);
        String comm = adresseDistante + "#LOGIN_CONT#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        System.out.println("TEST GROUPE : " + Thread.currentThread().getThreadGroup().getParent().getName());
        
        cs.TraceEvenements(comm);
        
        String[] arrayRequete = requete.split("#");
        String strReponse = null;
        
        if(arrayRequete[1].equals(LoginServeur))
        {
            if(arrayRequete[2].equals(PasswordServeur))
                strReponse = LOGIN_OK + Separator + "OK" + FinChaine;
            else
                strReponse = LOGIN_KO + Separator + "BAD_PASSWORD" + FinChaine;
        }
        else
            strReponse = LOGIN_KO + Separator + "BAD_USERNAME" + FinChaine;
        
        return strReponse;
    }
    
    private String traiteRequeteGetXY(Socket SocketCli, ConsoleServeur cs, Object param, String requete)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#GET_XY#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        HashMap hm = (HashMap)param;
        BeanBDAccess bdTrafic = (BeanBDAccess)hm.get(BeanTrafic);
        
        String[] arrayRequete = requete.split("#");
        ArrayList<String> listIdContainers = new ArrayList<>();
        listIdContainers.addAll(Arrays.asList(arrayRequete)); // On ajoute le tableau de la chaine de requête à une liste
        listIdContainers.remove(0); // On retire le 1er élément ==> numéro de requête
        String strImmatriculation = listIdContainers.remove(0); // On récupère/supprime l'immatriculation du camion de la liste des containers
        String strDestination = listIdContainers.remove(0); // On récupère/supprime la destination des containers de la liste des containers
        String strNumeroReservation = listIdContainers.remove(0); // On récupère/supprime le numéro de réservation de la liste des containers
        
        String strReponse = "";
        
        try
        {
            /* Récupération de la liste des containers pour ce numéro de réservation */
            QuerySelect qs = new QuerySelect();
            qs.AddFrom("Container");
            qs.AddSelect("idContainer, Destination");
            qs.AddWhere("NumeroReservation = '" + strNumeroReservation + "'");
            ResultSet rs = bdTrafic.Select(qs);
            
            if(!rs.first())
                strReponse = REPONSE_KO + Separator + "Le numero de reservation est inconnu." + FinChaine;
            else
            {
                rs.last();
                
                if(rs.getRow() != listIdContainers.size())
                    strReponse = REPONSE_KO + Separator + "Le numero de reservation concerne " + rs.getRow() + " containers. Vous en avez envoye " + listIdContainers.size() + "." + FinChaine;
                else
                {
                    rs.beforeFirst();
                    ArrayList<String> listIdContainersFromDB = new ArrayList<>();
                    ArrayList<String> listDestinationFromDB = new ArrayList<>();
                    
                    while(rs.next())
                    {
                        listIdContainersFromDB.add(rs.getString("idContainer"));
                        listDestinationFromDB.add(rs.getString("Destination"));
                    }
                    
                    int i = 0;
                    
                    /* Vérification de la concordance des identifiants des containers envoyés */
                    for(i = 0; i < listIdContainers.size(); i++)
                    {
                        if(!listIdContainersFromDB.contains(listIdContainers.get(i)))
                            break;
                    }
                    
                    if(i < listIdContainers.size())
                        strReponse = REPONSE_KO + Separator + "Le container " + listIdContainers.get(i) + " n'est pas concerne par le numero de reservation entre." + FinChaine;
                    else
                    {
                        /* Vérification de la concordance de la destination envoyée */
                        for(i = 0; i < listDestinationFromDB.size(); i++)
                        {
                            if(!strDestination.equals(listDestinationFromDB.get(i)))
                                break;
                        }

                        if(i < listDestinationFromDB.size())
                            strReponse = REPONSE_KO + Separator + "Erreur avec la destination entree." + FinChaine;
                        else
                        {
                            /* On récupère les n premiers emplacements réservés dans le parc */
                            QuerySelect qsEmplacements = new QuerySelect();
                            qsEmplacements.AddFrom("Parc");
                            qsEmplacements.AddSelect("Coordonnees");
                            qsEmplacements.AddWhere("EtatEmplacement = 1 LIMIT " + listIdContainers.size());
                            
                            ResultSet rsEmplacements = bdTrafic.Select(qsEmplacements);
                            
                            if(!rsEmplacements.first())
                                strReponse = REPONSE_KO + Separator + "Il n'y a pas d'emplacements reserves ..." + FinChaine;
                            else
                            {
                                rsEmplacements.last();
                                
                                if(rsEmplacements.getRow() != listIdContainers.size())
                                    strReponse = REPONSE_KO + Separator + "Il n'y a pas assez d'emplacements reserves ..." + FinChaine;
                                else
                                {
                                    /* Récupération des coordonnées */
                                    rsEmplacements.beforeFirst();
                                    strReponse = String.valueOf(REPONSE_OK);
                                    ArrayList<String> listCoordonnees = new ArrayList<>();
                                    
                                    while(rsEmplacements.next())
                                    {
                                        strReponse += Separator + rsEmplacements.getString("Coordonnees");
                                        listCoordonnees.add(rsEmplacements.getString("Coordonnees"));
                                    }
                                    
                                    strReponse += FinChaine;
                                    
                                    String strDateArrivee = Generator.GenerateDate();
                                    
                                    for(int j = 0; j < listCoordonnees.size(); j++)
                                    {
                                        QueryUpdate qu = new QueryUpdate();
                                        qu.setTable("Parc");
                                        qu.AddValue("EtatEmplacement", "2");
                                        qu.AddValue("idContainer", "'" + listIdContainers.get(j) + "'");
                                        qu.AddValue("Destination", "'" + strDestination + "'");
                                        qu.AddValue("DateArrivee", "'" + strDateArrivee + "'");
                                        qu.AddWhere("Coordonnees = '" + listCoordonnees.get(j) + "'");
                                        bdTrafic.Update(qu);
                                    }
                                    bdTrafic.Commit();
                                }
                            }
                            rsEmplacements.close();
                        }
                    }
                }
            }
            rs.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ThreadServeurBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return strReponse;
    }
    
    private String traiteRequeteSendWeight(Socket SocketCli, ConsoleServeur cs, Object param, String requete)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#SEND_WEIGHT#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        HashMap hm = (HashMap)param;
        BeanBDAccess bdTrafic = (BeanBDAccess)hm.get(BeanTrafic);
        
        String[] arrayRequete = requete.split("#");
        ArrayList<String> listParametres = new ArrayList<>();
        listParametres.addAll(Arrays.asList(arrayRequete)); // On ajoute le tableau de la chaine de requête à une liste
        listParametres.remove(0); // On retire le 1er élément ==> numéro de requête
        
        String strReponse = "";
        
        try
        {
            if(listParametres.size()% 3 != 0)
                strReponse = REPONSE_KO + Separator + "Erreur de parametres." + FinChaine;
            else
            {
                ArrayList<String> listIdContainers = new ArrayList<>();
                ArrayList<String> listCoordonnees = new ArrayList<>();
                ArrayList<String> listPoids = new ArrayList<>();

                for(int i = 0; i < listParametres.size(); i = i + 3)
                {
                    listIdContainers.add(listParametres.get(i));
                    listCoordonnees.add(listParametres.get(i + 1));
                    listPoids.add(listParametres.get(i + 2));
                }

                if(listIdContainers.size() == listCoordonnees.size() && listCoordonnees.size() == listPoids.size())
                {
                    for(int i = 0; i < listIdContainers.size(); i++)
                        System.out.println("Le container " + listIdContainers.get(i) + " se trouvant à l'emplacement " + listCoordonnees.get(i) + " pèse " + listPoids.get(i) + " tonnes");
                }

                for(int i = 0; i < listIdContainers.size(); i++)
                {
                    QueryUpdate qu = new QueryUpdate();
                    qu.setTable("Parc");
                    qu.AddValue("Poids", "'" + listPoids.get(i) + "'");
                    qu.AddWhere("Coordonnees = '" + listCoordonnees.get(i) + "' AND idContainer = '" + listIdContainers.get(i) + "'");
                    bdTrafic.Update(qu);
                }
                bdTrafic.Commit();
                strReponse = REPONSE_OK + Separator + "Oui" + FinChaine;
            }
        }
        catch (SQLException ex)
        {
            //Logger.getLogger(ThreadServeurBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
            strReponse = REPONSE_KO + Separator + "Non" + FinChaine;
        }
        
        return strReponse;
    }
    
    private String traiteRequeteGetList(Socket SocketCli, ConsoleServeur cs, Object param, String requete)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#GET_LIST#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        HashMap hm = (HashMap)param;
        BeanBDAccess bdTrafic = (BeanBDAccess)hm.get(BeanTrafic);
        
        String[] arrayRequete = requete.split("#");
        idTransporteurOut = arrayRequete[1];
        String strDestination = arrayRequete[2];
        NombreMaxContainers = Integer.parseInt(arrayRequete[3]);
        
        String strReponse = "";
        
        try
        {
            /* Récupération de la liste des containers pour cette destination */
            QuerySelect qs = new QuerySelect();
            qs.AddFrom("Parc");
            qs.AddSelect("Coordonnees");
            qs.AddWhere("EtatEmplacement = 2 AND Destination = '" + strDestination + "' ORDER BY DateArrivee");
            ResultSet rs = bdTrafic.Select(qs);
            
            if(!rs.first())
                strReponse = REPONSE_KO + Separator + "Il n'y a pas de container en attente pour " + strDestination + "." + FinChaine;
            else
            {
                strReponse = String.valueOf(REPONSE_OK);
                do
                    strReponse += Separator + rs.getString("Coordonnees");
                while(rs.next());
                
                strReponse += FinChaine;
            }
            rs.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ThreadServeurBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return strReponse;
    }
    
    private String traiteRequeteSignalDep(Socket SocketCli, ConsoleServeur cs, Object param, String requete)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#SIGNAL_DEP#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        HashMap hm = (HashMap)param;
        BeanBDAccess bdTrafic = (BeanBDAccess)hm.get(BeanTrafic);
        
        String[] arrayRequete = requete.split("#");
        ArrayList<String> listIdContainers = new ArrayList<>();
        listIdContainers.addAll(Arrays.asList(arrayRequete)); // On ajoute le tableau de la chaine de requête à une liste
        listIdContainers.remove(0); // On retire le 1er élément ==> numéro de requête
        String strIdTransporteurOut = listIdContainers.remove(0);
        
        String strReponse = "";
        
        try
        {
            QuerySelect qs = new QuerySelect();
            qs.AddFrom("Transporteur");
            qs.AddSelect("CaracteristiquesTechniques");
            qs.AddWhere("IdTransporteur = '" + strIdTransporteurOut + "'");
            ResultSet rs = bdTrafic.Select(qs);
            if(!rs.first())
            {
                strReponse = REPONSE_KO + Separator + "Transporteur inconnu" + FinChaine;
            }
            else
            {
                for(String idContainer : listIdContainers)
                {
                    QueryUpdate qu = new QueryUpdate();
                    qu.setTable("Parc");
                    qu.AddValue("EtatEmplacement", "0");
                    qu.AddWhere("idContainer = '" + idContainer + "'");
                    bdTrafic.Update(qu);
                }

                bdTrafic.Commit();

                strReponse = REPONSE_OK + Separator + "Oui" + FinChaine;
            }
            
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ThreadServeurBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return strReponse;
    }
}
