package br.edu.ifba.pedagio.servidor.impl;

public class Pedagio implements Comparable<Pedagio> {

    private String identificacao = "";
    private String modelo = "";

    // O(1)
    public Pedagio(String identificacao, String modelo) {
        this.identificacao = identificacao;
        this.modelo = modelo;
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
    public String getModelo() {
        return modelo;
    }

    // O(1)
    public void setModelo(String modelo) {
        this.modelo = modelo;
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

