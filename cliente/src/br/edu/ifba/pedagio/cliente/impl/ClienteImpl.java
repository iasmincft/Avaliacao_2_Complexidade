package br.edu.ifba.pedagio.cliente.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
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

    private static final int LIMIAR_ENVIO = 10;

    private Contagem ultimaContagem = new Contagem(0);
    private List<Contagem> contagensLocais = new ArrayList<>();

    // O(1)
    @Override
    public void configurar(Pedagio pedagio, Sensoriamento<Contagem> sensoriamento) {
        this.pedagio = pedagio;
        this.sensoriamento = sensoriamento;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Resultado enviar(Contagem contagem) throws Exception {
        Resultado resultado = Resultado.SUCESSO;

        URL urlEnvio = new URL(URL_PEDAGIOS + pedagio.getIdentificacao() + "/contagem/" + contagem.getTotal());

        HttpURLConnection conexao = (HttpURLConnection) urlEnvio.openConnection();
        conexao.setRequestMethod("POST");
        if (conexao.getResponseCode() != 200) {
            resultado = Resultado.ERRO;

            throw new Exception("erro de conexão com o servidor");
        }
        conexao.disconnect();

        return resultado;
    }

    // O(1) para requisição HTTP; O(1) para envio de resultado.
    @Override
    public Resultado enviarResultadoTrios(int totalTrios) throws Exception {
        Resultado resultado = Resultado.SUCESSO;

        URL urlEnvio = URI.create(URL_PEDAGIOS + pedagio.getIdentificacao() + "/trios/" + totalTrios).toURL();

        HttpURLConnection conexao = (HttpURLConnection) urlEnvio.openConnection();
        conexao.setRequestMethod("POST");
        if (conexao.getResponseCode() != 200) {
            resultado = Resultado.ERRO;
            throw new Exception("erro ao enviar resultado de trios ao servidor");
        }
        conexao.disconnect();

        return resultado;
    }

    // O(N^3), onde N é a quantidade de leituras geradas localmente (TOTAL_DE_LEITURAS).
    public int processarTriosLocalmente(int alvoSoma) {
        int contador = 0;
        int n = contagensLocais.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    int soma = contagensLocais.get(i).getTotal()
                            + contagensLocais.get(j).getTotal()
                            + contagensLocais.get(k).getTotal();

                    if (soma == alvoSoma) {
                        contador++;
                    }
                }
            }
        }

        return contador;
    }

    // O(N) para iteração das leituras; O(N^3) para processamento de trios; complexidade total O(N^3).
    @Override
    public void run() {
        List<Contagem> contagens = sensoriamento.gerar(TOTAL_DE_LEITURAS);

        for (Contagem contagem : contagens) {
            contagensLocais.add(contagem);

            int diferenca = Math.abs(contagem.getTotal() - ultimaContagem.getTotal());

            if (diferenca > LIMIAR_ENVIO) {
                ultimaContagem = contagem;

                System.out.println("contagem sendo enviada...");

                try {
                    enviar(contagem);

                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("não ocorreram diferenças significativas desde a última contagem");
            }
        }

        try {
            int totalTrios = processarTriosLocalmente(450);
            System.out.println("trios encontrados no cliente: " + totalTrios);
            enviarResultadoTrios(totalTrios);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
