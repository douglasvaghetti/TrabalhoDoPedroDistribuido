
import java.io.IOException;
import java.net.ServerSocket;

//PROTOCOLO DE COMUNICACAO
//CLIENTE: "querojogar:NumeroDePessoasNaSala"
//SERVIDOR: "xxx.xxx.xxx.xxx" que eh o ip, se tiver  ou "naoDeuLesk" caso nao tenha nenhum servidor desse tipo de jogo
public class ThreadGerenciadorDeSalas extends ThreadLimpavel {

    ServerSocket recebedorDeClientes;

    @Override
    public void run() {
        System.out.println("abrindo thread redirecionador de clientes para servidores de jogo");
        try {
            recebedorDeClientes = new ServerSocket(ServidorSalas.PORTACONECTACLIENTES);
            while (ServidorSalas.ehLider) {
                Conexao novaConexao = new Conexao(recebedorDeClientes.accept());

                String login = novaConexao.recebe();
                String senha = novaConexao.recebe();
                
                System.out.println("recebeu o login "+login+"e a senha "+senha);

                Jogador autenticado = null;
                synchronized (ServidorSalas.mutexJogadores) {
                    for (Jogador jogador : ServidorSalas.jogadores) {
                        if (!jogador.estaLogado && jogador.login.equals(login) && jogador.senha.equals(senha)) {
                            jogador.IP = novaConexao.getIP();
                            autenticado = jogador;
                        }
                    }
                }
                if (autenticado == null) {
                    novaConexao.envia("falhaDeAutenticacao");
                    System.out.println("mandou falha de autenticacao");
                    novaConexao.close();
                    continue;
                } else {
                    novaConexao.envia(autenticado.gold ? "autenticadoGold" : "autenticadoComum");
                    System.out.println("mandou autenticacao bem sucedida");
                }
                //falta confirma a entrada do jogador na sala.
                int qtdPessoasPorSala = Integer.parseInt(novaConexao.recebe());
                System.out.println("recebeu mensage de pedido de jogo do jogador" + autenticado.IP);

                System.out.println("quandidade de jogadores por sala que o jogador quer: " + qtdPessoasPorSala);
                if (validaPedidoJogo(qtdPessoasPorSala)) {
                    novaConexao.envia("oi jogador, vou montar uma sala e assim que possivel te boto em jogo");
                    novaConexao.close();
                    Sala salaEncontrada = retornaSalaDesseTamanho(qtdPessoasPorSala);
                    if (salaEncontrada == null) {
                        System.out.println("nao encontrou uma sala de tamanho "+qtdPessoasPorSala+" criou uma nova");
                        salaEncontrada = criaSala(qtdPessoasPorSala);
                    }
                    salaEncontrada.adicionaJogador(autenticado);
                    SincronizacaoReplicas.sincronizaSalas();
                } else {
                    novaConexao.envia("naoDeuLesk");
                }
                novaConexao.close();
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
        synchronized (ServidorSalas.mutexSalasEmMontagem) {
            for (Sala s : ServidorSalas.salasEmMontagem) {
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
        return new Sala(melhorServidor);
    }

}
