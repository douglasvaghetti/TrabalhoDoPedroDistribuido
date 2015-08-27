
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author aluno
 */
public class ThreadPegaPeso extends Thread {
    String ip;
    int vizinho;

    public ThreadPegaPeso(String ip,int vizinho) {
        this.ip = ip;
        this.vizinho = vizinho;
        System.out.println("iniciou thread para busca de peso do vizinho de ip "+ip);
    }
    
    @Override
    public void run(){
        System.out.println("abrindo thread pega peso");
        try {
            
            Conexao conexao = new Conexao(ip,ServidorSalas.PORTATROCAINFOPESO);
            int cap= Integer.parseInt(conexao.recebe());
            System.out.println("peso obtido de "+ip+" = "+cap);
            ServidorSalas.vizinhos.get(vizinho).forca = cap;
        } catch (IOException ex) {
            System.out.println("deu merda, peso não pode ser obtido");
            ServidorSalas.vizinhos.get(vizinho).forca = -1;
        }
    }
}
