/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application_compta;

import Commandes.ListBills;
import Compta.Facture;
import Encryption.Cryptographie;
import RequeteReponseBISAMAP.ReponseBISAMAP;
import RequeteReponseBISAMAP.RequeteBISAMAP;
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
public class ListBillsForm extends javax.swing.JDialog
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
    private String NameKeyStore;
    private String AliasKeyStore;
    private String PasswordKeyStore;
    private String PasswordKey;
    /**
     * Creates new form ListBillsForm
     */
    public ListBillsForm(java.awt.Frame parent, boolean modal) 
    {
        super(parent, modal);
        initComponents();
        FillComboBoxSociete();
        dlmFacture = new DefaultListModel();
        dlmContainers = new DefaultListModel();
        lstIdFacture.setModel(dlmFacture);
        lstContainersConcernes.setModel(dlmContainers);
        AddSelectionListener();
    }
    
    public ListBillsForm(java.awt.Frame parent, boolean modal, Socket CSocket, SecretKey CleChiffrement, String PathKeyStore, String AliasKeyStore, String PasswordKeyStore, String PasswordKey) 
    {
        super(parent, modal);
        initComponents();
        FillComboBoxSociete();
        FillComboBoxesDates();
        dlmFacture = new DefaultListModel();
        dlmContainers = new DefaultListModel();
        lstIdFacture.setModel(dlmFacture);
        lstContainersConcernes.setModel(dlmContainers);
        this.CSocket = CSocket;
        this.CleChiffrement = CleChiffrement;
        this.NameKeyStore = PathKeyStore;
        this.AliasKeyStore = AliasKeyStore;
        this.PasswordKeyStore = PasswordKeyStore;
        this.PasswordKey = PasswordKey;
        AddSelectionListener();
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
    
    private void FillComboBoxesDates()
    {
        dcbmMois1 = new DefaultComboBoxModel();
        dcbmMois2 = new DefaultComboBoxModel();
        
        for(int i = 1; i < 13; i++)
        {
            if(i < 10)
            {
                dcbmMois1.addElement("0" + String.valueOf(i));
                dcbmMois2.addElement("0" + String.valueOf(i));
            }
            else
            {
                dcbmMois1.addElement(String.valueOf(i));
                dcbmMois2.addElement(String.valueOf(i));
            }
        }
        
        dcbmAnnee1 = new DefaultComboBoxModel();
        dcbmAnnee1.addElement("2015");
        dcbmAnnee1.addElement("2016");
        dcbmAnnee2 = new DefaultComboBoxModel();
        dcbmAnnee2.addElement("2015");
        dcbmAnnee2.addElement("2016");
        cbxMois1.setModel(dcbmMois1);
        cbxMois2.setModel(dcbmMois2);
        cbxAnnee1.setModel(dcbmAnnee1);
        cbxAnnee2.setModel(dcbmAnnee2);
    }
    
    private void FillListFactures()
    {
        dlmFacture.clear();
        for(int i = 0; i < ListFactures.size(); i++)
            dlmFacture.addElement(ListFactures.get(i).getIdFacture());
        
        JOptionPane.showMessageDialog(this, "Cliquez sur l'un des identifiants de facture dans la liste de gauche pour afficher les détails", "Afficher le détail d'une facture", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void AddSelectionListener()
    {
        lstIdFacture.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0)
            {
                if (!arg0.getValueIsAdjusting())
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

        lblMoisAnnee = new javax.swing.JLabel();
        tfdIdMouvement = new javax.swing.JTextField();
        lblMontantHTVA = new javax.swing.JLabel();
        tfdMoisAnnee = new javax.swing.JTextField();
        lblMontantAPayer = new javax.swing.JLabel();
        tfdMontantHTVA = new javax.swing.JTextField();
        lblListeContainers = new javax.swing.JLabel();
        tfdMontantTotal = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        tfdDestination = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstContainersConcernes = new javax.swing.JList();
        lblDestination = new javax.swing.JLabel();
        lblIdFacture = new javax.swing.JLabel();
        lblIdSociete = new javax.swing.JLabel();
        tfdIdFacture = new javax.swing.JTextField();
        lblIdMouvement = new javax.swing.JLabel();
        tfdIdSociete = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstIdFacture = new javax.swing.JList();
        btnOk = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        cbxSociete = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnRechercher = new javax.swing.JButton();
        cbxMois1 = new javax.swing.JComboBox();
        cbxAnnee1 = new javax.swing.JComboBox();
        cbxMois2 = new javax.swing.JComboBox();
        cbxAnnee2 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Visualisation des factures d'une société");

        lblMoisAnnee.setText("Mois - année : ");

        lblMontantHTVA.setText("Montant total HTVA : ");

        lblMontantAPayer.setText("Montant total à payer : ");

        lblListeContainers.setText("Liste des containers concernés");

        jScrollPane1.setViewportView(lstContainersConcernes);

        lblDestination.setText("Destination : ");

        lblIdFacture.setText("Identifiant de la facture : ");

        lblIdSociete.setText("Identifiant de la société : ");

        lblIdMouvement.setText("Identifiant du mouvement : ");

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

        jLabel1.setText("Nom de la société : ");

        cbxSociete.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Entre le mois : ");

        jLabel3.setText("Année : ");

        jLabel4.setText("et le mois : ");

        jLabel5.setText("Année : ");

        btnRechercher.setText("Rechercher");
        btnRechercher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRechercherActionPerformed(evt);
            }
        });

        cbxMois1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbxAnnee1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbxMois2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbxAnnee2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblListeContainers)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblDestination, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblMontantAPayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblMontantHTVA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblMoisAnnee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblIdMouvement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblIdSociete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblIdFacture, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfdDestination, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfdIdFacture, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfdIdSociete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfdIdMouvement, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfdMoisAnnee, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfdMontantHTVA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfdMontantTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxMois1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxAnnee1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxMois2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxAnnee2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnRechercher, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxSociete, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cbxSociete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(cbxMois1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxAnnee1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxMois2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxAnnee2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRechercher)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                            .addComponent(tfdMontantTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMontantAPayer))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfdDestination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDestination))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblListeContainers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOk)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnRechercherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechercherActionPerformed
        String strIdSociete = (String)cbxSociete.getSelectedItem();
        String strRequete = strIdSociete + "#" + (String)cbxMois1.getSelectedItem() + "_" + (String)cbxAnnee1.getSelectedItem() + "#" + (String)cbxMois2.getSelectedItem() + "_" + (String)cbxAnnee2.getSelectedItem();
        byte[] signature = Cryptographie.CreateSignature(NameKeyStore, AliasKeyStore, PasswordKeyStore, PasswordKey, strRequete);
        
        try 
        {
            ListBills lb = new ListBills(strRequete, signature);
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            RequeteBISAMAP req = new RequeteBISAMAP(RequeteBISAMAP.REQUEST_LIST_BILLS, lb);
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
                ListFactures = (ArrayList<Facture>)Cryptographie.Dechiffrement(CleChiffrement, reponse);
                FillListFactures();
            }
            else
                JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Erreur lors de la récupération de la liste des factures", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex)
        {
            Logger.getLogger(GetNextBillForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(GetNextBillForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRechercherActionPerformed

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
            java.util.logging.Logger.getLogger(ListBillsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListBillsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListBillsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListBillsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ListBillsForm dialog = new ListBillsForm(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnRechercher;
    private javax.swing.JComboBox cbxAnnee1;
    private javax.swing.JComboBox cbxAnnee2;
    private javax.swing.JComboBox cbxMois1;
    private javax.swing.JComboBox cbxMois2;
    private javax.swing.JComboBox cbxSociete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
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
