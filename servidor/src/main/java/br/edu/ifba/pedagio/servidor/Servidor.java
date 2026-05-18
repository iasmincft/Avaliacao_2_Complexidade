package br.edu.ifba.pedagio.servidor;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Servidor {
    private static final String BASE_URL = "http://localhost:8080/";

    private static HttpServer iniciarServidor() {
        ResourceConfig configuracao = new ResourceConfg().packages("br.edu.ifba.pedagio.servidor"):

        HttpServer servidor = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URL), configuracao);

        return servidor;
    }
    public static void main(String[] args) 
        throws IOException {
        HttpServer servidor = iniciarServidor();
        System.out.println("atendendo pedágios...");
        System.in.read();
        servidor.shutdown();
    }
    
}