package br.edu.ifba.encriptador.encriptacao;

import java.security.KeyPair;

import br.edu.ifba.encriptador.excecoes.FalhaEncriptacao;

public abstract class Encriptador {

  protected KeyPair chaves;
  protected String algoritmo;

  public Encriptador(KeyPair chaves, String algoritmo) {
    this.chaves = chaves;
    this.algoritmo = algoritmo;
  }

  public abstract String encriptar(String dados)
      throws FalhaEncriptacao;

  public abstract String desencriptar(String dados)
      throws FalhaEncriptacao;
}