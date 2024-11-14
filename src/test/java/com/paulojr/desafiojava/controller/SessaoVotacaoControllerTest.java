package com.paulojr.desafiojava.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulojr.desafiojava.dto.ComandoAbrirSessaoVotacaoDTO;
import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.dto.ResultadoSessaoVotacaoDTO;
import com.paulojr.desafiojava.service.SessaoVotacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessaoVotacaoController.class)
public class SessaoVotacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessaoVotacaoService sessaoVotacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ComandoAbrirSessaoVotacaoDTO comandoAbrirSessaoVotacaoDTO;
    private MessageResponseDTO messageResponseDTO;
    private ResultadoSessaoVotacaoDTO resultadoSessaoVotacaoDTO;

    @BeforeEach
    public void setup() {

        comandoAbrirSessaoVotacaoDTO = new ComandoAbrirSessaoVotacaoDTO();
        comandoAbrirSessaoVotacaoDTO.setPautaId(1L);
        comandoAbrirSessaoVotacaoDTO.setDuracao(60);

        messageResponseDTO = new MessageResponseDTO();
        messageResponseDTO.setMessage("Sessão de votação aberta com sucesso.");

        resultadoSessaoVotacaoDTO = new ResultadoSessaoVotacaoDTO();
        resultadoSessaoVotacaoDTO.setSessaoId(1L);
        resultadoSessaoVotacaoDTO.setTotalVotos(10L);
        resultadoSessaoVotacaoDTO.setVotosFavoraveis(7L);
        resultadoSessaoVotacaoDTO.setVotosContra(3L);
    }

    @Test
    public void testAbrirSessao() throws Exception {

        when(sessaoVotacaoService.create(any(ComandoAbrirSessaoVotacaoDTO.class))).thenReturn(messageResponseDTO);

        mockMvc.perform(post("/api/v1/sessao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comandoAbrirSessaoVotacaoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Sessão de votação aberta com sucesso."));
    }

    @Test
    public void testBuscarResultadoSessaoVotacao() throws Exception {

        when(sessaoVotacaoService.buscarResultadoSessaoVotacao(anyLong())).thenReturn(resultadoSessaoVotacaoDTO);

        mockMvc.perform(get("/api/v1/sessao/1/resultado")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessaoId").value(1L))
                .andExpect(jsonPath("$.totalVotos").value(10))
                .andExpect(jsonPath("$.votosFavoraveis").value(7))
                .andExpect(jsonPath("$.votosContra").value(3));
    }
}
