package br.edu.ifba.avaliacao.pedagio.cliente;

import br.edu.ifba.avaliacao.pedagio.cliente.impl.ClienteImpl;
import br.edu.ifba.avaliacao.pedagio.impl.Pedagio;
import br.edu.ifba.avaliacao.pedagio.impl.SensoriamentoImpl;

public class ClienteMain {

    // Complexidade O(N)
    public static void main(String[] args) {

        int numPedagios = 10;

        int numLeituras = 10;

        SensoriamentoImpl sensoriamento =
            new SensoriamentoImpl(
                numPedagios,
                numLeituras
            );

        Pedagio[] pedagios =
            sensoriamento.getPedagios();

        // Uma thread por pedágio
        for (Pedagio pedagio : pedagios) {

            ClienteImpl cliente =
                new ClienteImpl();

            cliente.configurar(
                pedagio,
                sensoriamento
            );

            Thread thread =
                new Thread(cliente);

            thread.start();
        }
    }
}