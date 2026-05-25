package br.edu.ifba.pedagio.servidor.impl;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import br.edu.ifba.pedagio.servidor.operacoes.Operacoes;

public class OperacoesImpl implements Operacoes<Pedagio, Contagem> {

    private static final int LIMIAR_ROTACIONAMENTO_CONTAGENS = 40;

    private Map<Pedagio, Queue<Contagem>> bancoDeDados = new TreeMap<>();
    private Map<Pedagio, Integer> resultadosTrios = new TreeMap<>();

    // O(log N) para operações TreeMap (containsKey, get, put); O(1) para enfileiramento.
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

        if (contagens.size() > LIMIAR_ROTACIONAMENTO_CONTAGENS) {
            contagens.poll();

            System.out.println("limite de rotacionamento atingido, última contagem descartada");
        }
        contagens.add(contagem);

        System.out.println("gravada nova contagem para o pedagio: " + pedagio);
    }

    // O(1) para armazenar e acumular resultado de trios calculado pelo cliente.
    @Override
    public void gravar(Pedagio pedagio, int totalTrios) {
        System.out.println(totalTrios > 0 ? "trios informados pelo cliente para " + pedagio + ": " + totalTrios 
                                          : "nenhum trio informado pelo cliente para " + pedagio);
        if (resultadosTrios.containsKey(pedagio)) {
            totalTrios += resultadosTrios.get(pedagio);  // Acumula ao invés de sobrescrever
        }
        resultadosTrios.put(pedagio, totalTrios);
    }

    // O(M), onde M é a quantidade de pedágios com resultados.
    public String obterResultadosTrios() {
        StringBuilder resultado = new StringBuilder();
        for (Pedagio pedagio : resultadosTrios.keySet()) {
            resultado.append(pedagio).append(" = ").append(resultadosTrios.get(pedagio)).append("; ");
        }
        return resultado.toString();
    }

}
