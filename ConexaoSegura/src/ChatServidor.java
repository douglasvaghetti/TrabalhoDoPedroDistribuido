
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class ChatServidor {
    public static void main(String[] args) {
        String msg = JOptionPane.showInputDialog("informe a menssagem");
        
        try {
            
            ServerSocket abre = new ServerSocket(50111);
            
            ConexaoSegura conexao = new ConexaoSegura(abre.accept());
            
            JOptionPane.showMessageDialog(null, conexao.recebe());
            
            conexao.envia(msg);
            
            
        } catch (IOException ex) {
            Logger.getLogger(ChatCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
