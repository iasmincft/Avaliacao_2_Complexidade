package br.edu.ifba.pedagio.cliente.impl;

public class Pedagio implements Comparable<Pedagio> {

    private String identificacao = "";

    // O(1)
    public Pedagio(String identificacao) {
        this.identificacao = identificacao;
    }

    // O(1)
    public String getIdentificacao() {
        return identificacao;
    }

    // O(1)
    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    // O(1)
    @Override
    public String toString() {
        return "pedagio: " + identificacao;
    }

    // O(1)
    @Override
    public int compareTo(Pedagio outroPedagio) {
        return identificacao.compareTo(outroPedagio.getIdentificacao());
    }

}
