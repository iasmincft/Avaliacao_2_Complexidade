package br.edu.ifba.pedagio.servidor.impl;

import java.util.Random;

public class Contagem {

    private int id;
    private int[] leituras;

    public Contagem(int id, int totalLeituras) {

        this.id = id;
        this.leituras = new int[totalLeituras];

        gerarLeituras();
    }

    // Complexidade O(N)
    
    private void gerarLeituras() {

        Random random = new Random();

        for (int i = 0; i < leituras.length; i++) {

            leituras[i] = random.nextInt(100);
        }
    }

    public int getId() {
        return id;
    }

    public int[] getLeituras() {
        return leituras;
    }
}