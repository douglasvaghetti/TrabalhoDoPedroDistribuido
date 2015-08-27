import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;


//PROTOCOLO DE REGISTRO DE RECURSO
//quantidadeDeJogadoresPorSala;qtdSalasTotal
public class ThreadRegistradorRecursos extends Thread {
    ServerSocket recebedorDeConexoes;
    
    @Override
    public void run(){
        
        try {
            recebedorDeConexoes = new ServerSocket(ServidorSalas.PORTAREGISTRARECURSOS);
            while(true){
                Conexao novaConexao = new Conexao(recebedorDeConexoes.accept());
                String recebido = novaConexao.recebe();
                System.out.println("recebeu mensagem de registro do servidor "+recebido);
                
                String IPDoServidorDeJogo= novaConexao.getIP();
                
                int qtdJogadoresPorSala = Integer.parseInt(recebido.split(";")[0]);
                int qtdMaximaDeSalas = Integer.parseInt(recebido.split(";")[1]);
                System.out.println("registrou servidor de jogo no ip "+IPDoServidorDeJogo+" com "+qtdJogadoresPorSala+" jogadores por"
                        + "sala, com capacidade para "+qtdMaximaDeSalas+" salas");
                
                synchronized(ServidorSalas.mutexServidoresDeJogo){
                    ServidorSalas.servidoresDeJogo.add(new ServidorDeJogo(qtdJogadoresPorSala, qtdMaximaDeSalas, IPDoServidorDeJogo));
                }
                novaConexao.close();
                
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadRegistradorRecursos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
