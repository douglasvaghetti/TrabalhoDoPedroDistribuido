
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Douglas
 */
public class ThreadRespondeCargaAtual extends Thread {
    
    @Override
    public void run() {
        try {
            ServerSocket recebedorDeConexoes = new ServerSocket(ServidorDeJogo.PORTAATUALIZACARGA);
            while(true){
                Socket conexao = recebedorDeConexoes.accept();
                PrintWriter out = new PrintWriter(conexao.getOutputStream(),true);
                out.println(ServidorDeJogo.salasDeJogo.size());
                conexao.close();
            }
        } catch (IOException ex) {
            System.out.println("erro na conexao do enviador de carga atualizada");
        }
    }
    
}
