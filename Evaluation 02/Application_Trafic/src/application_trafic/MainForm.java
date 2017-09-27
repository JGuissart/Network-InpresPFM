/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application_trafic;

import AccessBD.BeanBDAccess;
import AccessBD.BeanBDMySql;
import Queries.QuerySelect;
import RequeteReponseTRAMAP.ReponseTRAMAP;
import RequeteReponseTRAMAP.RequeteTRAMAP;
import com.sun.corba.se.impl.io.IIOPOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Julien
 */
public class MainForm extends javax.swing.JFrame
{
    private Socket CSocket;
    private String Separator;
    private Properties PropertiesFileTrafic;
    private String FileSep;
    private String PropertiesFilePath;

    /**
     * Creates new form MainForm
     */
    public MainForm()
    {
        initComponents();
        LoadPropertiesFile();
    }
    
    private void LoadPropertiesFile()
    {
        PropertiesFileTrafic = new Properties();
        FileSep = System.getProperty("file.separator");
        PropertiesFilePath = System.getProperty("user.dir") + FileSep + "Serveur_Trafic.properties";
        
        try
        {
            PropertiesFileTrafic.load(new FileInputStream(PropertiesFilePath));
        }
        catch (IOException ex)
        {
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

        btnConnexion = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        btnInputLorry = new javax.swing.JButton();
        btnInputLorryWithoutReservation = new javax.swing.JButton();
        btnListOperation = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuConnexion = new javax.swing.JMenu();
        miConnexion = new javax.swing.JMenuItem();
        miDeconnexion = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        btnConnexion.setText("Connexion au serveur");
        btnConnexion.setPreferredSize(new java.awt.Dimension(149, 23));
        btnConnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnexionActionPerformed(evt);
            }
        });

        btnInputLorry.setText("Input Lorry");
        btnInputLorry.setEnabled(false);
        btnInputLorry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInputLorryActionPerformed(evt);
            }
        });

        btnInputLorryWithoutReservation.setText("Input Lorry Without Reservation");
        btnInputLorryWithoutReservation.setEnabled(false);
        btnInputLorryWithoutReservation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInputLorryWithoutReservationActionPerformed(evt);
            }
        });

        btnListOperation.setText("List Operations");
        btnListOperation.setEnabled(false);
        btnListOperation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListOperationActionPerformed(evt);
            }
        });

        menuConnexion.setText("Connexion");

        miConnexion.setText("Se connecter");
        miConnexion.setEnabled(false);
        miConnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miConnexionActionPerformed(evt);
            }
        });
        menuConnexion.add(miConnexion);

        miDeconnexion.setText("Se déconnecter");
        miDeconnexion.setEnabled(false);
        miDeconnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miDeconnexionActionPerformed(evt);
            }
        });
        menuConnexion.add(miDeconnexion);

        jMenuBar1.add(menuConnexion);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(btnInputLorry, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnInputLorryWithoutReservation, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                    .addComponent(btnListOperation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConnexion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnConnexion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInputLorry)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInputLorryWithoutReservation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnListOperation)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnexionActionPerformed
        try
        {
            String AdresseServeur = PropertiesFileTrafic.getProperty("ADRESSE_IP_SERVEUR");
            int PortIn = Integer.parseInt(PropertiesFileTrafic.getProperty("PORT_IN"));
            CSocket = new Socket(AdresseServeur, PortIn);
            System.out.println(CSocket.getInetAddress().toString());
            RequeteTRAMAP req = new RequeteTRAMAP(RequeteTRAMAP.REQUEST_CON, null);
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(CSocket.getOutputStream());
            oos.writeObject(req);
            oos.flush();
            btnConnexion.setEnabled(false);
            miConnexion.setEnabled(true);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnConnexionActionPerformed

    private void miConnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miConnexionActionPerformed
        ConnectionForm CF = new ConnectionForm(this, true);
        CF.setVisible(true);

        try
        {
            RequeteTRAMAP req = CF.getRequete();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(CSocket.getOutputStream());
            oos.writeObject(req);
            oos.flush();
            
            ObjectInputStream ois = null;
            ois = new ObjectInputStream(CSocket.getInputStream());
            ReponseTRAMAP rep = (ReponseTRAMAP)ois.readObject();
            
            if(rep.getCode() == ReponseTRAMAP.LOGIN_OK)
            {
                // Activer les boutons
                miConnexion.setEnabled(false);
                btnInputLorry.setEnabled(true);
                btnInputLorryWithoutReservation.setEnabled(true);
                btnListOperation.setEnabled(true);
                miDeconnexion.setEnabled(true);
            }
            else
                JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Echec de la connexion", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_miConnexionActionPerformed

    private void miDeconnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miDeconnexionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_miDeconnexionActionPerformed

    private void btnInputLorryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInputLorryActionPerformed
        InputLorryForm ILF = new InputLorryForm(this, true);
        ILF.setVisible(true);
        
        try
        {
            RequeteTRAMAP req = new RequeteTRAMAP(RequeteTRAMAP.REQUEST_INPUT_LORRY, ILF.getRequest());
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(CSocket.getOutputStream());
            oos.writeObject(req);
            oos.flush();
            
            ObjectInputStream ois = null;
            ois = new ObjectInputStream(CSocket.getInputStream());
            ReponseTRAMAP rep = (ReponseTRAMAP)ois.readObject();
            
            if(rep.getCode() == ReponseTRAMAP.REPONSE_OK)
            {
                String strReponse = (String)rep.getResult();
                //String[] arrayStringReponse = strReponse.split("#");
                String strMessage = "Voici les emplacements attribués :\n" + strReponse;
                /*for(int i = 0; i < arrayStringReponse.length; i++)
                    strMessage += "\n" + arrayStringReponse[i];*/
                
                JOptionPane.showMessageDialog(this, strMessage, "Réponse de la requête INPUT_LORRY", JOptionPane.INFORMATION_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Réponse de la requête INPUT_LORRY", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnInputLorryActionPerformed

    private void btnInputLorryWithoutReservationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInputLorryWithoutReservationActionPerformed
        try
        {
            String strRequest = "Ville#Destination#NULL";
            RequeteTRAMAP req = new RequeteTRAMAP(RequeteTRAMAP.REQUEST_DATABASE, strRequest);
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            oos.writeObject(req);
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(CSocket.getInputStream());
            ReponseTRAMAP repDestination = (ReponseTRAMAP)ois.readObject();
            HashMap hmDestination = (HashMap)repDestination.getResult();
            DefaultComboBoxModel dcbmDestination = (DefaultComboBoxModel)hmDestination.get("DCBM");
            
            strRequest = "IdTransporteur#Transporteur#CaracteristiquesTechniques = 'Camion' AND IdSociete IN (SELECT IdSociete FROM Societe)";
            req = new RequeteTRAMAP(RequeteTRAMAP.REQUEST_DATABASE, strRequest);
            
            ObjectOutputStream oos2 = new ObjectOutputStream(CSocket.getOutputStream());
            oos2.writeObject(req);
            oos2.flush();
            
            ObjectInputStream ois2 = new ObjectInputStream(CSocket.getInputStream());
            ReponseTRAMAP repSociete = (ReponseTRAMAP)ois2.readObject();
            HashMap hmSociete = (HashMap)repSociete.getResult();
            DefaultComboBoxModel dcbmImmatriculation = (DefaultComboBoxModel)hmSociete.get("DCBM");
            
            InputLorryWithoutReservationForm ILWRF = new InputLorryWithoutReservationForm(this, true);
            ILWRF.setModelComboBoxDestination(dcbmDestination);
            ILWRF.setModelComboBoxImmatriculation(dcbmImmatriculation);
            ILWRF.setVisible(true);
            
            if(!ILWRF.getRequest().isEmpty())
            {
                req = new RequeteTRAMAP(RequeteTRAMAP.REQUEST_INPUT_LORRY_WITHOUT_RESERVATION, ILWRF.getRequest());
                ObjectOutputStream oosILWR = new ObjectOutputStream(CSocket.getOutputStream());
                oosILWR.writeObject(req);
                oosILWR.flush();
                
                ObjectInputStream oisILWR = new ObjectInputStream(CSocket.getInputStream());
                ReponseTRAMAP rep = (ReponseTRAMAP)oisILWR.readObject();
                
                if(rep.getCode() == ReponseTRAMAP.REPONSE_OK)
                {
                    String strReponse = (String)rep.getResult();
                    String[] arrayStringReponse = strReponse.split("#");
                    String strMessage = "Voici le numéro de réservation " + arrayStringReponse[0] + " et les emplacements attribués :";
                    for(int i = 1; i < arrayStringReponse.length; i++)
                        strMessage += "\n" + arrayStringReponse[i];

                    JOptionPane.showMessageDialog(this, strMessage, "Réponse de la requête INPUT_LORRY_WITHOUT_RESERVATION", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                    JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Réponse de la requête INPUT_LORRY_WITHOUT_RESERVATION", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnInputLorryWithoutReservationActionPerformed

    private void btnListOperationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListOperationActionPerformed
        try
        {
            String strRequest = "Ville#Destination#NULL";
            RequeteTRAMAP req = new RequeteTRAMAP(RequeteTRAMAP.REQUEST_DATABASE, strRequest);
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            oos.writeObject(req);
            oos.flush();
            System.err.println("Apres 1er flush");
            ObjectInputStream ois = new ObjectInputStream(CSocket.getInputStream());
            ReponseTRAMAP repDestination = (ReponseTRAMAP)ois.readObject();
            System.err.println("Apres 1er readObject");
            HashMap hmDestination = (HashMap)repDestination.getResult();
            DefaultComboBoxModel dcbmDestination = (DefaultComboBoxModel)hmDestination.get("DCBM");
            
            strRequest = "IdSociete#Societe#NULL";
            req = new RequeteTRAMAP(RequeteTRAMAP.REQUEST_DATABASE, strRequest);
            
            ObjectOutputStream oos2 = new ObjectOutputStream(CSocket.getOutputStream());
            oos2.writeObject(req);
            oos2.flush();
            System.err.println("Apres 2e flush");
            ObjectInputStream ois2 = new ObjectInputStream(CSocket.getInputStream());
            ReponseTRAMAP repSociete = (ReponseTRAMAP)ois2.readObject();
            System.err.println("Apres 2e readObject");
            HashMap hmSociete = (HashMap)repSociete.getResult();
            DefaultComboBoxModel dcbmSociete = (DefaultComboBoxModel)hmSociete.get("DCBM");
            
            ListOperationsForm LOF = new ListOperationsForm(this, true, CSocket);
            LOF.setModelComboBoxDestination(dcbmDestination);
            LOF.setModelComboBoxSociete(dcbmSociete);
            LOF.setVisible(true);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnListOperationActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try
        {
            RequeteTRAMAP req = new RequeteTRAMAP(RequeteTRAMAP.REQUEST_DEC, null);
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            oos.writeObject(req);
            oos.flush();

            CSocket.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

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
    private javax.swing.JButton btnConnexion;
    private javax.swing.JButton btnInputLorry;
    private javax.swing.JButton btnInputLorryWithoutReservation;
    private javax.swing.JButton btnListOperation;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JMenu menuConnexion;
    private javax.swing.JMenuItem miConnexion;
    private javax.swing.JMenuItem miDeconnexion;
    // End of variables declaration//GEN-END:variables
}