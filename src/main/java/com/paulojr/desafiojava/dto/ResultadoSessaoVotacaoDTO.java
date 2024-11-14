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

    private Long id; // ID da sessão de votação
    private PautaDTO pauta; // A pauta associada à sessão
    private Long qtdVotosAprovativos; // Quantidade de votos favoráveis (Aprovativos)
    private Long qtdVotosRejeitivos; // Quantidade de votos contrários (Rejeitivos)
    private Long totalVotos; // Total de votos registrados
    private String resultadoFinal; // Resultado final da votação (Por exemplo: "Aprovado", "Rejeitado")

    /**
     * Método que calcula os votos favoráveis e contrários com base nos votos da sessão.
     * @param listaVotos Lista de votos da sessão
     */
    public void calcularVotos(List<VotoDTO> listaVotos) {
        // Inicializando os contadores de votos favoráveis e contrários
        this.qtdVotosAprovativos = 0L;
        this.qtdVotosRejeitivos = 0L;

        // Contando os votos
        for (VotoDTO voto : listaVotos) {
            if (voto.isEhVotoAprovativo()) {
                this.qtdVotosAprovativos++;
            } else {
                this.qtdVotosRejeitivos++;
            }
        }

        // Calculando o total de votos
        this.totalVotos = this.qtdVotosAprovativos + this.qtdVotosRejeitivos;

        // Definindo o resultado final
        this.resultadoFinal = this.qtdVotosAprovativos > this.qtdVotosRejeitivos ? "Aprovado" : "Rejeitado";
    }
}
