

package com.nmmc.hrms.Repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nmmc.hrms.Entity.EmployeeLeaveLedgerHistory;


@Repository
public interface EmployeeLeaveLedgerHistoryRepository extends JpaRepository<EmployeeLeaveLedgerHistory, UUID> {

	@Query(value = "SELECT * FROM hrms.employee_leave_ledger_history WHERE employee_leave_ledger_id = :employeeLeaveLedgerId", nativeQuery = true)
	List<EmployeeLeaveLedgerHistory> findByLeaveApplicationId(@Param("employeeLeaveLedgerId") UUID employeeLeaveLedgerId);


	@Query(value = "SELECT (um.user_first_name || ' ' || um.user_last_name) AS user_name FROM common.user_master um WHERE um.id = ?1", nativeQuery = true)
	String getUserName(UUID createdUserId);
	
	@Query(value = "SELECT um.designation AS user_name FROM common.designation_master um WHERE um.id = ?1", nativeQuery = true)
	String findByDesignationId(UUID designationId);
	
	 @Query(value = "select d.designation from common.designation_master d where d.id = ?1 ",nativeQuery =  true)
		String getDesignationOfNextStage(UUID uuid);
	 
	 @Query(value = "SELECT * FROM hrms.employee_leave_ledger_history " +
             "WHERE status = 'LEAVE_APPROVED' AND employee_id IN (:employeeIds) " +
             "ORDER BY create_dt_tm DESC", nativeQuery = true)
List<EmployeeLeaveLedgerHistory> findApprovedLeaveHistoryByEmployeeIds(@Param("employeeIds") List<String> employeeIds);

	

}
