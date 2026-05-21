package br.edu.ifba.pedagio.cliente;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.pedagio.cliente.impl.ClienteImpl;
import br.edu.ifba.pedagio.cliente.impl.Pedagio;
import br.edu.ifba.pedagio.cliente.impl.SensoriamentoImpl;

public class App {

    private static final int TOTAL_PEDAGIOS = 10;

    public static void main(String[] args) throws Exception {
        List<Thread> processos = new ArrayList<>();

        for (int i = 0; i < TOTAL_PEDAGIOS; i++) {
            String id = "PED-" + (i + 1);

            ClienteImpl cliente = new ClienteImpl();
            cliente.configurar(new Pedagio(id), new SensoriamentoImpl());

            Thread processo = new Thread(cliente);
            processos.add(processo);
            processo.start();
        }

        for (Thread processo : processos) {
            processo.join();
        }

        System.out.println("contagens enviadas");
    }
}
