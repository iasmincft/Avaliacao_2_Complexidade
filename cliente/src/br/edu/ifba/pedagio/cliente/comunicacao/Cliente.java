package br.edu.ifba.pedagio.cliente.comunicacao;

import br.edu.ifba.pedagio.cliente.sensoriamento.Sensoriamento;

public interface Cliente<Monitorado, Leitura> {

    public void configurar(Monitorado monitorado, Sensoriamento<Leitura> sensoriamento);

    public Resultado enviar(Leitura leitura) throws Exception;

}

