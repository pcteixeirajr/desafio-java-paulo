package com.paulojr.desafiojava.mapper;

import com.paulojr.desafiojava.dto.SessaoVotacaoDTO;
import com.paulojr.desafiojava.entity.SessaoVotacao;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SessaoVotacaoMapper {
    SessaoVotacaoMapper INSTANCE = Mappers.getMapper(SessaoVotacaoMapper.class);

    SessaoVotacao toModel(SessaoVotacaoDTO sessaoVotacaoDTO);

    SessaoVotacaoDTO toDTO(SessaoVotacao sessaoVotacao);
}
