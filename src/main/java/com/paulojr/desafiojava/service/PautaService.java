package com.paulojr.desafiojava.service;

import com.paulojr.desafiojava.dto.MessageResponseDTO;
import com.paulojr.desafiojava.dto.PautaDTO;
import com.paulojr.desafiojava.entity.Pauta;
import com.paulojr.desafiojava.mapper.PautaMapper;
import com.paulojr.desafiojava.repository.PautaRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public PautaDTO findById(Long id) throws NotFoundException {
        Pauta pauta = pautaRepository.findById(id).orElseThrow(() -> new NotFoundException("Pauta não encontrada."));
        return pautaMapper.toDTO(pauta);
    }
}
