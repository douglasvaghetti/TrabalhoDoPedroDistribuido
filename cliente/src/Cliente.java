
import java.io.IOException;
public class Cliente{
    
    public static void main(String[] args) throws IOException {
        
        if(args.length==0){
            System.out.println("modo de uso: java Cliente IPSSERVIDORSALAS1 IPSSERVIDORSALAS2");
            System.exit(0);
        }
        
        System.out.println("recebeu args:");
        String[] ipsServidoresSala;
        for(String s : args){
            System.out.println(s);
        }
        
        ClienteMiddleware c = new ClienteMiddleware();
            
    }
    
}