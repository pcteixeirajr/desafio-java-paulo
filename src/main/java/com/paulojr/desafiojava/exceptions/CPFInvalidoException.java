package com.paulojr.desafiojava.exceptions;

public class CPFInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;
    private final String cpf;
    private final String errorCode;

    public CPFInvalidoException(String cpf) {
        super("O CPF " + cpf + " é inválido.");
        this.cpf = cpf;
        this.errorCode = "CPF_INVALIDO";
    }

    public CPFInvalidoException(String cpf, Throwable cause) {
        super("O CPF " + cpf + " é inválido.", cause);
        this.cpf = cpf;
        this.errorCode = "CPF_INVALIDO"; // Código de erro customizado
    }

    public String getCpf() {
        return cpf;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return "O CPF " + cpf + " é inválido. Por favor, verifique o número e tente novamente.";
    }

    public String getFullErrorDetails() {
        return "Código de erro: " + errorCode + "\nMensagem: " + getMessage() + "\nCPF inválido: " + cpf;
    }

    @Override
    public String toString() {
        return "CPFInvalidoException{cpf='" + cpf + "', errorCode='" + errorCode + "'}";
    }
}
