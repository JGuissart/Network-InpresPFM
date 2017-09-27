/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application_compta;

import Commandes.ListBills;
import Commandes.ListWaiting;
import Commandes.ListWaitingSigned;
import Compta.Facture;
import Encryption.Cryptographie;
import RequeteReponseBISAMAP.ReponseBISAMAP;
import RequeteReponseBISAMAP.RequeteBISAMAP;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Julien
 */
public class ListWaitingForm extends javax.swing.JDialog 
{
    private DefaultListModel dlmFacture;
    private DefaultListModel dlmContainers;
    private DefaultComboBoxModel dcbmSociete;
    private DefaultComboBoxModel dcbmMois1;
    private DefaultComboBoxModel dcbmMois2;
    private DefaultComboBoxModel dcbmAnnee1;
    private DefaultComboBoxModel dcbmAnnee2;
    private ArrayList<Facture> ListFactures;
    private Socket CSocket;
    private SecretKey CleChiffrement;
    private String PathKeyStore;
    private String AliasKeyStore;
    private String PasswordKeyStore;
    private String PasswordKey;
    
    /**
     * Creates new form ListWaitingForm
     */
    public ListWaitingForm(java.awt.Frame parent, boolean modal) 
    {
        super(parent, modal);
        initComponents();
    }
    
    public ListWaitingForm(java.awt.Frame parent, boolean modal, Socket CSocket, SecretKey CleChiffrement, String PathKeyStore, String AliasKeyStore, String PasswordKeyStore, String PasswordKey) 
    {
        super(parent, modal);
        initComponents();
        FillComboBoxSociete();
        dlmFacture = new DefaultListModel();
        dlmContainers = new DefaultListModel();
        lstIdFacture.setModel(dlmFacture);
        lstContainersConcernes.setModel(dlmContainers);
        this.CSocket = CSocket;
        this.CleChiffrement = CleChiffrement;
        this.PathKeyStore = PathKeyStore;
        this.AliasKeyStore = AliasKeyStore;
        this.PasswordKeyStore = PasswordKeyStore;
        this.PasswordKey = PasswordKey;
        AddListeners();
    }
    
    private void FillComboBoxSociete()
    {
        dcbmSociete = new DefaultComboBoxModel();
        dcbmSociete.addElement("Amazon.com, Inc.");
        dcbmSociete.addElement("Apple Inc.");
        dcbmSociete.addElement("ASUSTek Computer");
        dcbmSociete.addElement("Charlet SARL");
        dcbmSociete.addElement("Cisco Systems");
        dcbmSociete.addElement("Desart SA");
        dcbmSociete.addElement("Google Inc.");
        dcbmSociete.addElement("Guissart SPRL");
        dcbmSociete.addElement("IG Farben");
        dcbmSociete.addElement("Krupp AG");
        dcbmSociete.addElement("Microsoft Corporation");
        dcbmSociete.addElement("Nokia Corporation");
        dcbmSociete.addElement("Vilvens Corporation");
        dcbmSociete.addElement("Volkswagen AG");
        cbxSociete.setModel(dcbmSociete);
    }
    
    private void FillListFactures()
    {
        dlmFacture.clear();
        for(int i = 0; i < ListFactures.size(); i++)
            dlmFacture.addElement(ListFactures.get(i).getIdFacture());
        
        JOptionPane.showMessageDialog(this, "Cliquez sur l'un des identifiants de facture dans la liste de gauche pour afficher les détails", "Afficher le détail d'une facture", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void AddListeners()
    {
        lstIdFacture.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0)
            {
                if(!arg0.getValueIsAdjusting())
                {
                    String strIdFacture = (String)lstIdFacture.getSelectedValue();
                    Facture f = null;
                    for(int i = 0; i < ListFactures.size(); i++) 
                    {
                        f = ListFactures.get(i);
                        if(f.getIdFacture().equals(strIdFacture))
                            break;
                    }
                    FilleFactureDetails(f);
                }
            }
        });
        
        cbxTypeRecherche.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(cbxTypeRecherche.getSelectedIndex() == 1)
                    cbxSociete.setEnabled(true);
                else
                    cbxSociete.setEnabled(false);
            }
        });
    }
    
    private void FilleFactureDetails(Facture f)
    {
        tfdIdFacture.setText(f.getIdFacture());
        tfdIdSociete.setText(f.getIdSociete());
        tfdIdMouvement.setText(f.getIdMouvement());
        tfdDestination.setText(f.getDestination());
        tfdMoisAnnee.setText(f.getMois() + "/" + f.getAnnee());
        tfdMontantHTVA.setText(String.valueOf(f.getMontantTotalHTVA()));
        tfdMontantTotal.setText(String.valueOf(f.getMontantTotalTVA()));
        
        dlmContainers.clear();
        for(int i = 0; i < f.getListContainers().size(); i++)
            dlmContainers.addElement(f.getListContainers().get(i));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfdMontantHTVA = new javax.swing.JTextField();
        lblIdMouvement = new javax.swing.JLabel();
        lblListeContainers = new javax.swing.JLabel();
        tfdIdSociete = new javax.swing.JTextField();
        tfdMontantTotal = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstIdFacture = new javax.swing.JList();
        tfdDestination = new javax.swing.JTextField();
        btnOk = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstContainersConcernes = new javax.swing.JList();
        lblDestination = new javax.swing.JLabel();
        lblIdFacture = new javax.swing.JLabel();
        lblIdSociete = new javax.swing.JLabel();
        tfdIdFacture = new javax.swing.JTextField();
        lblMoisAnnee = new javax.swing.JLabel();
        tfdIdMouvement = new javax.swing.JTextField();
        lblMontantHTVA = new javax.swing.JLabel();
        tfdMoisAnnee = new javax.swing.JTextField();
        lblMontantAPayer = new javax.swing.JLabel();
        btnRecherche = new javax.swing.JButton();
        cbxTypeRecherche = new javax.swing.JComboBox();
        cbxSociete = new javax.swing.JComboBox();
        jSeparator2 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblIdMouvement.setText("Identifiant du mouvement : ");

        lblListeContainers.setText("Liste des containers concernés");

        lstIdFacture.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstIdFacture);

        btnOk.setText("Ok");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(lstContainersConcernes);

        lblDestination.setText("Destination : ");

        lblIdFacture.setText("Identifiant de la facture : ");

        lblIdSociete.setText("Identifiant de la société : ");

        lblMoisAnnee.setText("Mois - année : ");

        lblMontantHTVA.setText("Montant total HTVA : ");

        lblMontantAPayer.setText("Montant total à payer : ");

        btnRecherche.setText("Lancer la recherche");
        btnRecherche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRechercheActionPerformed(evt);
            }
        });

        cbxTypeRecherche.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Toutes les factures", "Toutes les factures d'une société", "Toutes les factures émises depuis plus d'un mois" }));

        cbxSociete.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRecherche, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblIdMouvement)
                                .addGap(18, 18, 18)
                                .addComponent(tfdIdMouvement))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblIdFacture)
                                .addGap(28, 28, 28)
                                .addComponent(tfdIdFacture))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblIdSociete)
                                .addGap(29, 29, 29)
                                .addComponent(tfdIdSociete))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblMoisAnnee)
                                .addGap(81, 81, 81)
                                .addComponent(tfdMoisAnnee))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblMontantHTVA)
                                .addGap(48, 48, 48)
                                .addComponent(tfdMontantHTVA))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblMontantAPayer)
                                .addGap(37, 37, 37)
                                .addComponent(tfdMontantTotal))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblListeContainers)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDestination)
                                .addGap(88, 88, 88)
                                .addComponent(tfdDestination))
                            .addComponent(jSeparator1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)))
                    .addComponent(jSeparator2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cbxTypeRecherche, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(cbxSociete, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxTypeRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxSociete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRecherche)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
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
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMontantAPayer)
                            .addComponent(tfdMontantTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDestination)
                            .addComponent(tfdDestination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblListeContainers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOk)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnRechercheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechercheActionPerformed
        try 
        {
            ListWaiting lw = null;
        
            switch(cbxTypeRecherche.getSelectedIndex())
            {
                case 0: lw = new ListWaiting("ALL");
                    break;

                case 1: lw = new ListWaiting("SOCIETE", (String)cbxSociete.getSelectedItem());
                    break;

                case 2: lw = new ListWaiting("MONTH");
                    break;
            }

            byte[] signature = Cryptographie.CreateSignature(PathKeyStore, AliasKeyStore, PasswordKeyStore, PasswordKey, lw);
            ListWaitingSigned lws = new ListWaitingSigned(lw, signature);
            
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            RequeteBISAMAP req = new RequeteBISAMAP(RequeteBISAMAP.REQUEST_LIST_WAITING, lws);
            oos.writeObject(req);
            oos.flush();
            
            /* Attente de réponse */
            ObjectInputStream ois = null;
            ReponseBISAMAP rep = null;
            ois = new ObjectInputStream(CSocket.getInputStream());
            rep = (ReponseBISAMAP)ois.readObject();
            
            if(rep.getCode() == ReponseBISAMAP.REPONSE_OK)
            {
                ListFactures = (ArrayList<Facture>)rep.getResult();
                FillListFactures();
            }
            else
                JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Erreur lors de la récupération de la liste des factures", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ListWaitingForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ListWaitingForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRechercheActionPerformed

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
            java.util.logging.Logger.getLogger(ListWaitingForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListWaitingForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListWaitingForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListWaitingForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ListWaitingForm dialog = new ListWaitingForm(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRecherche;
    private javax.swing.JComboBox cbxSociete;
    private javax.swing.JComboBox cbxTypeRecherche;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblDestination;
    private javax.swing.JLabel lblIdFacture;
    private javax.swing.JLabel lblIdMouvement;
    private javax.swing.JLabel lblIdSociete;
    private javax.swing.JLabel lblListeContainers;
    private javax.swing.JLabel lblMoisAnnee;
    private javax.swing.JLabel lblMontantAPayer;
    private javax.swing.JLabel lblMontantHTVA;
    private javax.swing.JList lstContainersConcernes;
    private javax.swing.JList lstIdFacture;
    private javax.swing.JTextField tfdDestination;
    private javax.swing.JTextField tfdIdFacture;
    private javax.swing.JTextField tfdIdMouvement;
    private javax.swing.JTextField tfdIdSociete;
    private javax.swing.JTextField tfdMoisAnnee;
    private javax.swing.JTextField tfdMontantHTVA;
    private javax.swing.JTextField tfdMontantTotal;
    // End of variables declaration//GEN-END:variables
}
