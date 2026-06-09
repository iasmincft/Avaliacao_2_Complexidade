package br.edu.ifba.pedagio.servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifba.pedagio.servidor.impl.Contagem;
import br.edu.ifba.pedagio.servidor.impl.OperacoesImpl;
import br.edu.ifba.pedagio.servidor.impl.Pedagio;
import br.edu.ifba.pedagio.servidor.operacoes.Operacoes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("pedagios")
public class Rotas {

    private static Operacoes<Pedagio, Contagem> operacoes = null;

    private static Operacoes<Pedagio, Contagem> getOperacoes() {
        if (operacoes == null) {
            operacoes = new OperacoesImpl();
        }

        return operacoes;
    }

    private static final String INFORMACOES = "serviço de atendimento a pedagios, v1.0";
    private static final String ALGORITMO_DE_ENCRIPTACAO = "RSA";
    private static final String CAMINHO_CHAVE_PRIVADA = "chaves/ch_privada.bin";

    private PrivateKey chave = null;

    // O(1) amortizado: a chave e carregada do disco apenas na primeira chamada.
    private PrivateKey getChavePrivada() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (chave == null) {
            File arquivo = new File(CAMINHO_CHAVE_PRIVADA);
            FileInputStream stream = new FileInputStream(arquivo);
            byte[] bytes = stream.readAllBytes();
            stream.close();

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance(ALGORITMO_DE_ENCRIPTACAO);
            chave = kf.generatePrivate(spec);
        }

        return chave;
    }

    // O(1): a desencriptacao RSA opera sobre um bloco de tamanho fixo da chave.
    private String desencriptar(byte[] encriptado) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidKeySpecException, IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITMO_DE_ENCRIPTACAO);
        cipher.init(Cipher.DECRYPT_MODE, getChavePrivada());

        byte[] desencriptado = cipher.doFinal(encriptado);

        return new String(desencriptado);
    }

    // O(1)
    @GET
    @Path("/")
    public Response getInformacoes() {
        return Response.ok(INFORMACOES, MediaType.TEXT_PLAIN).build();
    }

    // O(log N) para operacoes TreeMap e operacoes de fila, alem de O(1) para desencriptacao RSA.
    @POST
    @Path("/leituras/{dados}")
    public Response gravarLeitura(@PathParam("dados") String dados) {
        Response resposta = Response.serverError().build();

        System.out.println("dados encriptados: " + dados);

        try {
            String json = desencriptar(Base64.getUrlDecoder().decode(dados));

            ObjectMapper mapeador = new ObjectMapper();
            JsonNode dic = mapeador.readTree(json);

            Pedagio pedagio = new Pedagio(dic.get("id").asText(), "único");
            Contagem contagem = new Contagem(dic.get("total").asInt());

            getOperacoes().gravar(pedagio, contagem);

            resposta = Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resposta;
    }

    // O(log N) para operacoes TreeMap, alem de O(1) para desencriptacao RSA.
    @POST
    @Path("/trios/{dados}")
    public Response gravarResultadoTrios(@PathParam("dados") String dados) {
        Response resposta = Response.serverError().build();

        System.out.println("dados encriptados: " + dados);

        try {
            String json = desencriptar(Base64.getUrlDecoder().decode(dados));

            ObjectMapper mapeador = new ObjectMapper();
            JsonNode dic = mapeador.readTree(json);

            Pedagio pedagio = new Pedagio(dic.get("id").asText(), "único");
            int trios = dic.get("trios").asInt();

            getOperacoes().gravar(pedagio, trios);

            resposta = Response.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resposta;
    }

    // O(M) para iterar sobre os pedágios com resultados.
    @GET
    @Path("/resultado-trios")
    public Response obterResultadosTrios() {
        String resultados = ((OperacoesImpl) getOperacoes()).obterResultadosTrios();
        return Response.ok(resultados, MediaType.TEXT_PLAIN).build();
    }

}
