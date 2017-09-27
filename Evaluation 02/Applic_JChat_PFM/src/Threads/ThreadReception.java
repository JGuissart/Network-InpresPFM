/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import Encryption.HashageNaif;
import applic_jchat_pfm.Requete;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Julien
 */
public class ThreadReception extends Thread
{
    String Login;
    MulticastSocket SocketGroupe;
    DefaultListModel DLM;

    public ThreadReception(String Login, MulticastSocket SocketGroupe, DefaultListModel DLM) 
    {
        this.Login = Login;
        this.SocketGroupe = SocketGroupe;
        this.DLM = DLM;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try 
            {
                byte[] arrayBytesMessage = new byte[1000];
                
                DatagramPacket DP = new DatagramPacket(arrayBytesMessage, arrayBytesMessage.length);
                
                SocketGroupe.receive(DP);
                
                String strRequete = new String(arrayBytesMessage, "UTF-8");
                System.out.println("strRequete = " + strRequete);
                StringTokenizer stToken = new StringTokenizer(strRequete, "|/:#");
                String strTypeRequete = stToken.nextToken();
                System.out.println("strTypeRequete = " + strTypeRequete);
                String strChamp = "";
                String strLogin = "";
                String strTag = "";
                String strMessageRecu = "";
                String strDigest = "";
                
                switch(strTypeRequete)
                {
                    case "POST_QUESTION":
                        while(stToken.hasMoreTokens())
                        {
                            strChamp = stToken.nextToken();
                            if(strChamp.equals("Login"))
                                strLogin = stToken.nextToken();
                            if(strChamp.equals("Tag"))
                                strTag = stToken.nextToken();
                            if(strChamp.equals("Message"))
                                strMessageRecu = stToken.nextToken();
                            if(strChamp.equals("Digest"))
                                strDigest = stToken.nextToken();
                        }

                        int iDigestDistant = Integer.parseInt(strDigest);
                        int iDigestLocal = HashageNaif.GetMessageDigest(strMessageRecu);
                        System.out.println("Message reçu : " + strMessageRecu);
                        System.out.println("RECEPT | DIGEST RECU : " + iDigestDistant);
                        System.out.println("RECEPT | DIGEST CREE : " + iDigestLocal);
                        
                        if(iDigestDistant == iDigestLocal)
                        {
                            Requete reqQuestion = new Requete(strLogin + " a envoyé une question > " + strTag + " : " + strMessageRecu.trim(), Integer.parseInt(strTag));
                            DLM.addElement(reqQuestion);
                            System.out.println("DIGEST > OK");
                        }
                        else
                            System.out.println("DIGEST > NOK");
                        break;
                        
                    case "ANSWER_QUESTION":
                        System.out.println("Je suis dans ANSWER_QUESTION");
                        while(stToken.hasMoreTokens())
                        {
                            strChamp = stToken.nextToken();
                            if(strChamp.equals("Login"))
                                strLogin = stToken.nextToken();
                            if(strChamp.equals("Tag"))
                                strTag = stToken.nextToken();
                            if(strChamp.equals("Message"))
                                strMessageRecu = stToken.nextToken();
                        }
                        Requete reqAnswer = new Requete(strLogin + " a envoyé une réponse > " + strTag + " : " + strMessageRecu, Integer.parseInt(strTag));
                        DLM.addElement(reqAnswer);
                        break;
                        
                    case "POST_EVENT":
                        while(stToken.hasMoreTokens())
                        {
                            strChamp = stToken.nextToken();
                            if(strChamp.equals("Login"))
                                strLogin = stToken.nextToken();
                            if(strChamp.equals("Message"))
                                strMessageRecu = stToken.nextToken();
                        }
                        Requete reqEvent = new Requete(strLogin + " a envoyé une info > " + strMessageRecu);
                        DLM.addElement(reqEvent);
                        break;
                        
                    default: // Ajout la chaine "xxx a rejoint le chat !"
                        Requete reqConnextion = new Requete("===== " + strRequete + " =====");
                        DLM.addElement(reqConnextion);
                        break;
                }
            }
            catch (IOException ex) 
            {
                Logger.getLogger(ThreadReception.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
