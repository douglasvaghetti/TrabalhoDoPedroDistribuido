
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

//PROTOCOLO DE COMUNICACAO
//CLIENTE: "querojogar:NumeroDePessoasNaSala"
//SERVIDOR: "xxx.xxx.xxx.xxx" que eh o ip, se tiver  ou "naoDeuLesk" caso nao tenha nenhum servidor desse tipo de jogo
public class ThreadGerenciadorDeSalas extends ThreadLimpavel {

    ServerSocket recebedorDeClientes;

    @Override
    public void run() {

        try {
            recebedorDeClientes = new ServerSocket(ServidorSalas.PORTACONECTACLIENTES);
            while (ServidorSalas.ehLider) {
                new ThreadLoginCliente(new ConexaoSegura(recebedorDeClientes.accept())).start();
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