package com.paulojr.desafiojava.service;

import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.dto.PautaDTO;
import com.paulojr.desafiojava.entity.Pauta;
import com.paulojr.desafiojava.mapper.PautaMapper;
import com.paulojr.desafiojava.repository.PautaRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class PautaService {
    private PautaRepository pautaRepository;
    private static final PautaMapper pautaMapper = PautaMapper.INSTANCE;

    @Autowired
    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public MessageResponseDTO create(PautaDTO pautaDTO) {
        Pauta pautaToCreate = pautaMapper.toModel(pautaDTO);
        Pauta pautaCreated = pautaRepository.save(pautaToCreate);

        return MessageResponseDTO.builder()
                .message("Pauta " + pautaCreated.getNome() + " foi adicionada à base de dados com o ID:" + pautaCreated.getId())
                .build();
    }

    @Cacheable(value = "pautas", key = "#id")
    public PautaDTO findById(Long id) throws NotFoundException {
        Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new NotFoundException("Pauta não encontrada."));
        return pautaMapper.toDTO(pauta);
    }

    @CacheEvict(value = "pautas", key = "#id")
    public void deleteById(Long id) {
        pautaRepository.deleteById(id);
    }

    @CacheEvict(value = "pautas", key = "#pautaDTO.id")
    public MessageResponseDTO update(PautaDTO pautaDTO) {
        Pauta pautaToUpdate = pautaMapper.toModel(pautaDTO);
        Pauta pautaUpdated = pautaRepository.save(pautaToUpdate);

        return MessageResponseDTO.builder()
                .message("Pauta " + pautaUpdated.getNome() + " foi atualizada com o ID:" + pautaUpdated.getId())
                .build();
    }
}
