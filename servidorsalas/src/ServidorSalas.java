import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class ServidorSalas {
    public static final int PORTAREGISTRARECURSOS = 50000;
    public static final int PORTACONECTACLIENTES = 50001;
    public static final int PORTAGERENCIADORDECADASTRO = 50002;
    public static final int PORTATROCAINFOPESO = 50012;
    public static final int PORTAANUNCIALIDER = 50013;
    public static final int PORTAAVISOSVIVO= 50014;
    public static final int PORTAMANDAPACOTEJOGADORESSALA= 50020;
    public static final int PORTAATUALIZACARGASERVIDORES= 50021;
    public static final int PORTAATUALIZASALASREPLICAS= 50030;
    public static final int PORTAATUALIZASERVIDORESREPLICAS= 50031;
    public static final int PORTAATUALIZAJOGADORES= 50032;
    public static ArrayList<ServidorDeJogo> servidoresDeJogo = new ArrayList<>();
    public static final Object mutexServidoresDeJogo = new Object();  //nao pode usar o syncronized do arraylist pq ele pode ser sobrescrito
    public static ArrayList<Jogador> jogadores = new ArrayList<>();
    public static final Object mutexJogadores = new Object();
    
    public static ArrayList<Sala> salasEmMontagem = new ArrayList<>();
    public static final Object mutexSalasEmMontagem = new Object(); //nao pode usar o syncronized do arraylist pq ele pode ser sobrescrito
    
    public static boolean ehLider = false;
    public static int forca;
    public static ArrayList<ReplicaServidorSalas> vizinhos;
    public static String IPLiderAtual;
    private static ThreadLimpavel[] threadsVivas = null;
    
    public static void main(String[] args) throws UnknownHostException {
        if(args.length>=1){ //ao menos a cap e um vizinho
            forca = Integer.parseInt(args[0]);
            vizinhos = new ArrayList<ReplicaServidorSalas>();
            for(int x=1;x<args.length;x++){
                vizinhos.add(new ReplicaServidorSalas(args[x], -1));
            }
        }else{
            System.out.println("voce deve especificar a capacidade desse servidor "
                    + "de sala e o ip do vizinho"
                    + "ex: java ServidorSalas 10 ip1 ip2 .. ip3 etc");
        }
        
        
        
        new ThreadEnviadorDeCapacidade().start();
        System.out.println("abrindo thread enviador de capacidade");
        new ThreadRegistradorRecursos().start();
        System.out.println("abrindo thread registrador de recursos");
        new ThreadRecebedorAnuncioLider().start();
        System.out.println("abrindo thread recebedor anuncio lider");
        new ThreadRecebeSalasAtualizadas().start();
        System.out.println("iniciou thread recebedora de salas atualizadas");
        new ThreadRecebeServidoresDeJogoAtualizados().start();
        System.out.println("iniciou a thread recebedora de servidores de jogo atualizados");
        new ThreadRecebeJogadoresAtualizados().start();
        System.out.println("iniciou a thread recebedora de jogadores atualizados");
        
        if(vizinhos.size()>0){
            System.out.println(">>>>aperte enter depois que todos servidores de salas "
                    + "estiverem abertos para inciar a eleicao.");
            new Scanner(System.in).nextLine();
            getVizinhos();
            System.out.println("obteve pesos de todos vizinhos");
        }else{
            System.out.println("");
        }
        
        System.out.println("verificando se eh lider");
        ehLider = verificaSeEhLider();
        
        if(ehLider){
            System.out.println("sou lider");
            iniciaThreadsLider();
            System.out.println("threads de lider abertas");
        }else{
            System.out.println("nao sou lider");
            inicioThreadsNaoLider();
            System.out.println("threads de nao lider abertas");
        }
    }
    
    public static void getVizinhos(){
        
        ThreadPegaPeso [] threads = new ThreadPegaPeso[vizinhos.size()];
        System.out.println("eu tenho "+vizinhos.size()+" vizinhos");
        for(int x=0;x<vizinhos.size();x++){
            System.out.println("buscando peso de "+x);
            threads[x] = new ThreadPegaPeso(vizinhos.get(x).IP, x);
            threads[x].start();
        }

        for(int x=0;x<vizinhos.size();x++){
            System.out.println("esperando "+x+" sincronizar ");
            try {
                threads[x].join();
            } catch (InterruptedException ex) {
                System.out.println("deu merda no join");
            }
            System.out.println(x+"sincronizou");
        }
    }

    public static boolean verificaSeEhLider() {
        for (ReplicaServidorSalas vizinho : vizinhos) {
            if(vizinho.forca>forca){
                try {
                    System.out.println("tentando conectar com "+vizinho.IP);
                    Conexao conexao = new Conexao(vizinho.IP,PORTATROCAINFOPESO);
                    System.out.println("conectou");
                    conexao.close();
                    return false;
                } catch (IOException ex) {
                    System.out.println("tentou criar conexao com "+vizinho.IP+""
                            + "para avisar que eh lider mas estava morta");
                }
            }
        }
        return true;
    }

    public static void avisaOutrosQueEhLider() {
        for (ReplicaServidorSalas vizinho : vizinhos) {
            System.out.println("avisando todo mundo que eh lider");
            try {
                System.out.println("tentando conectar com "+vizinho.IP);
                Conexao conexao = new Conexao(vizinho.IP,PORTATROCAINFOPESO);
                System.out.println("conectou");
                conexao.envia("EUSOUOLIDER:"+InetAddress.getLocalHost().toString().replaceAll("/", ""));
                //TODO: ARRUAMR ISSO, NAO FUNCIONA EM QUALQUER OS
                conexao.close();
            } catch (IOException ex) {
                 System.out.println("print tentou avisar para replica   "+vizinho.IP+" que eh lider mas replica nao esta viva");
            }
        }
    }
    
    public static void inicioThreadsNaoLider(){
        System.out.println("iniciando threads de nao lider");
        limpaThreads();
        
        threadsVivas = new ThreadLimpavel[1];
        threadsVivas[0] =  new ThreadRecedorMensagensLiderVivo();
        threadsVivas[0].start();
    }
    
    public static void iniciaThreadsLider(){
        System.out.println("iniciando threads de lider");
        limpaThreads();
        threadsVivas = new ThreadLimpavel[2];
        try {
            IPLiderAtual = InetAddress.getLocalHost().toString().replaceAll("/","");  //getlocalhost bota uma barra no inicio por padrao
            avisaOutrosQueEhLider();
            threadsVivas[0] = new ThreadEnviadorMensagemLiderVivo();
            threadsVivas[0].start();
            threadsVivas[1] = new ThreadGerenciadorDeSalas();
            threadsVivas[1].start();
            threadsVivas[2] = new ThreadGerenciadorDeCadastro();
            threadsVivas[2].start();
            
        } catch (UnknownHostException ex) {
            System.out.println("erro abrindo as threads de lider");
            ex.printStackTrace();
        }
    }
    
    public static void limpaThreads(){
        System.out.println("iniciando limpa threads");
        if(threadsVivas!=null){
            System.out.println("limpando threads");
            for (ThreadLimpavel t : threadsVivas){
                t.limpa();
            }
        }else{
            System.out.println("limpathreads nao fez nada pois nao havia threads ativas");
        }
    }
    
}
