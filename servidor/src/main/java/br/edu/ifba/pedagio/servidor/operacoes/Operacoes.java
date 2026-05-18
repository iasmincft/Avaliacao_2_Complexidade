package br.edu.ifba.pedagio.servidor.operacoes;

public interface Operacoes<Monitorado, Sensor> {

    void gravarContagens(Monitorado monitorado, Sensor sensor);

    String gerarRelatorioBahia(double totalCobradoBahia);
}
