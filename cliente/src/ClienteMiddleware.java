
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ClienteMiddleware {

    private static String[] ipsServidoresSalas;
    private static final int PORTASERVIDORSALAS = 50001;
    private static final int PORTASERVIDORCADASTRO = 50002;
    public static final int PORTAINFOJOGOCLIENTE = 50005;

    public final int PORTASERVIDORJOGO = 50003;
    public static String tipoUsuario = "comum", login, senha;

    private Socket jogo;

    public ClienteMiddleware(String[] ipsServidoresSalas) {
        JFrame janela = new Login();
        janela.setVisible(true);
        ClienteMiddleware.ipsServidoresSalas = ipsServidoresSalas;
    }

    public static boolean autentica(String login, String senha) {

        for (String ip : ipsServidoresSalas) {
            ConexaoSegura conexao;
            try {
                System.out.println("Ok, contatando o servidor " + ip);
                conexao = new ConexaoSegura(ip, PORTASERVIDORSALAS);
            } catch (IOException e) {
                System.out.println("ip " + ip + " nao está conectado ou não é o lider, tentando o proximo");
                continue;
            }

            try {
                conexao.envia(login);
                conexao.envia(senha);
                String resposta = conexao.recebe();
                System.out.println(">>>recebeu " + resposta);
                if (resposta.equals("autenticadoComum")) {
                    System.out.println("autenticou cliente " + login + " como comum");
                    conexao.close();
                    ClienteMiddleware.login = login;
                    ClienteMiddleware.senha = senha;
                    return true;
                } else {
                    if (resposta.equals("autenticadoGold")) {
                        System.out.println("autenticou cliente " + login + " como gold");
                        conexao.close();
                        tipoUsuario = "gold";
                        ClienteMiddleware.login = login;
                        ClienteMiddleware.senha = senha;
                        return true;
                    }
                }
                conexao.close();
            } catch (IOException ex) {
                System.out.println("Erro ao enviar mensagem de autenticacao");
            }

        }
        return false;
    }

    public static boolean cadastrar(String login, String senha, String gold) {
        System.out.println("tentando cadastrar o usuario " + login + " com a senha " + senha);
        for (String ip : ipsServidoresSalas) {
            try {
                System.out.println("Ok, contatando o servidor" + ip);
                ConexaoSegura conexao = new ConexaoSegura(ip, PORTASERVIDORCADASTRO);
                conexao.envia(login);
                conexao.envia(senha);
                conexao.envia(gold);
                String resposta = conexao.recebe();
                if (resposta.equals("cadastroEfetuado")) {
                    return true;
                }
                conexao.close();
            } catch (IOException e) {
                System.out.println("ip " + ip + " nao está conectado ou não é o lider, tentando o proximo");
                //e.printStackTrace();
                continue;
            }
        }
        return false;
    }

    public static String toMD5(String texto) {
        String md5 = "";
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(texto.getBytes(), 0, texto.length());
            md5 = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Nao achou o algoritimo de md5");
        }
        return md5;
    }

    public static void esperaPartida(int numeroJogadores, String personagem, JFrame janelaInutil)  {
        for (String ip : ipsServidoresSalas) {
            try {
                ConexaoSegura conexaosalas = new ConexaoSegura(ip,PORTAINFOJOGOCLIENTE);
                conexaosalas.envia(login);
                conexaosalas.envia(senha);
                
                conexaosalas.envia(numeroJogadores + "");
                String resposta = conexaosalas.recebe();
                System.out.println("Resposta= " + resposta);
                if (resposta.equals("Voce nao esta logado. Sua secao pode ter expirado")){
                    conexaosalas.close();
                    janelaInutil.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Voce nao esta logado. Sua secao pode ter expirado","Erro!",JOptionPane.ERROR_MESSAGE);
                    new Login().setVisible(true);
                }else{
                    if(resposta.equals("naoDeuLesk")){
                        conexaosalas.close();
                        janelaInutil.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Não existe nenhum servidor de salas disponivel deste tamanho","Erro!",JOptionPane.ERROR_MESSAGE);
                        new Login().setVisible(true);
                    }
                }
                

                ServerSocket esperaPartida = new ServerSocket(50050);
                Conexao conexao = new Conexao(esperaPartida.accept());
                String ipDoServidor = conexao.getIP();
                System.out.println("recebeu conexao do servidor de jogo. ip obtido = " + ipDoServidor);
                int porta = Integer.parseInt(conexao.recebe());
                conexao.close();

                String pastaAtual = System.getProperty("user.dir");
                Runtime r = Runtime.getRuntime();
                System.out.println("abriu executavel do jogo");
                System.out.println("love " + pastaAtual + "/c3fighter " + porta + " " + ipDoServidor + " " + personagem);
                Process p = r.exec("love " + pastaAtual + "/c3fighter " + porta + " " + ipDoServidor + " " + personagem);
                janelaInutil.setVisible(false);
                Thread.sleep(1000);
                System.exit(0);
            } catch (IOException ex) {
                Logger.getLogger(ClienteMiddleware.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClienteMiddleware.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
