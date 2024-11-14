package com.paulojr.desafiojava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComandoAdicionarVotoDTO {
    private Long sessaoVotacao;
    private Long pautaId;
    private String associado;
    private String cpf;
    private boolean ehVotoAprovativo;
    private boolean voto;


}
