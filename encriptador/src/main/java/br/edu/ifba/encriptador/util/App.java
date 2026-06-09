package br.edu.ifba.encriptador.util;

import br.edu.ifba.encriptador.aleatoriedade.GeradorDeAleatoriedadeReal;
import br.edu.ifba.encriptador.chaves.GeradorDeChaves;
import br.edu.ifba.encriptador.impl.GeradorDeChavesImpl;

public class App {

  private static final String VIDEO = "src/main/resources/video/RecifeDeCoralComPeixes.mp4";

  private static final String ALGORITMO = "RSA";

  private static final String CHAVE_PUBLICA = "../cliente/chaves/ch_publica.bin";

  private static final String CHAVE_PRIVADA = "../servidor/chaves/ch_privada.bin";

  public static void main(String[] args)
      throws Exception {

    GeradorDeAleatoriedadeReal aleatoriedade = new GeradorDeAleatoriedadeReal(
        VIDEO);

    GeradorDeChaves<GeradorDeAleatoriedadeReal> gerador = new GeradorDeChavesImpl();

    gerador.inicializar(
        aleatoriedade,
        ALGORITMO);

    gerador.gerarChaves(
        CHAVE_PRIVADA,
        CHAVE_PUBLICA);

    gerador.finalizar();

    System.out.println(
        "chaves RSA geradas com sucesso");
  }
}