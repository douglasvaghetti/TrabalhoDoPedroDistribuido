
import java.io.IOException;
import java.util.ArrayList;

//PROTOCOLO ip1;ip2;ip3;ip4;etc

public class Sala {
    private ArrayList<Jogador> jogadores;
    private ServidorDeJogo servidorAlvo;

    public Sala(ServidorDeJogo servidorAlvo) {
        this.servidorAlvo = servidorAlvo;
        this.jogadores = new ArrayList<>(); 
    }
    
    public void adicionaJogador(Jogador jogador){
        this.jogadores.add(jogador);
        if(jogadores.size()==servidorAlvo.clientesPorSala){
            
            System.out.println("todos jogadores da sala estao ok. Mandando array de jogadores ");
            enviaParaServidor();
            
        }else{
            System.out.println("adicionou jogador na sala,"+jogadores.size()+" de "+servidorAlvo.clientesPorSala);
        }
    }
    
    public int getQtdJogadoresSala(){
        return servidorAlvo.clientesPorSala;
    }
    
    public int getJogadoresAtuais(){
        return jogadores.size();
    }

    
    private void enviaParaServidor(){
        
        String mensagem = jogadores.get(0).IP;
        for(int x=1;x>jogadores.size();x++){
            mensagem +=";"+jogadores.get(x).IP;
        }
        
        try {
            System.out.println("enviando o pacote de jogadores "+mensagem+" para "+servidorAlvo.IP);
            Conexao conexao = new Conexao(servidorAlvo.IP,ServidorSalas.PORTAMANDAPACOTEJOGADORESSALA);
            conexao.envia(mensagem);
            conexao.close();
            synchronized(ServidorSalas.mutexSalasEmMontagem){
                ServidorSalas.salasEmMontagem.remove(this);
            }
        } catch (IOException ex) {
            System.out.println("tentou conectar com servidor para enviar sala "
                    + "completa mas servidor estava indisponivel");
            ex.printStackTrace();
        }
    }
}
