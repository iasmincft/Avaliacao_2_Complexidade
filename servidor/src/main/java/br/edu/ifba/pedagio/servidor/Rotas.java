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

    private static final int LIMIAR_OSCILACOES = 25;

    private static Operacoes<Pedagio, Contagem> operacoes = null;

    private static Operacoes<Pedagio, Contagem> getOperacoes() {
        if (operacoes == null) {
            operacoes = new OperacoesImpl();
        }

        return operacoes;
    }

    private static final String INFORMACOES = "serviço de atendimento a pedagios, v1.0";

    @GET
    @Path("/")
    public Response getInformacoes() {
        return Response.ok(INFORMACOES, MediaType.TEXT_PLAIN).build();
    }

    @POST
    @Path("{id}/{total}")
    public Response gravarLeitura(@PathParam("id") String idPedagio, @PathParam("total") int total) {
        Pedagio pedagio = new Pedagio(idPedagio, "único");
        Contagem contagem = new Contagem(total);

        getOperacoes().gravar(pedagio, contagem);

        return Response.ok().build();
    }

    @GET
    @Path("/oscilacoes")
    public Response detectarOscilacoes() {
        int oscilacoes = getOperacoes().detectarAltasOscilacoes(LIMIAR_OSCILACOES);

        return Response.ok(oscilacoes + "", MediaType.TEXT_PLAIN).build();
    }

}
