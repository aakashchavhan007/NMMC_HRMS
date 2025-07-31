package com.nmmc.hrms.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nmmc.hrms.Entity.TrnDocumentList;

public interface TrnDocumentListRepository extends JpaRepository<TrnDocumentList, UUID> {

    @Query(value = "SELECT td.* FROM hrms.trn_document_list td WHERE td.transaction_id = ?1 and td.transaction_key = ?2", nativeQuery = true)
    List<TrnDocumentList> findAllDocumentListByTransactionIdAndTransactionKey(UUID transactionId,
            String transactionKey);

    @Query(value = "select d from TrnDocumentList d where d.id = ?1")
	TrnDocumentList getByIds(UUID id);

	List<TrnDocumentList> findByTransactionId(UUID id);

}
