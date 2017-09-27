/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application_compta;

import Compta.Facture;
import Encryption.Cryptographie;
import Commandes.ValidateBill;
import Commandes.ValidateBillSigned;
import RequeteReponseBISAMAP.ReponseBISAMAP;
import RequeteReponseBISAMAP.RequeteBISAMAP;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Julien
 */
public class GetNextBillForm extends javax.swing.JDialog
{
    private String Comptable;
    private Socket CSocket;
    private DefaultListModel dlm;
    private SecretKey CleChiffrement;
    private String NameKeyStore;
    private String AliasKeyStore;
    private String PasswordKeyStore;
    private String PasswordKey;
    /**
     * Creates new form GetNextBillForm
     */
    public GetNextBillForm(java.awt.Frame parent, boolean modal) 
    {
        super(parent, modal);
        initComponents();
    }
    
    public GetNextBillForm(java.awt.Frame parent, boolean modal, String Comptable, Socket CSocket, SecretKey CleChiffrement, String NameKeyStore, String AliasKeyStore, String PasswordKeyStore, String PasswordKey) 
    {
        super(parent, modal);
        initComponents();
        this.Comptable = Comptable;
        this.CSocket = CSocket;
        this.CleChiffrement = CleChiffrement;
        this.NameKeyStore = NameKeyStore;
        this.AliasKeyStore = AliasKeyStore;
        this.PasswordKeyStore = PasswordKeyStore;
        this.PasswordKey = PasswordKey;
        dlm = new DefaultListModel();
        lstContainersConcernes.setModel(dlm);
        GetNextBillRequest();
    }
    
    private void GetNextBillRequest()
    {
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            RequeteBISAMAP req = new RequeteBISAMAP(RequeteBISAMAP.REQUEST_GET_NEXT_BILL, null);
            oos.writeObject(req);
            oos.flush();
            
            /* Attente de réponse */
            ObjectInputStream ois = null;
            ReponseBISAMAP rep = null;
            ois = new ObjectInputStream(CSocket.getInputStream());
            rep = (ReponseBISAMAP)ois.readObject();
            
            if(rep.getCode() == ReponseBISAMAP.REPONSE_OK)
            {
                byte[] reponse = (byte[])rep.getResult();
                Facture facture = (Facture)Cryptographie.Dechiffrement(CleChiffrement, reponse);
                FillInformations(facture);
            }
            else
                JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Erreur lors de la récupération de la facture", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex)
        {
            Logger.getLogger(GetNextBillForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(GetNextBillForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void FillInformations(Facture facture)
    {
        tfdIdFacture.setText(facture.getIdFacture());
        tfdIdSociete.setText(facture.getIdSociete());
        tfdIdMouvement.setText(facture.getIdMouvement());
        tfdDestination.setText(facture.getDestination());
        tfdMoisAnnee.setText(facture.getMois() + "/" + facture.getAnnee());
        tfdMontantHTVA.setText(String.valueOf(facture.getMontantTotalHTVA()));
        tfdMontantTotal.setText(String.valueOf(facture.getMontantTotalTVA()));
        
        for(int i = 0; i < facture.getListContainers().size(); i++)
            dlm.addElement(facture.getListContainers().get(i));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblIdFacture = new javax.swing.JLabel();
        lblIdSociete = new javax.swing.JLabel();
        lblIdMouvement = new javax.swing.JLabel();
        lblMoisAnnee = new javax.swing.JLabel();
        lblMontantHTVA = new javax.swing.JLabel();
        lblMontantAPayer = new javax.swing.JLabel();
        lblListeContainers = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstContainersConcernes = new javax.swing.JList();
        lblDestination = new javax.swing.JLabel();
        btnValider = new javax.swing.JButton();
        tfdIdFacture = new javax.swing.JTextField();
        tfdIdSociete = new javax.swing.JTextField();
        tfdIdMouvement = new javax.swing.JTextField();
        tfdMoisAnnee = new javax.swing.JTextField();
        tfdMontantHTVA = new javax.swing.JTextField();
        tfdMontantTotal = new javax.swing.JTextField();
        tfdDestination = new javax.swing.JTextField();
        btnInvalider = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Consultation de la plus ancienne facture non validée");

        lblIdFacture.setText("Numéro de facture : ");

        lblIdSociete.setText("Nom de la société : ");

        lblIdMouvement.setText("Identifiant du mouvement : ");

        lblMoisAnnee.setText("Mois - année : ");

        lblMontantHTVA.setText("Montant total HTVA : ");

        lblMontantAPayer.setText("Montant total à payer : ");

        lblListeContainers.setText("Liste des containers concernés");

        jScrollPane1.setViewportView(lstContainersConcernes);

        lblDestination.setText("Destination : ");

        btnValider.setText("Valider la facture");
        btnValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValiderActionPerformed(evt);
            }
        });

        btnInvalider.setText("Invalider la facture");
        btnInvalider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvaliderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnValider, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnInvalider, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(lblListeContainers, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblDestination, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMontantAPayer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMontantHTVA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMoisAnnee, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblIdMouvement, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblIdFacture, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblIdSociete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfdIdFacture, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tfdIdSociete, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tfdIdMouvement, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tfdMoisAnnee, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tfdMontantHTVA, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tfdMontantTotal)
                            .addComponent(tfdDestination, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIdFacture)
                            .addComponent(tfdIdFacture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIdSociete)
                            .addComponent(tfdIdSociete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIdMouvement)
                            .addComponent(tfdIdMouvement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMoisAnnee)
                            .addComponent(tfdMoisAnnee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMontantHTVA)
                            .addComponent(tfdMontantHTVA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMontantAPayer)
                            .addComponent(tfdMontantTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDestination)
                            .addComponent(tfdDestination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblListeContainers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnValider)
                    .addComponent(btnInvalider))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValiderActionPerformed
        try 
        {
            ValidateBill vb = new ValidateBill(tfdIdFacture.getText(), Comptable);
            byte[] signature = Cryptographie.CreateSignature(NameKeyStore, AliasKeyStore, PasswordKeyStore, PasswordKey, vb);
            ValidateBillSigned vbs = new ValidateBillSigned(vb, signature);
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            RequeteBISAMAP req = new RequeteBISAMAP(RequeteBISAMAP.REQUEST_VALIDATE_BILL, vbs);
            oos.writeObject(req);
            oos.flush();
            
            /* Attente de réponse */
            ObjectInputStream ois = null;
            ReponseBISAMAP rep = null;
            ois = new ObjectInputStream(CSocket.getInputStream());
            rep = (ReponseBISAMAP)ois.readObject();
            
            if(rep.getCode() == ReponseBISAMAP.REPONSE_OK)
            {
                JOptionPane.showMessageDialog(this, "La facture a été validée !", "Validation de la facture", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
            else
                JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Erreur lors de la récupération de la facture", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex)
        {
            Logger.getLogger(GetNextBillForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(GetNextBillForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnValiderActionPerformed

    private void btnInvaliderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvaliderActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnInvaliderActionPerformed

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
            java.util.logging.Logger.getLogger(GetNextBillForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GetNextBillForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GetNextBillForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GetNextBillForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GetNextBillForm dialog = new GetNextBillForm(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInvalider;
    private javax.swing.JButton btnValider;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDestination;
    private javax.swing.JLabel lblIdFacture;
    private javax.swing.JLabel lblIdMouvement;
    private javax.swing.JLabel lblIdSociete;
    private javax.swing.JLabel lblListeContainers;
    private javax.swing.JLabel lblMoisAnnee;
    private javax.swing.JLabel lblMontantAPayer;
    private javax.swing.JLabel lblMontantHTVA;
    private javax.swing.JList lstContainersConcernes;
    private javax.swing.JTextField tfdDestination;
    private javax.swing.JTextField tfdIdFacture;
    private javax.swing.JTextField tfdIdMouvement;
    private javax.swing.JTextField tfdIdSociete;
    private javax.swing.JTextField tfdMoisAnnee;
    private javax.swing.JTextField tfdMontantHTVA;
    private javax.swing.JTextField tfdMontantTotal;
    // End of variables declaration//GEN-END:variables
}