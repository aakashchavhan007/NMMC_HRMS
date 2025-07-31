package com.nmmc.auth.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nmmc.auth.entity.TitleMaster;

@Repository
public interface TitleMasterRepository extends JpaRepository<TitleMaster, UUID>{
    
    List<TitleMaster> findAllByActiveFlagOrderByIdDesc(Character activeFlag);

	Page<TitleMaster> findAll(Pageable pageable);

    @Query("SELECT a FROM TitleMaster a ORDER BY a.id DESC")
    List<TitleMaster> findAllOrderByIdDesc();

    // @Query(value = "SELECT MAX(sequence_no) FROM msmam.sub_cast_master", nativeQuery = true)
    // Long getMaxSequenceNo();

    @Query(value = "select * from common.title_master where lower(title_name) = lower(?1)", nativeQuery = true)
    TitleMaster findByTitleName(String titleName);

}
