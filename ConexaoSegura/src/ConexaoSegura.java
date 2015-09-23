
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexaoSegura {

    private Socket socket;
    private ObjectInputStream objInput = null;
    private ObjectOutputStream objOut = null;
    private byte keyCompartilhada[] = null;
    private String keyParticular = "abcdefghijklmnop";
    
    public ConexaoSegura(Socket socket) throws IOException {
        this.socket = socket;
        try {
            this.keyCompartilhada = Criptografia.descriptografa((byte[]) recebeKey(), keyParticular.getBytes());
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro no recebimento da chave");
            Logger.getLogger(ConexaoSegura.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public ConexaoSegura(String IP, int porta) throws IOException {
        try {
            ConexaoObjeto pegaChave = new ConexaoObjeto("IpServidorKey", 50666);
            pegaChave.enviaObjeto(IP);
            keyCompartilhada = Criptografia.descriptografa((byte[]) pegaChave.recebeObjeto(), keyParticular.getBytes());
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

    public void envia(String o) throws IOException {
        if (objOut == null) {
            objOut = new ObjectOutputStream(socket.getOutputStream());
        }
        objOut.writeObject(Criptografia.criptografa(o.getBytes(), keyCompartilhada));
    }

    public void enviaObjeto(Object o) throws IOException {
        if (objOut == null) {
            objOut = new ObjectOutputStream(socket.getOutputStream());
        }
        objOut.writeObject(o);
    }
    private Object recebeKey() throws IOException, ClassNotFoundException {
        if(objInput == null){
            objInput = new ObjectInputStream(socket.getInputStream());            
        }
        return objInput.readObject();
    }
    
    public String recebe() throws IOException, ClassNotFoundException {
        if(objInput == null){
            objInput = new ObjectInputStream(socket.getInputStream());            
        }
        return new String(Criptografia.descriptografa((byte[]) objInput.readObject(), keyCompartilhada));
    }
}
