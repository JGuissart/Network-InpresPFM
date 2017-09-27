/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RequeteReponseBOOMAP;

import AccessBD.BeanBDAccess;
import Queries.QuerySelect;
import Utils.ConsoleServeur;
import Utils.Requete;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julien
 */
public class RequeteBOOMAP implements Requete, Serializable
{
    public static String BeanTrafic = "_$BDTRAFIC";
    public static int REQUEST_DEC = -1;
    public static int REQUEST_CON = 0;
    public static int REQUEST_LOGIN_CONT = 1;
    public static int REQUEST_GET_XY = 2;
    public static int REQUEST_SEND_WEIGHT = 3;
    public static int REQUEST_GET_LIST = 4;
    public static int REQUEST_SIGNAL_DEP = 5;
    
    private int RequestType;
    private String Parameters;
    private Socket CSocket;
    private String LoginPersonnel;
    
    public RequeteBOOMAP(int t, String param)
    {
        RequestType = t;
        this.setParameters(param);
    }
    
    public RequeteBOOMAP(int t, String param, Socket s)
    {
        RequestType = t;
        this.setParameters(param); 
        CSocket =s;
    }
    
    @Override
    public Runnable createRunnable (final Socket s, final ConsoleServeur cs, final Object param)
    {
        if (RequestType == REQUEST_CON)
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
        else if (RequestType == REQUEST_LOGIN_CONT)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteLoginCont(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_GET_XY)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteGetXY(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_SEND_WEIGHT)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteSendWeight(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_GET_LIST)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteGetList(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_SIGNAL_DEP)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteSignalDep(s, cs, param);
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
            DataInputStream dis = null;
            RequeteBOOMAP req = null;
            try
            {
                System.out.println("Avant réception requête BOOMAP");
                dis = new DataInputStream(SocketCli.getInputStream());
                StringBuilder sb = new StringBuilder();
                byte b;
                while((b = dis.readByte()) != (byte)'\n')
                    sb.append((char)b);
                System.out.println("Message recu : " + sb.toString().trim() + "\n");
                String[] arrayMessage = sb.toString().trim().split("#");
                switch(arrayMessage[0])
                {
                    case "1" : req = new RequeteBOOMAP(RequeteBOOMAP.REQUEST_LOGIN_CONT, sb.toString().trim());
                        break;
                    case "2" : req = new RequeteBOOMAP(RequeteBOOMAP.REQUEST_GET_XY, sb.toString().trim());
                        break;
                    case "3" : req = new RequeteBOOMAP(RequeteBOOMAP.REQUEST_SEND_WEIGHT, sb.toString().trim());
                        break;
                    case "4" : req = new RequeteBOOMAP(RequeteBOOMAP.REQUEST_GET_LIST, sb.toString().trim());
                        break;
                    case "5" : req = new RequeteBOOMAP(RequeteBOOMAP.REQUEST_SIGNAL_DEP, sb.toString().trim());
                        break;
                    default : req = new RequeteBOOMAP(RequeteBOOMAP.REQUEST_DEC, null);
                        break;
                }
                System.out.println("Requete lue par le serveur, instance de " + req.getClass().getName());
            }
            catch (IOException e)
            {
                System.err.println("Erreur ? [" + e.getMessage() + "]");
                System.exit(-1);
            }
            
            if(req.RequestType == RequeteBOOMAP.REQUEST_DEC)
                cont = false;
            else
            {
                Runnable travail = req.createRunnable(SocketCli, cs, param);
                if (travail != null)
                {
                    travail.run();
                    System.out.println("Tâche envoyée");
                }
                else System.out.println("Pas de tâche à envoyer");

                System.out.println("Après réception requête BOOMAP");
            }
        }
    }

    private void traiteRequeteLoginCont(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        System.out.println("Début de traiteRequeteLoginCont : adresse distante = " + adresseDistante);
        String comm = adresseDistante + "#LOGIN_CONT#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        System.out.println("TEST GROUPE : " + Thread.currentThread().getThreadGroup().getParent().getName());
        
        cs.TraceEvenements(comm);
        
        String[] arrayStringParameters = this.getParameters().split("#");
        StringBuilder sbReponse = null;
        
        ReponseBOOMAP rep;
        if(arrayStringParameters[1].equals("ServeurConteneur"))
        {
            if(arrayStringParameters[2].equals("azerty123"))
                sbReponse = new StringBuilder("OK");
            else
                sbReponse = new StringBuilder("BAD_PASSWORD");
        }
        else
            sbReponse = new StringBuilder("BAD_USERNAME");
        
        try 
        {
            DataOutputStream dos;
            dos = new DataOutputStream(SocketCli.getOutputStream());
            dos.write(sbReponse.toString().getBytes());
            dos.flush();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(RequeteBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteGetXY(Socket SocketCli, ConsoleServeur cs, Object param) 
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#GET_XY#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        HashMap hm = (HashMap)param;
        BeanBDAccess bdTrafic = (BeanBDAccess)hm.get(RequeteBOOMAP.BeanTrafic);
        
        String[] arrayStringParameters = this.getParameters().split("#");
        ArrayList<String> listIdContainers = new ArrayList<>();
        listIdContainers.addAll(Arrays.asList(arrayStringParameters)); // On ajoute le tableau de la chaine de requête à une liste
        listIdContainers.remove(0); // On retire le 1er élément ==> numéro de requête
        String strImmatriculation = listIdContainers.remove(0); // On récupère/supprime l'immatriculation du camion de la liste des containers
        String strDestination = listIdContainers.remove(0); // On récupère/supprime la destination des containers de la liste des containers
        String strNumeroReservation = "";
        
        StringBuilder sbReponse = null;
        
        /* Récupération du numéro de réservation pour la liste des containers */
        QuerySelect qsNumeroReservation = new QuerySelect();
        qsNumeroReservation.AddFrom("Container");
        qsNumeroReservation.AddSelect("DISTINCT NumeroReservation");
        String strWhereIn = "";
        for(int i = 0; i < listIdContainers.size(); i++)
        {
            if(i == listIdContainers.size() - 1)
                strWhereIn += "'" + listIdContainers.get(i) + "'";
            else
                strWhereIn += "'" + listIdContainers.get(i) + ",";
        }
        
        qsNumeroReservation.AddWhere("IdContainer IN (" + strWhereIn + ")");
        
        try
        {
            ResultSet rsNumeroReservation = bdTrafic.Select(qsNumeroReservation);
            
            if(!rsNumeroReservation.first()) // Si pas de résultat -> bordel
                //sbReponse = new StringBuilder();
                System.err.println("MERDE");
            else
            {
                rsNumeroReservation.last();
                
                if(rsNumeroReservation.getRow() > 1) // Si plus d'un résultat -> problème
                    System.err.println("Merde");
                else
                    strNumeroReservation = rsNumeroReservation.getString("NumeroReservation");
            }
            rsNumeroReservation.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RequeteBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        try 
        {
            DataOutputStream dos;
            dos = new DataOutputStream(SocketCli.getOutputStream());
            dos.write(sbReponse.toString().getBytes());
            dos.flush();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(RequeteBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteSendWeight(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#SEND_WEIGHT#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        String[] arrayStringParameters = this.getParameters().split("#");
        ArrayList<String> listParametres = new ArrayList<>();
        listParametres.addAll(Arrays.asList(arrayStringParameters)); // On ajoute le tableau de la chaine de requête à une liste
        listParametres.remove(0); // On retire le 1er élément ==> numéro de requête
        
        StringBuilder sbReponse = null;
        
        if(listParametres.size() % 3 != 0)
            sbReponse = new StringBuilder("PARAMETERS_ERROR");
        else
        {
            ArrayList<String> listIdContainers = new ArrayList<>();
            ArrayList<String> listCoordonnees = new ArrayList<>();
            ArrayList<String> listPoids = new ArrayList<>();
            
            for(int i = 0; i < listParametres.size(); i++)
            {
                if(i % 3 == 0)
                    listPoids.add(listParametres.get(i));
                else if(i % 2 == 0)
                    listCoordonnees.add(listParametres.get(i));
                else
                    listIdContainers.add(listParametres.get(i));
            }
            
            if(listIdContainers.size() == listCoordonnees.size() && listCoordonnees.size() == listPoids.size())
            {
                for(int i = 0; i < listIdContainers.size(); i++)
                    System.out.println("Le container " + listIdContainers.get(i) + "se trouvant à l'emplacement " + listCoordonnees.get(i) + "pèse " + listPoids.get(i) + " tonnes");
                
                
            }
        }
        
        try 
        {
            DataOutputStream dos;
            dos = new DataOutputStream(SocketCli.getOutputStream());
            dos.write(sbReponse.toString().getBytes());
            dos.flush();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(RequeteBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteGetList(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#GET_LIST#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        String[] arrayStringParameters = this.getParameters().split("#");
        StringBuilder sbReponse = null;
        
        
        
        try 
        {
            DataOutputStream dos;
            dos = new DataOutputStream(SocketCli.getOutputStream());
            dos.write(sbReponse.toString().getBytes());
            dos.flush();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(RequeteBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteSignalDep(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#SIGNAL_DEP#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        String[] arrayStringParameters = this.getParameters().split("#");
        StringBuilder sbReponse = null;
        
        
        
        try 
        {
            DataOutputStream dos;
            dos = new DataOutputStream(SocketCli.getOutputStream());
            dos.write(sbReponse.toString().getBytes());
            dos.flush();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(RequeteBOOMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the Parameters
     */
    public String getParameters() {
        return Parameters;
    }

    /**
     * @param Parameters the Parameters to set
     */
    public void setParameters(String Parameters) {
        this.Parameters = Parameters;
    }
}