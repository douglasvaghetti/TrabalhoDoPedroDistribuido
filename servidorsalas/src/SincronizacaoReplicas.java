public class SincronizacaoReplicas {
    
    public static void sincronizaSalas(){
        for(ReplicaServidorSalas r: ServidorSalas.vizinhos){
            r.sincronizaSalas();
        }
    }
    
    public static void sincronizaServidoresDeJogo(){
        for(ReplicaServidorSalas r: ServidorSalas.vizinhos){
            r.sincronizaServidoresDeJogo();
        }
    }
}
