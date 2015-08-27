
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteMiddleware {

    private String[] IPsServidoresSalas;
    private final int PORTASERVIDORSALAS = 50001;
    private final int PORTASERVIDORJOGO = 50003;

    private Socket jogo;

    public void conecta(int nJogadoresSala, String[] ipsServidoresSalas) {
        //conecta no dns
        this.IPsServidoresSalas = ipsServidoresSalas;

        for (String ip : ipsServidoresSalas) {
            String resposta = "naoDeuLesk";
            try {
                System.out.println("Ok, contatando o servidor");
                Socket conexao = new Socket(ip, PORTASERVIDORSALAS);
                //recebe o ip do servidor
                BufferedReader input = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                PrintWriter out = new PrintWriter(conexao.getOutputStream(), true);
                out.println("querojogar:" + nJogadoresSala);
                resposta = input.readLine();
                if (resposta.equals("naoDeuLesk")) {
                    System.out.println("lider disse que não existe nenhum "
                            + "servdor desse tipo de partida");
                    break;
                }
                
                System.out.println("recebeu a resposta do servidor: "+resposta);
                conexao.close();
                //realizou o contato inicial, confirmou que existe um servidor desse tipo
                //aguarda nova conexao para confirmação que está vivo
                conexao = new ServerSocket(PORTASERVIDORSALAS).accept();
                input = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                out = new PrintWriter(conexao.getOutputStream(), true);
                System.out.println("recebeu a mensagem "+input.readLine()+" do "
                        + "servidor de salas para confimação,enviando resposta");
                out.println("sim");
                conexao.close();
                System.out.println("tudo ok, esperando conexão do servidor de jogo");
                //TODO: aguardar a conexão do servidor mesmo
                
            } catch (IOException e) {
                System.out.println("ip "+ip+" nao está conectado ou não é o lider, tentando o proximo");
                continue;
            }
        }
    }
}
