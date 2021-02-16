package com.test.terminology.repository;

import com.test.terminology.model.Terminology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminologyRepository extends JpaRepository<Terminology,String> {
}
