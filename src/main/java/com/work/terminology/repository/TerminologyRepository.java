package com.work.terminology.repository;

import com.work.terminology.model.Terminology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TerminologyRepository extends JpaRepository<Terminology,String> {

    @Procedure
    void MAKE_DIC();

    @Transactional
    @Modifying
    @Query(
            value="TRUNCATE TABLE 000_temp_dictionary",
            nativeQuery = true
    )
    void truncateTerminologyTable();

}
