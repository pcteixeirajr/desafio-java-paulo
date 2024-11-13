package com.paulojr.desafiojava.controller;

import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.dto.PautaDTO;
import com.paulojr.desafiojava.exceptions.ErrorResponse;
import com.paulojr.desafiojava.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/v1/pauta")
public class PautaController {

    private final PautaService pautaService;

    @Autowired
    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @Operation(summary = "Cadastrar pauta", description = "Responsável por cadastrar uma nova pauta", security = {

            @SecurityRequirement(name = "bearer")}, tags = {"Pauta"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})

    @PostMapping
    public ResponseEntity<MessageResponseDTO> cadastrarPauta(
            @Parameter(in = ParameterIn.DEFAULT, description = "Informações da pauta", required = true, schema = @Schema(implementation = PautaDTO.class))
            @Valid @RequestBody PautaDTO pautaDTO) {
        MessageResponseDTO response = pautaService.create(pautaDTO);
        return ResponseEntity.ok(response);
    }
}


