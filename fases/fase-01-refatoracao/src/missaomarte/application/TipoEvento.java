package missaomarte.application;

/** Tipos de evento que um turno pode gerar. */
public enum TipoEvento {
    MOVIMENTO,
    EMBARQUE,
    EMBARQUE_RECUSADO_SEM_PASSAGEIRO,
    EMBARQUE_RECUSADO_NAVE_CHEIA,
    ENTREGA,
    COLISAO,
    VITORIA,
    DERROTA_NAVE_DESTRUIDA,
    DERROTA_PONTUACAO_ZERADA
}
