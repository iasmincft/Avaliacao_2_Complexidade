package br.edu.ifba.pedagio.cliente.impl;

public class Pedagio {

    private Contagem contagem;

    public Pedagio(int id, int totalLeituras) {

        this.contagem = new Contagem(id, totalLeituras);
    }

    public Contagem getContagem() {
        return contagem;
    }
}
