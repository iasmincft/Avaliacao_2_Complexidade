package br.edu.ifba.pedagio.cliente.sensoriamento;

import java.util.List;

public interface Sensoriamento<TipoDado> {
    public List<TipoDado> gerar(int totalCarros);
}

