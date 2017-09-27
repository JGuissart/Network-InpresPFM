/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur_trafic;

import AccessBD.BeanBDAccess;
import AccessBD.BeanBDMySql;
import Encryption.Cryptographie;
import RequeteReponseCHAMAP.ReponseCHAMAP;
import RequeteReponseCHAMAP.RequeteCHAMAP;
import RequeteReponseTRAMAP.RequeteTRAMAP;
import Utils.ConsoleServeur;
import Utils.ListeTaches;
import Utils.ThreadServeur;
import Utils.ThreadServeurBOOMAP;
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
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Julien
 */
public class MainForm extends javax.swing.JFrame implements ConsoleServeur
{
    private ThreadServeur _ThreadServeurTRAMAP = null;
    private ThreadServeurBOOMAP _ThreadServeurBOOMAP = null;
    private Socket CSocket;
    private Properties PropertiesTrafic;
    private int NombreMaxClients;
    private String Separator;
    private String FinChaine;
    
    /**
     * Creates new form MainForm
     */
    public MainForm()
    {
        initComponents();
        LoadPropertiesFile();
    }
    
    /**
     * Création/chargement du fichier properties Serveur_Trafic.properties
     */
    private void LoadPropertiesFile()
    {
        File f = new File("Serveur_Trafic.properties");
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
            propToCreate.put("PORT_IN", "31012");
            propToCreate.put("PORT_RES", "31013");
            propToCreate.put("PORT_COMPTA", "31017");
            propToCreate.put("ADRESSE_IP_SERVEUR", "192.168.1.100");
            propToCreate.put("MAX_CLI", "3");
            propToCreate.put("SEP", "#");
            propToCreate.put("ADRESSE_IP_BD", "127.0.0.1");
            propToCreate.put("PORT_BD", "3306");
            propToCreate.put("SCHEMA_BD", "bd_trafic");
            propToCreate.put("SEPARATOR", "#");
            propToCreate.put("FINCHAINE", "$");
            
            try
            {
                propToCreate.store(os, "Serveur_Trafic");
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
            PropertiesTrafic = new Properties();
            PropertiesTrafic.load(is);
            tfdPortIn.setText(PropertiesTrafic.getProperty("PORT_IN"));
            tfdPortRes.setText(PropertiesTrafic.getProperty("PORT_RES"));
            NombreMaxClients = Integer.valueOf(PropertiesTrafic.getProperty("MAX_CLI"));
            Separator = PropertiesTrafic.getProperty("SEPARATOR");
            FinChaine = PropertiesTrafic.getProperty("FINCHAINE");
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean ConnexionServeurCompta()
    {
        try
        {
            String strAdresseServeur = PropertiesTrafic.getProperty("ADRESSE_IP_SERVEUR");
            int iPortCompta = Integer.parseInt(PropertiesTrafic.getProperty("PORT_COMPTA"));
            CSocket = new Socket(strAdresseServeur, iPortCompta);
            RequeteCHAMAP req = new RequeteCHAMAP(RequeteCHAMAP.REQUEST_CON, null);
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(CSocket.getOutputStream());
            oos.writeObject(req);
            oos.flush();
            
            return LoginTraf();
        }
        catch (IOException ex) 
        {
            System.err.println("IOException ConnexionServeurCompta");
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private boolean LoginTraf()
    {
        String strLogin = "Serveur";
        String strPassword = "Trafic";
        
        System.out.println("Message à envoyer au serveur :");
        System.out.println("Login : " + strLogin);
        System.out.println("Password : " + strPassword);
        
        /* Génération du sel de guérande */
        long lDate = (new Date()).getTime();
        Double dRandom = Math.random();
        System.out.println("Le sel est composé de l'heure au format long (" + lDate + ") et d'un nombre aléatoire (" + dRandom + ")");

        byte[] Digest = Cryptographie.CreateDigest(strPassword, lDate, dRandom);
        
        try
        {
            /* Envoi de la requête au serveur */
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            RequeteCHAMAP req = new RequeteCHAMAP(RequeteCHAMAP.REQUEST_LOGIN_TRAF, null);
            oos.writeObject(req);
            oos.flush();

            System.out.println("Le digest vaut : " + Digest);
            
            /* Envoi du message au serveur */
            DataOutputStream dos = new DataOutputStream(CSocket.getOutputStream());
            dos.writeUTF(strLogin); // Envoi du login
            dos.writeLong(lDate); // Envoi de la date au format long
            dos.writeDouble(dRandom); // Envoi du random
            dos.writeInt(Digest.length); // Envoi de la taille du digest
            dos.write(Digest); // Envoi du digest
            dos.flush();
            
            /* Attente de réponse */
            ObjectInputStream ois = null;
            ReponseCHAMAP rep = null;
            
            ois = new ObjectInputStream(CSocket.getInputStream());
            rep = (ReponseCHAMAP)ois.readObject();
            
            return rep.getCode() == ReponseCHAMAP.LOGIN_OK;
        }
        catch (IOException ex) 
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } 
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    @Override
    public void TraceEvenements(String commentaire)
    {
        Vector ligne = new Vector();
        StringTokenizer parser = new StringTokenizer(commentaire, String.valueOf(Separator));
        while (parser.hasMoreTokens())
            ligne.add(parser.nextToken());
        DefaultTableModel dtm = (DefaultTableModel)this.tblRequete.getModel();
        dtm.insertRow(dtm.getRowCount(), ligne);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblPortIn = new javax.swing.JLabel();
        tfdPortIn = new javax.swing.JTextField();
        btnDemarrerServeur = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRequete = new javax.swing.JTable();
        btnStopperServeur = new javax.swing.JButton();
        lblPortRes = new javax.swing.JLabel();
        tfdPortRes = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblPortIn.setText("PORT_IN : ");

        btnDemarrerServeur.setText("Démarrer le serveur");
        btnDemarrerServeur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDemarrerServeurActionPerformed(evt);
            }
        });

        tblRequete.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Origine", "Requête", "Thread"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblRequete);

        btnStopperServeur.setText("Stopper le serveur");
        btnStopperServeur.setEnabled(false);
        btnStopperServeur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopperServeurActionPerformed(evt);
            }
        });

        lblPortRes.setText("PORT_RES :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPortIn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfdPortIn, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblPortRes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfdPortRes, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDemarrerServeur)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnStopperServeur)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPortIn)
                    .addComponent(tfdPortIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPortRes)
                    .addComponent(tfdPortRes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDemarrerServeur)
                    .addComponent(btnStopperServeur))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDemarrerServeurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDemarrerServeurActionPerformed
        try
        {
            if(ConnexionServeurCompta()) // Si la connexion à Serveur_Compta plante, on ne fait rien ! Il est indispensable
            {
                int PortIn = Integer.parseInt(this.tfdPortIn.getText());
                int PortRes = Integer.parseInt(this.tfdPortRes.getText());
                btnDemarrerServeur.setEnabled(false);
                btnStopperServeur.setEnabled(true);
                BeanBDAccess BeanTrafic = new BeanBDMySql(PropertiesTrafic.getProperty("ADRESSE_IP_BD"), PropertiesTrafic.getProperty("PORT_BD"), PropertiesTrafic.getProperty("SCHEMA_BD"), "root", "");
                HashMap hm = new HashMap();
                hm.put(RequeteTRAMAP.BeanTrafic, BeanTrafic);

                /* Instanciation et lancement du ThreadServeur du protocole TRAMAP (Application_Trafic) */
                this.setThreadServeurTRAMAP(new ThreadServeur(PortIn, new ListeTaches(), this, NombreMaxClients, hm));
                this.getThreadServeurTRAMAP().start();

                /* Instanciation et lancement du ThreadServeur du protocole BOOMAP (Serveur_Conteneurs) */
                this.setThreadServeurServeurConteneur(new ThreadServeurBOOMAP(PortRes, this, hm, Separator, FinChaine));
                this.getThreadServeurServeurConteneur().start();

                this.TraceEvenements("Main#Lancement serveur#Main");
            }
        }
        catch(NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Le port entré est invalide.", "Erreur de saisie du port", JOptionPane.ERROR_MESSAGE);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDemarrerServeurActionPerformed

    private void btnStopperServeurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopperServeurActionPerformed
        if(this.getThreadServeurTRAMAP() != null)
        {
            this.getThreadServeurTRAMAP().interrupt();
            this.getThreadServeurServeurConteneur().interrupt();
            this.TraceEvenements("Main#Arrêt du serveur#Main");
            btnDemarrerServeur.setEnabled(true);
            btnStopperServeur.setEnabled(false);
        }
    }//GEN-LAST:event_btnStopperServeurActionPerformed

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
    private javax.swing.JButton btnDemarrerServeur;
    private javax.swing.JButton btnStopperServeur;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPortIn;
    private javax.swing.JLabel lblPortRes;
    private javax.swing.JTable tblRequete;
    private javax.swing.JTextField tfdPortIn;
    private javax.swing.JTextField tfdPortRes;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the _ThreadServeurTRAMAP
     */
    public ThreadServeur getThreadServeurTRAMAP() {
        return _ThreadServeurTRAMAP;
    }

    /**
     * @param _ThreadServeurTRAMAP the _ThreadServeurTRAMAP to set
     */
    public void setThreadServeurTRAMAP(ThreadServeur _ThreadServeurTRAMAP) {
        this._ThreadServeurTRAMAP = _ThreadServeurTRAMAP;
    }

    /**
     * @return the _ThreadServeurBOOMAP
     */
    public ThreadServeurBOOMAP getThreadServeurServeurConteneur() {
        return _ThreadServeurBOOMAP;
    }

    /**
     * @param _ThreadServeurBOOMAP the _ThreadServeurBOOMAP to set
     */
    public void setThreadServeurServeurConteneur(ThreadServeurBOOMAP _ThreadServeurBOOMAP) {
        this._ThreadServeurBOOMAP = _ThreadServeurBOOMAP;
    }
}
