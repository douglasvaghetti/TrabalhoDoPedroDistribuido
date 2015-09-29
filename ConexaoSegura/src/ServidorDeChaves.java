
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

//Protocolo Server01:Server02
public class ServidorDeChaves {

    private static HashMap<String, byte[]> chaves;

    public static void main(String[] args) {
/*
        String chave = "asdfghjkloiuytre";
        String texto = "Leque piranha!hiuhiuhiuhiihsdaiuhdasihdsiahiasdasasda";
        
        String criptografada = new String(Criptografia.criptografa(texto.getBytes(), chave.getBytes()));
        byte[] aux = Criptografia.criptografa(texto.getBytes(), chave.getBytes());
        System.out.println(criptografada);
        
        String descriptografada = new String(Criptografia.descriptografa(aux, chave.getBytes()));
        
        System.out.println(descriptografada);
*/      
        chaves = new HashMap<>();

        chaves.put("192.168.1.18", "chavechavechavea".getBytes());
        chaves.put("192.168.1.17", "chavechavechaveb".getBytes());
        chaves.put("192.168.1.19", "chavechavechavec".getBytes());
        chaves.put("192.168.1.12", "chavechavechaved".getBytes());
        chaves.put("192.168.1.6" , "chavechavechavee".getBytes());

        ServerSocket novaConexao;
        try {
            novaConexao = new ServerSocket(50666);
        
        while (true) {
            
                ConexaoObjeto conexao = new ConexaoObjeto(novaConexao.accept());
                System.out.println("Recebeu comexao do cliente"+conexao.getIP());
                String server01 = conexao.getIP();
                String server02 = (String) conexao.recebeObjeto();
                System.out.println("Fulaniho "+server01);
                System.out.println("Quer falar com "+server02);
                System.out.println("Chave compartilhada criada");
                String strChaveCompartilhada = "ABCDEFGHJKLMNOPR";//Criptografia.nextSessionId();
                byte[] chaveCompartilhada = strChaveCompartilhada.getBytes();
                System.out.println("str chave compartilhada = "+Arrays.toString(strChaveCompartilhada.getBytes()));
                
                
                conexao.enviaObjeto(Criptografia.criptografa(chaveCompartilhada,chaves.get(server01)));
                System.out.println("envio chave 1");
                conexao.enviaObjeto(Criptografia.criptografa(chaveCompartilhada,chaves.get(server02)));
                System.out.println("Envio chave 2");
            
        }   
            } catch (IOException ex) {
                Logger.getLogger(ServidorDeChaves.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServidorDeChaves.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
