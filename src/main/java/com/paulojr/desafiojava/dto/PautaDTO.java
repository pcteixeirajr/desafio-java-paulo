package com.paulojr.desafiojava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PautaDTO {
    private Long id;
    private String nome;
    private String titulo;
    private String descricao;
}
