package com.paulojr.desafiojava.controller;

import com.paulojr.desafiojava.dto.ComandoAdicionarVotoDTO;
import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.exceptions.CPFInvalidoException;
import com.paulojr.desafiojava.exceptions.GenericException;
import com.paulojr.desafiojava.exceptions.SessaoExpiradaException;
import com.paulojr.desafiojava.exceptions.VotoExistenteException;
import com.paulojr.desafiojava.service.VotoService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/voto")
public class VotoController {

    private final VotoService votoService;

    @Autowired
    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @Operation(
            summary = "Adicionar voto",
            description = "Responsável por adicionar um voto em uma pauta",
            security = @SecurityRequirement(name = "bearer"),
            tags = {"Voto"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto adicionado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<MessageResponseDTO> adicionarVoto(
            @Parameter(in = ParameterIn.DEFAULT, description = "Comando para adicionar voto", required = true, schema = @Schema(implementation = ComandoAdicionarVotoDTO.class))
            @Valid @RequestBody ComandoAdicionarVotoDTO comandoAdicionarVotoDTO
    ) throws SessaoExpiradaException, VotoExistenteException, CPFInvalidoException, GenericException, NotFoundException {

        MessageResponseDTO response = votoService.create(comandoAdicionarVotoDTO);
        return ResponseEntity.ok(response);
    }
}
