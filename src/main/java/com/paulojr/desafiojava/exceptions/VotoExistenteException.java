package com.paulojr.desafiojava.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VotoExistenteException extends Exception {
    private static final long serialVersionUID = 1L;

    private String cpf;
    private Long idSessao;
    private LocalDateTime dataHoraVoto;
    private String mensagemUsuario;
    private String mensagemTecnica;

    /**
     * Construtor padrão com informações detalhadas.
     *
     * @param cpf          CPF do associado
     * @param idSessao     ID da sessão de votação
     * @param dataHoraVoto Data e hora do voto (opcional, pode ser calculado)
     */
    public VotoExistenteException(String cpf, Long idSessao, LocalDateTime dataHoraVoto) {
        super("O associado " + cpf + " já votou na sessão " + idSessao + ". Apenas um voto é permitido.");
        this.cpf = cpf;
        this.idSessao = idSessao;
        this.dataHoraVoto = dataHoraVoto;
        this.mensagemUsuario = "Você já votou nesta sessão de votação. Apenas um voto por associado é permitido.";
        this.mensagemTecnica = "O CPF " + cpf + " tentou votar mais de uma vez na sessão " + idSessao + " às " + dataHoraVoto;
    }

    /**
     * Construtor com uma mensagem customizada.
     *
     * @param cpf             CPF do associado
     * @param idSessao        ID da sessão de votação
     * @param dataHoraVoto    Data e hora do voto
     * @param mensagemUsuario Mensagem customizada para o usuário
     * @param mensagemTecnica Mensagem detalhada para o log
     */
    public VotoExistenteException(String cpf, Long idSessao, LocalDateTime dataHoraVoto, String mensagemUsuario, String mensagemTecnica) {
        super(mensagemUsuario);
        this.cpf = cpf;
        this.idSessao = idSessao;
        this.dataHoraVoto = dataHoraVoto;
        this.mensagemUsuario = mensagemUsuario;
        this.mensagemTecnica = mensagemTecnica;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String logError() {
        return "Erro técnico: " + this.mensagemTecnica;
    }
}
