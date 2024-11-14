package com.paulojr.desafiojava.controller;

import com.paulojr.desafiojava.dto.ComandoAbrirSessaoVotacaoDTO;
import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.dto.ResultadoSessaoVotacaoDTO;
import com.paulojr.desafiojava.exceptions.ErrorResponse;
import com.paulojr.desafiojava.service.SessaoVotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/sessao")
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoVotacaoService;

    @Autowired
    public SessaoVotacaoController(SessaoVotacaoService sessaoVotacaoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @Operation(summary = "Abrir sessão de votação", description = "Responsável por abrir uma nova sessão de votação", security = {
            @SecurityRequirement(name = "bearer")}, tags = {"SessaoVotacao"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseEntity<MessageResponseDTO> abrirSessao(
            @Parameter(in = ParameterIn.DEFAULT, description = "Informações para abrir sessão de votação", required = true, schema = @Schema(implementation = ComandoAbrirSessaoVotacaoDTO.class))
            @Valid @RequestBody ComandoAbrirSessaoVotacaoDTO comandoAbrirSessaoVotacaoDTO) throws NotFoundException {
        MessageResponseDTO response = sessaoVotacaoService.create(comandoAbrirSessaoVotacaoDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar resultado da sessão de votação", description = "Responsável por buscar o resultado de uma sessão de votação específica", security = {
            @SecurityRequirement(name = "bearer")}, tags = {"SessaoVotacao"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResultadoSessaoVotacaoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping("/{id}/resultado")
    public ResponseEntity<ResultadoSessaoVotacaoDTO> buscarResultadoSessaoVotacao(
            @Parameter(description = "ID da sessão de votação", required = true)
            @PathVariable Long id) throws NotFoundException {
        ResultadoSessaoVotacaoDTO resultado = sessaoVotacaoService.buscarResultadoSessaoVotacao(id);
        return ResponseEntity.ok(resultado);
    }
}
