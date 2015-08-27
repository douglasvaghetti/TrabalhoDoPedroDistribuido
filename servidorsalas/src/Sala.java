
import java.io.IOException;
import java.util.ArrayList;

//PROTOCOLO ip1;ip2;ip3;ip4;etc

public class Sala {
    private ArrayList<String> IPs;
    private ServidorDeJogo servidorAlvo;

    public Sala(ServidorDeJogo servidorAlvo) {
        this.servidorAlvo = servidorAlvo;
        this.IPs = new ArrayList<>(); 
    }
    
    public void adicionaJogador(String ip){
        this.IPs.add(ip);
        if(IPs.size()==servidorAlvo.clientesPorSala){
            if(validaJogadoresAntesDeEnviar()){
                System.out.println("todos jogadores da sala estão ok. Mandando array de jogadores ");
                enviaParaServidor();
            }else{
                System.out.println("Chegou no tamanho final da sala mas haviam "
                        + "jogadores desconectados que foram removidos");
            }
        }else{
            System.out.println("adicionou jogador na sala,"+IPs.size()+" de "+servidorAlvo.clientesPorSala);
        }
    }
    
    public int getQtdJogadoresSala(){
        return servidorAlvo.clientesPorSala;
    }
    
    private boolean validaJogadoresAntesDeEnviar(){
        Conexao conexao = null;
        boolean valido = true;
        ArrayList<String> filaDaMorte = new ArrayList<>();
        for(String ip:IPs){
            try{
                conexao = new Conexao(ip, ServidorSalas.PORTACONECTACLIENTES);
                conexao.envia("ta vivo champs?");
                String recebido = conexao.recebe();
                if(recebido.equals("sim")){
                    conexao.close();
                }
                
            }catch(IOException e){
                System.out.println("o jogador "+ip+"não parece estar mais conectado, sala continua na fila");
                filaDaMorte.add(ip);
                valido = false;
            }
        }
        
        for(String morto:filaDaMorte){ //nao pode remover dentro do for 
            IPs.remove(morto);
            System.out.println("jogador "+morto+" removido da sala");
        }
        return valido;
    }
    
    private void enviaParaServidor(){
        
        String mensagem = IPs.get(0);
        for(int x=1;x>IPs.size();x++){
            mensagem +=";"+IPs.get(x);
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
