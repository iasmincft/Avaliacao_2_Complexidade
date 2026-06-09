package br.edu.ifba.encriptador.impl;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import br.edu.ifba.encriptador.aleatoriedade.GeradorDeAleatoriedadeReal;
import br.edu.ifba.encriptador.chaves.GeradorDeChaves;
import br.edu.ifba.encriptador.excecoes.FalhaGeracaoDeChaves;

public class GeradorDeChavesImpl
    implements GeradorDeChaves<GeradorDeAleatoriedadeReal> {

  private GeradorDeAleatoriedadeReal gerador;
  private String algoritmo;

  @Override
  public void inicializar(
      GeradorDeAleatoriedadeReal gerador,
      String algoritmo) {

    this.gerador = gerador;
    this.algoritmo = algoritmo;
  }

  @Override
  public KeyPair gerarChaves()
      throws FalhaGeracaoDeChaves {

    try {

      KeyPairGenerator generator = KeyPairGenerator.getInstance(
          algoritmo);

      generator.initialize(
          2048,
          gerador);

      return generator.generateKeyPair();

    } catch (Exception e) {

      throw new FalhaGeracaoDeChaves(
          e.getMessage());
    }
  }

  @Override
  public KeyPair gerarChaves(
      String privada,
      String publica)
      throws FalhaGeracaoDeChaves {

    KeyPair pair = gerarChaves();

    try {

      FileOutputStream outPublic = new FileOutputStream(publica);

      outPublic.write(
          pair.getPublic().getEncoded());

      outPublic.close();

      FileOutputStream outPrivate = new FileOutputStream(privada);

      outPrivate.write(
          pair.getPrivate().getEncoded());

      outPrivate.close();

    } catch (Exception e) {

      throw new FalhaGeracaoDeChaves(
          e.getMessage());
    }

    return pair;
  }

  @Override
  public void finalizar()
      throws FalhaGeracaoDeChaves {

    gerador.finalizar();
  }
}