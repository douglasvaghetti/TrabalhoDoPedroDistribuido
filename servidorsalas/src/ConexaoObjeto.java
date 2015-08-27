
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConexaoObjeto {

    private Socket socket;
    private ObjectInputStream objInput = null;
    private ObjectOutputStream objOut = null;

    public ConexaoObjeto(Socket socket) throws IOException {
        this.socket = socket;
    }

    public ConexaoObjeto(String IP, int porta) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(IP, porta), 1000);
    }

    public ConexaoObjeto(String IP, int porta, int timeout) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(IP, porta), timeout);
    }
    
    public void close() throws IOException {
        socket.close();
    }

    public String getIP() {
        return socket.getInetAddress().toString().replace("/", "");
    }

    public void enviaObjeto(Object o) throws IOException {
        if (objOut == null) {
            objOut = new ObjectOutputStream(socket.getOutputStream());
        }
        objOut.writeObject(o);
    }

    public Object recebeObjeto() throws IOException, ClassNotFoundException {
        if(objInput == null){
            objInput = new ObjectInputStream(socket.getInputStream());            
        }
        return objInput.readObject();
    }
}
