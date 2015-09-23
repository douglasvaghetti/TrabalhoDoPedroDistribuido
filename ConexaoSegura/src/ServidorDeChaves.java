
import java.io.IOException;
import java.net.ServerSocket;
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

        chaves.put("ALICE", "ChaveChaveALICE*".getBytes());
        chaves.put("BOBOB", "ChaveChaveBOBOB*".getBytes());
        chaves.put("JOAOJ", "ChaveChaveJOAOJ*".getBytes());
        chaves.put("MARIA", "ChaveChaveMARIA*".getBytes());

        while (true) {
            try {
                ServerSocket novaConexao = new ServerSocket(50666);
                ConexaoObjeto conexao = new ConexaoObjeto(novaConexao.accept());

                String msg = (String) conexao.recebeObjeto();

                String server01 = msg.split(":")[0];
                String server02 = msg.split(":")[1];

                byte[] chaveCompartilhada = Criptografia.nextSessionId().getBytes();
                
                conexao.enviaObjeto(Criptografia.criptografa(chaveCompartilhada,chaves.get(server01)));
                conexao.enviaObjeto(Criptografia.criptografa(chaveCompartilhada,chaves.get(server02)));

            } catch (IOException ex) {
                Logger.getLogger(ServidorDeChaves.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServidorDeChaves.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
          
    }

}
