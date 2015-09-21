
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ThreadRecebeJogadoresAtualizados  extends Thread{

    
    @Override
    public void run() {
        try {
            ServerSocket recebedorDeConexoes= new ServerSocket(ServidorSalas.PORTAATUALIZAJOGADORES);
            while(true){
                ConexaoObjeto conexao = new ConexaoObjeto(recebedorDeConexoes.accept());
                synchronized(ServidorSalas.mutexJogadores){
                    ServidorSalas.jogadores = (ArrayList<Jogador>)conexao.recebeObjeto();
                }
                conexao.close();
            }
        } catch (IOException ex) {
            System.out.println("erro na conexao para sincronização das salas");
        } catch (ClassNotFoundException ex) {
            System.out.println("recebeu uma variavel de classe desconhecida do "
                    + "recebeObjeto no recebedor de salas atualizadas");
        }
            
    }
    
}
