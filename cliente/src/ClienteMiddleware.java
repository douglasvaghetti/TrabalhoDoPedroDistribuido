
import java.io.IOException;
import java.math.BigInteger;
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
    public final int PORTASERVIDORJOGO = 50003;
    public static String tipoUsuario = "comum";

    private Socket jogo;

    public ClienteMiddleware() {
        JFrame janela = new Login();
        janela.setVisible(true);
    }

    public static boolean autentica(String login, String senha) {

        for (String ip : ipsServidoresSalas) {
            Conexao conexao;
            try {
                System.out.println("Ok, contatando o servidor");
                conexao = new Conexao(ip, PORTASERVIDORSALAS);
            } catch (IOException e) {
                System.out.println("ip " + ip + " nao está conectado ou não é o lider, tentando o proximo");
                continue;
            }

            try {
                conexao.envia(login);
                conexao.envia(senha);
                String resposta = conexao.recebe();
                if (resposta.equals("autenticadoComum")) {
                    return true;
                } else {
                    if (resposta.equals("autenticadoGold")) {
                        tipoUsuario = "gold";
                        return true;
                    }
                }
            } catch (IOException ex) {
                System.out.println("Erro ao enviar mensagem de autenticacao");
            }

        }
        return false;
    }

    public static boolean cadastrar(String login, String senha) {

        for (String ip : ipsServidoresSalas) {
            Conexao conexao;
            try {
                System.out.println("Ok, contatando o servidor");
                conexao = new Conexao(ip, PORTASERVIDORCADASTRO);
                conexao.envia(login);
                conexao.envia(senha);
                String resposta = conexao.recebe();
                if (resposta.equals("cadastroEfetuado")) {
                    return true;
                }
            } catch (IOException e) {
                System.out.println("ip " + ip + " nao está conectado ou não é o lider, tentando o proximo");
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
}
