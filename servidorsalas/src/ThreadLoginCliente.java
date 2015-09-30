
import java.io.IOException;

public class ThreadLoginCliente extends Thread {
    ConexaoSegura conexao;

    public ThreadLoginCliente(ConexaoSegura conexao) {
        this.conexao = conexao;
    }
    
    public void run() {

        try{
            String login = conexao.recebe();
            String senha = conexao.recebe();

            System.out.println("recebeu o login "+login+"e a senha "+senha);

            Jogador autenticado = null;
            synchronized (ServidorSalas.mutexJogadores) {
                for (Jogador jogador : ServidorSalas.jogadores) {
                    if (!jogador.estaLogado && jogador.login.equals(login) && jogador.senha.equals(senha)) {
                        jogador.IP = conexao.getIP();
                        autenticado = jogador;
                    }
                }
            }
            if (autenticado == null) {
                conexao.envia("falhaDeAutenticacao");
                System.out.println("mandou falha de autenticacao");
                conexao.close();
                
            } else {
                conexao.envia(autenticado.gold ? "autenticadoGold" : "autenticadoComum");
                System.out.println("mandou autenticacao bem sucedida");
            }
            if(autenticado!=null){
                int qtdPessoasPorSala = Integer.parseInt(conexao.recebe());
                System.out.println("recebeu mensage de pedido de jogo do jogador" + autenticado.IP);

                System.out.println("quandidade de jogadores por sala que o jogador quer: " + qtdPessoasPorSala);
                if (validaPedidoJogo(qtdPessoasPorSala)) {
                    conexao.envia("oi jogador, vou montar uma sala e assim que possivel te boto em jogo");
                    conexao.close();
                    Sala salaEncontrada = retornaSalaDesseTamanho(qtdPessoasPorSala);
                    if (salaEncontrada == null) {
                        System.out.println("nao encontrou uma sala de tamanho "+qtdPessoasPorSala+" criou uma nova");
                        salaEncontrada = criaSala(qtdPessoasPorSala);
                    }
                    salaEncontrada.adicionaJogador(autenticado);

                    SincronizacaoReplicas.sincronizaSalas();
                } else {
                    conexao.envia("naoDeuLesk");
                }
                conexao.close();
            }
        }catch (IOException ex) {
            System.out.println("redirecionador para jogos fechado");
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
}

