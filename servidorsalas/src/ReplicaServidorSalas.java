
import java.io.IOException;

class ReplicaServidorSalas {
    public String IP;
    public int forca;

    public ReplicaServidorSalas(String IP, int forca) {
        this.IP = IP;
        this.forca = forca;
    }
    
    public void sincronizaSalas(){
        try {
            ConexaoObjeto conexaoAtualizaSalas = new ConexaoObjeto(IP,ServidorSalas.PORTAATUALIZASALASREPLICAS);
            synchronized(ServidorSalas.mutexSalasEmMontagem){
                conexaoAtualizaSalas.enviaObjeto(ServidorSalas.salasEmMontagem);
            }
            conexaoAtualizaSalas.close();
        } catch (IOException ex) {
            System.out.println("erro na sincronização das salas da replica "+IP);
            ex.printStackTrace();
        }
            
    }
    
    public void sincronizaServidoresDeJogo(){
        try {
            ConexaoObjeto conexaoAtualizaServidores = new ConexaoObjeto(IP,ServidorSalas.PORTAATUALIZASERVIDORESREPLICAS);
            synchronized(ServidorSalas.mutexServidoresDeJogo){
                conexaoAtualizaServidores.enviaObjeto(ServidorSalas.servidoresDeJogo);
            }
            conexaoAtualizaServidores.close();
        } catch (IOException ex) {
            System.out.println("erro na sincronização dos servidores de jogo da replica "+IP);
            ex.printStackTrace();
        }
    }
}
