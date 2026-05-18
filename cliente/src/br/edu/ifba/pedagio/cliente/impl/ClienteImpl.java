package br.edu.ifba.pedagio.cliente.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.edu.ifba.avaliacao.pedagio.cliente.comunicacao.Cliente;
import br.edu.ifba.avaliacao.pedagio.cliente.comunicacao.Resultado;
import br.edu.ifba.avaliacao.pedagio.impl.Contagem;
import br.edu.ifba.avaliacao.pedagio.impl.Pedagio;
import br.edu.ifba.avaliacao.pedagio.sensoriamento.Sensoriamento;

public class ClienteImpl
implements Cliente<Pedagio, Contagem>, Runnable {

    private static final int TOTAL_DE_LEITURAS = 100;

    private static final String URL_SERVIDOR =
        "http://localhost:8080";

    private static final String URL_PEDAGIOS =
        URL_SERVIDOR + "/pedagios/";

    private Pedagio pedagio = null;

    private Sensoriamento<Contagem> sensoriamento = null;

    private static final int LIMIAR_ENVIO = 10;

    private Contagem ultimaContagem =
        new Contagem(0, 10);

    @Override
    public void configurar(Pedagio pedagio,Sensoriamento<Contagem> sensoriamento) {

        this.pedagio = pedagio;
        this.sensoriamento = sensoriamento;
    }

    // Complexidade O(1)
    @Override
    public Resultado enviar(Contagem contagem) throws Exception {

        Resultado resultado = Resultado.SUCESSO;

        URL urlEnvio = new URL(URL_PEDAGIOS+ pedagio.getContagem().getId());

        HttpURLConnection conexao =
            (HttpURLConnection) urlEnvio.openConnection();

        conexao.setRequestMethod("POST");

        if (conexao.getResponseCode() != 200) {

            resultado = Resultado.ERRO;

            throw new Exception(
                "Erro de conexão com servidor."
            );
        }

        conexao.disconnect();

        return resultado;
    }

    @Override
    public void run() {

        List<Contagem> contagens =
            sensoriamento.gerar(TOTAL_DE_LEITURAS);

        for (Contagem contagem : contagens) {

            try {

                System.out.println(
                    "Enviando contagem..."
                );

                enviar(contagem);

                Thread.sleep(50);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}