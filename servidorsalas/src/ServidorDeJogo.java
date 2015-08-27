
import java.io.IOException;
import java.io.Serializable;

public class ServidorDeJogo implements Serializable{
    public int clientesPorSala,salasOcupadas,maxSalas;
    public String IP;

    public ServidorDeJogo(int clientesPorSala, int maxSalas, String IP) {
        this.clientesPorSala = clientesPorSala;
        this.salasOcupadas = 0;
        this.maxSalas = maxSalas;
        this.IP = IP;
    }
    
    public void sincroniza(){
        try {
            Conexao conexao = new Conexao(IP, ServidorSalas.PORTAATUALIZACARGASERVIDORES);
            salasOcupadas = Integer.parseInt(conexao.recebe());
            conexao.close();

        } catch (IOException ex) {
            System.out.println("erro na conexao para atualizar dados do servidor "+IP);
            //TRATAMENTO DE EXCESSÃO NECESSARIO AQUI
            ex.printStackTrace();
        }
    }
    
    public int disponibilidade(){
        return maxSalas-salasOcupadas;
    }
}
