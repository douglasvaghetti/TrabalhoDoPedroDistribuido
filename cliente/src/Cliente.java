
import java.io.IOException;
import javax.swing.JFrame;
public class Cliente{
    
    public static void main(String[] args) throws IOException {
        int playersNaSala;
        String[] Ips;
        System.out.println("tamanho do args = "+args.length);
        JFrame janela = new Login();
        janela.setVisible(true);
        if(args.length<2){
            System.out.println("java Cliente numeroDeJogadoresNaSala ip1 ip2 ip3 \n"
                    + "sendo ips os ips dos servidores de sala. (minimo de"
                    + "2 servidores de sala");
            return;
        }else{
            System.out.println("conectando");
            playersNaSala = Integer.parseInt(args[0]);
            Ips  = new String[args.length-1];
            for(int x=0;x<args.length-1;x++){
                Ips[x] = args[x+1];
            }
        }

        ClienteMiddleware c = new ClienteMiddleware();

        c.conecta(playersNaSala,Ips);
            
    }
    
}