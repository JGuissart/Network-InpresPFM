/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applictestbeanbd;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Julien
 */
public class ChoixBdForm extends javax.swing.JDialog
{
    private String Port;
    private String Schema;
    private String Password;
    private String AdresseIp;
    private String SID;
    private int Choix;
    /**
     * Creates new form ChoixBdForm
     */
    public ChoixBdForm(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();
        DefaultComboBoxModel DCBM = new DefaultComboBoxModel () ;
        DCBM.addElement("MySQL");
        DCBM.addElement("Oracle");
        cbxBaseDeDonnees.setModel(DCBM);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfdPort = new javax.swing.JTextField();
        cbxBaseDeDonnees = new javax.swing.JComboBox();
        btnValider = new javax.swing.JButton();
        lblBaseDeDonnees = new javax.swing.JLabel();
        lblSchema = new javax.swing.JLabel();
        tfdSchema = new javax.swing.JTextField();
        lblMotDePasse = new javax.swing.JLabel();
        tfdMotDePasse = new javax.swing.JTextField();
        lblAdresseIp = new javax.swing.JLabel();
        tfdAdresseIp = new javax.swing.JTextField();
        lblPort = new javax.swing.JLabel();
        lblSID = new javax.swing.JLabel();
        tfdSID = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tfdPort.setText("1521");

        btnValider.setText("Valider");
        btnValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValiderActionPerformed(evt);
            }
        });

        lblBaseDeDonnees.setText("Base de donnée : ");

        lblSchema.setText("Schéma : ");

        tfdSchema.setText("BD_TRAFIC");

        lblMotDePasse.setText("Mot de passe : ");

        tfdMotDePasse.setText("azerty123");

        lblAdresseIp.setText("Adresse IP : ");

        tfdAdresseIp.setText("127.0.0.1");

        lblPort.setText("Port : ");

        lblSID.setText("SID :");

        tfdSID.setText("orcl");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfdPort)
                    .addComponent(btnValider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxBaseDeDonnees, 0, 206, Short.MAX_VALUE)
                            .addComponent(lblBaseDeDonnees)
                            .addComponent(lblSchema)
                            .addComponent(tfdSchema)
                            .addComponent(lblMotDePasse)
                            .addComponent(tfdMotDePasse)
                            .addComponent(lblAdresseIp)
                            .addComponent(tfdAdresseIp)
                            .addComponent(lblPort)
                            .addComponent(lblSID)
                            .addComponent(tfdSID))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBaseDeDonnees)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxBaseDeDonnees, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSchema)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfdSchema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMotDePasse)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfdMotDePasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(lblSID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfdSID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAdresseIp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfdAdresseIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPort)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfdPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnValider)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValiderActionPerformed
        if (!tfdAdresseIp.getText().isEmpty() &&
                !tfdPort.getText().isEmpty() &&
                !tfdSchema.getText().isEmpty())
        {
            this.setAdresseIp(tfdAdresseIp.getText());
            this.setPort(tfdPort.getText());
            this.setSchema(tfdSchema.getText());
            this.setPassword(tfdMotDePasse.getText());
            if(cbxBaseDeDonnees.getSelectedIndex() == 1)
                if(!tfdSID.getText().isEmpty())
                    this.setSID(tfdSID.getText());
            this.setChoix(cbxBaseDeDonnees.getSelectedIndex());
            this.dispose();
        }
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
            java.util.logging.Logger.getLogger(ChoixBdForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChoixBdForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChoixBdForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChoixBdForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChoixBdForm dialog = new ChoixBdForm(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnValider;
    private javax.swing.JComboBox cbxBaseDeDonnees;
    private javax.swing.JLabel lblAdresseIp;
    private javax.swing.JLabel lblBaseDeDonnees;
    private javax.swing.JLabel lblMotDePasse;
    private javax.swing.JLabel lblPort;
    private javax.swing.JLabel lblSID;
    private javax.swing.JLabel lblSchema;
    private javax.swing.JTextField tfdAdresseIp;
    private javax.swing.JTextField tfdMotDePasse;
    private javax.swing.JTextField tfdPort;
    private javax.swing.JTextField tfdSID;
    private javax.swing.JTextField tfdSchema;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the Port
     */
    public String getPort() {
        return Port;
    }

    /**
     * @param Port the Port to set
     */
    public void setPort(String Port) {
        this.Port = Port;
    }

    /**
     * @return the Schema
     */
    public String getSchema() {
        return Schema;
    }

    /**
     * @param Schema the Schema to set
     */
    public void setSchema(String Schema) {
        this.Schema = Schema;
    }

    /**
     * @return the Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     * @param Password the Password to set
     */
    public void setPassword(String Password) {
        this.Password = Password;
    }

    /**
     * @return the AdresseIp
     */
    public String getAdresseIp() {
        return AdresseIp;
    }

    /**
     * @param AdresseIp the AdresseIp to set
     */
    public void setAdresseIp(String AdresseIp) {
        this.AdresseIp = AdresseIp;
    }

    /**
     * @return the Choix
     */
    public int getChoix() {
        return Choix;
    }

    /**
     * @param Choix the Choix to set
     */
    public void setChoix(int Choix) {
        this.Choix = Choix;
    }

    /**
     * @return the SID
     */
    public String getSID() {
        return SID;
    }

    /**
     * @param SID the SID to set
     */
    public void setSID(String SID) {
        this.SID = SID;
    }
}