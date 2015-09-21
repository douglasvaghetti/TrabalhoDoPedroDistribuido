public class Login extends javax.swing.JFrame {

    public Login() {
        initComponents();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BotaoCadastro = new javax.swing.JButton();
        TextoUsuario = new javax.swing.JTextField();
        BotaoLogin = new javax.swing.JButton();
        LabelUsuario = new javax.swing.JLabel();
        LabelSenha = new javax.swing.JLabel();
        TextoSenha = new javax.swing.JPasswordField();
        msgErro = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");

        BotaoCadastro.setText("Cadastrar");
        BotaoCadastro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotaoCadastroActionPerformed(evt);
            }
        });

        TextoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextoUsuarioActionPerformed(evt);
            }
        });

        BotaoLogin.setText("Login");
        BotaoLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotaoLoginActionPerformed(evt);
            }
        });

        LabelUsuario.setText("Usuario");

        LabelSenha.setText("Senha");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(msgErro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TextoSenha)
                    .addComponent(TextoUsuario)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BotaoLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(BotaoCadastro))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabelUsuario)
                            .addComponent(LabelSenha))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelUsuario)
                .addGap(12, 12, 12)
                .addComponent(TextoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LabelSenha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextoSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(msgErro, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BotaoCadastro)
                    .addComponent(BotaoLogin))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotaoLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoLoginActionPerformed
        String senha = ClienteMiddleware.toMD5(new String(TextoSenha.getPassword()));
        if(ClienteMiddleware.autentica(TextoUsuario.getText(), null)){
            this.setVisible(false);
            if(ClienteMiddleware.tipoUsuario.equals("comum")){
                new SeletorPartidaComum().setVisible(true);
            }else{
                new SeletorPartidaGold().setVisible(true);
            }
        }else{
            msgErro.setText("Erro ao autenticar!");
        }
    }//GEN-LAST:event_BotaoLoginActionPerformed

    private void TextoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextoUsuarioActionPerformed

    }//GEN-LAST:event_TextoUsuarioActionPerformed

    private void BotaoCadastroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotaoCadastroActionPerformed
        new Cadastro().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_BotaoCadastroActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotaoCadastro;
    private javax.swing.JButton BotaoLogin;
    private javax.swing.JLabel LabelSenha;
    private javax.swing.JLabel LabelUsuario;
    private javax.swing.JPasswordField TextoSenha;
    private javax.swing.JTextField TextoUsuario;
    private javax.swing.JLabel msgErro;
    // End of variables declaration//GEN-END:variables
}
