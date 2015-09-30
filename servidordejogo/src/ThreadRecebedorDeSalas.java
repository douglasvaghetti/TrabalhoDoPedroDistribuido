import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadRecebedorDeSalas extends Thread{
    public static final int PORTACONECTACLIENTE = 50003;
    public static final int PORTARECEBESALA = 50020;
    private int porta = 50100;
  
    
    ServerSocket recebedorDeConexoes;
    @Override
    public void run(){
        try {
            recebedorDeConexoes = new ServerSocket(PORTARECEBESALA);
            
            while(true){
                Conexao conexao = new Conexao(recebedorDeConexoes.accept());
                String dadosSala = conexao.recebe();
                conexao.close();
                
                ThreadSalaDeJogo temp = new ThreadSalaDeJogo(porta);
                porta ++;
                ServidorDeJogo.salasDeJogo.add(temp);
                temp.start();
                Thread.sleep(2000);
                //da um tempo para o servidor abrir
                
                for(String ip: dadosSala.split(";")){
                    System.out.println("iniciand jogo no cliente "+ip);
                    Conexao s = new Conexao(ip,50050);  //só conecta e espera pegarem o ip
                    s.envia(porta);
                    s.close();
                    Thread.sleep(1000);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadRecebedorDeSalas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadRecebedorDeSalas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

