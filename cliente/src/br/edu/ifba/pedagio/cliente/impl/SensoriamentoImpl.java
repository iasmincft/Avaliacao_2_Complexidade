package br.edu.ifba.pedagio.cliente.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.edu.ifba.pedagio.cliente.sensoriamento.Sensoriamento;

public class SensoriamentoImpl implements Sensoriamento<Contagem> {

    private static final int CARROS_NORMAIS = 150;
    private static final int OSCILACAO_MAXIMA = 30;

    // Complexidade Linear, O(N)
    @Override
    public List<Contagem> gerar(int totalLeituras) {
        List<Contagem> contagens = new ArrayList<>();

        Random randomizador = new Random();
        for (int i = 0; i < totalLeituras; i++) {
            int oscilacao = CARROS_NORMAIS * randomizador.nextInt(OSCILACAO_MAXIMA) / 100;
            int total = randomizador.nextBoolean() ? CARROS_NORMAIS + oscilacao : CARROS_NORMAIS - oscilacao;

            contagens.add(new Contagem(total));
        }

        return contagens;
    }

}

