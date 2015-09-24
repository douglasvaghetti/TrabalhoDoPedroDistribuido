
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ThreadRecebeSalasAtualizadas  extends Thread{

    
    @Override
    public void run() {
        try {
            ServerSocket recebedorDeConexoes= new ServerSocket(ServidorSalas.PORTAATUALIZASALASREPLICAS);
            while(true){
                ConexaoObjeto conexao = new ConexaoObjeto(recebedorDeConexoes.accept());
                synchronized(ServidorSalas.mutexSalasEmMontagem){
                    ServidorSalas.salasEmMontagem = (ArrayList<Sala>)conexao.recebeObjeto();
                }
                conexao.close();
            }
        } catch (IOException ex) {
            System.out.println("erro na conexao para sincronizacao das salas");
        } catch (ClassNotFoundException ex) {
            System.out.println("recebeu uma variavel de classe desconhecida do "
                    + "recebeObjeto no recebedor de salas atualizadas");
        }
            
    }
    
}
