/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Douglas
 */
public class SincronizacaoServidoresDeJogo {
    public static void sicronizaDadosServidoresDeJogo() {
        for(ServidorDeJogo s: ServidorSalas.servidoresDeJogo){
            s.sincroniza();
        }
    }
    
    public static void printDadosServidoresDeJogo(){
        System.out.println("lista de servidores de jogo:");
        for(ServidorDeJogo s: ServidorSalas.servidoresDeJogo){
            System.out.println("IP: "+s.IP+" carga: "+s.salasOcupadas+" maximo:"+s.maxSalas+" clientes/sala = "+s.clientesPorSala);
        }
    }
}
