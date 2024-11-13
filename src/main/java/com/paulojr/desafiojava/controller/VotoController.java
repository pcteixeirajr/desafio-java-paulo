package com.paulojr.desafiojava.controller;

import com.paulojr.desafiojava.dto.ComandoAdicionarVotoDTO;
import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.exceptions.CPFInvalidoException;
import com.paulojr.desafiojava.exceptions.GenericException;
import com.paulojr.desafiojava.exceptions.SessaoExpiradaException;
import com.paulojr.desafiojava.exceptions.VotoExistenteException;
import com.paulojr.desafiojava.service.VotoService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/voto")
public class VotoController {

    private VotoService votoService;

    @Autowired
    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    // Adicionar Voto
    @PostMapping
    public MessageResponseDTO adicionarVoto(@RequestBody ComandoAdicionarVotoDTO comandoAdicionarVotoDTO) throws NotFoundException, SessaoExpiradaException, VotoExistenteException, CPFInvalidoException, GenericException {
        return votoService.create(comandoAdicionarVotoDTO);
    }
}
