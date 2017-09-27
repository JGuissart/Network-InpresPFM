/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application_compta;

import Commandes.ComputeSal;
import Commandes.ComputeSalSigned;
import Commandes.ValidateSal;
import Commandes.ValidateSalSigned;
import Compta.Employe;
import Compta.Facture;
import Compta.Prime;
import Compta.Salaire;
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
public class ComputeSalForm extends javax.swing.JDialog 
{
    private DefaultListModel dlmEmployes;
    private DefaultListModel dlmPrimes;
    private DefaultComboBoxModel dcbmMois;
    private DefaultComboBoxModel dcbmAnnee;
    private ArrayList<Salaire> ListSalaires;
    private Socket CSocket;
    private SecretKey CleChiffrement;
    private SecretKey CleHMAC;
    private String NameKeyStore;
    private String AliasKeyStore;
    private String PasswordKeyStore;
    private String PasswordKey;
    /**
     * Creates new form ComputeSalForm
     */
    public ComputeSalForm(java.awt.Frame parent, boolean modal) 
    {
        super(parent, modal);
        initComponents();
        FillComboBoxesDates();
        dlmEmployes = new DefaultListModel();
        dlmPrimes = new DefaultListModel();
        lstEmployes.setModel(dlmEmployes);
        lstPrimes.setModel(dlmPrimes);
        AddSelectionListener();
    }
    
    public ComputeSalForm(java.awt.Frame parent, boolean modal, Socket CSocket, SecretKey CleChiffrement, SecretKey CleHMAC, String PathKeyStore, String AliasKeyStore, String PasswordKeyStore, String PasswordKey) 
    {
        super(parent, modal);
        initComponents();
        FillComboBoxesDates();
        dlmEmployes = new DefaultListModel();
        dlmPrimes = new DefaultListModel();
        lstEmployes.setModel(dlmEmployes);
        lstPrimes.setModel(dlmPrimes);
        this.CSocket = CSocket;
        this.CleChiffrement = CleChiffrement;
        this.CleHMAC = CleHMAC;
        this.NameKeyStore = PathKeyStore;
        this.AliasKeyStore = AliasKeyStore;
        this.PasswordKeyStore = PasswordKeyStore;
        this.PasswordKey = PasswordKey;
        AddSelectionListener();
    }
    
    private void FillComboBoxesDates()
    {
        dcbmMois = new DefaultComboBoxModel();
        
        for(int i = 1; i < 13; i++)
        {
            if(i < 10)
                dcbmMois.addElement("0" + String.valueOf(i));
            else
                dcbmMois.addElement(String.valueOf(i));
        }
        
        dcbmAnnee = new DefaultComboBoxModel();
        dcbmAnnee.addElement("2015");
        dcbmAnnee.addElement("2016");
        cbxMois.setModel(dcbmMois);
        cbxAnnee.setModel(dcbmAnnee);
    }
    
    private void FillListEmployes()
    {
        dlmEmployes.clear();
        for(int i = 0; i < ListSalaires.size(); i++)
            dlmEmployes.addElement(ListSalaires.get(i).getEmploye());
        
        JOptionPane.showMessageDialog(this, "Cliquez sur l'un des employés dans la liste de gauche pour afficher les détails", "Afficher le détail d'un salaire", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void AddSelectionListener()
    {
        lstEmployes.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0)
            {
                if (!arg0.getValueIsAdjusting())
                {
                    Employe e = (Employe)lstEmployes.getSelectedValue();
                    Salaire s = null;
                    for(int i = 0; i < ListSalaires.size(); i++) 
                    {
                        s = ListSalaires.get(i);
                        if(e.getLogin().equals(s.getEmploye().getLogin()))
                            break;
                    }
                    FillSalaireDetails(s);
                }
            }
        });
        
        lstPrimes.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0)
            {
                if (!arg0.getValueIsAdjusting())
                {
                    Prime p = (Prime)lstPrimes.getSelectedValue();
                    FillPrimeDetails(p);
                }
            }
        });
    }
    
    private void FillSalaireDetails(Salaire s)
    {
        tfdBeneficiaire.setText(s.getEmploye().toString());
        tfdFonction.setText(s.getEmploye().getFonction());
        tfdSalaireBrut.setText(s.getMontantBrut().toString());
        tfdONSS.setText(s.getRetraitONSS().toString());
        tfdPrecompte.setText(s.getRetraitPrecompte().toString());
        Double salaireNet = s.getMontantBrut() - s.getRetraitONSS() - s.getRetraitPrecompte();
        tfdSalaireNet.setText(salaireNet.toString());
        
        dlmPrimes.clear();
        tfdMontantPrime.setText("");
        tfdMotifPrime.setText("");
        tfdDatePrime.setText("");
        
        if(s.getListPrimes().size() > 0)
        {
            for(int i = 0; i < s.getListPrimes().size(); i++)
                dlmPrimes.addElement(s.getListPrimes().get(i));
            
            lstPrimes.setSelectedIndex(0);
        }
    }
    
    private void FillPrimeDetails(Prime p)
    {
        try
        {
            tfdMontantPrime.setText(p.getMontant().toString());
            tfdMotifPrime.setText(p.getMotif());
            tfdDatePrime.setText(p.getDateOctroi());
        }
        catch(NullPointerException ex)
        {
            //System.err.println("Erreur = " + ex.getMessage());
        }
    }
    
    private void ValidateSal()
    {
        ValidateSal vs = new ValidateSal((String)cbxMois.getSelectedItem(), (String)cbxAnnee.getSelectedItem());
        byte[] signature = Cryptographie.CreateSignature(NameKeyStore, AliasKeyStore, PasswordKeyStore, PasswordKey, vs);
        byte[] hmac = Cryptographie.CreateHMAC(CleHMAC, vs);
        ValidateSalSigned vss = new ValidateSalSigned(vs, hmac, signature);
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            RequeteBISAMAP req = new RequeteBISAMAP(RequeteBISAMAP.REQUEST_VALIDATE_SAL, vss);
            oos.writeObject(req);
            oos.flush();
            
            /* Attente de réponse */
            ReponseBISAMAP rep = null;
            ObjectInputStream ois = new ObjectInputStream(CSocket.getInputStream());
            rep = (ReponseBISAMAP)ois.readObject();
            
            if(rep.getCode() == ReponseBISAMAP.REPONSE_OK)
            {
                ListSalaires = (ArrayList<Salaire>) Cryptographie.Dechiffrement(CleChiffrement, (byte[]) rep.getResult());
                FillListEmployes();
            }
            else
                JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Erreur lors du calcul des salaires", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ComputeSalForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ComputeSalForm.class.getName()).log(Level.SEVERE, null, ex);
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

        lblIdFacture = new javax.swing.JLabel();
        lblIdSociete = new javax.swing.JLabel();
        tfdBeneficiaire = new javax.swing.JTextField();
        lblIdMouvement = new javax.swing.JLabel();
        tfdFonction = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstEmployes = new javax.swing.JList();
        btnValiderSalaires = new javax.swing.JButton();
        lblMoisAnnee = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        tfdSalaireBrut = new javax.swing.JTextField();
        lblMontantHTVA = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tfdONSS = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lblMontantAPayer = new javax.swing.JLabel();
        tfdPrecompte = new javax.swing.JTextField();
        lblListeContainers = new javax.swing.JLabel();
        btnCalculerSalaires = new javax.swing.JButton();
        tfdSalaireNet = new javax.swing.JTextField();
        cbxMois = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        cbxAnnee = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstPrimes = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfdMontantPrime = new javax.swing.JTextField();
        tfdMotifPrime = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfdDatePrime = new javax.swing.JTextField();
        btnInvaliderSalaire = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblIdFacture.setText("Bénéficiaire : ");

        lblIdSociete.setText("Fonction : ");

        lblIdMouvement.setText("Salaire brut : ");

        lstEmployes.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstEmployes);

        btnValiderSalaires.setText("Valider les salaires");
        btnValiderSalaires.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValiderSalairesActionPerformed(evt);
            }
        });

        lblMoisAnnee.setText("Retrait ONSS : ");

        lblMontantHTVA.setText("Retrait précompte : ");

        jLabel2.setText("Mois : ");

        jLabel3.setText("Année : ");

        lblMontantAPayer.setText("Salaire à verser : ");

        lblListeContainers.setText("Primes de l'employé sélectionné");

        btnCalculerSalaires.setText("Calculer les salaires pour le mois et l'année sélectionnés");
        btnCalculerSalaires.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculerSalairesActionPerformed(evt);
            }
        });

        cbxMois.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbxAnnee.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jScrollPane1.setViewportView(lstPrimes);

        jLabel1.setText("Montant de la prime : ");

        jLabel4.setText("Motif : ");

        jLabel5.setText("Date : ");

        btnInvaliderSalaire.setText("Invalider les salaires");
        btnInvaliderSalaire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvaliderSalaireActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblMontantHTVA, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                                    .addComponent(lblMoisAnnee, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblIdMouvement, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblIdSociete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblIdFacture, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblMontantAPayer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(tfdPrecompte, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                                        .addComponent(tfdSalaireNet, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                                        .addComponent(tfdONSS))
                                    .addComponent(tfdBeneficiaire)
                                    .addComponent(tfdFonction)
                                    .addComponent(tfdSalaireBrut)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(lblListeContainers, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tfdMotifPrime)
                                    .addComponent(tfdDatePrime)
                                    .addComponent(tfdMontantPrime)))))
                    .addComponent(btnCalculerSalaires, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxMois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbxAnnee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnValiderSalaires, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnInvaliderSalaire, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbxMois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxAnnee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCalculerSalaires)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIdFacture)
                            .addComponent(tfdBeneficiaire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIdSociete)
                            .addComponent(tfdFonction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblIdMouvement)
                            .addComponent(tfdSalaireBrut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMoisAnnee)
                            .addComponent(tfdONSS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMontantHTVA)
                            .addComponent(tfdPrecompte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfdSalaireNet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMontantAPayer))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblListeContainers)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfdMontantPrime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfdMotifPrime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfdDatePrime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnValiderSalaires)
                    .addComponent(btnInvaliderSalaire))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnValiderSalairesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValiderSalairesActionPerformed
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            RequeteBISAMAP req = new RequeteBISAMAP(RequeteBISAMAP.REQUEST_VALIDATE_SAL, true);
            oos.writeObject(req);
            oos.flush();
            
            /* Attente de réponse */
            ObjectInputStream ois = new ObjectInputStream(CSocket.getInputStream());
            ReponseBISAMAP rep = (ReponseBISAMAP)ois.readObject();
            
            if(rep.getCode() == ReponseBISAMAP.REPONSE_OK)
            {
                JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Validation des salaires", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
            else
                JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Erreur lors de la validation des salaires", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ComputeSalForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ComputeSalForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnValiderSalairesActionPerformed

    private void btnCalculerSalairesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculerSalairesActionPerformed
        ComputeSal cs = new ComputeSal((String)cbxMois.getSelectedItem(), (String)cbxAnnee.getSelectedItem());
        byte[] signature = Cryptographie.CreateSignature(NameKeyStore, AliasKeyStore, PasswordKeyStore, PasswordKey, cs);
        byte[] hmac = Cryptographie.CreateHMAC(CleHMAC, cs);
        ComputeSalSigned css = new ComputeSalSigned(cs, hmac, signature);
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            RequeteBISAMAP req = new RequeteBISAMAP(RequeteBISAMAP.REQUEST_COMPUTE_SAL, css);
            oos.writeObject(req);
            oos.flush();
            
            /* Attente de réponse */
            ReponseBISAMAP rep = null;
            ObjectInputStream ois = new ObjectInputStream(CSocket.getInputStream());
            rep = (ReponseBISAMAP)ois.readObject();
            
            if(rep.getCode() == ReponseBISAMAP.REPONSE_OK)
                ValidateSal(); // On lance la commande VALIDATE_SAL
            else
                JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Erreur lors du calcul des salaires", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ComputeSalForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ComputeSalForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCalculerSalairesActionPerformed

    private void btnInvaliderSalaireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvaliderSalaireActionPerformed
        try 
        {
            ObjectOutputStream oos = new ObjectOutputStream(CSocket.getOutputStream());
            RequeteBISAMAP req = new RequeteBISAMAP(RequeteBISAMAP.REQUEST_VALIDATE_SAL, false);
            oos.writeObject(req);
            oos.flush();
            
            /* Attente de réponse */
            ObjectInputStream ois = new ObjectInputStream(CSocket.getInputStream());
            ReponseBISAMAP rep = (ReponseBISAMAP)ois.readObject();
            
            if(rep.getCode() == ReponseBISAMAP.REPONSE_OK)
            {
                JOptionPane.showMessageDialog(this, (String) rep.getResult(), "Validation des salaires", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
            else
                JOptionPane.showMessageDialog(this, (String)rep.getResult(), "Erreur lors de la validation des salaires", JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ComputeSalForm.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ComputeSalForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnInvaliderSalaireActionPerformed

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
            java.util.logging.Logger.getLogger(ComputeSalForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ComputeSalForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ComputeSalForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ComputeSalForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ComputeSalForm dialog = new ComputeSalForm(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCalculerSalaires;
    private javax.swing.JButton btnInvaliderSalaire;
    private javax.swing.JButton btnValiderSalaires;
    private javax.swing.JComboBox cbxAnnee;
    private javax.swing.JComboBox cbxMois;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblIdFacture;
    private javax.swing.JLabel lblIdMouvement;
    private javax.swing.JLabel lblIdSociete;
    private javax.swing.JLabel lblListeContainers;
    private javax.swing.JLabel lblMoisAnnee;
    private javax.swing.JLabel lblMontantAPayer;
    private javax.swing.JLabel lblMontantHTVA;
    private javax.swing.JList lstEmployes;
    private javax.swing.JList lstPrimes;
    private javax.swing.JTextField tfdBeneficiaire;
    private javax.swing.JTextField tfdDatePrime;
    private javax.swing.JTextField tfdFonction;
    private javax.swing.JTextField tfdMontantPrime;
    private javax.swing.JTextField tfdMotifPrime;
    private javax.swing.JTextField tfdONSS;
    private javax.swing.JTextField tfdPrecompte;
    private javax.swing.JTextField tfdSalaireBrut;
    private javax.swing.JTextField tfdSalaireNet;
    // End of variables declaration//GEN-END:variables
}
