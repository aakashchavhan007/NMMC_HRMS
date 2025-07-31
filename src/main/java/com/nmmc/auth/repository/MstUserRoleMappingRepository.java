package com.nmmc.auth.repository;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nmmc.auth.entity.MstUserRoleMapping;

@Repository
public interface MstUserRoleMappingRepository extends JpaRepository<MstUserRoleMapping, UUID> {

        @Query("select h from MstUserRoleMapping  h where userId=?1")
        List<MstUserRoleMapping> getListByUserId(UUID userId);

        // @Query("select h from MstUserRoleMapping h where roleId=?1")
        // List<MstUserRoleMapping> getByRoleId(UUID roleId);

        @Query(value = "SELECT cast(sm.id as varchar) id,"
                        + " cast(sm.role_id as varchar) roleId, mn.name, sm.active_flag"
                        + " FROM common.mst_user_role_mapping sm"
                        + " left join common.roles mn on mn.id = sm.role_id"
                        + " WHERE sm.user_id = ?1", nativeQuery = true)
        List<Object[]> getByUserId(UUID userId);

        @Query(value = "SELECT cast(sm.id as varchar) id,"
                        + " cast(sm.user_id as varchar) userId, CONCAT(mn.user_first_name , mn.user_last_name) AS fullName, sm.active_flag"
                        + " FROM common.mst_user_role_mapping sm"
                        + " left join common.user_master mn on mn.id = sm.user_id"
                        + " WHERE sm.role_id = ?1", nativeQuery = true)
        List<Object[]> getByRoleId(UUID roleId);

        @Modifying
        @Transactional
        @Query(value = "delete from common.mst_user_role_mapping h where h.user_id= ?1", nativeQuery = true)
        void deleteByUserId(UUID userId);

        @Query(value = "select * from common.mst_user_role_mapping where department_id = ?1 and role_id = ?2 and user_id = ?3", nativeQuery = true)
        List<MstUserRoleMapping> getByDepartmentIdAndRoleIdAndUserId(UUID departmentId, UUID roleId, UUID userId);

        @Query(value = "select * from common.mst_user_role_mapping where user_id = ?1", nativeQuery = true)
        List<MstUserRoleMapping> getByListUserId(UUID userId);

        @Query(value = "SELECT Cast(ur.id as VARCHAR) as id,"
                        + " Cast(ur.role_id as VARCHAR) as role_id, mr.name as role"
                        + " FROM common.mst_user_role_mapping ur"
                        + " Inner Join common.mst_roles mr on mr.id = ur.role_id"
                        + " WHERE user_id = ?1", nativeQuery = true)
        List<Object[]> getRolesByUserId(UUID userId);

}
