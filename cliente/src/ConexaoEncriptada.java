import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public final class ConexaoEncriptada {
    private final Socket socket;
    private BufferedReader input = null;
    private PrintWriter out = null;

    public ConexaoEncriptada(Socket socket) throws IOException {
        this.socket = socket;
    }
    
    public ConexaoEncriptada(String IP,int porta) throws IOException{
        socket = new Socket();
        socket.connect(new InetSocketAddress(IP, porta),1000);
    }
    
    public ConexaoEncriptada(String IP,int porta,int timeout) throws IOException{
        socket = new Socket();
        socket.connect(new InetSocketAddress(IP, porta),timeout);
    }
    
    public void envia(Object mensagem) throws IOException{
        if(out == null){
            out = new PrintWriter(socket.getOutputStream(),true);
        }
        out.println(mensagem.toString());
    }
    
    public String recebe() throws IOException{
        if(input == null){
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        return input.readLine();
    }
    
    public void close() throws IOException{
        socket.close();
    }
    
    public String getIP(){
        return socket.getInetAddress().toString().replace("/", "");
    }
    
}
