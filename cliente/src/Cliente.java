
import java.io.IOException;
public class Cliente{
    
    public static void main(String[] args) throws IOException {
        
        if(args.length==0){
            System.out.println("modo de uso: java Cliente IPSSERVIDORSALAS1 IPSSERVIDORSALAS2");
            System.exit(0);
        }
        
        String[] ipsServidoresSala = args;
        System.out.println("lista de ips dos servidores de sala:");
        for(String ip : ipsServidoresSala){
            System.out.println(ip);
        }
        
        ClienteMiddleware c = new ClienteMiddleware(ipsServidoresSala);
            
    }
    
}