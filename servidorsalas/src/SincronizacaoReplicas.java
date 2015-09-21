public class SincronizacaoReplicas {
    
    public static void sincronizaSalas(){
        for(ReplicaServidorSalas r: ServidorSalas.vizinhos){
            r.sincronizaSalas();
        }
    }
    
    public static void sincronizaJogadores(){
        for(ReplicaServidorSalas r: ServidorSalas.vizinhos){
            r.sincronizaJogadores();
        }
    }
    
    public static void sincronizaServidoresDeJogo(){
        for(ReplicaServidorSalas r: ServidorSalas.vizinhos){
            r.sincronizaServidoresDeJogo();
        }
    }
}
