package com.group.ensprojectspringboot.repository;

import com.group.ensprojectspringboot.consts.EnsConsts;
import com.group.ensprojectspringboot.model.Bursarship;
import com.group.ensprojectspringboot.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BursarshipRepository extends JpaRepository<Bursarship, Long> {

    @Modifying
    @Query(EnsConsts.UPDATE_BURSARSHIP)
    void updateBursarship(@Param("bursarshipId") Long bursarshipId, @Param("bursaryName") String bursaryName, @Param("amount") Double amount, @Param("sourceId") Source sourceId);
}
