package br.edu.ifba.pedagio.servidor;

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

    // Valor alvo da busca O(N³)
    private static final int VALOR_ALVO = 100;

    // Operações do servidor
    private static Operacoes operacoes = null;

    // Complexidade O(1)
    private static Operacoes getOperacoes() {

        if (operacoes == null) {

            operacoes = new OperacoesImpl(100);
        }

        return operacoes;
    }

    private static final String INFORMACOES =
        "serviço de atendimento a pedágios v1.0";

    // Endpoint de informações
    @GET
    @Path("/")
    public Response getInformacoes() {

        return Response.ok(
            INFORMACOES,
            MediaType.TEXT_PLAIN
        ).build();
    }

    // Recebe leituras dos pedágios
    @POST
    @Path("{id}/{l1}/{l2}/{l3}")
    public Response gravarLeitura(

        @PathParam("id") int idPedagio,

        @PathParam("l1") int leitura1,

        @PathParam("l2") int leitura2,

        @PathParam("l3") int leitura3
    ) {

        int[] leituras = {
            leitura1,
            leitura2,
            leitura3
        };

        Contagem contagem =
            new Contagem(idPedagio, 3);

        contagem.setLeituras(leituras);

        Pedagio pedagio =
            new Pedagio(idPedagio, 3);

        pedagio.getContagem()
            .setLeituras(leituras);

        // Grava no servidor
        ((OperacoesImpl) getOperacoes())
            .adicionarPedagio(pedagio);

        System.out.println(
            "Leituras recebidas do Pedágio "
            + idPedagio
        );

        return Response.ok().build();
    }

    // Endpoint para imprimir leituras
    @GET
    @Path("leituras")
    public Response imprimirLeituras() {

        getOperacoes().imprimirLeituras(
            "Pedágio ID: "
        );

        return Response.ok(
            "Leituras impressas no servidor.",
            MediaType.TEXT_PLAIN
        ).build();
    }

    // Endpoint para ordenar
    @GET
    @Path("ordenar")
    public Response ordenarLeituras() {

        getOperacoes().ordenarLeituras();

        return Response.ok(
            "Leituras ordenadas.",
            MediaType.TEXT_PLAIN
        ).build();
    }

    // Endpoint da funcionalidade O(N³)
    @GET
    @Path("combinacoes")
    public Response buscarCombinacoes() {

        getOperacoes()
            .buscarTrioCombinacoes(
                VALOR_ALVO
            );

        return Response.ok(
            "Busca de combinações executada.",
            MediaType.TEXT_PLAIN
        ).build();
    }
}