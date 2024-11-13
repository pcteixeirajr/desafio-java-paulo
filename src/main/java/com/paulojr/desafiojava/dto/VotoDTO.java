package com.paulojr.desafiojava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotoDTO {
    private Long id;
    private SessaoVotacaoDTO sessaoVotacao;
    private String associado;
    private boolean ehVotoAprovativo;
}
