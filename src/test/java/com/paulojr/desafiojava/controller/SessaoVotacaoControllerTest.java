package com.paulojr.desafiojava.controller;

import com.paulojr.desafiojava.dto.ComandoAbrirSessaoVotacaoDTO;
import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.dto.ResultadoSessaoVotacaoDTO;
import com.paulojr.desafiojava.entity.Pauta;
import com.paulojr.desafiojava.entity.SessaoVotacao;
import com.paulojr.desafiojava.entity.Voto;
import com.paulojr.desafiojava.repository.PautaRepository;
import com.paulojr.desafiojava.repository.SessaoVotacaoRepository;
import com.paulojr.desafiojava.repository.VotoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessaoVotacaoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private SessaoVotacaoRepository sessaoVotacaoRepository;
    @MockBean
    private PautaRepository pautaRepository;
    @MockBean
    private VotoRepository votoRepository;

    @Test
    void abrirSessao() {
        // Criando uma pauta de teste
        Pauta pauta = new Pauta(999L, "Pauta teste");

        // Criando o DTO para abrir a sessão
        ComandoAbrirSessaoVotacaoDTO comandoAbrirSessaoVotacaoDTO = new ComandoAbrirSessaoVotacaoDTO().builder()
                .pauta(pauta.getId()) // Id da pauta
                .tempoDeAberturaEmSegundos(1000) // Tempo de abertura da sessão em segundos
                .build();

        // Criando a Sessao de Votação associada à pauta
        SessaoVotacao sessaoVotacao = new SessaoVotacao().builder()
                .dataHoraAbertura(new Date())
                .pauta(pauta)
                .tempoDeAberturaEmSegundos(1000)
                .build();

        // Definindo o comportamento esperado dos mocks
        BDDMockito.when(pautaRepository.save(pauta)).thenReturn(pauta);
        BDDMockito.when(pautaRepository.findById(pauta.getId())).thenReturn(java.util.Optional.of(pauta));
        BDDMockito.when(sessaoVotacaoRepository.save(sessaoVotacao)).thenReturn(sessaoVotacao);

        // Realizando a requisição POST para abrir a sessão
        ResponseEntity<MessageResponseDTO> response = restTemplate
                .postForEntity("/api/v1/sessao", comandoAbrirSessaoVotacaoDTO, MessageResponseDTO.class);

        // Verificando o status da resposta
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
        Assertions.assertThat(response.getBody().getMessage()).isEqualTo("Sessão de votação aberta com sucesso!");
    }

    @Test
    void buscarResultadoSessaoVotacao() {

        Pauta pauta = new Pauta(999L, "Pauta teste");
        SessaoVotacao sessaoVotacao = new SessaoVotacao().builder()
                .id(1L)
                .dataHoraAbertura(new Date())
                .pauta(pauta)
                .tempoDeAberturaEmSegundos(1000)
                .build();

        Voto voto1 = new Voto().builder()
                .id(1L)
                .associado("05676307307")
                .sessaoVotacao(sessaoVotacao)
                .ehVotoAprovativo(true)
                .build();

        Voto voto2 = new Voto().builder()
                .id(2L)
                .associado("05676307307")
                .sessaoVotacao(sessaoVotacao)
                .ehVotoAprovativo(true)
                .build();

        Voto voto3 = new Voto().builder()
                .id(3L)
                .associado("05676307307")
                .sessaoVotacao(sessaoVotacao)
                .ehVotoAprovativo(false)
                .build();

        List<Voto> listaVotos = new ArrayList<>();
        listaVotos.add(voto1);
        listaVotos.add(voto2);
        listaVotos.add(voto3);

        BDDMockito.when(sessaoVotacaoRepository.findById(sessaoVotacao.getId())).thenReturn(Optional.of(sessaoVotacao));
        BDDMockito.when(votoRepository.findAllBySessaoVotacao_Id(sessaoVotacao.getId())).thenReturn(listaVotos);

        ResponseEntity<ResultadoSessaoVotacaoDTO> response = restTemplate
                .getForEntity("/api/v1/sessao/1/resultado", ResultadoSessaoVotacaoDTO.class);

        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);

        // Verificando se a resposta contém o número correto de votos e resultado
        ResultadoSessaoVotacaoDTO resultado = response.getBody();
        Assertions.assertThat(resultado).isNotNull();
        Assertions.assertThat(resultado.getQtdVotosAprovativos()).isEqualTo(2);
        Assertions.assertThat(resultado.getQtdVotosRejeitivos()).isEqualTo(1);
    }
}
