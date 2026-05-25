package br.edu.ifba.pedagio.servidor.operacoes;

public interface Operacoes<Monitorado, Leitura> {

    public void gravar(Monitorado monitorado, Leitura leitura);

    public void gravar(Monitorado monitorado, int totalTrios);

    public String obterResultadosTrios();

}
