package com.paulojr.desafiojava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoSessaoVotacaoDTO {

    private Long sessaoId;
    private PautaDTO pauta;
    private Long votosFavoraveis;
    private Long votosContra;
    private Long totalVotos;
    private String resultadoFinal;

    /**
     * Método que calcula os votos favoráveis e contrários com base nos votos da sessão.
     * @param listaVotos Lista de votos da sessão
     */
    public void calcularVotos(List<VotoDTO> listaVotos) {

        this.votosFavoraveis = 0L;
        this.votosContra = 0L;

        for (VotoDTO voto : listaVotos) {
            if (voto.isEhVotoAprovativo()) {
                this.votosFavoraveis++;
            } else {
                this.votosContra++;
            }
        }

        this.totalVotos = this.votosFavoraveis + this.votosContra;

        this.resultadoFinal = this.votosFavoraveis > this.votosContra ? "Aprovado" : "Rejeitado";
    }
}
