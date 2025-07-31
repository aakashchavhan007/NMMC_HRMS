package com.nmmc.hrms.Entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nmmc.hrms.Audit.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "trn_document_list", schema = "hrms")
public class TrnDocumentList extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "application_no")
    private String applicationNo;

    @Column(name = "transaction_key")
    private String transactionKey;
    
    @Column(name = "is_valid_doc")
    private Character isValidDoc;

}

