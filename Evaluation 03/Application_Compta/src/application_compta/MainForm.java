/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application_compta;

import Encryption.Cryptographie;
import Commandes.Handshake;
import RequeteReponseBISAMAP.ReponseBISAMAP;
import RequeteReponseBISAMAP.RequeteBISAMAP;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

/**
 *
 * @author Julien
 */
public class MainForm extends javax.swing.JFrame 
{
    private Socket CSocket;
    private Properties PropertiesCompta;
    private int Port;
    private String AdresseServeur;
    private String NameKeyStore;
    private SecretKey CleSymetriqueHMAC;
    private SecretKey CleSymetriqueChiffrement;
    private String UtilisateurConnecte;
    private String AliasServeur;
    private String AliasClient;
    private String PasswordKeyStore;
    private String PasswordKey;
    
    /**
     * Creates new form MainForm
     */
    public MainForm()
    {
        initComponents();
        LoadPropertiesFile();
        ConnexionServeur();
    }
    
    private void LoadPropertiesFile()
    {
        File f = new File("ApplicCompta.properties");
        if(!f.exists())
        {
            OutputStream os = null;
            
            try 
            {
                os = new FileOutputStream(f);
            } 
            catch (FileNotFoundException ex) 
            {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Properties propToCreate = new Properties();
            propToCreate.put("ADRESSE_IP_SERVEUR", "192.168.1.100");
            propToCreate.put("PORT_COMPTA", "31017");
            propToCreate.put("MAX_CLI", "3");
            propToCreate.put("SEP", "#");
            propToCreate.put("ADRESSE_IP_BD", "127.0.0.1");
            propToCreate.put("PORT_BD", "3306");
            propToCreate.put("SCHEMA_COMPTA", "bd_compta");
            propToCreate.put("SCHEMA_TRAFIC", "bd_trafic");
            propToCreate.put("KEYSTORE", "KS_ApplicCompta.p12");
            propToCreate.put("ALIAS_SERVEUR", "ServeurCompta");
            propToCreate.put("ALIAS_CLIENT", "ApplicCompta");
            propToCreate.put("PASSWORD_KEYSTORE", "azerty");
            propToCreate.put("PASSWORD_KEY", "azerty");
            
            try
            {
                propToCreate.store(os, "ApplicCompta");
                os.flush();
            } 
            catch (IOException ex)
            {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try
        {
            InputStream is = new FileInputStream(f);
            PropertiesCompta = new Properties();
            PropertiesCompta.load(is);
            Port = Integer.valueOf(PropertiesCompta.getProperty("PORT_COMPTA"));
            AdresseServeur = PropertiesCompta.getProperty("ADRESSE_IP_SERVEUR");
            NameKeyStore = PropertiesCompta.getProperty("KEYSTORE");
            AliasServeur = PropertiesCompta.getProperty("ALIAS_SERVEUR");
            AliasClient = PropertiesCompta.getProperty("ALIAS_CLIENT");
            PasswordKeyStore = PropertiesCompta.getProperty("PASSWORD_KEYSTORE");
            PasswordKey = PropertiesCompta.getProperty("PASSWORD_KEY");
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("FileNotFoundException");
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            System.err.println("IOException LoadPropertiesFile");
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void ConnexionServeur()
    {
        try
        {
            CSocket = new Socket(AdresseServeur, Port);
            RequeteBISAMAP req = new RequeteBISAMAP(RequeteBISAMAP.REQUEST_CON, null);
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            oos.writeObject(req);
            oos.flush();
            btnLogin.setEnabled(true);
        }
        catch (IOException ex) 
        {
            System.err.println("IOException ConnexionServeur");
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLogin = new javax.swing.JButton();
        btnGetNextBill = new javax.swing.JButton();
        btnListBills = new javax.swing.JButton();
        btnSendBills = new javax.swing.JButton();
        btnRecPay = new javax.swing.JButton();
        btnListWaiting = new javax.swing.JButton();
        btnComputeSal = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblBonjour = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnGetNextBill.setText("Récupérer la facture la plus ancienne");
        btnGetNextBill.setEnabled(false);
        btnGetNextBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGetNextBillActionPerformed(evt);
            }
        });

        btnListBills.setText("Lister les factures d'une société");
        btnListBills.setEnabled(false);
        btnListBills.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListBillsActionPerformed(evt);
            }
        });

        btnSendBills.setText("Envoyer les factures validées");
        btnSendBills.setEnabled(false);
        btnSendBills.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendBillsActionPerformed(evt);
            }
        });

        btnRecPay.setText("Enregistrer le paiement d'une facture");
        btnRecPay.setEnabled(false);
        btnRecPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecPayActionPerformed(evt);
            }
        });

        btnListWaiting.setText("Liste des factures en attente d'être réglées");
        btnListWaiting.setEnabled(false);
        btnListWaiting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListWaitingActionPerformed(evt);
            }
        });

        btnComputeSal.setText("Lancer le calcul des salaires mensuelles");
        btnComputeSal.setEnabled(false);
        btnComputeSal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComputeSalActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Application_Compta");

        lblBonjour.setText("Bonjour, veuillez vous connecter s'il vous plait");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGetNextBill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnListBills, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSendBills, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRecPay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnListWaiting, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnComputeSal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblBonjour, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblBonjour)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGetNextBill)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnListBills)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSendBills)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRecPay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnListWaiting)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnComputeSal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        ConnectionForm CF = new ConnectionForm(this, true);
        CF.setVisible(true);
        String strLogin = CF.getLogin();
        String strPassword = CF.getPassword();
        
        System.out.println("Message à envoyer au serveur :");
        System.out.println("Login : " + strLogin);
        System.out.println("Password : " + strPassword);
        
        /* Génération du sel de guérande */
        long lDate = (new Date()).getTime();
        Double dRandom = Math.random();
        System.out.println("Le sel est composé de l'heure au format long (" + lDate + ") et d'un nombre aléatoire (" + dRandom + ")");

        byte[] Digest = Cryptographie.CreateDigest(strPassword, lDate, dRandom);
        System.out.println("Le digest vaut : " + Digest);
        
        try
        {
            /* Envoi de la requête au serveur */
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            RequeteBISAMAP req = new RequeteBISAMAP(RequeteBISAMAP.REQUEST_LOGIN, null);
            oos.writeObject(req);
            oos.flush();
            
            /* Envoi du message au serveur */
            DataOutputStream dos = new DataOutputStream(CSocket.getOutputStream());
            dos.writeUTF(strLogin); // Envoi du login
            dos.writeLong(lDate); // Envoi de la date au format long
            dos.writeDouble(dRandom); // Envoi du random
            dos.writeInt(Digest.length); // Envoi de la taille du digest
            dos.write(Digest); // Envoi du digest
            dos.flush();
        
            /* Attente de réponse */
            ObjectInputStream ois = new ObjectInputStream(CSocket.getInputStream());
            ReponseBISAMAP rep = (ReponseBISAMAP)ois.readObject();
            
            if(rep.getCode() == ReponseBISAMAP.LOGIN_OK)
            {
                lblBonjour.setText("Bonjour " + strLogin + ", que voulez-vous faire ?");
                UtilisateurConnecte = strLogin;
                btnGetNextBill.setEnabled(true);
                btnListBills.setEnabled(true);
                btnListWaiting.setEnabled(true);
                btnRecPay.setEnabled(true);
                btnSendBills.setEnabled(true);
                
                btnLogin.setEnabled(false);
                
                Handshake handshake = (Handshake)rep.getResult();
                CleSymetriqueChiffrement = Cryptographie.DechiffrementCleSession(handshake.getCleSymetriqueChiffrement(), NameKeyStore, PasswordKeyStore, PasswordKey, AliasClient);
                CleSymetriqueHMAC = Cryptographie.DechiffrementCleSession(handshake.getCleSymetriqueHMAC(), NameKeyStore, PasswordKeyStore, PasswordKey, AliasClient);
                System.out.println("CleSymetriqueHMAC = " + CleSymetriqueHMAC);
                System.out.println("CleSymetriqueChiffrement = " + CleSymetriqueChiffrement);
                String strFonction = handshake.getFonction();
                if(strFonction.equals("Master") || strFonction.equals("Chef-comptable"))
                    btnComputeSal.setEnabled(true);
            }
            else
                JOptionPane.showConfirmDialog(this, (String)rep.getResult(), "Erreur lors du login", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex)
        {
            System.err.println("IOException btnLogin");
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            System.err.println("ClassNotFoundException btnLogin");
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnGetNextBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGetNextBillActionPerformed
        GetNextBillForm gnbf = new GetNextBillForm(this, true, UtilisateurConnecte, CSocket, CleSymetriqueChiffrement, NameKeyStore, AliasClient, PasswordKeyStore, PasswordKey);
        gnbf.setVisible(true);
    }//GEN-LAST:event_btnGetNextBillActionPerformed

    private void btnListBillsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListBillsActionPerformed
        ListBillsForm lbf = new ListBillsForm(this, true, CSocket, CleSymetriqueChiffrement, NameKeyStore, AliasClient, PasswordKeyStore, PasswordKey);
        lbf.setVisible(true);
    }//GEN-LAST:event_btnListBillsActionPerformed

    private void btnSendBillsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendBillsActionPerformed
        SendBillsForm sbf = new SendBillsForm(this, true, CSocket, CleSymetriqueChiffrement, NameKeyStore, AliasClient, PasswordKeyStore, PasswordKey);
        sbf.setVisible(true);
    }//GEN-LAST:event_btnSendBillsActionPerformed

    private void btnRecPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecPayActionPerformed
        RecPayForm rpf = new RecPayForm(this, true, CSocket, CleSymetriqueChiffrement, CleSymetriqueHMAC);
        rpf.setVisible(true);
    }//GEN-LAST:event_btnRecPayActionPerformed

    private void btnListWaitingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListWaitingActionPerformed
        ListWaitingForm lwf = new ListWaitingForm(this, true, CSocket, CleSymetriqueChiffrement, NameKeyStore, AliasClient, PasswordKeyStore, PasswordKey);
        lwf.setVisible(true);
    }//GEN-LAST:event_btnListWaitingActionPerformed

    private void btnComputeSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComputeSalActionPerformed
        ComputeSalForm csf = new ComputeSalForm(this, true, CSocket, CleSymetriqueChiffrement, CleSymetriqueHMAC, NameKeyStore, AliasClient, PasswordKeyStore, PasswordKey);
        csf.setVisible(true);
    }//GEN-LAST:event_btnComputeSalActionPerformed

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
    private javax.swing.JButton btnComputeSal;
    private javax.swing.JButton btnGetNextBill;
    private javax.swing.JButton btnListBills;
    private javax.swing.JButton btnListWaiting;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRecPay;
    private javax.swing.JButton btnSendBills;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblBonjour;
    // End of variables declaration//GEN-END:variables
}
