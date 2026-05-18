package br.edu.ifba.avaliacao.pedagio.impl;

import java.util.Random;

public class SensoriamentoImpl {
    private Pedagio[] pedagios;

    public SensoriamentoImpl(int numPedagios, int numLeituras) {
        pedagios = new Pedagio[numPedagios];
        Random random = new Random();
        
        for (int i = 0; i < numPedagios; i++) {
            pedagios[i] = new Pedagio(i + 1, numLeituras); // Aqui é onde estamos criando um novo objeto Pedagio.
            int[] leituras = new int[numLeituras];
            for (int j = 0; j < numLeituras; j++) {
                leituras[j] = random.nextInt(100); // Gera leituras aleatórias entre 0 e 99.
            }
            pedagios[i].getContagem().setLeituras(leituras); // Definindo as leituras no Pedágio.
        }
    }

    public Pedagio[] getPedagios() {
        return pedagios;
    }
}
