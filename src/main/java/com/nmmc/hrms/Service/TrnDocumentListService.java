package com.nmmc.hrms.Service;

import java.util.List;
import java.util.UUID;

import com.nmmc.hrms.Dao.TrnDocumentListDao;

public interface TrnDocumentListService {

    String save(TrnDocumentListDao trnDocumentListDao);

    List<TrnDocumentListDao> findAllDocumentListByTransactionIdAndTransactionKey(UUID transactionId,
            String transactionKey);

}

