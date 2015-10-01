public class SincronizacaoReplicas {
    
    public static void sincronizaSalas(){
        for(ReplicaServidorSalas r: ServidorSalas.vizinhos){
            r.sincronizaSalas();
        }
        System.out.println("sincronizou salas com as replicas");
    }
    
    public static void sincronizaJogadores(){
        for(ReplicaServidorSalas r: ServidorSalas.vizinhos){
            r.sincronizaJogadores();
        }
        System.out.println("sincronizou jogadores com as replicas");
    }
    
    public static void sincronizaServidoresDeJogo(){
        for(ReplicaServidorSalas r: ServidorSalas.vizinhos){
            r.sincronizaServidoresDeJogo();
        }
        System.out.println("sincronizou servidores de jogo com as replicas");
    }
}
