package br.edu.ifba.pedagio.cliente.impl;

public class Contagem {

    private int total;

    public Contagem(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "contagem: total=" + total;
    }

}
