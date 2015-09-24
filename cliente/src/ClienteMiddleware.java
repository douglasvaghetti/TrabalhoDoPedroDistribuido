
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

    private static String[] ipsServidoresSalas = {"192.168.1.20","192.168.1.16"};
    private static final int PORTASERVIDORSALAS = 50001;
    private static final int PORTASERVIDORCADASTRO = 50002;
    public final int PORTASERVIDORJOGO = 50003;
    public static String tipoUsuario = "comum";
    private static Conexao conectado = null;

    private Socket jogo;

    public ClienteMiddleware() {
        JFrame janela = new Login();
        janela.setVisible(true);
    }

    public static boolean autentica(String login, String senha) {

        for (String ip : ipsServidoresSalas) {
            Conexao conexao;
            try {
                System.out.println("Ok, contatando o servidor "+ip);
                conexao = new Conexao(ip, PORTASERVIDORSALAS);
            } catch (IOException e) {
                System.out.println("ip " + ip + " nao está conectado ou não é o lider, tentando o proximo");
                e.printStackTrace();
                continue;
            }

            try {
                conexao.envia(login);
                conexao.envia(senha);
                String resposta = conexao.recebe();
                System.out.println(">>>recebeu "+resposta);
                if (resposta.equals("autenticadoComum")) {
                    System.out.println("autenticou cliente "+login+" como comum");
                    conectado = conexao;
                    return true;
                } else {
                    if (resposta.equals("autenticadoGold")) {
                        System.out.println("autenticou cliente "+login+" como gold");
                        conectado = conexao;
                        tipoUsuario = "gold";
                        return true;
                    }
                }
                conexao.close();
            } catch (IOException ex) {
                System.out.println("Erro ao enviar mensagem de autenticacao");
            }

        }
        conectado = null;
        return false;
    }

    public static boolean cadastrar(String login, String senha, String gold) {
        for (String ip : ipsServidoresSalas) {
            try {
                System.out.println("Ok, contatando o servidor");
                Conexao conexao = new Conexao(ip, PORTASERVIDORCADASTRO);
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
    
    public static void esperaPartida(int numeroJogadores, String personagem){
        if(conectado != null){
            try {
                conectado.envia(numeroJogadores);
                
                //resposta da ThreadGerenciadorDeSalas
                System.out.println("Resposta= "+conectado.recebe());
                conectado.close();
                
                ServerSocket esperaPartida = new ServerSocket(50050);
                Conexao conexao = new Conexao(esperaPartida.accept());
                String ipDoServidor = conexao.getIP();
                conexao.close();
                
                String pastaAtual = System.getProperty("user.dir");
                Runtime r = Runtime.getRuntime();
                Process p = r.exec("love "+pastaAtual+"/c3fighter "+ipDoServidor+" "+personagem);
                
                //Aqui conecta no servidor de jogo (chama o lua);
            } catch (IOException ex) {
                Logger.getLogger(ClienteMiddleware.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Você não deve chegar aqui sem estar logado seu malandrinho!");
        }
    }
    
}
