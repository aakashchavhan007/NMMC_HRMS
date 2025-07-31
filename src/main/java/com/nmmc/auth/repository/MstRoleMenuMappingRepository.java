package com.nmmc.auth.repository;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nmmc.auth.entity.MstRoleMenuMapping;

@Repository
public interface MstRoleMenuMappingRepository extends JpaRepository<MstRoleMenuMapping, UUID> {

        // @Query("select h from MstRoleMenuMapping h where menuId=?1")
        // List<MstRoleMenuMapping> getByMenuId(UUID menuId);

        @Query("select h from MstRoleMenuMapping  h where roleId=?1")
        List<MstRoleMenuMapping> getListByRoleId(UUID roleId);

        @Query(value = "Select * from common.mst_role_menu_mapping where role_id = ?1 and department_id = ?2", nativeQuery = true)
        List<MstRoleMenuMapping> getByRoleIdAndDepartmentId(UUID roleId, UUID departmentId);

        @Query(value = "Select * from common.mst_role_menu_mapping where role_id = ?1", nativeQuery = true)
        List<MstRoleMenuMapping> getDetailsByRoleId(UUID roleId);

        @Query(value = "Select * from common.mst_role_menu_mapping where role_id = ?1 and department_id = ?2 AND active_flag = 'Y'", nativeQuery = true)
        List<MstRoleMenuMapping> getActiveByRoleIdAndDepartmentId(UUID roleId, UUID departmentId);

        @Query(value = "SELECT cast(sm.id as varchar) id,"
                        + " mn.menu_name_eng, mn.url, sm.title_name, cast(sm.department_id as varchar) departmentId, dm.department, sm.active_flag,cast(sm.menu_id as varchar) as menu_id, "
                        + " cast(sm.module_id as varchar) as module_id,sm.access_type  "
                        + " FROM common.mst_role_menu_mapping sm"
                        + " left join common.mst_menu mn on mn.id = sm.menu_id"
                        + " left join common.department_master dm on dm.id = sm.department_id"
                        + " WHERE sm.role_id = ?1", nativeQuery = true)
        List<Object[]> getByRoleId(UUID roleId);

        @Modifying
        @Transactional
        @Query(value = "delete from common.mst_role_menu_mapping h where h.role_id= ?1 and h.departmentId = ?2", nativeQuery = true)
        void deleteByRoleId(UUID roleId, UUID departmentId);

        // @Query(value = "SELECT Cast(rm.id as VARCHAR) as id,"
        // + " rm.menu_name, rm.menu_url, rm.title_name"
        // + " FROM common.mst_role_menu_mapping rm"
        // + " WHERE rm.role_id = ?1", nativeQuery = true)
        // List<Object[]> getRoleMenuList(UUID roleId);

//        @Query(value = "Select Cast(rm.id as VARCHAR) as id,"
//                        + " Cast(rm.menu_id as VARCHAR) as menuId, mm.menu_name_eng, mm.menu_name_mr, mm.menu_flag, mm.url,"
//                        + " Cast(rm.role_id as VARCHAR) as roleId, mr.name as roleName, mr.role_prefix,"
//                        + " Cast(rm.module_id as VARCHAR) as moduleId, mmm.module_name,"
//                        + " Cast(rm.department_id as VARCHAR) as departmentId"
//                        + " FROM common.mst_role_menu_mapping rm"
//                        + " Inner Join common.mst_menu mm on mm.id = rm.menu_id"
//                        + " Inner Join common.mst_roles mr on mr.id = rm.role_id"
//                        + " Inner Join common.mst_module mmm on mmm.id = rm.module_id"
//                        + " WHERE rm.role_id = ?1", nativeQuery = true)
//        List<Object[]> getRoleMenuList(UUID roleId);
        
        @Query(value = "Select "
                + " Cast(rm.menu_id as VARCHAR) as menuId, mm.menu_name_eng, mm.menu_name_mr, mm.menu_flag, mm.url,"
                + " Cast(rm.role_id as VARCHAR) as roleId, mr.name as roleName, mr.role_prefix,"
                + " Cast(rm.module_id as VARCHAR) as moduleId, mmm.module_name,"
                + " Cast(rm.department_id as VARCHAR) as departmentId"
                + " FROM common.mst_role_menu_mapping rm"
                + " Inner Join common.mst_menu mm on mm.id = rm.menu_id"
                + " Inner Join common.mst_roles mr on mr.id = rm.role_id"
                + " Inner Join common.mst_module mmm on mmm.id = rm.module_id"
                + " WHERE rm.role_id = ?1 AND rm.active_flag = 'Y'", nativeQuery = true)
        List<Object[]> getRoleMenuList(UUID roleId);

}

