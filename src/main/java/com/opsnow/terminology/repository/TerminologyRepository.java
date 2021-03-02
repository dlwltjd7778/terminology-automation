package com.opsnow.terminology.repository;

import com.opsnow.terminology.model.Terminology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminologyRepository extends JpaRepository<Terminology,String> {

    @Procedure
    void MAKE_DIC();

    @Query(
            value="TRUNCATE table 000_temp_dictionary",
            nativeQuery = true
    )
    void truncateTerminologyTable();

}
