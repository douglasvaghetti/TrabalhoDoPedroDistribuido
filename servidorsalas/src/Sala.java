
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
            if(validaJogadoresAntesDeEnviar()){
                System.out.println("todos jogadores da sala estão ok. Mandando array de jogadores ");
                enviaParaServidor();
            }else{
                System.out.println("Chegou no tamanho final da sala mas haviam "
                        + "jogadores desconectados que foram removidos");
            }
        }else{
            System.out.println("adicionou jogador na sala,"+jogadores.size()+" de "+servidorAlvo.clientesPorSala);
        }
    }
    
    public int getQtdJogadoresSala(){
        return servidorAlvo.clientesPorSala;
    }
    
    private boolean validaJogadoresAntesDeEnviar(){
        Conexao conexao = null;
        boolean valido = true;
        ArrayList<Jogador> filaDaMorte = new ArrayList<>();
        for(Jogador j : jogadores){
            try{
                conexao = new Conexao(j.IP, ServidorSalas.PORTACONECTACLIENTES);
                conexao.envia("ta vivo champs?");
                String recebido = conexao.recebe();
                if(recebido.equals("sim")){
                    conexao.close();
                }
                
            }catch(IOException e){
                System.out.println("o jogador "+j.IP+"não parece estar mais conectado, sala continua na fila");
                filaDaMorte.add(j);
                valido = false;
            }
        }
        
        for(Jogador morto : filaDaMorte){ //nao pode remover dentro do for 
            jogadores.remove(morto);
            System.out.println("jogador "+morto.login+" removido da sala");
        }
        return valido;
    }
    
    private void enviaParaServidor(){
        
        String mensagem = jogadores.get(0).IP;
        for(int x=1;x>jogadores.size();x++){
            mensagem +=";"+jogadores.get(x).IP;
        }
        
        try {
            System.out.println("enviand o pacote de jogadores "+mensagem+" para "+servidorAlvo.IP);
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
