package br.edu.ifba.pedagio.servidor.operacoes;

public interface Operacoes<Monitorado, Leitura> {

    public void gravar(Monitorado monitorado, Leitura leitura);

    public int buscarTrioCombinacoes(int alvoSoma);

}
