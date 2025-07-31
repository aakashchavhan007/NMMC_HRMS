package com.nmmc.auth.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nmmc.hrms.Audit.AbstractEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="title_master", schema = "common")
public class TitleMaster extends AbstractEntity {
    
    private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name="title_name")
	private String titleName;

    @Column(name="title_name_mr")
	private String titleNameMr;

    @Column(name="valid")
	private String valid;

}
