/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RequeteReponseTRAMAP;

import AccessBD.BeanBDAccess;
import Database.BDTrafic;
import Generators.Generator;
import Queries.QuerySelect;
import Utils.ConsoleServeur;
import Utils.Convert;
import Utils.DataSet;
import Utils.Requete;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Julien
 */
public class RequeteTRAMAP implements Requete, Serializable
{
    public static String BeanTrafic = "_$BDTRAFIC";
    public static int REQUEST_DEC = -1;
    public static int REQUEST_CON = 0;
    public static int REQUEST_LOGIN = 1;
    public static int REQUEST_INPUT_LORRY = 2;
    public static int REQUEST_INPUT_LORRY_WITHOUT_RESERVATION = 3;
    public static int REQUEST_LIST_OPERATIONS = 4;
    public static int REQUEST_LOGOUT = 5;
    public static int REQUEST_DATABASE = 6;
    
    private int RequestType;
    private String Parameters;
    private Socket CSocket;
    
    public RequeteTRAMAP(int RequestType, String Parameters)
    {
        this.RequestType = RequestType;
        this.Parameters = Parameters;
    }
    
    public RequeteTRAMAP(int RequestType, String Parameters, Socket CSocket)
    {
        this.RequestType = RequestType;
        this.Parameters = Parameters;
        this.CSocket = CSocket;
    }

    @Override
    public Runnable createRunnable(final Socket s, final ConsoleServeur cs, final Object param)
    {
        if (getRequestType() == REQUEST_CON)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteConnexion(s, cs, param);
                }
            };
        }
        else if (getRequestType() == REQUEST_LOGIN)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteLogin(s, cs, param);
                }
            };
        }
        else if (getRequestType() == REQUEST_INPUT_LORRY)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteInputLorry(s, cs, param);
                }
            };
        }
        else if (getRequestType() == REQUEST_INPUT_LORRY_WITHOUT_RESERVATION)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteInputLorryWithoutReservation(s, cs, param);
                }
            };
        }
        else if (getRequestType() == REQUEST_LIST_OPERATIONS)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteListOperations(s, cs, param);
                }
            };
        }
        else if (getRequestType() == REQUEST_LOGOUT)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteLogout(s, cs, param);
                }
            };
        }
        else if (getRequestType() == REQUEST_DATABASE)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteDatabase(s, cs, param);
                }
            };
        }
        else 
            return null;
    }
    
    private void traiteRequeteConnexion(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        boolean cont = true;
        
        while(cont)
        {
            ObjectInputStream ois = null;
            RequeteTRAMAP req = null;
            try
            { 
                System.out.println("Avant réception requête TRAMAP");
                ois = new ObjectInputStream(SocketCli.getInputStream());
                req = (RequeteTRAMAP)ois.readObject();
                System.out.println("Requete lue par le serveur, instance de " + req.getClass().getName());
            }
            catch (ClassNotFoundException e)
            {
                System.err.println("Erreur de def de classe [" + e.getMessage() + "]");
            }
            catch (IOException e)
            {
                System.err.println("Erreur ? [" + e.getMessage() + "]");
                System.exit(-1);
            }
            
            if(req.getRequestType() == RequeteTRAMAP.REQUEST_DEC)
                cont = false;
            else
            {
                Runnable travail = req.createRunnable(SocketCli, cs, param);
                if (travail != null)
                {
                    travail.run();
                    System.out.println("Tâche envoyée");
                }
                else
                    System.out.println("Pas de tâche à envoyer");

                System.out.println("Après réception requête TRAMAP");
            }
        }
    }

    private void traiteRequeteLogin(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        System.out.println("Début de traiteRequeteLogin : adresse distante = " + adresseDistante);
        String comm = adresseDistante + "#LOGIN#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        System.out.println("TEST GROUPE : " + Thread.currentThread().getThreadGroup().getParent().getName());
        
        cs.TraceEvenements(comm);
        
        String[] log = this.getParameters().split("#");
        HashMap hm = (HashMap)param;
        BeanBDAccess bdTrafic = (BeanBDAccess)hm.get(RequeteTRAMAP.BeanTrafic);
        QuerySelect qs = new QuerySelect();
        qs.AddSelect("*");
        qs.AddFrom("user");
        qs.AddWhere("Login = '" + log[0] + "'");
        
        try 
        {
            ResultSet rs = bdTrafic.Select(qs);
            ReponseTRAMAP rep;
            if(!rs.next())
                rep = new ReponseTRAMAP(ReponseTRAMAP.LOGIN_KO, "BAD_LOGIN");
            else if(rs.getString("Password").equals(log[1]))
                rep = new ReponseTRAMAP(ReponseTRAMAP.LOGIN_OK, "OK");
            else
                rep = new ReponseTRAMAP(ReponseTRAMAP.LOGIN_KO, "BAD_PASSWORD");
                
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RequeteTRAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(RequeteTRAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteInputLorry(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#INPUT_LORRY#Thread Client : " + String.valueOf(Thread.currentThread().getId());        
        cs.TraceEvenements(comm);
        
        String[] arrayStringParameters = this.getParameters().split("#"); // Récupération de la chaine de requête
        ArrayList<String> listIdContainers = new ArrayList<>();
        listIdContainers.addAll(Arrays.asList(arrayStringParameters)); // On ajoute le tableau de la chaine de requête à une liste
        String strNumeroReservation = listIdContainers.remove(0); // On récupère/supprime l'élément à l'index 0 (==> numéro de réservation)
        System.out.println("strNumeroReservation = " + strNumeroReservation);
        
        HashMap hm = (HashMap)param;
        BeanBDAccess bdTrafic = (BeanBDAccess)hm.get(RequeteTRAMAP.BeanTrafic);
        
        try 
        {
            String strReponse = "";
            ReponseTRAMAP rep = null;
            ArrayList<String> listEmplacements = BDTrafic.SelectEmplacement(bdTrafic, strNumeroReservation, listIdContainers);
            
            String strFirstElemList = listEmplacements.get(0);
            String[] arrayFirstElemList = strFirstElemList.split("#");
            if(arrayFirstElemList[0].equals("ERREUR"))
                rep = new ReponseTRAMAP(ReponseTRAMAP.REPONSE_KO, arrayFirstElemList[1]);
            else
            {
                for(int i = 0; i < listIdContainers.size(); i++)
                    strReponse += listIdContainers.get(i) + " => " + listEmplacements.get(i) + "\n";

                System.out.println("Valeur de strReponse = " + strReponse);

                rep = new ReponseTRAMAP(ReponseTRAMAP.REPONSE_OK, strReponse);
            }
                
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RequeteTRAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(RequeteTRAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteInputLorryWithoutReservation(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#INPUT_LORRY_WITHOUT_RESERVATION#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        HashMap hm = (HashMap)param;
        BeanBDAccess bdTrafic = (BeanBDAccess)hm.get(RequeteTRAMAP.BeanTrafic);
        
        String[] arrayStringParameters = this.getParameters().split("#"); // Récupération de la chaine de requête
        ArrayList<String> listIdContainers = new ArrayList<>();
        listIdContainers.addAll(Arrays.asList(arrayStringParameters)); // On ajoute le tableau de la chaine de requête à une liste
        String strImmatriculation = listIdContainers.remove(0); // On supprime l'élément à l'index 0 (==> immatriculation)
        String strDestination = listIdContainers.remove(listIdContainers.size() - 1); // On récupère la destination
        System.out.println("strDestination = " + strDestination);
        
        QuerySelect qs = new QuerySelect();
        qs.AddSelect("Coordonnees");
        qs.AddFrom("Parc");
        qs.AddWhere("EtatEmplacement = 0 LIMIT " + listIdContainers.size());
        
        try 
        {
            ResultSet rs = bdTrafic.Select(qs);
            ReponseTRAMAP rep = null;
            if(!rs.first()) // Pas d'emplacement libre
                rep = new ReponseTRAMAP(ReponseTRAMAP.REPONSE_KO, "NO_PLACE_AVAILABLE");
            else
            {
                rs.last();
                int iNombreResultat = rs.getRow(); // On récupère le nombre d'emplacements libres
                if(iNombreResultat < listIdContainers.size())
                    rep = new ReponseTRAMAP(ReponseTRAMAP.REPONSE_KO, "TOO_MANY_CONTAINERS");
                else
                {
                    rs.close();
                    /* On préfixe les containers */
                    BDTrafic.PrefixeContainer(bdTrafic, listIdContainers, "", strImmatriculation);
                    
                    /* Insertion des containers dans la table container */
                    String strNumeroReservation = BDTrafic.InsertContainerForILWR(bdTrafic, listIdContainers, strImmatriculation, strDestination);
                    String strDateReservation = Generator.GenerateDate();
                    ArrayList<String> listEmplacements = BDTrafic.UpdateParcForReservation(bdTrafic, strDateReservation, listIdContainers);
                    
                    String strFirstElemList = listEmplacements.get(0);
                    String[] arrayFirstElemList = strFirstElemList.split("#");
                    if(arrayFirstElemList[0].equals("ERREUR"))
                        rep = new ReponseTRAMAP(ReponseTRAMAP.REPONSE_KO, arrayFirstElemList[1]);
                    else
                    {
                        System.out.println("Nombre de coordonnées récupérées = " + listEmplacements.size());
                        String strReponse = strNumeroReservation;

                        for(int i = 0; i < listIdContainers.size(); i++)
                            strReponse += "#" + listIdContainers.get(i) + " => " + listEmplacements.get(i);

                        System.out.println("Valeur de strReponse = " + strReponse);

                        rep = new ReponseTRAMAP(ReponseTRAMAP.REPONSE_OK, strReponse);
                    }
                }
            }
            
            ObjectOutputStream oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RequeteTRAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(RequeteTRAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteListOperations(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#LIST_OPERATIONS#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        ReponseTRAMAP rep = null;
        System.out.println("Valeur de paramètre = " + this.getParameters());
        String[] Parametres = this.getParameters().split("#");
        HashMap hm = (HashMap)param;
        BeanBDAccess bdTrafic = (BeanBDAccess)hm.get(RequeteTRAMAP.BeanTrafic);
        QuerySelect qs = new QuerySelect();
        qs.AddSelect("*");
        qs.AddFrom("Mouvement");
        
        try 
        {
            if(Parametres[0].equals("DESTINATION"))
                qs.AddWhere("Destination = '" + Parametres[1]
                            + "' AND STR_TO_DATE(DateArrivee, '%d/%m/%y') BETWEEN STR_TO_DATE('" + Parametres[2] + "', '%d/%m/%y') AND STR_TO_DATE('" + Parametres[3] + "', '%d/%m/%y')");
            else
            {
                if(Parametres[0].equals("SOCIETE"))
                {
                    String strIdSociete = Parametres[1];
                    String strIdTransporteur1, strIdTransporteur2, strIdTransporteur3;

                    QuerySelect qsTransporteur = new QuerySelect();
                    qsTransporteur.AddFrom("Transporteur");
                    qsTransporteur.AddSelect("idTransporteur");
                    qsTransporteur.AddWhere("idSociete = '" + strIdSociete + "'");

                    ResultSet rsTransporteur = bdTrafic.Select(qsTransporteur);
                    if(!rsTransporteur.first())
                        rep = new ReponseTRAMAP(ReponseTRAMAP.REPONSE_KO, "Il n'y a pas de société nommée " + strIdSociete);
                    else
                    {
                        strIdTransporteur1 = rsTransporteur.getString("idTransporteur");
                        rsTransporteur.next();
                        strIdTransporteur2 = rsTransporteur.getString("idTransporteur");
                        rsTransporteur.next();
                        strIdTransporteur3 = rsTransporteur.getString("idTransporteur");
                        rsTransporteur.next();

                        qs.AddWhere("(IdTransporteurIn IN ('" + strIdTransporteur1 + "', '" + strIdTransporteur2 + "', '"+ strIdTransporteur3 + "') "
                                + "OR IdTransporteurOut IN ('" + strIdTransporteur1 + "', '" + strIdTransporteur2 + "', '"+ strIdTransporteur3 + "')) "
                                + "AND STR_TO_DATE(DateArrivee, '%d/%m/%y') BETWEEN STR_TO_DATE('" + Parametres[2] + "', '%d/%m/%y') AND STR_TO_DATE('" + Parametres[3] + "', '%d/%m/%y')");
                        
                        ResultSet rs = bdTrafic.Select(qs);
                        if(!rs.first())
                            rep = new ReponseTRAMAP(ReponseTRAMAP.REPONSE_KO, null);
                        else
                        {
                            DataSet ds = Convert.ResultSetToDataSet(rs);
                            rep = new ReponseTRAMAP(ReponseTRAMAP.REPONSE_OK, ds);
                        }
                    }
                }
            }
            
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RequeteTRAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(RequeteTRAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteLogout(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#LOGOUT#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
    }
    
    private void traiteRequeteDatabase(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#DATABASE#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        HashMap hm = (HashMap)param;
        BeanBDAccess bdTrafic = (BeanBDAccess)hm.get(RequeteTRAMAP.BeanTrafic);
        ReponseTRAMAP rep = null;
        System.err.println("Valeur du paramètre = " + this.getParameters());
        String[] strRequestDb = this.getParameters().split("#");
        String strSelect = strRequestDb[0];
        String strFrom = strRequestDb[1];
        String strWhere = "";
        if(!strRequestDb[2].equals("NULL"))
            strWhere = strRequestDb[2];
        
        QuerySelect qs = new QuerySelect(strSelect, strFrom);
        if(!strWhere.isEmpty())
            qs.AddWhere(strWhere);
        try
        {
            ResultSet rs = bdTrafic.Select(qs);
            
            DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
            while(rs.next())
                dcbm.addElement(rs.getString(1));
            rs.close();
            
            HashMap<String, Object> hashModel = new HashMap<>();
            hashModel.put("DCBM", dcbm);
            
            rep = new ReponseTRAMAP(ReponseTRAMAP.REPONSE_OK, hashModel);
            
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RequeteTRAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(RequeteTRAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the arrayStringParameters
     */
    public String getParameters() {
        return Parameters;
    }

    /**
     * @param Parameters the arrayStringParameters to set
     */
    public void setParameters(String Parameters) {
        this.Parameters = Parameters;
    }

    /**
     * @return the RequestType
     */
    public int getRequestType() {
        return RequestType;
    }
}
