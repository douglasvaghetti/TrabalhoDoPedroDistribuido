public class ThreadTimeoutLoginJogador extends Thread {
    Jogador j;

    public ThreadTimeoutLoginJogador(Jogador j) {
        this.j = j;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(120000);
        } catch (InterruptedException ex) {
            System.out.println("falhou no sleep do timeout");
        }
        j.estaLogado = false;
    }
    
    
    
}
