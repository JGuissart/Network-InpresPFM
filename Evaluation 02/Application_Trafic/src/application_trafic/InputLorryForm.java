/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application_trafic;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Julien
 */
public class InputLorryForm extends javax.swing.JDialog
{
    private DefaultListModel dlm;
    private String Request;
    /**
     * Creates new form InputLorryForm
     */
    public InputLorryForm(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        dlm = new DefaultListModel();
        lstIdContainer.setModel(dlm);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNumReservation = new javax.swing.JLabel();
        tfdNumReservation = new javax.swing.JTextField();
        lblIdContainer = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstIdContainer = new javax.swing.JList();
        tfdIdContainer = new javax.swing.JTextField();
        btnAjouterListe = new javax.swing.JButton();
        btnSupprimerListe = new javax.swing.JButton();
        btnValider = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblNumReservation.setText("Numéro de réservation : ");

        lblIdContainer.setText("Identifiant du container : ");

        jScrollPane1.setViewportView(lstIdContainer);

        btnAjouterListe.setText("Ajouter l'identifiant du container à la liste");
        btnAjouterListe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAjouterListeActionPerformed(evt);
            }
        });

        btnSupprimerListe.setText("Retirer l'identifiant sélectionné dans la liste");
        btnSupprimerListe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupprimerListeActionPerformed(evt);
            }
        });

        btnValider.setText("Valider");
        btnValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValiderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnValider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfdNumReservation)
                    .addComponent(tfdIdContainer)
                    .addComponent(btnAjouterListe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnSupprimerListe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNumReservation)
                            .addComponent(lblIdContainer))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNumReservation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfdNumReservation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIdContainer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfdIdContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAjouterListe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSupprimerListe)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnValider)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAjouterListeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjouterListeActionPerformed
        if(!tfdIdContainer.getText().isEmpty())
            dlm.addElement(tfdIdContainer.getText());
        else
            JOptionPane.showMessageDialog(this, "Vous devez entrez un identifiant de container pour l'ajouter à la liste.", "Identifiant de container inexistant", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnAjouterListeActionPerformed

    private void btnSupprimerListeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupprimerListeActionPerformed
        if(lstIdContainer.getSelectedIndex() >= 0)
            dlm.remove(lstIdContainer.getSelectedIndex());
        else
            JOptionPane.showMessageDialog(this, "Vous devez sélectionner un identifiant de container dans la liste.", "Identifiant de container non-sélectionné", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnSupprimerListeActionPerformed

    private void btnValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValiderActionPerformed
        if(!tfdNumReservation.getText().isEmpty())
        {
            Request = tfdNumReservation.getText();
            for(int i = 0; i < dlm.size(); i++)
                Request += "#" + (String)dlm.getElementAt(i);

            this.dispose();
        }
        else
            JOptionPane.showMessageDialog(this, "Le numéro de réservation est obligatoire.", "Numéro de réservation inexistant", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnValiderActionPerformed

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
            java.util.logging.Logger.getLogger(InputLorryForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InputLorryForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InputLorryForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InputLorryForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InputLorryForm dialog = new InputLorryForm(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAjouterListe;
    private javax.swing.JButton btnSupprimerListe;
    private javax.swing.JButton btnValider;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblIdContainer;
    private javax.swing.JLabel lblNumReservation;
    private javax.swing.JList lstIdContainer;
    private javax.swing.JTextField tfdIdContainer;
    private javax.swing.JTextField tfdNumReservation;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the Request
     */
    public String getRequest() {
        return Request;
    }
}
