/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applic_jchat_pfm;

import Encryption.HashageNaif;
import Threads.ThreadReception;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Julien
 */
public class MainForm extends javax.swing.JFrame
{
    private String FileSep;
    private Properties PropertiesFile;
    private String PropertiesFilePath;
    private int Port;
    private String ServeurAdress;
    private Socket CSocketTCP;
    private DataInputStream dis;
    private DataOutputStream dos;
    private MulticastSocket SocketGroupe;
    private InetAddress AdresseGroupe;
    private int PortChat;
    private DefaultListModel DLM;
    private String Login;
    /**
     * Creates new form MainForm
     */
    public MainForm()
    {
        initComponents();
        DefaultComboBoxModel DCBM = new DefaultComboBoxModel();
        DCBM.addElement("QUESTION");
        DCBM.addElement("REPONSE");
        DCBM.addElement("INFORMATION");
        cbxChoixRequete.setModel(DCBM);
        btnEnvoyer.setEnabled(false);
        txtarMessage.setEditable(false);
        DLM = new DefaultListModel();
        lstMessages.setModel(DLM);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lstMessages = new javax.swing.JList();
        cbxChoixRequete = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtarMessage = new javax.swing.JTextArea();
        btnEnvoyer = new javax.swing.JButton();
        btnQuitter = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        itemSeConnecter = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(lstMessages);

        txtarMessage.setColumns(20);
        txtarMessage.setRows(5);
        jScrollPane2.setViewportView(txtarMessage);

        btnEnvoyer.setText("Envoyer un message");
        btnEnvoyer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnvoyerActionPerformed(evt);
            }
        });

        btnQuitter.setText("Quitter le chat");
        btnQuitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitterActionPerformed(evt);
            }
        });

        jMenu1.setText("Connexion");

        itemSeConnecter.setText("Se connecter au chat");
        itemSeConnecter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSeConnecterActionPerformed(evt);
            }
        });
        jMenu1.add(itemSeConnecter);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbxChoixRequete, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEnvoyer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnQuitter, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxChoixRequete, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEnvoyer)
                    .addComponent(btnQuitter))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemSeConnecterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSeConnecterActionPerformed
        ConnectionForm CF = new ConnectionForm(this, true);
        CF.setVisible(true);
        Login = CF.getLogin();
        String strPassword = CF.getPassword();

        PropertiesFile = new Properties();
        FileSep = System.getProperty("file.separator");
        PropertiesFilePath = System.getProperty("user.dir") + FileSep + "Chat_PFM.properties";
        
        try
        {
            PropertiesFile.load(new FileInputStream(PropertiesFilePath));
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        Port = Integer.parseInt(PropertiesFile.getProperty("PORT_FOR_US_ONLY"));
        ServeurAdress = PropertiesFile.getProperty("ADRESSE_CHAT_TCP");
        
        System.out.println("Port = " + Port);
        System.out.println("Adresse Serveur = " + ServeurAdress);

        try
        {
            CSocketTCP = new Socket(ServeurAdress, Port);
            System.out.println(CSocketTCP.getInetAddress().toString());
            dos = new DataOutputStream(CSocketTCP.getOutputStream());
            dis = new DataInputStream(CSocketTCP.getInputStream());
        }
        catch (UnknownHostException e)
        {
            System.err.println("Erreur ! Host non trouvé [" + e + "]");
        }
        catch (IOException e)
        {
            System.err.println("Erreur ! Pas de connexion ? [" + e + "]");
        }
        
        System.out.println("Message à envoyer au serveur :");
        System.out.println("Login : " + Login);
        System.out.println("Password : " + strPassword);
        
        // "Génération" du sel
        long lDate = (new Date()).getTime();

        Random r = new Random();
        int iRandom = r.nextInt(100);
        
        System.out.println("Le sel est composé de l'heure au format long (" + lDate + ") et d'un nombre aléatoire (" + iRandom + ")");
        
        // Génération du digest naif
        int iDigest = HashageNaif.GetMessageDigest(strPassword + String.valueOf(lDate) + String.valueOf(iRandom));
        
        System.out.println("Le digest vaut : " + iDigest);

        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dosDigest = new DataOutputStream(baos);
        
        try
        {
            dosDigest.writeLong(lDate);
            dosDigest.writeInt(iRandom);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        String strVerification = "LOGIN_GROUP|Login:" + Login + "/Date:" + lDate + "/Rand:" + iRandom + "/Digest:" + iDigest + "#";
        System.out.println("strVerification = " + strVerification);

        try 
        {
            /* Envoi du message au serveur */
            /*dos.writeInt(iDigest); // Envoi du digest naif
            dos.writeUTF(strVerification);*/
            dos.write(strVerification.getBytes());
            dos.flush();
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* Attente de réponse */
        String strReponse = null;
        try
        {
            byte b;
                StringBuilder sbReponse = new StringBuilder();

            while((b = dis.readByte()) != ((byte)'#'))
                sbReponse.append((char)b);
            
            strReponse = sbReponse.toString().trim();
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Requete recue : " + strReponse);

        StringTokenizer tok = new StringTokenizer(strReponse, "|/:#");
        String strTypeRequete = tok.nextToken();
        String strChamp;
        
        if(strTypeRequete.equals("RESP_LOGIN"))
        {
            String strEtat = "";
            String strAdresse = "";
            String strPort = "";
            
            while(tok.hasMoreTokens())
            {
                strChamp = tok.nextToken();
                
                if(strChamp.equals("Etat"))
                    strEtat = tok.nextToken();
                if(strChamp.equals("Adresse"))
                    strAdresse = tok.nextToken();
                if(strChamp.equals("Port"))
                    strPort = tok.nextToken();
            }
            
            if (strEtat.equals("OUI"))
            {
                System.out.println("CONNECTE");
                PortChat = Integer.parseInt(strPort);
                System.out.println("PortChat = " + PortChat);
                try
                {
                    SocketGroupe = new MulticastSocket(PortChat);
                    AdresseGroupe = InetAddress.getByName(strAdresse);
                    SocketGroupe.setInterface(InetAddress.getByName(ServeurAdress)); // On associe le multicast sur l'interface utilisée pour le TCP
                    SocketGroupe.joinGroup(AdresseGroupe); // Rejoindre un groupe
                    SocketGroupe.setTimeToLive(2);
                    SocketGroupe.setBroadcast(true);
                    
                    btnEnvoyer.setEnabled(true);
                    txtarMessage.setEditable(true);

                    ThreadReception threadreception = new ThreadReception(Login, SocketGroupe, DLM);
                    threadreception.start();

                    String strMessageConnexion = Login + " a rejoint le chat !";
                    System.out.println("Message à envoyer au groupe : " + strMessageConnexion);
                    DatagramPacket DP = new DatagramPacket(strMessageConnexion.getBytes(), strMessageConnexion.length(), AdresseGroupe, PortChat);
                    System.out.println("J'ai instancié un DatagramPacket");
                    SocketGroupe.send(DP);
                    System.out.println("J'ai envoyé un DatagramPacket");
                }
                catch (UnknownHostException ex)
                {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (SocketException ex)
                {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (IOException ex)
                {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_itemSeConnecterActionPerformed

    private void btnEnvoyerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnvoyerActionPerformed
        DatagramPacket DP;
        String strMessage;
        switch (cbxChoixRequete.getSelectedIndex())
        {
            case 0: // POST_QUESTION
                // Random pour le tag du message
                Random r = new Random();
                int iTagMessage = r.nextInt(10000);
                
                int iDigest = HashageNaif.GetMessageDigest(txtarMessage.getText());
                
                System.out.println("ENVOI | Tag :" + iTagMessage + "DIGEST :" + iDigest);
                
                strMessage = "POST_QUESTION|Login:" + Login + "/Tag:" + iTagMessage + "/Message:" + txtarMessage.getText() + "/Digest:" + iDigest + "#";

                DP = new DatagramPacket(strMessage.getBytes(), strMessage.length(), AdresseGroupe, PortChat);
                try
                {
                    System.out.println("J'envoie " + strMessage);
                    SocketGroupe.send(DP);
                    txtarMessage.setText("");
                }
                catch (IOException ex)
                {
                    System.err.println("Erreur d'envoie du message " + ex.getMessage());
                }
                break;
                
            case 1: // ANSWER_QUESTION
                if (DLM.getElementAt(lstMessages.getSelectedIndex()).toString().contains("question"))
                {
                    Requete Req = (Requete) DLM.getElementAt(lstMessages.getSelectedIndex());
                    strMessage = "ANSWER_QUESTION|Login:" + Login + "/Tag:" + Req.getTag() + "/Message:" + txtarMessage.getText() + "#";
                    DP = new DatagramPacket(strMessage.getBytes(), strMessage.length(), AdresseGroupe, PortChat);
                    try 
                    {
                        SocketGroupe.send(DP);
                        txtarMessage.setText("");
                    }
                    catch (IOException ex)
                    {
                        System.err.println("Erreur d'envoie du message " + ex.getMessage());
                    }
                }
                else
                    JOptionPane.showMessageDialog(this, "Veuillez choisir une question dans la liste.", "Erreur lors de la réponse à une question", JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case 2: // POST_EVENT
                strMessage = "POST_EVENT|Login:" + Login + "/Message:" + txtarMessage.getText() + "#";
                DP = new DatagramPacket(strMessage.getBytes(), strMessage.length(), AdresseGroupe, PortChat);
                try
                {
                    SocketGroupe.send(DP);
                    txtarMessage.setText("");
                }
                catch (IOException ex)
                {
                    System.err.println("Erreur d'envoie du message " + ex.getMessage());
                }
                break;
        }
    }//GEN-LAST:event_btnEnvoyerActionPerformed

    private void btnQuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitterActionPerformed
        try
        {
            String strMessageDeconnexion = Login + " a quitté le chat !";
            System.out.println("Message à envoyer au groupe : " + strMessageDeconnexion);
            DatagramPacket DP = new DatagramPacket(strMessageDeconnexion.getBytes(), strMessageDeconnexion.length(), AdresseGroupe, PortChat);
            System.out.println("J'ai instancié un DatagramPacket");
            SocketGroupe.send(DP);
            System.out.println("J'ai envoyé un DatagramPacket");
            SocketGroupe.leaveGroup(AdresseGroupe);
            System.exit(0);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnQuitterActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnvoyer;
    private javax.swing.JButton btnQuitter;
    private javax.swing.JComboBox cbxChoixRequete;
    private javax.swing.JMenuItem itemSeConnecter;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList lstMessages;
    private javax.swing.JTextArea txtarMessage;
    // End of variables declaration//GEN-END:variables
}