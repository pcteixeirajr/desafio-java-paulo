package com.paulojr.desafiojava.service;

import com.paulojr.desafiojava.dto.ComandoAdicionarVotoDTO;
import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.dto.VotoDTO;
import com.paulojr.desafiojava.entity.Voto;
import com.paulojr.desafiojava.exceptions.CPFInvalidoException;
import com.paulojr.desafiojava.exceptions.GenericException;
import com.paulojr.desafiojava.exceptions.SessaoExpiradaException;
import com.paulojr.desafiojava.exceptions.VotoExistenteException;
import com.paulojr.desafiojava.external.integrations.UsuarioExternoService;
import com.paulojr.desafiojava.mapper.VotoMapper;
import com.paulojr.desafiojava.repository.VotoRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VotoService {
    private VotoRepository votoRepository;
    private SessaoVotacaoService sessaoVotacaoService;
    private UsuarioExternoService usuarioExternoService;
    private static final VotoMapper votoMapper = VotoMapper.INSTANCE;

    @Autowired
    public VotoService(VotoRepository votoRepository, SessaoVotacaoService sessaoVotacaoService, UsuarioExternoService usuarioExternoService) {
        this.votoRepository = votoRepository;
        this.sessaoVotacaoService = sessaoVotacaoService;
        this.usuarioExternoService = usuarioExternoService;
    }

    public MessageResponseDTO create(ComandoAdicionarVotoDTO comandoAdicionarVotoDTO) throws NotFoundException, SessaoExpiradaException, VotoExistenteException, CPFInvalidoException, GenericException {
        VotoDTO votoDTO = new VotoDTO().builder()
                .ehVotoAprovativo(comandoAdicionarVotoDTO.isEhVotoAprovativo())
                .associado(comandoAdicionarVotoDTO.getAssociado())
                .sessaoVotacao(sessaoVotacaoService.findById(comandoAdicionarVotoDTO.getSessaoVotacao()))
                .build();

        if (usuarioExternoService.podeVotar(votoDTO.getAssociado())) {
            validaSeJaVotou(votoDTO);
            validaStatusSessao(votoDTO);

            Voto votoToCreate = votoMapper.toModel(votoDTO);
            Voto votoCreated = votoRepository.save(votoToCreate);

            // Evitar cache de criação, pois ele não é necessário para consultas futuras.
            return MessageResponseDTO.builder()
                    .message("Voto adicionado")
                    .build();
        } else {
            throw new GenericException("Usuário não pode votar.");
        }
    }

    @Cacheable(value = "voto", key = "#cpf + '-' + #sessaoVotacaoId") // Cachea a consulta do voto por CPF e ID da Sessão
    public VotoDTO findBySessaoVotacaoIdAndAssociado(String cpf, Long sessaoVotacaoId) {
        Voto voto = votoRepository.findBySessaoVotacao_IdAndAssociado(sessaoVotacaoId, cpf);
        return votoMapper.toDTO(voto);
    }

    private void validaStatusSessao(VotoDTO votoDTO) throws SessaoExpiradaException {
        Date deadlineParaVotacao = votoDTO.getSessaoVotacao().getDataHoraAbertura();
        deadlineParaVotacao.setTime(deadlineParaVotacao.getTime() + (votoDTO.getSessaoVotacao().getTempoDeAberturaEmSegundos() * 1000));

        if (deadlineParaVotacao.before(new Date())) {
            throw new SessaoExpiradaException(votoDTO.getSessaoVotacao().getId());
        }
    }

    private void validaSeJaVotou(VotoDTO votoDTO) throws VotoExistenteException {
        VotoDTO votoComputado = findBySessaoVotacaoIdAndAssociado(votoDTO.getAssociado(), votoDTO.getSessaoVotacao().getId());

        if (votoComputado != null && votoComputado.getId() != null) {
            throw new VotoExistenteException(votoDTO.getAssociado(), votoDTO.getSessaoVotacao().getId());
        }
    }

    // Método para limpar o cache quando o voto for removido
    @CacheEvict(value = "voto", key = "#cpf + '-' + #sessaoVotacaoId")
    public void deleteVoto(String cpf, Long sessaoVotacaoId) {
        votoRepository.deleteBySessaoVotacao_IdAndAssociado(sessaoVotacaoId, cpf);
    }
}
