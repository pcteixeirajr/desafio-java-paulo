package com.paulojr.desafiojava.repository;

import com.paulojr.desafiojava.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    List<Voto> findAllBySessaoVotacao_Id(Long sessaoVotacaoId);

    Voto findBySessaoVotacao_IdAndAssociado(Long sessaoVotacaoId, String cpf);

    Voto deleteBySessaoVotacao_IdAndAssociado(Long sessaoVotacaoId, String cpf);
}
