package com.tmt.tmdt.repository;

import com.tmt.tmdt.entities.RootFilterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RootFilterRepo extends JpaRepository<RootFilterEntity, Integer> {

}
