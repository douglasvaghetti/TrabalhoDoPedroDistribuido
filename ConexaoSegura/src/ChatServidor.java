
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class ChatServidor {
    public static void main(String[] args) {
        try {
            
            ServerSocket abre = new ServerSocket(50111);
            System.out.println("briu Socket");
            ConexaoSegura conexao = new ConexaoSegura(abre.accept());
            System.out.println("ceitou Conexao");
            JOptionPane.showMessageDialog(null, conexao.recebe());
            
            String msg = JOptionPane.showInputDialog("informe a menssagem");
            System.out.println("Mandando mensagem "+msg);
            conexao.envia(msg);
            System.out.println("envio menssagem");
            
        } catch (IOException ex) {
            Logger.getLogger(ChatCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
