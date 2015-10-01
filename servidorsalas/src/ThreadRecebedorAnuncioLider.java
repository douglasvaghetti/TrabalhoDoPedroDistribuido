
import java.io.IOException;
import java.net.ServerSocket;


public class ThreadRecebedorAnuncioLider  extends Thread{

    
    @Override
    public void run(){
        
        try {
            ServerSocket recebedor = new ServerSocket(ServidorSalas.PORTAANUNCIALIDER);
            while(true){
                Conexao conexao = new Conexao(recebedor.accept());
                String recebido = conexao.recebe();
                System.out.println("recebeu mensagem "+recebido+" no recebedor de anuncio de lider");
                String novoIPLider = recebido.split(":")[1];
                ServidorSalas.IPLiderAtual = novoIPLider;
                System.out.println(">>>>>>atualizou lider para "+novoIPLider);
                if(ServidorSalas.ehLider){
                    ServidorSalas.inicioThreadsNaoLider();
                    SincronizacaoReplicas.sincronizaJogadores();
                    SincronizacaoReplicas.sincronizaSalas();
                    SincronizacaoReplicas.sincronizaServidoresDeJogo();
                }
                ServidorSalas.ehLider = false;
            }
        } catch (IOException ex) {
            System.out.println(">>>>>>>ISSO NAO DEVE ACONTECER DE JEITO NENHUM");
            ex.printStackTrace();
        }
    }
}
