package com.nmmc.hrms.Dao;

import java.util.UUID;

import lombok.Data;

@Data
public class TrnDocumentListDao {

    private UUID id;

    private String documentName;

    private String fileName;

    private String filePath;

    private UUID transactionId;

    private String applicationNo;

    private String transactionKey;
    
    private Character isValidDoc;

}
