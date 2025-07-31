package com.nmmc.hrms.Audit;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity {

	// private static final long serialVersionUID = 1L;

	@CreatedDate
	@Column(name = "create_Dt_Tm")
	private LocalDateTime createDtTm;

	@LastModifiedDate
	@Column(name = "update_dt_tm")
	private LocalDateTime updateDtTm;

	@Column(name = "active_flag")
	private Character activeFlag;

	@PrePersist
	public void onPrePersist() {
		if (this.activeFlag == null) {
			this.activeFlag = 'Y';
		}
		this.createDtTm = LocalDateTime.now();
	}

	@PreUpdate
	public void onPreUpdate() {
		this.updateDtTm = LocalDateTime.now();
	}

	@CreatedBy
	// @GeneratedValue(generator = "UUID")
	@Column(name = "created_user_id", insertable = true, updatable = false)
	private UUID createdUserId;

	@LastModifiedBy
	// @GeneratedValue(generator = "UUID")
	@Column(name = "update_user_id", insertable = false, updatable = true)
	private UUID updateUserId;

}

