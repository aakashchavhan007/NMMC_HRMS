package com.nmmc.auth.entity;

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
@Table(name = "mst_role_menu_mapping", schema = "common")
public class MstRoleMenuMapping extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "menu_id")
    private UUID menuId;

    @Column(name = "module_id")
    private UUID moduleId;
    
    @Column(name = "access_type")
    private String accessType;

}

