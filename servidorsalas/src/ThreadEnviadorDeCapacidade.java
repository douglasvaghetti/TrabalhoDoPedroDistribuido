
import java.io.IOException;
import java.net.ServerSocket;

public class ThreadEnviadorDeCapacidade extends Thread{
    
    @Override
    public void run(){
        
        ServerSocket recebedorConexoes;
        try {
            recebedorConexoes = new ServerSocket(ServidorSalas.PORTATROCAINFOPESO);
        } catch (IOException ex) {
            return;
        }
        while(true){
            try {
                Conexao novaConexao = new Conexao(recebedorConexoes.accept());
                System.out.println("abriu nova conexao com "+novaConexao.getIP());
                novaConexao.envia(ServidorSalas.forca);
                System.out.println("enviou o peso "+ServidorSalas.forca);
            } catch (IOException ex) {
                System.out.println("voce pode me obrigar a usar  try catch mas não a fase algo nele.");
            }
        }
    }
}
