package br.edu.ifba.pedagio.servidor.impl;

import br.edu.ifba.pedagio.servidor.operacoes.Operacoes;

public class OperacoesImpl implements Operacoes {

    private Pedagio[] pedagios;
    private int totalPedagios;

    // Complexidade O(1)
    public OperacoesImpl(int capacidade) {

        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade deve ser maior que zero.");
        }

        this.pedagios = new Pedagio[capacidade];
        this.totalPedagios = 0;
    }

    // Complexidade O(1)
    public void adicionarPedagio(Pedagio pedagio) {

        if (pedagio == null) {
            throw new IllegalArgumentException("Pedágio não pode ser nulo.");
        }

        if (totalPedagios < pedagios.length) {
            pedagios[totalPedagios] = pedagio;
            totalPedagios++;
        } else {
            System.out.println("Limite máximo de pedágios atingido.");
        }
    }

    // Complexidade O(N)
    @Override
    public void imprimirContagens() {

        for (int i = 0; i < totalPedagios; i++) {

            System.out.println(
                "Pedágio ID: "
                + pedagios[i].getContagem().getId()
            );
        }
    }

    // Complexidade O(N²)
    @Override
    public void imprimirLeituras(String mensagemPersonalizada) {

        for (int i = 0; i < totalPedagios; i++) {

            System.out.print(
                mensagemPersonalizada
                + pedagios[i].getContagem().getId()
                + " Leituras: "
            );

            int[] leituras =
                pedagios[i].getContagem().getLeituras();

            for (int leitura : leituras) {
                System.out.print(leitura + " ");
            }

            System.out.println();
        }
    }

    // Complexidade O(N²)
    @Override
    public void ordenarLeituras() {

        for (int i = 0; i < totalPedagios; i++) {

            int[] leituras =
                pedagios[i].getContagem().getLeituras();

            // Bubble Sort
            for (int j = 0; j < leituras.length - 1; j++) {

                for (int k = 0; k < leituras.length - j - 1; k++) {

                    if (leituras[k] > leituras[k + 1]) {

                        int temp = leituras[k];
                        leituras[k] = leituras[k + 1];
                        leituras[k + 1] = temp;
                    }
                }
            }
        }
    }

    // Complexidade O(N³)
    @Override
    public void buscarTrioCombinacoes(int alvo) {

        for (int i = 0; i < totalPedagios; i++) {

            int[] leituras =
                pedagios[i].getContagem().getLeituras();

            for (int j = 0; j < leituras.length; j++) {

                for (int k = j + 1; k < leituras.length; k++) {

                    for (int l = k + 1; l < leituras.length; l++) {

                        if (
                            leituras[j]
                            + leituras[k]
                            + leituras[l]
                            == alvo
                        ) {

                            System.out.println(
                                "Combinação encontrada no Pedágio ID "
                                + pedagios[i].getContagem().getId()
                                + ": "
                                + leituras[j]
                                + " + "
                                + leituras[k]
                                + " + "
                                + leituras[l]
                                + " = "
                                + alvo
                            );
                        }
                    }
                }
            }
        }
    }
}