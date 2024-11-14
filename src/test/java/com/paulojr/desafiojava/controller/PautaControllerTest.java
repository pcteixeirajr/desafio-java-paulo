package com.paulojr.desafiojava.controller;

import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.dto.PautaDTO;
import com.paulojr.desafiojava.service.PautaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class PautaControllerTest {

    @InjectMocks
    private PautaController pautaController;

    @Mock
    private PautaService pautaService;

    private PautaDTO pautaDTO;
    private MessageResponseDTO messageResponseDTO;

    @BeforeEach
    void setUp() {
        pautaDTO = new PautaDTO();
        pautaDTO.setId(1L);
        pautaDTO.setNome("Maria do Carmo");
        pautaDTO.setTitulo("Nova Pauta");
        pautaDTO.setDescricao("Descrição da pauta");

        messageResponseDTO = new MessageResponseDTO();
        messageResponseDTO.setMessage("Pauta cadastrada com sucesso");
    }

    @Test
    void testCadastrarPauta_Success() {
        when(pautaService.create(pautaDTO)).thenReturn(messageResponseDTO);

        ResponseEntity<MessageResponseDTO> response = pautaController.cadastrarPauta(pautaDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("Pauta cadastrada com sucesso", response.getBody().getMessage());
    }

    @Test
    void testCadastrarPauta_Failure() {
        when(pautaService.create(pautaDTO)).thenThrow(new RuntimeException("Erro ao cadastrar pauta"));

        try {
            pautaController.cadastrarPauta(pautaDTO);
        } catch (Exception e) {
            assertEquals("Erro ao cadastrar pauta", e.getMessage());
        }
    }
}
