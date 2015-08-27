
import java.util.Random;

public class ThreadSalaDeJogo extends Thread{
    String listaIps;

    public ThreadSalaDeJogo(String listaIps) {
        this.listaIps = listaIps;
    }

    @Override
    public void run() {
        System.out.println("sou a thread de jogo, eu que vou abrir conexoes "
                + "com esses ips:"+listaIps);
        
        try {
            long tempo = 20000+(new Random().nextInt(20000));
            System.out.println("esse jogo vai durar "+tempo/1000+" segundos");
            sleep(tempo);
        } catch (InterruptedException ex) {
            System.out.println("erro no sleep da simulação do jogo.");
        }
        System.out.println("sala de jogo com os ips "+listaIps+" terminou");
        ServidorDeJogo.salasDeJogo.remove(this);
    }
    
    
}
