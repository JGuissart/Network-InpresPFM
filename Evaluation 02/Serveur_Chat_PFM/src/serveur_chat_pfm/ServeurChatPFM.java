/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur_chat_pfm;

import AccessBD.BeanBDAccess;
import AccessBD.BeanBDMySql;
import Encryption.HashageNaif;
import Queries.QuerySelect;
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
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Julien
 */
public class ServeurChatPFM 
{
    private DefaultListModel DLM;
    private Properties PropertiesFileChatPFM;
    String FileSep;
    ServerSocket SocketServeur;
    int Port;
    Socket SocketClient;
    DataInputStream dis;
    DataOutputStream dos;
    
    public ServeurChatPFM()
    {
        LoadPropertiesFile();
        RunServer();
    }
    
    private void LoadPropertiesFile()
    {
        File f = new File("Chat_PFM.properties");
        if(!f.exists())
        {
            OutputStream os = null;
            try
            {
                os = new FileOutputStream(f);
            } 
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(ServeurChatPFM.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Properties propToCreate = new Properties();
            propToCreate.put("PORT_FOR_US_ONLY", "31014");
            propToCreate.put("PORT_CHAT", "31015");
            propToCreate.put("ADRESSE_CHAT_TCP", "192.168.1.102");
            propToCreate.put("ADRESSE_CHAT_UDP", "234.5.5.5");
            propToCreate.put("ADRESSE_IP_BD", "127.0.0.1");
            propToCreate.put("PORT_BD", "3306");
            propToCreate.put("SCHEMA_BD", "bd_compta");
            
            try
            {
                propToCreate.store(os, "Chat_PFM");
                os.flush();
            } 
            catch (IOException ex)
            {
                Logger.getLogger(ServeurChatPFM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try
        {
            InputStream is = new FileInputStream(f);
            PropertiesFileChatPFM = new Properties();
            PropertiesFileChatPFM.load(is);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ServeurChatPFM.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServeurChatPFM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void RunServer()
    {
        try 
        {
            SocketServeur = new ServerSocket(Integer.parseInt(PropertiesFileChatPFM.getProperty("PORT_FOR_US_ONLY")));
            System.out.println("Socket lance sur : " + SocketServeur.getInetAddress() + "/" + SocketServeur.getLocalPort());
            
            /* Connection à BD_Compta */
            System.out.println("BD_COMPTA : ");
            BeanBDAccess BeanCompta = new BeanBDMySql(PropertiesFileChatPFM.getProperty("ADRESSE_IP_BD"), PropertiesFileChatPFM.getProperty("PORT_BD"), PropertiesFileChatPFM.getProperty("SCHEMA_BD"), "root", "");
            
            while(true)
            {
                System.out.println("Serveur en attente d'un client ...");
                SocketClient = SocketServeur.accept();
                dis = new DataInputStream(SocketClient.getInputStream());
                dos = new DataOutputStream(SocketClient.getOutputStream());
                
                /*int iDigestDistant = dis.readInt();
                String strRequete = dis.readUTF();*/
                
                byte b;
                StringBuilder sbReponse = new StringBuilder();
                
                while((b = dis.readByte()) != ((byte)'#'))
                    sbReponse.append((char)b);
                
                String strRequete = sbReponse.toString().trim();
                
                System.out.println("Requete recue : " + strRequete);
                
                StringTokenizer stToken = new StringTokenizer(strRequete, "|/:#");
                String strTypeRequete = stToken.nextToken();

                if(strTypeRequete.equals("LOGIN_GROUP"))
                {
                    String strLogin = "";
                    String strDate = "";
                    String strRandom = "";
                    String strDigest = "";

                    String strChamp;
                    while(stToken.hasMoreTokens())
                    {
                        strChamp = stToken.nextToken();
                        if(strChamp.equals("Login"))
                            strLogin = stToken.nextToken();
                        if(strChamp.equals("Date"))
                            strDate = stToken.nextToken();
                        if(strChamp.equals("Rand"))
                            strRandom = stToken.nextToken();
                        if(strChamp.equals("Digest"))
                            strDigest = stToken.nextToken();
                    }
                    
                    /*long lDate = Long.parseLong(strDate);
                    int iRandom = Integer.parseInt(strRandom);*/

                    QuerySelect qs = new QuerySelect();
                    qs.AddSelect("Password");
                    qs.AddFrom("Personnel");
                    qs.AddWhere("Login = '" + strLogin + "'");
                    
                    ResultSet rs = BeanCompta.Select(qs);
                    Boolean bDigestOk = false;
                    
                    if(rs.next())
                    {
                        String strPasswordClair = rs.getString("Password");
                        System.out.println("Password en clair : " + strPasswordClair);
                        int iDigestDistant = Integer.parseInt(strDigest);
                        int iDigestLocal = HashageNaif.GetMessageDigest(strPasswordClair + strDate + strRandom);

                        if(iDigestDistant == iDigestLocal)
                        {
                            bDigestOk = true;
                            String strReponse = "RESP_LOGIN|Etat:OUI/Adresse:" + PropertiesFileChatPFM.getProperty("ADRESSE_CHAT_UDP") + "/Port:" + PropertiesFileChatPFM.getProperty("PORT_CHAT") + "#";
                            System.out.println("Reponse : " + strReponse);
                            dos.write(strReponse.getBytes());
                            //dos.writeUTF(strReponse);
                            dos.flush();
                        }
                    }
                    if (!bDigestOk)
                    {
                        String strReponse = "RESP_LOGIN|Etat:NON/Adresse:" + 0 + "/Port:" + 0 + "#";
                        System.out.println("Reponse : " + strReponse);
                        dos.write(strReponse.getBytes());
                        //dos.writeUTF(strReponse);
                        dos.flush();
                    }
                }
            }
        }
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(ServeurChatPFM.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ServeurChatPFM.class.getName()).log(Level.SEVERE, null, ex);
        }  
        catch (SQLException ex) 
        {
            Logger.getLogger(ServeurChatPFM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
