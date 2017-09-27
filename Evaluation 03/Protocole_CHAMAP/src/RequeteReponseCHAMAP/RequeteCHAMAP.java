/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RequeteReponseCHAMAP;

import AccessBD.BeanBDAccess;
import Encryption.Cryptographie;
import Utils.ConsoleServeur;
import Utils.Requete;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julien
 */
public class RequeteCHAMAP implements Requete, Serializable
{
    public static String BeanTrafic = "_$BDTRAFIC";
    public static String BeanCompta = "_$BDCOMPTA";
    public static int REQUEST_DEC = -1;
    public static int REQUEST_CON = 0;
    public static int REQUEST_LOGIN_TRAF = 1;
    public static int REQUEST_MAKE_BILL = 2;
    
    private int RequestType;
    private String Parameters;
    private Socket CSocket;
    private Properties PropertiesFile;
    
    public RequeteCHAMAP(int t, String param)
    {
        RequestType = t;
        this.setParameters(param);
        
    }
    
    public RequeteCHAMAP(int t, String param, Socket s)
    {
        RequestType = t;
        this.setParameters(param); 
        CSocket = s;
    }
    
    private void LoadPropertiesFile()
    {
        File f = new File("LoginTrafic.properties");
        if(!f.exists())
        {
            OutputStream os = null;
            try
            {
                os = new FileOutputStream(f);
            }
            catch (FileNotFoundException ex)
            {
                    Logger.getLogger(RequeteCHAMAP.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Properties propToCreate = new Properties();
            propToCreate.put("LOGIN", "Serveur");
            propToCreate.put("PASSWORD", "Trafic");
            
            try
            {
                propToCreate.store(os, "Serveur_Trafic");
                os.flush();
            }
            catch (IOException ex)
            {
                Logger.getLogger(RequeteCHAMAP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try
        {
            InputStream is = new FileInputStream(f);
            PropertiesFile = new Properties();
            PropertiesFile.load(is);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(RequeteCHAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(RequeteCHAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        else if (RequestType == REQUEST_LOGIN_TRAF)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteLoginTraf(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_MAKE_BILL)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteMakeBill(s, cs, param);
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
            RequeteCHAMAP req = null;
            try
            { 
                System.out.println("Avant réception requête CHAMAP");
                ois = new ObjectInputStream(SocketCli.getInputStream());
                req = (RequeteCHAMAP)ois.readObject();
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
            
            if(req.RequestType == RequeteCHAMAP.REQUEST_DEC)
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

                System.out.println("Après réception requête CHAMAP");
            }
        }
    }

    private void traiteRequeteLoginTraf(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        System.out.println("Début de traiteRequete : adresse distante = " + adresseDistante);
        String comm = adresseDistante + "#LOGIN_TRAF#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        System.out.println("TEST GROUPE : " + Thread.currentThread().getThreadGroup().getParent().getName());
        cs.TraceEvenements(comm);
        
        ReponseCHAMAP rep = null;
        
        LoadPropertiesFile();
        String strLoginTraf = PropertiesFile.getProperty("LOGIN");
        String strPasswordClair = PropertiesFile.getProperty("PASSWORD");
        
        try 
        {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(SocketCli.getInputStream()));
            
            /* Récupération info du réseaux */
            
            String strLogin = dis.readUTF();
            Long lDate = dis.readLong();
            Double dRandom = dis.readDouble();
            Integer iLongueurDigest = dis.readInt();
            byte[] DigestDistant = new byte[iLongueurDigest];
            dis.readFully(DigestDistant);
            System.out.println("Le sel est composé de l'heure au format long (" + lDate + ") et d'un nombre aléatoire (" + dRandom + ")");
            
            if(strLoginTraf.equals(strLogin))
            {
                /* Confection du digest local */
                byte[] DigestLocal = Cryptographie.CreateDigest(strPasswordClair, lDate, dRandom);

                if(!MessageDigest.isEqual(DigestLocal, DigestDistant))
                    rep = new ReponseCHAMAP(ReponseCHAMAP.LOGIN_KO, "BAD_PASSWORD");
                else
                    rep = new ReponseCHAMAP(ReponseCHAMAP.LOGIN_OK, "OK");
            }
            else
                rep = new ReponseCHAMAP(ReponseCHAMAP.LOGIN_KO, "UNKNOWN_USERNAME");
            
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteCHAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteMakeBill(Socket SocketCli, ConsoleServeur cs, Object param)
    {
        /*
            provoque la génération des factures pour les containers embarqués à partir des informations de mouvements
            paramètres : identifiant bateau ou train + liste des identifiants des containers effectivement embarqués
        */
        
        
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#MAKE_BILL#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        ReponseCHAMAP rep = null;
 
        HashMap hm = (HashMap)param;
        BeanBDAccess bdCompta = (BeanBDAccess)hm.get(RequeteCHAMAP.BeanCompta);
        BeanBDAccess bdTrafic = (BeanBDAccess)hm.get(RequeteCHAMAP.BeanTrafic);

        try 
        {
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteCHAMAP.class.getName()).log(Level.SEVERE, null, ex);
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