
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ThreadRecebeInfoJogoClientes extends ThreadLimpavel{
    ServerSocket recebedorDeConexoes;
    
    @Override
    public void run() {
        try {
            recebedorDeConexoes = new ServerSocket(ServidorSalas.PORTAINFOJOGOCLIENTE);
            while(true){
                ConexaoSegura conexao = new ConexaoSegura(recebedorDeConexoes.accept());
                
                
                String login = conexao.recebe();
                String senha = conexao.recebe();
                int qtdPessoasPorSala = Integer.parseInt(conexao.recebe());
                Jogador autenticado = null;
                for(Jogador j : ServidorSalas.jogadores){
                    if (j.estaLogado && j.login.equals(login) && j.senha.equals(senha)){
                        autenticado = j;
                    }
                }
                if (autenticado != null) {
                    System.out.println("recebeu mensage de pedido de jogo do jogador" + autenticado.IP);

                    System.out.println("quandidade de jogadores por sala que o jogador quer: " + qtdPessoasPorSala);
                    if (validaPedidoJogo(qtdPessoasPorSala)) {
                        conexao.envia("oi jogador, vou montar uma sala e assim que possivel te boto em jogo");
                        conexao.close();
                        Sala salaEncontrada = retornaSalaDesseTamanho(qtdPessoasPorSala);
                        if (salaEncontrada == null) {
                            System.out.println("nao encontrou uma sala de tamanho " + qtdPessoasPorSala + " criou uma nova");
                            salaEncontrada = criaSala(qtdPessoasPorSala);
                        }
                        salaEncontrada.adicionaJogador(autenticado);

                        SincronizacaoReplicas.sincronizaSalas();
                    } else {
                        conexao.envia("naoDeuLesk");
                    }
                    
                }else{
                    System.out.println("cliente "+login+" tentou entrar mandar info de jogo mas nao tinha logado ainda");
                    conexao.envia("Voce nao esta logado. Sua secao pode ter expirado");
                }
                conexao.close();
            }
        } catch (IOException ex) {
            System.out.println("erro nas conexoes do recebedor info jogo clientes");
        }
    }
    
    private boolean validaPedidoJogo(int numeroDeJogadoresPorSala) {
        SincronizacaoServidoresDeJogo.sicronizaDadosServidoresDeJogo();
        SincronizacaoReplicas.sincronizaServidoresDeJogo();
        SincronizacaoServidoresDeJogo.printDadosServidoresDeJogo();
        synchronized (ServidorSalas.mutexServidoresDeJogo) {
            for (ServidorDeJogo s : ServidorSalas.servidoresDeJogo) {
                if (s.clientesPorSala == numeroDeJogadoresPorSala && s.salasOcupadas < s.maxSalas) {
                    return true;
                }
            }
            return false;
        }
    }

    private Sala retornaSalaDesseTamanho(int qtdJogadoresPorSala) {
        System.out.println("procurando  sala de tamanho "+qtdJogadoresPorSala);
        synchronized (ServidorSalas.mutexSalasEmMontagem) {
            
            for (Sala s : ServidorSalas.salasEmMontagem) {
                System.out.println("sala com tamanho "+s.getQtdJogadoresSala()+" com "+s.getJogadoresAtuais());
                if (s.getQtdJogadoresSala() == qtdJogadoresPorSala) {
                    return s;
                }
            }
            return null;
        }
    }

    private Sala criaSala(int qtdPessoasPorSala) {
        ServidorDeJogo melhorServidor = null;
        for (ServidorDeJogo s : ServidorSalas.servidoresDeJogo) {
            if (s.clientesPorSala == qtdPessoasPorSala && (melhorServidor == null || (s.disponibilidade() > melhorServidor.disponibilidade()))) {
                melhorServidor = s;
            }
        }
        Sala novaSala= new Sala(melhorServidor);
        ServidorSalas.salasEmMontagem.add(novaSala);
        System.out.println("criou uma nova sala com numero de jogadores "+novaSala.getQtdJogadoresSala());
        return novaSala;
    }
    
    @Override
    public void limpa() {
        try {
            recebedorDeConexoes.close();
            System.out.println("socket fechado");
        } catch (IOException ex) {
            System.out.println("erro no metodo limpa");
        }
    }
}