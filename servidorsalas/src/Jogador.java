
import java.io.Serializable;

public class Jogador implements Serializable{

    public String login, senha;
    public boolean estaLogado;
    public boolean gold;
    public String IP;
    public String personagem;
    
    public Jogador(String login, String senha) {
        this.login = login;
        this.senha = senha;
        this.estaLogado = false;
    }
    
    
    
}
