
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class ChatCliente {
    public static void main(String[] args) {
        String ip = JOptionPane.showInputDialog("informe o ip");
        String msg = JOptionPane.showInputDialog("informe a menssagem");
        
        try {
            ConexaoSegura conexao = new ConexaoSegura(ip, 50111);
            
            conexao.envia(msg);
            
            JOptionPane.showMessageDialog(null, conexao.recebe());
            
        } catch (IOException ex) {
            Logger.getLogger(ChatCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
