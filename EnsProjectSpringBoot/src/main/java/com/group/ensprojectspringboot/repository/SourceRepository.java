package com.group.ensprojectspringboot.repository;

import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {
    @Modifying
    @Query(EnsConsts.UPDATE_SOURCE)
    void updateSource(@Param("sourceId") Long sourceId, @Param("sourceName") String sourceName, @Param("sourceDescription") String sourceDescription);
}
