import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


//PROTOCOLO: qtdJogadoresPorPartida;qtdSalasSuportadas 
public class ServidorDeJogo {
    public static int PORTASERVIDORSALAS = 50000;
    public static int PORTAATUALIZACARGA = 50021;
    public static String IPSERVIDORSALAS;
    public static ArrayList<ThreadSalaDeJogo> salasDeJogo ;
    
    public static void main(String[] args) throws IOException{
        int qtdJogadoresPorPartida;
        int qtdSalas;
        
        salasDeJogo = new ArrayList<>();
        new ThreadRespondeCargaAtual().start();
        if(args.length==3){
            IPSERVIDORSALAS = args[0];
            qtdJogadoresPorPartida = Integer.parseInt(args[1]);
            
            qtdSalas = Integer.parseInt(args[2]);
            //qtdSalas = 10;
            Socket conexaoInformaDados = new Socket(IPSERVIDORSALAS,PORTASERVIDORSALAS);
            PrintWriter out = new PrintWriter(conexaoInformaDados.getOutputStream(),true);
            out.println(qtdJogadoresPorPartida+";"+qtdSalas);
            System.out.println("ok, enviou informação desse servidor ao "
                    + "servidor de salas");
            new ThreadRecebedorDeSalas().start();
            System.out.println("tudo ok, aguardando salas de jogo");
            
        }else{
            System.out.println("MODO DE USO: IPDOSERVIDORDESALAS QTDJOGADORESPORPARTIDA QTDSALASSUPORTADAS");
        }
    }
}