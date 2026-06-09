package br.edu.ifba.encriptador.impl;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;

import javax.crypto.Cipher;

import br.edu.ifba.encriptador.encriptacao.Encriptador;
import br.edu.ifba.encriptador.excecoes.FalhaEncriptacao;

public class EncriptadorImpl extends Encriptador {

  public EncriptadorImpl(
      KeyPair chaves,
      String algoritmo) {
    super(chaves, algoritmo);
  }

  @Override
  public String encriptar(String dados)
      throws FalhaEncriptacao {

    try {

      Cipher cipher = Cipher.getInstance(algoritmo);

      cipher.init(
          Cipher.ENCRYPT_MODE,
          chaves.getPublic());

      byte[] bytes = cipher.doFinal(
          dados.getBytes(
              StandardCharsets.UTF_8));

      return Base64
          .getEncoder()
          .encodeToString(bytes);

    } catch (Exception e) {

      throw new FalhaEncriptacao(
          "erro ao encriptar: "
              + e.getMessage());
    }
  }

  @Override
  public String desencriptar(String dados)
      throws FalhaEncriptacao {

    try {

      Cipher cipher = Cipher.getInstance(algoritmo);

      cipher.init(
          Cipher.DECRYPT_MODE,
          chaves.getPrivate());

      byte[] bytes = Base64.getDecoder()
          .decode(dados);

      byte[] resultado = cipher.doFinal(bytes);

      return new String(
          resultado,
          StandardCharsets.UTF_8);

    } catch (Exception e) {

      throw new FalhaEncriptacao(
          "erro ao desencriptar: "
              + e.getMessage());
    }
  }
}