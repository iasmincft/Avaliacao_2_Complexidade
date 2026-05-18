package br.edu.ifba.pedagio.servidor.operacoes;

public interface Operacoes {

    // Complexidade O(N)
    void imprimirContagens();

    // Complexidade O(N²)
    void imprimirLeituras(String mensagemPersonalizada);

    // Complexidade O(N²)
    void ordenarLeituras();

    // Complexidade O(N³)
    void buscarTrioCombinacoes(int alvo);
}