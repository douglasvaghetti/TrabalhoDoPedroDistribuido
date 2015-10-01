
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

//PROTOCOLO DE COMUNICACAO
//CLIENTE: "querojogar:NumeroDePessoasNaSala"
//SERVIDOR: "xxx.xxx.xxx.xxx" que eh o ip, se tiver  ou "naoDeuLesk" caso nao tenha nenhum servidor desse tipo de jogo
public class ThreadRecebeConexoesLogin extends ThreadLimpavel {

    ServerSocket recebedorDeClientes;

    @Override
    public void run() {

        try {
            recebedorDeClientes = new ServerSocket(ServidorSalas.PORTALOGINCLIENTES);
            while (ServidorSalas.ehLider) {
                ConexaoSegura conexao = new ConexaoSegura(recebedorDeClientes.accept());
                try {
                    String login = conexao.recebe();
                    String senha = conexao.recebe();

                    System.out.println("recebeu o login " + login + "e a senha " + senha);

                    Jogador autenticado = null;
                    synchronized (ServidorSalas.mutexJogadores) {
                        for (Jogador jogador : ServidorSalas.jogadores) {
                            if (!jogador.estaLogado && jogador.login.equals(login) && jogador.senha.equals(senha)) {
                                jogador.IP = conexao.getIP();
                                jogador.estaLogado = true;
                                autenticado = jogador;
                                new ThreadTimeoutLoginJogador(jogador).start();
                                SincronizacaoReplicas.sincronizaJogadores();
                            }
                        }
                    }
                    if (autenticado == null) {
                        conexao.envia("falhaDeAutenticacao");
                        System.out.println("mandou falha de autenticacao");
                    } else {
                        conexao.envia(autenticado.gold ? "autenticadoGold" : "autenticadoComum");
                        System.out.println("mandou autenticacao bem sucedida");
                    }
                    conexao.close();
                } catch (IOException ex) {
                    System.out.println("redirecionador para jogos fechado");
                }
            }
        } catch (IOException ex) {
            System.out.println("erro estabelecendo conexao com o cliente para login");
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

}
