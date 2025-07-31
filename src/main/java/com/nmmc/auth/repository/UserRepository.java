package com.nmmc.auth.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nmmc.auth.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

        List<User> findAllByActiveFlagOrderByIdDesc(Character activeFlag);

        @Query(value = "SELECT * FROM common.user_master WHERE dpt_id = ?1 and designation_id = ?2 and active_flag = 'Y' Order by id desc", nativeQuery = true)
        List<User> findAllByDptIdAndDesignationId(UUID dptId, UUID designationId);

        // User findById(Long id);

        @Query("SELECT u FROM User u WHERE u.id = ?1")
        User getById(UUID id);

        @Query(value = "select t.* from common.user_master t where t.user_name = ?1 And t.raw_password = ?2", nativeQuery = true)
        User authenticateUser(String userName, String userPassword);

        @Query("SELECT user FROM User  user WHERE userName = ?1")
        User isUserExist(String userName);

        @Query(value = "select * from common.user_master where ugp_prev_id = 2 and active_flag = 'Y'", nativeQuery = true)
        List<User> getAllDepartmentUser();

        @Query(value = "select * from common.user_master where ugp_prev_id = 3", nativeQuery = true)
        List<User> getAllCitizenUsers();

        @Query(value = "select * from common.user_master where designation_id = ?1", nativeQuery = true)
        List<User> getUsersByDesignation(UUID designationId);

        @Query(value = "select * from common.user_master where dpt_id = ?1", nativeQuery = true)
        List<User> getUsersByDepartment(UUID departmentId);

        @Query(value = "select * from common.user_master where ugp_prev_id = 4", nativeQuery = true)
        List<User> getAllCfcUsers();

        @Query("SELECT a FROM User a ORDER BY a.id DESC")
        List<User> findAllOrderByIdDesc();

        @Transactional
        @Query(value = "select Cast(d.id as varchar) id from centralworkshopModule.mst_driver d where d.userid = ?1", nativeQuery = true)
        UUID getDriverId(UUID id);

        @Query(value = "SELECT MAX(sequence_no) FROM common.user_master", nativeQuery = true)
        Long getMaxSequenceNo();

        @Query(value = "select um.* from common.user_master um where um.designation_id = ?1 and um.ward_id =?2 and um.dpt_name = ?3 and um.active_flag ='Y'", nativeQuery = true)
        List<User> getUserDesignationAndDepartmentWise(UUID designationId, UUID wardId, String departmemtName);

        @Transactional
        // @Query(value = "select Cast(wm.id as varchar) id,
        // wm.active_flag,Cast(wm.designation_id as varchar) designation_id,
        // wm.module_name,"
        // + " Cast(wm.sector_id as varchar) sector_id, wm.template_name,
        // Cast(wm.user_id as varchar) user_id,"
        // + " Cast(wm.ward_id as varchar) ward_id,wm.warkflowstage"
        // + " from enterpriseworkflowmanagement.workflow_manage wm where wm.user_id =
        // ?1", nativeQuery = true)
        // List<Object[]> getWorkflow(UUID id);

        @Query(value = "select * from common.user_master where user_Mobile = ?1", nativeQuery = true)
        User findByUserMobile(String userMobile);

        @Query(value = "SELECT cast(um.id as varchar) id, um.user_first_name, um.user_first_name_mr , um.user_last_name, um.user_last_name_mr , um.user_email, um.user_Mobile, um.user_name, um.user_password, um.ugp_prev_id, um.is_cfc_user, um.is_dpt_user, um.mobile_otp, um.email_otp, um.gender, um.adress, um.state, um.city, um.pin_code, um.documents,"
                        + " um.active_flag, wm.ward_name , cast(um.id as varchar) ward_id, zm.zone_name , cast(um.id as varchar) zone_id, dm.department , cast(um.id as varchar) dpt_id, sdm.sub_department , cast(um.id as varchar) subdpt_id, dem.designation , cast(um.id as varchar) designation_id,"
                        + " um.user_middle_name,um.user_middle_name_mr,cast(um.title_id as varchar) titleId,tm.title_name,tm.title_name_mr,cast(um.location_id as varchar) locationId, lm.location_name, lm.location_name_mr"
                        + " FROM common.user_master  um"
                        + " left join common.mst_ward wm on wm.id = um.ward_id"
                        + " left join common.mst_zone zm on zm.id = um.zone_id"
                        + " left join common.department_master dm on dm.id = um.dpt_id"
                        + " left join common.sub_department_master sdm on sdm.id = um.subdpt_id"
                        + " left join common.designation_master dem on dem.id = um.designation_id"
                        + " left join common.title_master tm on tm.id = um.title_id"
                        + " left join common.location_master lm on lm.id = um.location_id"
                        + " ORDER BY um.id desc", nativeQuery = true)
        List<Object[]> getAllDashboard();

        @Query(value = "SELECT cast(um.id as varchar) id, um.user_first_name, um.user_first_name_mr , um.user_last_name, um.user_last_name_mr , um.user_email, um.user_Mobile, um.user_name, um.user_password, um.ugp_prev_id, um.is_cfc_user, um.is_dpt_user, um.mobile_otp, um.email_otp, um.gender, um.adress, um.state, um.city, um.pin_code, um.documents,"
                        + " um.active_flag, wm.ward_name , cast(um.id as varchar) ward_id, zm.zone_name , cast(um.id as varchar) zone_id, dm.department , cast(um.id as varchar) dpt_id, sdm.sub_department , cast(um.id as varchar) subdpt_id, dem.designation , cast(um.id as varchar) designation_id,cast(um.location_id as varchar) locationId, lm.location_name, lm.location_name_mr,"
                        + " um.user_middle_name,um.user_middle_name_mr,cast(um.title_id as varchar) titleId,tm.title_name,tm.title_name_mr"
                        + " FROM common.user_master  um"
                        + " left join common.mst_ward wm on wm.id = um.ward_id"
                        + " left join common.mst_zone zm on zm.id = um.zone_id"
                        + " left join common.department_master dm on dm.id = um.dpt_id"
                        + " left join common.sub_department_master sdm on sdm.id = um.subdpt_id"
                        + " left join common.designation_master dem on dem.id = um.designation_id"
                        + " left join common.location_master lm on lm.id = um.location_id"
                        + " left join common.title_master tm on tm.id = um.title_id"
                        + " where um.ward_id = ?1 and um.location_id = ?2 and um.dpt_id = ?3 and um.designation_id = ?4"
                        + " ORDER BY um.id desc", nativeQuery = true)
        List<Object[]> getUsersByWardLocDptDesg(UUID wardId, UUID locId, UUID dptId, UUID desgId);

        @Query(value = "SELECT cast(um.id as varchar) id, um.user_first_name, um.user_first_name_mr , um.user_last_name, um.user_last_name_mr , um.user_email, um.user_Mobile, um.user_name, um.user_password, um.ugp_prev_id, um.is_cfc_user, um.is_dpt_user, um.mobile_otp, um.email_otp, um.gender, um.adress, um.state, um.city, um.pin_code, um.documents,"
                        + " um.active_flag, wm.ward_name , cast(um.id as varchar) ward_id, zm.zone_name , cast(um.id as varchar) zone_id, dm.department , cast(um.id as varchar) dpt_id, sdm.sub_department , cast(um.id as varchar) subdpt_id, dem.designation , cast(um.id as varchar) designation_id,cast(um.location_id as varchar) locationId, lm.location_name, lm.location_name_mr,"
                        + " um.user_middle_name,um.user_middle_name_mr,cast(um.title_id as varchar) titleId,tm.title_name,tm.title_name_mr"
                        + " FROM common.user_master  um"
                        + " left join common.mst_ward wm on wm.id = um.ward_id"
                        + " left join common.mst_zone zm on zm.id = um.zone_id"
                        + " left join common.department_master dm on dm.id = um.dpt_id"
                        + " left join common.sub_department_master sdm on sdm.id = um.subdpt_id"
                        + " left join common.designation_master dem on dem.id = um.designation_id"
                        + " left join common.location_master lm on lm.id = um.location_id"
                        + " left join common.title_master tm on tm.id = um.title_id"
                        + " where um.ward_id = ?1 and um.dpt_id = ?2 and um.designation_id = ?3"
                        + " ORDER BY um.id desc", nativeQuery = true)
        List<Object[]> getUsersByWardDptDesg(UUID wardId, UUID dptId, UUID desgId);

        @Query(value = "Select cast(um.id as varchar) id, um.user_first_name, um.user_first_name_mr , um.user_last_name, um.user_last_name_mr , um.user_email, um.user_Mobile, um.user_name,um.adress, um.state, um.city, um.pin_code"
                        + " from common.user_master um "
                        + " inner join common.reg_digi_locker rdl on um.user_name = rdl.user_name"
                        + " where um.id = ?1", nativeQuery = true)
        List<Object[]> getUserDetailsFromDigi(UUID id);

        @Transactional
        @Query(value = "select Cast(wm.id as varchar) id, wm.active_flag, wm.module_name, wm.module_url"
                        + " from common.mst_user_module_mapping wm where wm.user_id = ?1", nativeQuery = true)
        List<Object[]> getModules(UUID id);

        @Query(value = "select Cast(um.id as varchar) id, um.user_first_name, um.user_first_name_mr, um.user_middle_name, um.user_middle_name_mr, um.user_last_name, um.user_last_name_mr, um.user_email, "
                        + " um.user_Mobile, um.user_password, Cast(um.ugp_prev_id as varchar) ugpPrevId, Cast(um.title_id as varchar) titleId, Cast(um.dpt_id as varchar) dptId, um.dpt_name,"
                        + " Cast(um.subdpt_id as varchar) subdptId, Cast(um.designation_id as varchar) designationId, um.is_cfc_user, um.is_dpt_user, um.mobile_otp, um.email_otp, um.gender, um.DOB, um.adress, um.state, um.city, um.pin_code, Cast(um.zone_id as varchar) zoneId, Cast(um.ward_id as varchar) wardId, um.documents, Cast(um.location_id as varchar) locationId"
                        + " from common.user_master um "
                        + " where um.user_name = ?1 and um.is_dpt_user = true", nativeQuery = true)
        List<Object[]> findUsersByUsername(String userName);

        @Transactional
        @Query(value = "select Cast(wm.id as varchar) id, Cast(wm.role_id as varchar) roleId, mr.name, wm.active_flag, wm.menu_name, wm.menu_url, wm.title_name"
                        + " from common.mst_role_menu_mapping wm "
                        + " inner join common.roles mr on wm.role_id = mr.id"
                        + " where wm.department_id = ?1", nativeQuery = true)
        List<Object[]> getRoles(UUID dptId);

        @Transactional
        @Query(value = "SELECT Cast(wm.id as varchar) id, wm.module_name, wm.template_name, Cast(wm.ward_id as varchar) wardId"
                        + " FROM enterpriseworkflowmanagement.workflow_manage wm"
                        + " INNER JOIN enterpriseworkflowmanagement.mst_workflow_details wd"
                        + " ON wm.id = wd.workflow_id"
                        + " WHERE wd.user_id = ?1", nativeQuery = true)
        List<Object[]> getWorkflowByUserId(UUID userId);

        @Transactional
        @Query(value = "Select wd.assign_stage"
                        + " from enterpriseworkflowmanagement.mst_workflow_details wd "
                        + " WHERE wd.workflow_id = ?1 AND wd.user_id = ?2", nativeQuery = true)
        List<Object[]> getAssignStageByWorkflowIdAndUserId(UUID workflowId, UUID userId);

        // @Transactional
        // @Query(value = "SELECT CONCAT(wd.positive_stage, ' , ', wd.negative_stage) AS
        // stage, mt.template_name"
        // + "FROM enterpriseworkflowmanagement.mst_workflow_manager_details wd"
        // + "INNER JOIN enterpriseworkflowmanagement.mst_workflow_manager wm ON wm.id =
        // wd.workflow_id"
        // + "INNER JOIN enterpriseworkflowmanagement.mst_template mt ON mt.id =
        // wm.template_id"
        // + "WHERE wd.user_id = ?1"
        // + "GROUP BY mt.template_name, wd.positive_stage, wd.negative_stage",
        // nativeQuery = true)
        // List<Object[]> getWorkflowByUserId1(UUID userId);

        @Transactional
        @Query(value = "SELECT CONCAT(wd.positive_stage, ' , ', wd.negative_stage) AS stage, mt.template_name "
                        + "FROM enterpriseworkflowmanagement.mst_workflow_manager_details wd "
                        + "INNER JOIN enterpriseworkflowmanagement.mst_workflow_manager wm ON wm.id = wd.workflow_id "
                        + "INNER JOIN enterpriseworkflowmanagement.mst_template mt ON mt.id = wm.template_id "
                        + "WHERE wd.user_id = ?1 "
                        + "GROUP BY mt.template_name, wd.positive_stage, wd.negative_stage", nativeQuery = true)
        List<Object[]> getWorkflowByUserId1(UUID userId);

        Optional<User> findByUserName(String username);
        
//        @Query(value = "SELECT "
//        		+ "    CAST(wmd.id AS varchar) AS workflow_detail_id, "
//        		+ "    CAST(wmd.workflow_id AS varchar) AS workflow_id,"
//        		+ "    wm.template_id,"
//        		+ "    wm.template_name,"
//        		+ "    wm.module_name,"
//        		+ "    CAST(wm.module_id AS varchar) AS module_id,"
//        		+ "    CAST(wm.ward_id AS varchar) AS ward_id,"
//        		+ "    CAST(wmd.user_id AS varchar) AS user_id,"
//        		+ "    CASE "
//        		+ "        WHEN wmd.positive_stage IS NOT NULL AND wmd.negative_stage IS NOT NULL THEN "
//        		+ "            wmd.positive_stage || ',' || wmd.negative_stage"
//        		+ "        WHEN wmd.positive_stage IS NOT NULL THEN "
//        		+ "            wmd.positive_stage"
//        		+ "        WHEN wmd.negative_stage IS NOT NULL THEN "
//        		+ "            wmd.negative_stage"
//        		+ "        ELSE "
//        		+ "            NULL"
//        		+ "    END AS authorized_stages"
//        		+ "FROM "
//        		+ "    enterpriseworkflowmanagement.mst_workflow_manager wm"
//        		+ "JOIN "
//        		+ "    enterpriseworkflowmanagement.mst_workflow_manager_details wmd "
//        		+ "    ON wm.id = wmd.workflow_id"
//        		+ "WHERE "
//        		+ "    wm.template_id = ?1"
//        		+ "    AND wmd.user_id = ?2;"
//        		+ "", nativeQuery = true)
//        	List<Object[]> getWorkflowByTemplateIdAndUserId(Long templateId, UUID userId);
        
        @Query(value = "SELECT "
                + "    CAST(wmd.id AS varchar) AS workflow_detail_id, "
                + "    CAST(wmd.workflow_id AS varchar) AS workflow_id,"
                + "    wm.template_id,"
                + "    wm.template_name,"
                + "    wm.module_name,"
                + "    CAST(wm.module_id AS varchar) AS module_id,"
                + "    CAST(wm.ward_id AS varchar) AS ward_id,"
                + "    CAST(wmd.user_id AS varchar) AS user_id,"
                + "    CASE "
                + "        WHEN wmd.positive_stage IS NOT NULL AND wmd.negative_stage IS NOT NULL THEN "
                + "            wmd.positive_stage || ',' || wmd.negative_stage"
                + "        WHEN wmd.positive_stage IS NOT NULL THEN "
                + "            wmd.positive_stage"
                + "        WHEN wmd.negative_stage IS NOT NULL THEN "
                + "            wmd.negative_stage"
                + "        ELSE "
                + "            NULL"
                + "    END AS authorized_stages "
                + "FROM "
                + "    enterpriseworkflowmanagement.mst_workflow_manager wm "
                + "JOIN "
                + "    enterpriseworkflowmanagement.mst_workflow_manager_details wmd "
                + "    ON wm.id = wmd.workflow_id "
                + "WHERE "
                + "    wm.template_id = ? "
                + "    AND wmd.user_id = ?;",
                nativeQuery = true)
        List<Object[]> getWorkflowByTemplateIdAndUserId(Long templateId, UUID userId);

        @Query("SELECT DISTINCT d.designationId FROM User d WHERE d.ddoId = ?1 AND d.dptId = ?2")
        List<UUID> findDistinctDesignationIdsByDdoIdAndDptId(UUID ddoId, UUID dptId);




}
