
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Cadastro extends javax.swing.JFrame {

    public Cadastro() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPasswordField1 = new javax.swing.JPasswordField();
        labelLogin = new javax.swing.JLabel();
        textoLogin = new javax.swing.JTextField();
        labelSenha = new javax.swing.JLabel();
        labelRepeteSenha = new javax.swing.JLabel();
        botaoCadastrar = new javax.swing.JButton();
        gold = new javax.swing.JCheckBox();
        senha = new javax.swing.JPasswordField();
        repeteSenha = new javax.swing.JPasswordField();

        jPasswordField1.setText("jPasswordField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cadastro");

        labelLogin.setText("Login");

        textoLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textoLoginActionPerformed(evt);
            }
        });

        labelSenha.setText("Senha");

        labelRepeteSenha.setText("Repita a senha");

        botaoCadastrar.setText("Cadastrar");
        botaoCadastrar.setEnabled(false);
        botaoCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoCadastrarActionPerformed(evt);
            }
        });

        gold.setText("Conta Gold");
        gold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goldActionPerformed(evt);
            }
        });

        senha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                senhaActionPerformed(evt);
            }
        });

        repeteSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeteSenhaActionPerformed(evt);
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
                        .addComponent(gold)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botaoCadastrar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textoLogin)
                            .addComponent(labelLogin)
                            .addComponent(labelSenha)
                            .addComponent(labelRepeteSenha)
                            .addComponent(senha, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(repeteSenha))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textoLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelSenha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(senha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelRepeteSenha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(repeteSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botaoCadastrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gold, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoCadastrarActionPerformed
        if(Arrays.equals(repeteSenha.getPassword(), senha.getPassword())){
            if(ClienteMiddleware.cadastrar(textoLogin.getText(), ClienteMiddleware.toMD5(new String(senha.getPassword())), (gold.isSelected() ? "gold": ""))){
                this.setVisible(false);
                new Login().setVisible(true);
                JOptionPane.showMessageDialog(null, "Cadastro efetuado, agora vamos ao jogo!");
            }else{
                JOptionPane.showMessageDialog(null, "Este login já está em uso, tente outro!");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Senhas não conferem!");
        }
    }//GEN-LAST:event_botaoCadastrarActionPerformed

    private void goldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_goldActionPerformed

    private void textoLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textoLoginActionPerformed
        if(!"".equals(textoLogin.getText()) && senha.getPassword().length>0 && repeteSenha.getPassword().length>0){
            botaoCadastrar.setEnabled(true);
        }else{
            botaoCadastrar.setEnabled(false);
        }
    }//GEN-LAST:event_textoLoginActionPerformed

    private void senhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_senhaActionPerformed
        if(!"".equals(textoLogin.getText()) && senha.getPassword().length>0 && repeteSenha.getPassword().length>0){
            botaoCadastrar.setEnabled(true);
        }else{
            botaoCadastrar.setEnabled(false);
        }
    }//GEN-LAST:event_senhaActionPerformed

    private void repeteSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeteSenhaActionPerformed
        if(!"".equals(textoLogin.getText()) && senha.getPassword().length>0 && repeteSenha.getPassword().length>0){
            botaoCadastrar.setEnabled(true);
        }else{
            botaoCadastrar.setEnabled(false);
        }
    }//GEN-LAST:event_repeteSenhaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoCadastrar;
    private javax.swing.JCheckBox gold;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JLabel labelLogin;
    private javax.swing.JLabel labelRepeteSenha;
    private javax.swing.JLabel labelSenha;
    private javax.swing.JPasswordField repeteSenha;
    private javax.swing.JPasswordField senha;
    private javax.swing.JTextField textoLogin;
    // End of variables declaration//GEN-END:variables
}
