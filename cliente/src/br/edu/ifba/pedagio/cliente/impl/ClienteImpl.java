package br.edu.ifba.pedagio.cliente.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.edu.ifba.pedagio.cliente.comunicacao.Cliente;
import br.edu.ifba.pedagio.cliente.comunicacao.Resultado;
import br.edu.ifba.pedagio.cliente.sensoriamento.Sensoriamento;

public class ClienteImpl implements Cliente<Pedagio, Contagem>, Runnable {

    private static final int TOTAL_DE_LEITURAS = 1000;

    private static final String URL_SERVIDOR = "http://localhost:8081";
    private static final String URL_PEDAGIOS = URL_SERVIDOR + "/pedagios/";

    private Pedagio pedagio = null;
    private Sensoriamento<Contagem> sensoriamento = null;

    @Override
    public void configurar(Pedagio pedagio, Sensoriamento<Contagem> sensoriamento) {
        this.pedagio = pedagio;
        this.sensoriamento = sensoriamento;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Resultado enviar(Contagem contagem) throws Exception {
        Resultado resultado = Resultado.SUCESSO;

        URL urlEnvio = new URL(URL_PEDAGIOS + pedagio.getIdentificacao() + "/" + contagem.getTotal());

        HttpURLConnection conexao = (HttpURLConnection) urlEnvio.openConnection();
        conexao.setRequestMethod("POST");
        if (conexao.getResponseCode() != 200) {
            resultado = Resultado.ERRO;

            throw new Exception("erro de conexão com o servidor");
        }
        conexao.disconnect();

        return resultado;
    }

    @Override
    public void run() {
        List<Contagem> contagens = sensoriamento.gerar(TOTAL_DE_LEITURAS);

        for (Contagem contagem : contagens) {
            System.out.println("contagem sendo enviada...");

            try {
                enviar(contagem);

                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
