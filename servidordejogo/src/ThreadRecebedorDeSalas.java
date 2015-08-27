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

                BufferedReader input = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                String dadosSala = input.readLine();
                System.out.println("recebeu dados da sala "+dadosSala);
                
                ThreadSalaDeJogo temp = new ThreadSalaDeJogo(dadosSala);
                ServidorDeJogo.salasDeJogo.add(temp);
                temp.start();
                
                conexao.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadRecebedorDeSalas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

