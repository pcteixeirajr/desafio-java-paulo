package com.paulojr.desafiojava.service;

import com.paulojr.desafiojava.dto.ComandoAbrirSessaoVotacaoDTO;
import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.dto.ResultadoSessaoVotacaoDTO;
import com.paulojr.desafiojava.dto.SessaoVotacaoDTO;
import com.paulojr.desafiojava.entity.SessaoVotacao;
import com.paulojr.desafiojava.entity.Voto;
import com.paulojr.desafiojava.mapper.SessaoVotacaoMapper;
import com.paulojr.desafiojava.repository.SessaoVotacaoRepository;
import com.paulojr.desafiojava.repository.VotoRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SessaoVotacaoService {
    private VotoRepository votoRepository;
    private SessaoVotacaoRepository sessaoVotacaoRepository;
    private static final SessaoVotacaoMapper sessaoVotacaoMapper = SessaoVotacaoMapper.INSTANCE;
    private PautaService pautaService;

    @Autowired
    public SessaoVotacaoService(SessaoVotacaoRepository sessaoVotacaoRepository, VotoRepository votoRepository, PautaService pautaService) {
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.votoRepository = votoRepository;
        this.pautaService = pautaService;
    }

    public MessageResponseDTO create(ComandoAbrirSessaoVotacaoDTO comandoAbrirSessaoVotacaoDTO) throws NotFoundException {

        SessaoVotacaoDTO sessaoVotacaoDTO = new SessaoVotacaoDTO().builder()
                .pauta(pautaService.findById(comandoAbrirSessaoVotacaoDTO.getPauta()))
                .tempoDeAberturaEmSegundos(decideTempoDeAberturaEmSegundos(comandoAbrirSessaoVotacaoDTO.getTempoDeAberturaEmSegundos()))
                .dataHoraAbertura(new Date())
                .build();

        SessaoVotacao sessaoVotacaoToCreate = sessaoVotacaoMapper.toModel(sessaoVotacaoDTO);
        sessaoVotacaoRepository.save(sessaoVotacaoToCreate);

        return MessageResponseDTO.builder()
                .message("Sessão de Votação criada")
                .build();
    }

    public SessaoVotacaoDTO findById(Long id) throws NotFoundException {
        SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.findById(id).orElseThrow(() -> new NotFoundException("Sessão de Votação não encontrada."));
        return sessaoVotacaoMapper.toDTO(sessaoVotacao);
    }

    public ResultadoSessaoVotacaoDTO buscarResultadoSessaoVotacao(Long id) throws NotFoundException {
        SessaoVotacaoDTO sessaoVotacaoDTO = findById(id);
        List<Voto> votos = findAllBySessaoVotacaoId(id);

        return new ResultadoSessaoVotacaoDTO().builder()
                .id(id)
                .pauta(sessaoVotacaoDTO.getPauta())
                .votosContra(votos.stream().filter(voto -> !voto.isEhVotoAprovativo()).count())
                .votosFavoraveis(votos.stream().filter(voto -> voto.isEhVotoAprovativo()).count())
                .build();
    }

    private Integer decideTempoDeAberturaEmSegundos(Integer tempoDeAberturaEmSegundos) {
        return (tempoDeAberturaEmSegundos == null || tempoDeAberturaEmSegundos <= 0) ? 60 : tempoDeAberturaEmSegundos;
    }

    private List<Voto> findAllBySessaoVotacaoId(Long idSessao) {
        return votoRepository.findAllBySessaoVotacao_Id(idSessao);
    }
}
