
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aluno
 */
public class ThreadEnviadorMensagemLiderVivo extends ThreadLimpavel {

    @Override
    public void run() {
        System.out.println("abrindo thresad enviador de mensagem lider vivo");
        byte[] mensagemVivo = "estouvivo".getBytes();
        try{
            while(ServidorSalas.ehLider){
                for(ReplicaServidorSalas r: ServidorSalas.vizinhos){
                    DatagramSocket conexao = new DatagramSocket();
                    DatagramPacket mensagem;
                    InetAddress endereco = InetAddress.getByName(r.IP);
                    mensagem = new DatagramPacket(mensagemVivo,mensagemVivo.length,endereco,ServidorSalas.PORTAAVISOSVIVO);
                    conexao.send(mensagem);
                    //System.out.println("mandando mensagem de que estou vivo para "+r.IP);
                }
                Thread.sleep(500);
            }
        }catch(SocketException ex){
            System.out.println("erro no socket");
        } catch (UnknownHostException ex) {
            System.out.println("deu merda com o IP que voce mandou lesk");
        } catch (IOException ex) {
            System.out.println("nao foi possivel enviar a mensagem");
        } catch (InterruptedException ex) {
        }
    }
    
}
