import java.io.IOException;
import java.net.ServerSocket;

public class ThreadGerenciadorDeCadastro extends ThreadLimpavel{
    ServerSocket recebedorDeClientes;
    @Override
    public void run(){
        System.out.println("abrindo thread de cadastro de jogadores");
        try {
            recebedorDeClientes= new ServerSocket(ServidorSalas.PORTAGERENCIADORDECADASTRO);
            while(ServidorSalas.ehLider){
               Conexao novaConexao = new Conexao(recebedorDeClientes.accept());
                
               String login = novaConexao.recebe();
               String senha = novaConexao.recebe();
               String gold = novaConexao.recebe();
               
               boolean jaExiste = false;
               
                for (Jogador jogador : ServidorSalas.jogadores) {
                    if(jogador.login.equals(login)){
                        jaExiste = true;
                    }
                }
                if(jaExiste){
                    novaConexao.envia("cadastroNaoEfetuado");
                    novaConexao.close();
                    continue;
                }else{
                    Jogador novo = new Jogador(login, senha);
                    if(gold.equals("gold")){
                            novo.gold = true;
                        }
                    synchronized(ServidorSalas.mutexJogadores){
                        ServidorSalas.jogadores.add(novo);
                        System.out.println("cadastrou o jogador "+novo.login+"com o ip "+novo.IP);
                        SincronizacaoReplicas.sincronizaJogadores();
                        
                        novaConexao.envia("cadastroEfetuado");
                    }
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
}
