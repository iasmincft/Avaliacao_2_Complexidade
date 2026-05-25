package br.edu.ifba.pedagio.servidor.impl;

public class Contagem {

    private int total;

    // O(1)
    public Contagem(int total) {
        this.total = total;
    }

    // O(1)
    public int getTotal() {
        return total;
    }

    // O(1)
    public void setTotal(int total) {
        this.total = total;
    }

    // O(1)
    @Override
    public String toString() {
        return "contagem: total=" + total;
    }

}
