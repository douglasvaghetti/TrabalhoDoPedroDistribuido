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
  
    
    ServerSocket recebedorDeConexoes;
    @Override
    public void run(){
        try {
            recebedorDeConexoes = new ServerSocket(PORTARECEBESALA);
            
        
            while(true){
                Socket conexao = recebedorDeConexoes.accept();
                
                ThreadSalaDeJogo temp = new ThreadSalaDeJogo();
                ServidorDeJogo.salasDeJogo.add(temp);
                temp.start();
                
                BufferedReader input = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                String dadosSala = input.readLine();
                for(String ip: dadosSala.split(";")){
                    Socket s = new Socket(ip,50050);  //só conecta e espera pegarem o ip
                    Thread.sleep(100);
                    s.close();
                }
                
                
                
                conexao.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadRecebedorDeSalas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadRecebedorDeSalas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

