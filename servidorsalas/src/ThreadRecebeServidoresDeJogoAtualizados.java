
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

class ThreadRecebeServidoresDeJogoAtualizados extends Thread{

    @Override
    public void run() {
        try {
            ServerSocket recebedorDeConexoes= new ServerSocket(ServidorSalas.PORTAATUALIZASERVIDORESREPLICAS);
            while(true){
                ConexaoObjeto conexao = new ConexaoObjeto(recebedorDeConexoes.accept());
                synchronized(ServidorSalas.mutexServidoresDeJogo){
                    ServidorSalas.servidoresDeJogo= (ArrayList<ServidorDeJogo>)conexao.recebeObjeto();
                }
                conexao.close();
            }
        } catch (IOException ex) {
            System.out.println("erro na conexao para sincronização dos servidores de jogo");
        } catch (ClassNotFoundException ex) {
            System.out.println("recebeu uma variavel de classe desconhecida do "
                    + "recebeObjeto no recebedor de servidores de jogo");
        }
    }

    
    
}
