import java.io.IOException;
import java.net.ServerSocket;

//PROTOCOLO DE COMUNICACAO
//CLIENTE: "querojogar:NumeroDePessoasNaSala"
//SERVIDOR: "xxx.xxx.xxx.xxx" que é o ip, se tiver  ou "naoDeuLesk" caso nao tenha nenhum servidor desse tipo de jogo

public class ThreadGerenciadorDeSalas extends ThreadLimpavel{
    ServerSocket recebedorDeClientes;
    @Override
    public void run(){
        System.out.println("abrindo thread redirecionador de clientes para servidores de jogo");
        try {
            recebedorDeClientes= new ServerSocket(ServidorSalas.PORTACONECTACLIENTES);
            while(ServidorSalas.ehLider){
                Conexao novaConexao= new Conexao(recebedorDeClientes.accept());
                
                String recebido = novaConexao.recebe();
                String ipJogador = novaConexao.getIP();
                System.out.println("recebeu mensage de pedido de jogo do jogador"+ipJogador+" : "+recebido);
                int qtdPessoasPorSala = Integer.parseInt(recebido.split(":")[1]);
                System.out.println("quandidade de jogadores por sala que o jogador quer: "+ qtdPessoasPorSala);
                if(validaPedidoJogo(qtdPessoasPorSala)){
                    novaConexao.envia("oi jogador, vou montar uma sala e assim que possivel te boto em jogo");
                    novaConexao.close();
                    Sala salaEncontrada = retornaSalaDesseTamanho(qtdPessoasPorSala);
                    if(salaEncontrada==null){
                        salaEncontrada = criaSala(qtdPessoasPorSala);
                    }
                    salaEncontrada.adicionaJogador(ipJogador);
                    SincronizacaoReplicas.sincronizaSalas();
                }else{
                     novaConexao.envia("naoDeuLesk");
                }
            }
        } catch (IOException ex) {
            System.out.println("redirecionador para jogos fechado");
        }
    }
    
    @Override
    public void limpa() {
        try {
            recebedorDeClientes.close();
            System.out.println("socket fechado");
        } catch (IOException ex) {
            System.out.println("erro no metodo limpa");
        }
    }
    
    private boolean validaPedidoJogo(int numeroDeJogadoresPorSala){
        SincronizacaoServidoresDeJogo.sicronizaDadosServidoresDeJogo();
        SincronizacaoReplicas.sincronizaServidoresDeJogo();
        SincronizacaoServidoresDeJogo.printDadosServidoresDeJogo();
        synchronized(ServidorSalas.mutexServidoresDeJogo){    
            for(ServidorDeJogo s:ServidorSalas.servidoresDeJogo){
                if(s.clientesPorSala==numeroDeJogadoresPorSala && s.salasOcupadas<s.maxSalas)
                    return true;
            }
            return false;
           
        }
    }
    
    private Sala retornaSalaDesseTamanho(int qtdJogadoresPorSala){
        synchronized(ServidorSalas.mutexSalasEmMontagem){
            for(Sala s: ServidorSalas.salasEmMontagem){
                if(s.getQtdJogadoresSala()==qtdJogadoresPorSala){
                    return s;
                }
            }
            return null;
        }
    }

    private Sala criaSala(int qtdPessoasPorSala) {
        ServidorDeJogo melhorServidor = null;
        for(ServidorDeJogo s: ServidorSalas.servidoresDeJogo){
            if(s.clientesPorSala ==qtdPessoasPorSala && (melhorServidor==null || (s.disponibilidade()>melhorServidor.disponibilidade()))){
                melhorServidor = s;
            }
        }
        return new Sala(melhorServidor);
    }
    
        
}
