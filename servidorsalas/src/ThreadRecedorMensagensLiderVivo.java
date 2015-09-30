
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ThreadRecedorMensagensLiderVivo extends ThreadLimpavel{
    DatagramSocket socket = null;
    @Override
    public void run() {
        System.out.println("abrindo thread recebedor mensagens lider vivo");
        byte[] buffer = new byte[1024];
        try {
            socket = new DatagramSocket(ServidorSalas.PORTAAVISOSVIVO);
            socket.setSoTimeout(5000);
        } catch (SocketException ex) {
            System.out.println("erro criando socket para receber mensagens de lider vivo");
            ex.printStackTrace();
            System.exit(0);
            
        }
        
        while(!ServidorSalas.ehLider){
            try {
                
                DatagramPacket recebido = new DatagramPacket(buffer, 1024);
                socket.receive(recebido);
                
            } catch (SocketTimeoutException ex) {
                System.out.println("ERRO, NAO RECEBEU HEARTBEAT DO LIDER");
                ServidorSalas.ehLider = ServidorSalas.verificaSeEhLider();
                if(ServidorSalas.ehLider){
                   ServidorSalas.iniciaThreadsLider();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("socket recebedor de mensagem lider vivo fechado com sucesso");
            }
        }
        socket.close();
    }

    @Override
    public void limpa() {
        System.out.println("limpu socket recebedor mensagens lider vivo");
        socket.close();
    }   
}
