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

    private static Operacoes<Pedagio, Contagem> operacoes = null;

    private static Operacoes<Pedagio, Contagem> getOperacoes() {
        if (operacoes == null) {
            operacoes = new OperacoesImpl();
        }

        return operacoes;
    }

    private static final String INFORMACOES = "serviço de atendimento a pedagios, v1.0";

    // O(1)
    @GET
    @Path("/")
    public Response getInformacoes() {
        return Response.ok(INFORMACOES, MediaType.TEXT_PLAIN).build();
    }

    // O(log N) para operações TreeMap e operações de fila.
    @POST
    @Path("{id}/contagem/{total}")
    public Response gravarLeitura(@PathParam("id") String idPedagio, @PathParam("total") int total) {
        Pedagio pedagio = new Pedagio(idPedagio, "único");
        Contagem contagem = new Contagem(total);

        getOperacoes().gravar(pedagio, contagem);

        return Response.ok().build();
    }

    // O(log N) para operações TreeMap.
    @POST
    @Path("{id}/trios/{trios}")
    public Response gravarResultadoTrios(@PathParam("id") String idPedagio, @PathParam("trios") int trios) {
        Pedagio pedagio = new Pedagio(idPedagio, "único");
        getOperacoes().gravar(pedagio, trios);

        return Response.ok().build();
    }

    // O(M) para iterar sobre os pedágios com resultados.
    @GET
    @Path("/resultado-trios")
    public Response obterResultadosTrios() {
        String resultados = ((OperacoesImpl) getOperacoes()).obterResultadosTrios();
        return Response.ok(resultados, MediaType.TEXT_PLAIN).build();
    }

}
