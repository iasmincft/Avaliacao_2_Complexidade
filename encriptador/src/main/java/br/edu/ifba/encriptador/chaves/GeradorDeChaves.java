package br.edu.ifba.encriptador.chaves;

import java.security.KeyPair;
import java.security.SecureRandom;

import br.edu.ifba.encriptador.excecoes.FalhaGeracaoDeChaves;

public interface GeradorDeChaves<T extends SecureRandom> {

  void inicializar(T gerador, String algoritmo);

  KeyPair gerarChaves() throws FalhaGeracaoDeChaves;

  KeyPair gerarChaves(
      String caminhoPrivada,
      String caminhoPublica) throws FalhaGeracaoDeChaves;

  void finalizar() throws FalhaGeracaoDeChaves;
}