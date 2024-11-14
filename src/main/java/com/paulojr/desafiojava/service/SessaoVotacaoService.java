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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
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

    @Cacheable(value = "sessaoVotacao", key = "#id") // Cachea o resultado de findById
    public SessaoVotacaoDTO findById(Long id) throws NotFoundException {
        SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.findById(id).orElseThrow(() -> new NotFoundException("Sessão de Votação não encontrada."));
        return sessaoVotacaoMapper.toDTO(sessaoVotacao);
    }

    @Cacheable(value = "resultadoSessaoVotacao", key = "#id") // Cachea o resultado da sessão de votação
    public ResultadoSessaoVotacaoDTO buscarResultadoSessaoVotacao(Long id) throws NotFoundException {
        SessaoVotacaoDTO sessaoVotacaoDTO = findById(id);
        List<Voto> votos = findAllBySessaoVotacaoId(id);

        return new ResultadoSessaoVotacaoDTO().builder()
                .id(id)
                .pauta(sessaoVotacaoDTO.getPauta())
                .totalVotos(votos.stream().filter(voto -> !voto.isEhVotoAprovativo()).count())
                .qtdVotosAprovativos(votos.stream().filter(voto -> voto.isEhVotoAprovativo()).count())
                .qtdVotosRejeitivos(votos.stream().filter(voto -> voto.isEhVotosRejeitivos()).count())
                .build();
    }

    @CacheEvict(value = "sessaoVotacao", key = "#id") // Remove o cache de sessão de votação ao deletar
    public void deleteById(Long id) {
        sessaoVotacaoRepository.deleteById(id);
    }

    @CacheEvict(value = "sessaoVotacao", key = "#id") // Limpa o cache se a sessão de votação for atualizada
    public MessageResponseDTO update(SessaoVotacaoDTO sessaoVotacaoDTO) {
        SessaoVotacao sessaoVotacaoToUpdate = sessaoVotacaoMapper.toModel(sessaoVotacaoDTO);
        SessaoVotacao sessaoVotacaoUpdated = sessaoVotacaoRepository.save(sessaoVotacaoToUpdate);

        return MessageResponseDTO.builder()
                .message("Sessão de Votação atualizada com ID:" + sessaoVotacaoUpdated.getId())
                .build();
    }

    private Integer decideTempoDeAberturaEmSegundos(Integer tempoDeAberturaEmSegundos) {
        return (tempoDeAberturaEmSegundos == null || tempoDeAberturaEmSegundos <= 0) ? 60 : tempoDeAberturaEmSegundos;
    }

    private List<Voto> findAllBySessaoVotacaoId(Long idSessao) {
        return votoRepository.findAllBySessaoVotacao_Id(idSessao);
    }
}
