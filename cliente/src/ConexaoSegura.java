
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConexaoSegura {

    private Socket socket;
    private ObjectInputStream objInput = null;
    private ObjectOutputStream objOut = null;
    private byte keyCompartilhada[] = null;
    private String keyDistribuidorChaves="chavechavechavec"; ;
    
    public ConexaoSegura(Socket socket) throws IOException {
        
        this.socket = socket;
        try {
            this.keyCompartilhada = Criptografia.descriptografa(recebeKey(), keyDistribuidorChaves.getBytes());
            System.out.println("Recebeu chave compartilhada com "+getIP()+"= "+Arrays.toString(keyCompartilhada));
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro no recebimento da chave");
            Logger.getLogger(ConexaoSegura.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public ConexaoSegura(String IP, int porta) throws IOException {
        
        try {
            ConexaoObjeto pegaChave = new ConexaoObjeto("172.16.50.105", 50666);
            pegaChave.enviaObjeto(IP);
            keyCompartilhada = Criptografia.descriptografa((byte[]) pegaChave.recebeObjeto(), keyDistribuidorChaves.getBytes());
            socket = new Socket();
            socket.connect(new InetSocketAddress(IP, porta), 1000);
            enviaObjeto(pegaChave.recebeObjeto());
            pegaChave.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexaoSegura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close() throws IOException {
        socket.close();
    }

    public String getIP() {
        return socket.getInetAddress().toString().replace("/", "");
    }

    public void envia(String o)  {
        if (objOut == null) {
            try {
                objOut = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(ConexaoSegura.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            objOut.writeObject(Criptografia.criptografa(o.getBytes(), keyCompartilhada));
        } catch (IOException ex) {
            Logger.getLogger(ConexaoSegura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviaObjeto(Object o) throws IOException {
        if (objOut == null) {
            objOut = new ObjectOutputStream(socket.getOutputStream());
        }
        objOut.writeObject(o);
    }
    private byte[] recebeKey() throws IOException, ClassNotFoundException {
        if(objInput == null){
            objInput = new ObjectInputStream(socket.getInputStream());            
        }
        return (byte[]) objInput.readObject();
    }
    
    public String recebe(){
        if(objInput == null){
            
            try {            
                objInput = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ConexaoSegura.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            return new String(Criptografia.descriptografa((byte[]) objInput.readObject(), keyCompartilhada));
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ConexaoSegura.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}