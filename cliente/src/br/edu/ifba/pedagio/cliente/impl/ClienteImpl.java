package br.edu.ifba.pedagio.cliente.impl;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;

import br.edu.ifba.pedagio.cliente.comunicacao.Cliente;
import br.edu.ifba.pedagio.cliente.comunicacao.Resultado;
import br.edu.ifba.pedagio.cliente.sensoriamento.Sensoriamento;

public class ClienteImpl implements Cliente<Pedagio, Contagem>, Runnable {

    private static final int TOTAL_DE_LEITURAS = 1000;

    private static final String URL_SERVIDOR = "http://localhost:8081";
    private static final String URL_PEDAGIOS = URL_SERVIDOR + "/pedagios/";

    private static final String ALGORITMO_ENCRIPTACAO = "RSA";
    private static final String CAMINHO_CHAVE_PUBLICA = "chaves/ch_publica.bin";

    private static final int ALVO_SOMA_TRIOS = 450;

    private Pedagio pedagio = null;
    private Sensoriamento<Contagem> sensoriamento = null;

    private static final int LIMIAR_ENVIO = 10;

    private Contagem ultimaContagem = new Contagem(0);
    private List<Contagem> contagensLocais = new ArrayList<>();

    private PublicKey chave = null;

    // O(1) para atribuicoes; o carregamento da chave publica do disco e O(1).
    @Override
    public void configurar(Pedagio pedagio, Sensoriamento<Contagem> sensoriamento) throws Exception {
        this.pedagio = pedagio;
        this.sensoriamento = sensoriamento;
        this.chave = getChave();
    }

    // O(1): le a chave publica do arquivo e a reconstroi via X509EncodedKeySpec.
    private PublicKey getChave() throws Exception {
        File arquivo = new File(CAMINHO_CHAVE_PUBLICA);
        FileInputStream stream = new FileInputStream(arquivo);

        byte[] bytes = stream.readAllBytes();
        stream.close();

        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        KeyFactory kf = KeyFactory.getInstance(ALGORITMO_ENCRIPTACAO);

        return kf.generatePublic(spec);
    }

    // O(1): a encriptacao RSA opera sobre um bloco de tamanho fixo da chave.
    private byte[] encriptar(String dados) throws Exception {
        Cipher cifrador = Cipher.getInstance(ALGORITMO_ENCRIPTACAO);
        cifrador.init(Cipher.ENCRYPT_MODE, chave);

        return cifrador.doFinal(dados.getBytes());
    }

    // O(1): requisicao HTTP unica com a contagem encriptada no caminho da URL.
    @SuppressWarnings("deprecation")
    @Override
    public Resultado enviar(Contagem contagem) throws Exception {
        Resultado resultado = Resultado.SUCESSO;

        String json = "{\"id\":\"" + pedagio.getIdentificacao() + "\",\"total\":" + contagem.getTotal() + "}";
        String dados = new String(Base64.getUrlEncoder().encode(encriptar(json)));

        URL urlEnvio = new URL(URL_PEDAGIOS + "leituras/" + dados);

        HttpURLConnection conexao = (HttpURLConnection) urlEnvio.openConnection();
        conexao.setRequestMethod("POST");
        if (conexao.getResponseCode() != 200) {
            resultado = Resultado.ERRO;

            throw new Exception("erro de conexão com o servidor");
        }
        conexao.disconnect();

        return resultado;
    }

    // O(1): requisicao HTTP unica com o resultado de trios encriptado no caminho da URL.
    @SuppressWarnings("deprecation")
    @Override
    public Resultado enviarResultadoTrios(int totalTrios) throws Exception {
        Resultado resultado = Resultado.SUCESSO;

        String json = "{\"id\":\"" + pedagio.getIdentificacao() + "\",\"trios\":" + totalTrios + "}";
        String dados = new String(Base64.getUrlEncoder().encode(encriptar(json)));

        URL urlEnvio = URI.create(URL_PEDAGIOS + "trios/" + dados).toURL();

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
            int totalTrios = processarTriosLocalmente(ALVO_SOMA_TRIOS);
            System.out.println("trios encontrados no cliente: " + totalTrios);
            enviarResultadoTrios(totalTrios);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
