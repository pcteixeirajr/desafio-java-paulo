package com.paulojr.desafiojava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComandoAbrirSessaoVotacaoDTO {
    private Long pautaId;
    private Integer duracao;
    private Long pauta;
}
