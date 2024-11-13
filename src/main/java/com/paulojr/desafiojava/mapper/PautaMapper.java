package com.paulojr.desafiojava.mapper;

import com.paulojr.desafiojava.dto.PautaDTO;
import com.paulojr.desafiojava.entity.Pauta;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PautaMapper {
    PautaMapper INSTANCE = Mappers.getMapper(PautaMapper.class);

    Pauta toModel(PautaDTO pautaDTO);

    PautaDTO toDTO(Pauta pauta);
}
