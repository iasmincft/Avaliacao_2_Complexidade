package br.edu.ifba.pedagio.servidor.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import br.edu.ifba.pedagio.servidor.operacoes.Operacoes;

public class OperacoesImpl implements Operacoes<Pedagio, Contagem> {

    private Map<Pedagio, Queue<Contagem>> bancoDeDados = new TreeMap<>();

    @Override
    public void gravar(Pedagio pedagio, Contagem contagem) {
        Queue<Contagem> contagens = new LinkedList<>();
        if (bancoDeDados.containsKey(pedagio)) {
            contagens = bancoDeDados.get(pedagio);
        } else {
            bancoDeDados.put(pedagio, contagens);
        }

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        contagens.add(contagem);

        System.out.println("gravada nova contagem para o pedagio: " + pedagio);
    }

    // O(M * N^3), onde M é a quantidade de pedágios e N é a quantidade de contagens
    // por pedágio.
    @Override
    public int buscarTrioCombinacoes(int alvoSoma) {
        int contador = 0;

        for (Pedagio pedagio : bancoDeDados.keySet()) {
            List<Contagem> contagensPorPedagio = new ArrayList<>(bancoDeDados.get(pedagio));
            int n = contagensPorPedagio.size();

            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    for (int k = j + 1; k < n; k++) {
                        int soma = contagensPorPedagio.get(i).getTotal()
                                + contagensPorPedagio.get(j).getTotal()
                                + contagensPorPedagio.get(k).getTotal();

                        if (soma == alvoSoma) {
                            contador++;
                        }
                    }
                }
            }
        }

        return contador;
    }

}
