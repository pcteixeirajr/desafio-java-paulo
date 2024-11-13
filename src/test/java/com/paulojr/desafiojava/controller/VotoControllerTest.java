package com.paulojr.desafiojava.controller;

import com.paulojr.desafiojava.dto.ComandoAdicionarVotoDTO;
import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.exceptions.CPFInvalidoException;
import com.paulojr.desafiojava.exceptions.GenericException;
import com.paulojr.desafiojava.exceptions.SessaoExpiradaException;
import com.paulojr.desafiojava.exceptions.VotoExistenteException;
import com.paulojr.desafiojava.service.VotoService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VotoControllerTest {

    @InjectMocks
    private VotoController votoController;

    @Mock
    private VotoService votoService;

    private ComandoAdicionarVotoDTO comandoAdicionarVotoDTO;
    private MessageResponseDTO messageResponseDTO;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

        comandoAdicionarVotoDTO = new ComandoAdicionarVotoDTO();
        comandoAdicionarVotoDTO.setCpf("797.721.267-61");
        comandoAdicionarVotoDTO.setPautaId(1L);
        comandoAdicionarVotoDTO.setVoto(true);

        messageResponseDTO = new MessageResponseDTO();
        messageResponseDTO.setMessage("Voto adicionado com sucesso");
    }

    @Test
    void testAdicionarVoto_Success() throws SessaoExpiradaException, VotoExistenteException, CPFInvalidoException, GenericException, NotFoundException {

        when(votoService.create(comandoAdicionarVotoDTO)).thenReturn(messageResponseDTO);

        ResponseEntity<MessageResponseDTO> response = votoController.adicionarVoto(comandoAdicionarVotoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Voto adicionado com sucesso", response.getBody().getMessage());
    }

    @Test
    void testAdicionarVoto_Failure_SessaoExpirada() throws SessaoExpiradaException, VotoExistenteException, CPFInvalidoException, GenericException, NotFoundException {

        when(votoService.create(comandoAdicionarVotoDTO)).thenThrow(new SessaoExpiradaException(5L));

        SessaoExpiradaException exception = assertThrows(SessaoExpiradaException.class, () -> {
            votoController.adicionarVoto(comandoAdicionarVotoDTO);
        });

        assertEquals("Sessão expirada", exception.getMessage());
    }

    @Test
    void testAdicionarVoto_Failure_VotoExistente() throws SessaoExpiradaException, VotoExistenteException, CPFInvalidoException, GenericException, NotFoundException {

        when(votoService.create(comandoAdicionarVotoDTO)).thenThrow(new VotoExistenteException("123.456.789-00", 3L));

        VotoExistenteException exception = assertThrows(VotoExistenteException.class, () -> {
            votoController.adicionarVoto(comandoAdicionarVotoDTO);
        });

        assertEquals("Voto já registrado", exception.getMessage());
    }

    @Test
    void testAdicionarVoto_Failure_CPFInvalido() throws SessaoExpiradaException, VotoExistenteException, CPFInvalidoException, GenericException, NotFoundException {

        when(votoService.create(comandoAdicionarVotoDTO)).thenThrow(new CPFInvalidoException("CPF inválido"));

        CPFInvalidoException exception = assertThrows(CPFInvalidoException.class, () -> {
            votoController.adicionarVoto(comandoAdicionarVotoDTO);
        });

        assertEquals("CPF inválido", exception.getMessage());
    }

    @Test
    void testAdicionarVoto_Failure_GenericException() throws SessaoExpiradaException, VotoExistenteException, CPFInvalidoException, GenericException, NotFoundException {

        when(votoService.create(comandoAdicionarVotoDTO)).thenThrow(new GenericException("Erro genérico"));

        GenericException exception = assertThrows(GenericException.class, () -> {
            votoController.adicionarVoto(comandoAdicionarVotoDTO);
        });

        assertEquals("Erro genérico", exception.getMessage());
    }

    @Test
    void testAdicionarVoto_Failure_NotFoundException() throws SessaoExpiradaException, VotoExistenteException, CPFInvalidoException, GenericException, NotFoundException {

        when(votoService.create(comandoAdicionarVotoDTO)).thenThrow(new NotFoundException("Pauta não encontrada"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            votoController.adicionarVoto(comandoAdicionarVotoDTO);
        });

        assertEquals("Pauta não encontrada", exception.getMessage());
    }
}
