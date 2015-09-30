
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadSalaDeJogo extends Thread{

    private int porta;

    public ThreadSalaDeJogo(int porta) {
        this.porta = porta;
    }
        
    @Override
    public void run() {
        String pastaAtual = System.getProperty("user.dir");
        Runtime r = Runtime.getRuntime();
        try {
            System.out.println("love "+pastaAtual+"/c3fighter/servidor "+porta+" "+ServidorDeJogo.qtdJogadoresPorPartida);
            Process p = r.exec("love "+pastaAtual+"/c3fighter/servidor "+porta+" "+ServidorDeJogo.qtdJogadoresPorPartida);
        } catch (IOException ex) {
            Logger.getLogger(ThreadSalaDeJogo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("sala de jogo terminou");
        ServidorDeJogo.salasDeJogo.remove(this);
    }
    
    
}
