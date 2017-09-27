/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RequeteReponseBISAMAP;

import AccessBD.BeanBDAccess;
import Compta.Facture;
import Encryption.Cryptographie;
import Commandes.ComputeSalSigned;
import Commandes.Handshake;
import Commandes.ListBills;
import Commandes.ListWaitingSigned;
import Commandes.RecPayAuth;
import Commandes.SendBills;
import Commandes.ValidateBillSigned;
import Commandes.ValidateSalSigned;
import Compta.Employe;
import Compta.Prime;
import Compta.Salaire;
import Generators.Generator;
import Queries.QueryInsert;
import Queries.QuerySelect;
import Queries.QueryUpdate;
import Utils.ConsoleServeur;
import Utils.Requete;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;

/**
 *
 * @author Julien
 */
public class RequeteBISAMAP implements Requete, Serializable
{
    public static String BeanCompta = "_$BDCOMPTA";
    public static int REQUEST_DEC = -1;
    public static int REQUEST_CON = 0;
    public static int REQUEST_LOGIN = 1;
    public static int REQUEST_GET_NEXT_BILL = 2;
    public static int REQUEST_VALIDATE_BILL = 3;
    public static int REQUEST_LIST_BILLS = 4;
    public static int REQUEST_SEND_BILLS = 5;
    public static int REQUEST_REC_PAY = 6;
    public static int REQUEST_LIST_WAITING = 7;
    public static int REQUEST_COMPUTE_SAL = 8;
    public static int REQUEST_VALIDATE_SAL = 9;
    public static int REQUEST_GET_BILLS = 10;
    
    private static SecretKey CleSymetriqueHMAC;
    private static SecretKey CleSymetriqueChiffrement;
    
    private static String NameKeyStoreServeur = "KS_ServeurCompta.p12";
    private static String NameKeyStoreApplic = "KS_ApplicCompta.p12";
    private static String PasswordKeyStore = "azerty";
    private static String PasswordKey = "azerty";
    private static String AliasClient = "ApplicCompta";
    private static String AliasServeur = "ServeurCompta";
    private int RequestType;
    private Object Parameters;
    private Socket CSocket;
    
    
    public RequeteBISAMAP(int t, Object param)
    {
        RequestType = t;
        this.setParameters(param);
    }
    
    public RequeteBISAMAP(int t, Object param, Socket s)
    {
        RequestType = t;
        this.setParameters(param);
        CSocket = s;
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
        else if (RequestType == REQUEST_LOGIN)
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
        else if (RequestType == REQUEST_GET_NEXT_BILL)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteGetNextBill(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_VALIDATE_BILL)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteValidateBill(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_LIST_BILLS)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteListBills(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_SEND_BILLS)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteSendBills(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_REC_PAY)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteRecPay(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_LIST_WAITING)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteListWaiting(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_COMPUTE_SAL)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteComputeSal(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_VALIDATE_SAL)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteValidateSal(s, cs, param);
                }
            };
        }
        else if (RequestType == REQUEST_GET_BILLS)
        {
            return new Runnable()
            {
                @Override
                public void run()
                {
                    traiteRequeteGetBills(s, cs, param);
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
            RequeteBISAMAP req = null;
            try
            { 
                System.out.println("Avant réception requête BISAMAP");
                ois = new ObjectInputStream(SocketCli.getInputStream());
                req = (RequeteBISAMAP)ois.readObject();
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
            
            if(req.RequestType == RequeteBISAMAP.REQUEST_DEC)
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

                System.out.println("Après réception requête BISAMAP");
            }
        }
    }

    private void traiteRequeteLogin(Socket SocketCli, ConsoleServeur cs, Object param)  // --> Commande finie et testée
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        System.out.println("Début de traiteRequete : adresse distante = " + adresseDistante);
        String comm = adresseDistante + "#LOGIN#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        System.out.println("TEST GROUPE : " + Thread.currentThread().getThreadGroup().getParent().getName());
        cs.TraceEvenements(comm);
        
        ReponseBISAMAP rep = null;
        
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
            
            /* Récupération du mot de passe dans la BD */
            
            HashMap hm = (HashMap)param;
            
            BeanBDAccess bdCompta = (BeanBDAccess)hm.get(RequeteBISAMAP.BeanCompta);
            QuerySelect qs = new QuerySelect();
            qs.AddSelect("Password, Fonction");
            qs.AddFrom("personnel");
            qs.AddWhere("Login = '" + strLogin + "'");
            
            ResultSet rs = bdCompta.Select(qs);
            
            if(!rs.first()) // S'il n'y a pas de résultat
                rep = new ReponseBISAMAP(ReponseBISAMAP.LOGIN_KO, "UNKNOWN_USERNAME");
            else
            {
                rs.first();
                String strFonction = rs.getString("Fonction");
                /* On ne veut que les comptables, chef-comptable ou les "maîtres" (des supers utilisateurs) */
                if(!strFonction.equals("Master") && !strFonction.equals("Comptable") && !strFonction.equals("Chef-comptable"))
                    rep = new ReponseBISAMAP(ReponseBISAMAP.LOGIN_KO, "PERMISSION_DENIED");
                else
                {
                    String strPasswordClair = rs.getString("Password");
                    System.out.println("Password clair = " + strPasswordClair);

                    /* Confection du digest local */
                    byte[] DigestLocal = Cryptographie.CreateDigest(strPasswordClair, lDate, dRandom);

                    if(!Cryptographie.CompareDigest(DigestLocal, DigestDistant))
                        rep = new ReponseBISAMAP(ReponseBISAMAP.LOGIN_KO, "BAD_PASSWORD");
                    else
                    {
                        CleSymetriqueHMAC = Cryptographie.CreateSecretKey();
                        byte[] cleSymetriqueHMACChiffree = Cryptographie.ChiffrementCleSession(CleSymetriqueHMAC, NameKeyStoreServeur, PasswordKeyStore, AliasClient);
                        CleSymetriqueChiffrement = Cryptographie.CreateSecretKey();
                        byte[] cleSymetriqueChiffrementChiffree = Cryptographie.ChiffrementCleSession(CleSymetriqueChiffrement, NameKeyStoreServeur, PasswordKeyStore, AliasClient);
                        Handshake handshake = new Handshake(cleSymetriqueChiffrementChiffree, cleSymetriqueHMACChiffree, strFonction);
                        rep = new ReponseBISAMAP(ReponseBISAMAP.LOGIN_OK, handshake);
                        System.out.println("CleSymetriqueHMAC = " + CleSymetriqueHMAC);
                        System.out.println("CleSymetriqueChiffrement = " + CleSymetriqueChiffrement);
                    }
                }
            }
            
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteGetNextBill(Socket SocketCli, ConsoleServeur cs, Object param)  // --> Commande finie et testée
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#GET_NEXT_BILL#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        ReponseBISAMAP rep = null;
        
        HashMap hm = (HashMap)param;
        BeanBDAccess bdCompta = (BeanBDAccess)hm.get(RequeteBISAMAP.BeanCompta);
        
        /*
            REQUETE
                pour obtenir la facture la plus ancienne non encore validée paramètres : -
            REPONSE
                oui + la facture chiffrée symétriquement + attente de la commande suivante ou non (pas de facture)
        */
        
        try 
        {
            QuerySelect qs = new QuerySelect("Facture NATURAL JOIN Item_facture");
            qs.AddSelect("idFacture, idSociete, idMouvement, Mois, Annee, Destination, MontantTotalHTVA, MontantTotalTVAC, idContainer");
            qs.AddWhere("idFacture IN (SELECT idFacture FROM Facture WHERE FactureValidee = 0 AND Annee = (SELECT MIN(Annee) FROM Facture) AND Mois = (SELECT MIN(mois) FROM Facture)) ORDER BY idFacture");
            /* Pour une meilleure visibilité de la requête
                SELECT idFacture, idSociete, idMouvement, Mois, Annee, Destination, MontantTotalHTVA, MontantTotalTVAC, idContainer
                FROM Facture NATURAL JOIN Item_facture
                WHERE idFacture IN 
                (
                        SELECT idFacture
                        FROM Facture
                        WHERE FactureValidee = 0
                        AND Annee = (SELECT MIN(Annee) FROM Facture)
                        AND Mois = (SELECT MIN(mois) FROM Facture)
                )
                ORDER BY idFacture;
            */
            
            ResultSet rs = bdCompta.Select(qs);
            if(!rs.first())
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "Il n'y a aucune facture non-validée");
            else
            {
                String strIdFacture = rs.getString("idFacture");
                Facture facture = new Facture(strIdFacture, rs.getString("idSociete"), rs.getString("idMouvement"), rs.getString("Mois"), rs.getString("Annee"), rs.getString("Destination"), rs.getDouble("MontantTotalHTVA"), rs.getDouble("MontantTotalTVAC"));
                facture.AddContainer(rs.getString("idContainer"));
                while(rs.next() && strIdFacture.equals(rs.getString("idFacture")))
                    facture.AddContainer(rs.getString("idContainer"));
                
                byte[] factureChiffre = Cryptographie.Chiffrement(CleSymetriqueChiffrement, facture);
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_OK, factureChiffre);
            }
            
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteValidateBill(Socket SocketCli, ConsoleServeur cs, Object param)  // --> Commande finie et testée
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#VALIDATE_BILL#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        ReponseBISAMAP rep = null;
        ValidateBillSigned vbs = (ValidateBillSigned)this.getParameters();
        /*
            REQUETE
                valider cette facture si elle est correctement libellée ou invalider 
                paramètres : numéro de facture, signature du comptable
            REPONSE
                oui + confirmation ou non (signature non vérifiée)
        */
        
        try 
        {
            if(!Cryptographie.CompareSignature(NameKeyStoreServeur, AliasClient, PasswordKeyStore, vbs.getValidateBill(), vbs.getSignature()))
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "L'objet reçu ne provient pas d'Application_Comptable !");
            else
            {
                HashMap hm = (HashMap)param;
                BeanBDAccess bdCompta = (BeanBDAccess)hm.get(RequeteBISAMAP.BeanCompta);
                QueryUpdate qu = new QueryUpdate("Facture");
                qu.AddValue("FactureValidee", "1");
                qu.AddValue("Login", "'" + vbs.getValidateBill().getComptable() + "'");
                qu.AddWhere("idFacture = '" + vbs.getValidateBill().getIdFacture() + "'");
                
                bdCompta.Update(qu);
                bdCompta.Commit();
                
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_OK, "La facture a bien été validée !");
            }
            
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteListBills(Socket SocketCli, ConsoleServeur cs, Object param)  // --> Commande finie et testée
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#LIST_BILLS#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        ReponseBISAMAP rep = null;
        ListBills lb = (ListBills)this.getParameters();
        
        /*
            REQUETE
                pour obtenir la liste de toutes les factures (payées ou non) d'une société donnée pour un intervalle de temps donné
                paramètres : identifiant société, dates de l'intervalle, signature du comptable
            REPONSE
                oui + liste chiffrée symétriquement ou non
        */
        
        try 
        {
            if(!Cryptographie.CompareSignature(NameKeyStoreServeur, AliasClient, PasswordKeyStore, lb.getRequete(), lb.getSignature()))
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "L'objet reçu ne provient pas d'Application_Comptable !");
            else
            {
                String[] arrayRequete = lb.getRequete().split("#"); // 0 = société; 1 = mois/année1; 2 = mois/année2;
                String[] arrayMoisAnnee1 = arrayRequete[1].split("_"); // 0 = mois; 1 = année
                String[] arrayMoisAnnee2 = arrayRequete[2].split("_");
                
                HashMap hm = (HashMap)param;
                BeanBDAccess bdCompta = (BeanBDAccess)hm.get(RequeteBISAMAP.BeanCompta);
                
                QuerySelect qs = new QuerySelect("Facture NATURAL JOIN Item_facture");
                qs.AddSelect("idFacture, idSociete, idMouvement, Mois, Annee, Destination, MontantTotalHTVA, MontantTotalTVAC, idContainer");
                qs.AddWhere("idFacture IN(SELECT idFacture FROM Facture WHERE idSociete = '" + arrayRequete[0] + "' AND Annee BETWEEN '" + arrayMoisAnnee1[1] + "' AND '" + arrayMoisAnnee2[1] + "' AND Mois BETWEEN '" + arrayMoisAnnee1[0] + "' AND '" + arrayMoisAnnee2[0] + "') ORDER BY idFacture");
                /* Pour une meilleure visibilité de la requête
                    SELECT idFacture, idSociete, idMouvement, Mois, Annee, Destination, MontantTotalHTVA, MontantTotalTVAC, idContainer
                    FROM Facture NATURAL JOIN Item_facture
                    WHERE idFacture IN
                    (
                            SELECT idFacture
                        FROM Facture
                        WHERE idSociete = 'Guissart SPRL'
                        AND Annee BETWEEN '2015' AND '2016'
                        AND Mois BETWEEN '01' AND '08'
                    )
                    ORDER BY idFacture;
                */

                ResultSet rs = bdCompta.Select(qs);
                if(!rs.first())
                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "Il n'y a aucune facture pour la société \"" + arrayRequete[0] + "\" pour l'intervalle demandé");
                else
                {
                    ArrayList<Facture> listFactures = new ArrayList<>();
                    String strIdFacture = rs.getString("idFacture");
                    Facture facture = new Facture(strIdFacture, rs.getString("idSociete"), rs.getString("idMouvement"), rs.getString("Mois"), rs.getString("Annee"), rs.getString("Destination"), rs.getDouble("MontantTotalHTVA"), rs.getDouble("MontantTotalTVAC"));
                    facture.AddContainer(rs.getString("idContainer"));
                    
                    while(rs.next())
                    {
                        if(strIdFacture.equals(rs.getString("idFacture")))
                            facture.AddContainer(rs.getString("idContainer"));
                        else
                        {
                            listFactures.add(facture);
                            strIdFacture = rs.getString("idFacture");
                            facture = new Facture(strIdFacture, rs.getString("idSociete"), rs.getString("idMouvement"), rs.getString("Mois"), rs.getString("Annee"), rs.getString("Destination"), rs.getDouble("MontantTotalHTVA"), rs.getDouble("MontantTotalTVAC"));
                            facture.AddContainer(rs.getString("idContainer"));
                        }
                    }
                    listFactures.add(facture);
                    
                    byte[] listeFacturesChiffree = Cryptographie.Chiffrement(CleSymetriqueChiffrement, listFactures);
                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_OK, listeFacturesChiffree);
                }
            }
            
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteSendBills(Socket SocketCli, ConsoleServeur cs, Object param)  // --> Commande finie et testée
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#SEND_BILLS#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        ReponseBISAMAP rep = null;
        SendBills sb = (SendBills)this.getParameters();
        
        /*
            REQUETE
                pour obtenir la liste de toutes les factures (payées ou non) d'une société donnée pour un intervalle de temps donné
                paramètres : identifiant société, dates de l'intervalle, signature du comptable
            REPONSE
                oui + liste chiffrée symétriquement ou non
        */
        
        try 
        {
            if(!Cryptographie.CompareSignature(NameKeyStoreServeur, AliasClient, PasswordKeyStore, sb.getListIdFacture(), sb.getSignature()))
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "L'objet reçu ne provient pas d'Application_Comptable !");
            else
            {
                HashMap hm = (HashMap)param;
                BeanBDAccess bdCompta = (BeanBDAccess)hm.get(RequeteBISAMAP.BeanCompta);
                
                /* On vérifie d'abord que les identifiants des factures à ne pas envoyer existent bien (pour la forme) */
                QuerySelect qs = new QuerySelect("Facture");
                qs.AddSelect("IdFacture");
                String strCondition = "";
                
                if(sb.getListIdFacture().size() > 0)
                {
                    for(int i = 0; i < sb.getListIdFacture().size(); i++)
                    {
                        if(i == sb.getListIdFacture().size() - 1)
                            strCondition += sb.getListIdFacture().get(i);
                        else
                            strCondition += sb.getListIdFacture().get(i) + "','";
                    }
                }
                qs.AddWhere("IdFacture IN('" + strCondition + "')");
                ResultSet rs = bdCompta.Select(qs);
                
                if(!rs.first())
                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "Les factures à ne pas envoyer n'existent pas ...");
                else
                {
                    rs.last();
                    if(rs.getRow() != sb.getListIdFacture().size())
                        rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "Une ou plusieurs factures à ne pas envoyer n'existe(nt) pas ...");
                    else
                    {
                        QueryUpdate qu = new QueryUpdate("Facture");
                        qu.AddValue("FactureEnvoyee", "1");
                        qu.AddWhere("FactureValidee = 1 AND IdFacture NOT IN('" + strCondition + "')");
                        bdCompta.Update(qu);
                        bdCompta.Commit();
                        
                        rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_OK, "Les factures ont bien été envoyées !");
                    }
                }
            }
            
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteRecPay(Socket SocketCli, ConsoleServeur cs, Object param)  // --> Commande finie et testée
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#REC_PAY#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        ReponseBISAMAP rep = null;
        byte[] recPayChiffre = (byte[])this.getParameters();
        RecPayAuth rpa = (RecPayAuth)Cryptographie.Dechiffrement(CleSymetriqueChiffrement, recPayChiffre);
        /*
            REQUETE
                RECeiving PAYment: enregistrement du paiement d'une facture
                paramètres : numéro de la facture, montant, informations bancaires + chiffré symétriquement + HMAC du comptable
            REPONSE
                oui + confirmation ou non + pourquoi (ex: montant payé différent du montant attendu)
        */
        
        try 
        {
            if(Cryptographie.CompareHMAC(CleSymetriqueHMAC, rpa.getRecPay(), rpa.getHMAC()))
            {
                HashMap hm = (HashMap)param;
                BeanBDAccess bdCompta = (BeanBDAccess)hm.get(RequeteBISAMAP.BeanCompta);
                
                QuerySelect qs = new QuerySelect("Facture");
                qs.AddSelect("FactureValidee, FactureEnvoyee, MontantTotalTVAC");
                qs.AddWhere("IdFacture = '" + rpa.getRecPay().getIdFacture() + "'");
                
                ResultSet rs = bdCompta.Select(qs);
                
                if(!rs.next())
                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "Cette facture n'existe pas !");
                else
                {
                    rs.last();
                    
                    if(rs.getRow() > 1)
                        rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "Ce numéro de facture concerne plusieurs factures ...");
                    else
                    {
                        // Pas besoin de revenir sur rs.first() car si rs.getRow() = 1 ==> on est sur le seul tuple renvoyé
                        
                        if(rs.getShort("FactureValidee") == 0) // Pour être sûr
                            rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "La facture n'a pas été validée par un comptable ...");
                        else
                        {
                            if(rs.getShort("FactureEnvoyee") == 0) // Pour être sûr
                                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "La facture n'a pas été envoyée ...");
                            else
                            {
                                if(rs.getDouble("MontantTotalTVAC") != rpa.getRecPay().getMontant())
                                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "Le montant payé n'est pas le montant attendu !");
                                else
                                {
                                    QueryUpdate qu = new QueryUpdate("Facture");
                                    qu.AddValue("FacturePayee", "1");
                                    qu.AddWhere("IdFacture = '" + rpa.getRecPay().getIdFacture() + "'");
                                    bdCompta.Update(qu);
                                    bdCompta.Commit();

                                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_OK, "Le paiement de la facture a bien été enregistré !");
                                }
                            }
                        }
                    }
                }
            }
            else
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "Le HMAC ne provient pas d'Application_Compta !");
            
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteListWaiting(Socket SocketCli, ConsoleServeur cs, Object param) // --> Commande finie et testée
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#LIST_WAITING#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        ReponseBISAMAP rep = null;
        ListWaitingSigned lws = (ListWaitingSigned)this.getParameters();
        
        /*
            REQUETE
                List of WAITING payements : liste des factures non encore payées : toutes ou celles d'une société donnée ou celles qui ont été
                émises depuis plus d'un mois
                paramètres : indications sur la nature de la liste, signature du comptable
            REPONSE
                la liste ou non + pourquoi
        */
        
        try 
        {
            if(!Cryptographie.CompareSignature(NameKeyStoreServeur, AliasClient, PasswordKeyStore, lws.getListWaiting(), lws.getSignature()))
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "L'objet reçu ne provient pas d'Application_Comptable !");
            else
            {
                HashMap hm = (HashMap)param;
                BeanBDAccess bdCompta = (BeanBDAccess)hm.get(RequeteBISAMAP.BeanCompta);
                QuerySelect qs = new QuerySelect("Facture NATURAL JOIN Item_facture");
                qs.AddSelect("idFacture, idSociete, idMouvement, Mois, Annee, Destination, MontantTotalHTVA, MontantTotalTVAC, idContainer");
                
                switch (lws.getListWaiting().getTypeListe())
                {
                    case "ALL": // On récupère toutes les factures en attente de paiement
                        qs.AddWhere("FactureEnvoyee = 1 AND FacturePayee = 0");
                        break;
                    case "SOCIETE": // On récupère toutes les factures en attente de paiement pour une société donnée
                        qs.AddWhere("FactureEnvoyee = 1 AND FacturePayee = 0 AND idSociete = '" + lws.getListWaiting().getIdSociete() + "'");
                        break;
                    case "MONTH": // On récupère toutes les factures en attente de paiement émises depuis plus d'un mois
                        
                        String strCurrentMonth = Generator.getCurrentMonth();
                        if(strCurrentMonth.equals("01")) // Si on est en janvier, on recule d'un an
                            qs.AddWhere("FactureEnvoyee = 1 AND FacturePayee = 0 AND Annee NOT IN(YEAR(CURDATE()))");
                        else
                            qs.AddWhere("FactureEnvoyee = 1 AND FacturePayee = 0 AND idFacture NOT IN(SELECT idFacture FROM Facture WHERE Mois = MONTH(CURDATE()) AND Annee = YEAR(CURDATE())) ORDER BY idFacture");
                        /* Pour une meilleure visibilité de la requête
                            SELECT idFacture, idSociete, idMouvement, Mois, Annee, Destination, MontantTotalHTVA, MontantTotalTVAC, idContainer
                            FROM Facture NATURAL JOIN Item_facture
                            WHERE FacturePayee = 0 
                            AND idFacture NOT IN
                            (
                                SELECT idFacture
                                FROM Facture
                                WHERE Mois = 02 AND Annee = 2016
                            )
                            ORDER BY idFacture;
                        */
                        break;
                }
                
                ResultSet rs = bdCompta.Select(qs);
                if(!rs.first())
                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "Il n'y a aucune facture en attente de paiement !");
                else
                {
                    ArrayList<Facture> listFactures = new ArrayList<>();
                    String strIdFacture = rs.getString("idFacture");
                    Facture facture = new Facture(strIdFacture, rs.getString("idSociete"), rs.getString("idMouvement"), rs.getString("Mois"), rs.getString("Annee"), rs.getString("Destination"), rs.getDouble("MontantTotalHTVA"), rs.getDouble("MontantTotalTVAC"));
                    facture.AddContainer(rs.getString("idContainer"));
                    
                    while(rs.next())
                    {
                        if(strIdFacture.equals(rs.getString("idFacture")))
                            facture.AddContainer(rs.getString("idContainer"));
                        else
                        {
                            listFactures.add(facture);
                            strIdFacture = rs.getString("idFacture");
                            facture = new Facture(strIdFacture, rs.getString("idSociete"), rs.getString("idMouvement"), rs.getString("Mois"), rs.getString("Annee"), rs.getString("Destination"), rs.getDouble("MontantTotalHTVA"), rs.getDouble("MontantTotalTVAC"));
                            facture.AddContainer(rs.getString("idContainer"));
                        }
                    }
                    listFactures.add(facture);
                    
                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_OK, listFactures);
                }
            }
            
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteComputeSal(Socket SocketCli, ConsoleServeur cs, Object param) // --> Commande finie et testée
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#COMPUTE_SAL#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        ReponseBISAMAP rep = null;
        ComputeSalSigned css = (ComputeSalSigned)this.getParameters();
        
        /*
            REQUETE
                Lancer le calcul des salaires mensuels sur base du barème et des primes éventuelle. Seul le chef-comptable peut lancer cet ordre
                paramètres : HMAC du comptable + signature du comptable
            REPONSE
                oui ou non
        */
        
        try 
        {
            if(Cryptographie.CompareHMAC(CleSymetriqueHMAC, css.getComputeSal(), css.getHMAC()))
            {
                if(Cryptographie.CompareSignature(NameKeyStoreServeur, AliasClient, PasswordKeyStore, css.getComputeSal(), css.getSignature()))
                {
                    /* 
                        Sur base du site http://www.adecco.be/candidats/travailler-via-adecco/informations-pratiques/votre-salaire.aspx 
                        on fixe la cotisation ONSS à 13.07% du salaire brut et le précompte à 11.11%
                    */
                    
                    HashMap hm = (HashMap)param;
                    BeanBDAccess bdCompta = (BeanBDAccess)hm.get(RequeteBISAMAP.BeanCompta);
                    
                    /* Récupération des primes octroyées le mois et l'année donnés */
                    
                    QuerySelect qs = new QuerySelect("Primes");
                    qs.AddSelect("Login, SUM(Montant)");
                    qs.AddWhere("SUBSTR(DateOctroi, 4, 2) = '" + css.getComputeSal().getMois() + "' AND SUBSTR(DateOctroi, 7, 4) = '" + css.getComputeSal().getAnnee()+ "' GROUP BY Login");
                    HashMap<String, Double> hmPrimes = new HashMap<>();
                    ResultSet rs = bdCompta.Select(qs);
                    while(rs.next())
                        hmPrimes.put(rs.getString("Login"), rs.getDouble(2));

                    qs = new QuerySelect("Personnel");
                    qs.AddSelect("Login");
                    rs = bdCompta.Select(qs);

                    /* Calcul effectif des salaires */
                    while(rs.next())
                    {
                        String strLogin = rs.getString("Login");
                        Double Prime = hmPrimes.getOrDefault(strLogin, 0.0); // On récupère la somme des primes si elle existe, 0.0 sinon
                        System.err.println("Prime pour " + strLogin + " = " + Prime.toString());
                        Double MontantBrut = Prime + 3648.69; // 3648.69 --> Salaire brut arbitraire
                        System.err.println("Salaire brut pour " + strLogin + " = " + MontantBrut.toString());
                        Double ONSS = MontantBrut * 13.07 / 100; // Calcul de l'ONSS
                        Double MontantImposable = MontantBrut - ONSS; // Nécessaire pour le calcul du précompte (si j'ai bien compris vu que je suis pas comptable)
                        Double MontantPrecompte = MontantImposable * 11.11 / 100; // Calcul du précompte
                        QueryInsert qi = new QueryInsert("Salaires");
                        qi.AddValue("Login", "'" + strLogin + "'");
                        qi.AddValue("MontantBrut", String.valueOf(new BigDecimal(MontantBrut.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue()));
                        qi.AddValue("RetraitONSS", String.valueOf(new BigDecimal(ONSS.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue()));
                        qi.AddValue("RetraitPrecompte", String.valueOf(new BigDecimal(MontantPrecompte.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue()));
                        qi.AddValue("Mois", "'" + css.getComputeSal().getMois() + "'");
                        qi.AddValue("Annee", "'" + css.getComputeSal().getAnnee() + "'");
                        bdCompta.Insert(qi);
                        bdCompta.Commit();
                    }
                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_OK, null);
                }
                else
                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "La signature ne provient pas d'Application_Compta !");
            }
            else
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "Le HMAC ne provient pas d'Application_Compta !");
            
            ObjectOutputStream oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void traiteRequeteValidateSal(Socket SocketCli, ConsoleServeur cs, Object param) // --> Commande finie et testée
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#VALIDATE_SAL#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        ReponseBISAMAP rep = null;
        ValidateSalSigned vss = (ValidateSalSigned)this.getParameters();
        
        /*
            REQUETE
                Récupération et examen des virements prévus et validation éventuelle. Seul le chefcomptable peut lancer cette requête
                paramètres : HMAC du comptable + signature du comptable
            REPONSE
                oui + liste chiffrée symétriquement ou non
        */
        
        try 
        {
            HashMap hm = (HashMap)param;
            BeanBDAccess bdCompta = (BeanBDAccess)hm.get(RequeteBISAMAP.BeanCompta);
            
            if(Cryptographie.CompareHMAC(CleSymetriqueHMAC, vss.getValidateSal(), vss.getHMAC()))
            {
                if(Cryptographie.CompareSignature(NameKeyStoreServeur, AliasClient, PasswordKeyStore, vss.getValidateSal(), vss.getSignature()))
                {
                    /* On sélectionne d'abord les salaires que l'on vient de calculer pour les envoyer au mec d'en face */
                    
                    QuerySelect qs = new QuerySelect("Salaires NATURAL JOIN Personnel");
                    qs.AddSelect("Login, Nom, Prenom, Fonction, idSalaire, MontantBrut, RetraitONSS, RetraitPrecompte, Mois, Annee");
                    qs.AddWhere("Mois = '" + vss.getValidateSal().getMois() + "' AND Annee = '" + vss.getValidateSal().getAnnee()+ "' ORDER BY Login");

                    /* Pour une meilleure visibilité
                        SELECT Login, Nom, Prenom, Fonction, idSalaire, MontantBrut, RetraitONSS, RetraitPrecompte, Mois, Annee
                        FROM Salaires NATURAL JOIN Personnel
                        WHERE Mois = '01' AND Annee = '2015'
                        ORDER BY Login;
                    */

                    ResultSet rs = bdCompta.Select(qs);
                    ArrayList<Salaire> listSalaires = new ArrayList<>();
                    
                    while(rs.next())
                    {
                        Employe e = new Employe(rs.getString("Login"), rs.getString("Nom"), rs.getString("Prenom"), rs.getString("Fonction"));
                        Salaire s = new Salaire(rs.getString("idSalaire"), e, rs.getDouble("MontantBrut"), rs.getDouble("RetraitONSS"), rs.getDouble("RetraitPrecompte"), rs.getString("Mois"), rs.getString("Annee"));
                        listSalaires.add(s);
                    }
                    
                    /* On récupère ensuite les éventuelles primes pour les salaires que l'on vient de récupérer */
                    
                    qs = new QuerySelect("Primes");
                    qs.AddSelect("idPrime, Login, Montant, DateOctroi, Motif");
                    qs.AddWhere("SUBSTR(DateOctroi, 4, 2) = '" + vss.getValidateSal().getMois() + "' AND SUBSTR(DateOctroi, 7, 4) = '" + vss.getValidateSal().getAnnee()+ "' ORDER BY Login");

                    /* Pour une meilleure visibilité
                        SELECT idPrime, Login, Montant, DateOctroi, Motif
                        FROM Primes
                        WHERE SUBSTR(DateOctroi, 4, 2) = '11' AND SUBSTR(DateOctroi, 7, 4) = '2015'
                        ORDER BY Login;
                    */

                    rs = bdCompta.Select(qs);
                    
                    while(rs.next())
                    {
                        Prime p = new Prime(rs.getString("idPrime"), rs.getDouble("Montant"), rs.getString("DateOctroi"), rs.getString("Motif"));
                        for (Salaire salaire : listSalaires)
                        {
                            if(salaire.getEmploye().getLogin().equals(rs.getString("Login")))
                            {
                                salaire.AddPrime(p);
                                break;
                            }
                        }
                    }

                    byte[] listeSalairesChiffree = Cryptographie.Chiffrement(CleSymetriqueChiffrement, listSalaires);
                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_OK, listeSalairesChiffree);
                }
                else
                    rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "La signature ne provient pas d'Application_Compta !");
            }
            else
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, "Le HMAC ne provient pas d'Application_Compta !");
            
            ObjectOutputStream oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
            
            /* On attend la réponse afin de savoir s'il faut valider ou non les salaires */
            
            ObjectInputStream ois = new ObjectInputStream(SocketCli.getInputStream());
            RequeteBISAMAP req = (RequeteBISAMAP)ois.readObject();
            
            boolean bValider = (boolean) req.getParameters();
            
            if(bValider)
            {
                QueryUpdate qu = new QueryUpdate("Salaires");
                qu.AddValue("SalaireValide", "1");
                qu.AddWhere("Mois = '" + vss.getValidateSal().getMois() + "' AND Annee = '" + vss.getValidateSal().getAnnee()+ "'");
                bdCompta.Update(qu);
                bdCompta.Commit();
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_OK, "Les salaires ont été validés !");
            }
            else
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_OK, "Vous avez décidé de ne pas valider les salaires !");
            
            ObjectOutputStream oos2 = new ObjectOutputStream(SocketCli.getOutputStream());
            oos2.writeObject(rep);
            oos2.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void traiteRequeteGetBills(Socket SocketCli, ConsoleServeur cs, Object param) // --> Commande finie et testée
    {
        String adresseDistante = SocketCli.getRemoteSocketAddress().toString();
        String comm = adresseDistante + "#GET_BILLS#Thread Client : " + String.valueOf(Thread.currentThread().getId());
        cs.TraceEvenements(comm);
        
        ReponseBISAMAP rep = null;
        String strParametre = (String)this.getParameters();
        
        /*
            REQUETE
                Requête additionnelle permettant de récupérer une liste spécifique de facture
            REPONSE
                oui + liste chiffrée symétriquement ou non
        */
        
        try 
        {
            HashMap hm = (HashMap)param;
            BeanBDAccess bdCompta = (BeanBDAccess)hm.get(RequeteBISAMAP.BeanCompta);
            
            QuerySelect qs = new QuerySelect("Facture");
            qs.AddSelect("IdFacture");
            String strMessageErreur = "";
            switch(strParametre)
            {
                case "ALL": // On fait rien, on veut juste récupérer toutes les factures dans la base de données
                    strMessageErreur = "Il n'y a pas de facture";
                    break;
                case "VALIDEE": qs.AddWhere("FactureValidee = 1");
                    strMessageErreur = "Il n'y a pas de facture validée";
                    break;
                case "ENVOYEE": qs.AddWhere("FactureEnvoyee = 1"); // Implique que la facture a été validée
                    strMessageErreur = "Il n'y a pas de facture envoyée";
                    break;
                case "PAYEE": qs.AddWhere("FacturePayee = 1"); // Implique que la facture a été envoyée
                    strMessageErreur = "Il n'y a pas de facture payée";
                    break;
            }
            
            ResultSet rs = bdCompta.Select(qs);
            ArrayList<String> listFactures = new ArrayList<>();
            
            if(!rs.next())
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_KO, strMessageErreur);
            else
            {
                listFactures.add(rs.getString("IdFacture"));
                while(rs.next())
                    listFactures.add(rs.getString("IdFacture"));
                
                byte[] listeFacturesChiffree = Cryptographie.Chiffrement(CleSymetriqueChiffrement, listFactures);
                rep = new ReponseBISAMAP(ReponseBISAMAP.REPONSE_OK, listeFacturesChiffree);
            }
            
            ObjectOutputStream oos = new ObjectOutputStream(SocketCli.getOutputStream());
            oos.writeObject(rep);
            oos.flush();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(RequeteBISAMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @return the Parameters
     */
    public Object getParameters() {
        return Parameters;
    }

    /**
     * @param Parameters the Parameters to set
     */
    public void setParameters(Object Parameters) {
        this.Parameters = Parameters;
    }
}