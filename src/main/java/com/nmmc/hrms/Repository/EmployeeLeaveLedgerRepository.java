package com.nmmc.hrms.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nmmc.hrms.Entity.EmployeeLeaveLedger;
import com.nmmc.hrms.Entity.TrnLeaveApplication;

public interface EmployeeLeaveLedgerRepository extends JpaRepository<EmployeeLeaveLedger, UUID>,JpaSpecificationExecutor<EmployeeLeaveLedger> {

	
	List<EmployeeLeaveLedger> findByEmployeeIdAndApplFromDateLessThanEqualAndApplToDateGreaterThanEqual(
		    Long employeeId, LocalDateTime toDate, LocalDateTime fromDate);
	
	@Query(value = "SELECT um.designation AS user_name FROM common.designation_master um WHERE um.id = ?1", nativeQuery = true)
	String findDesignationNameById(UUID designationId);

	@Query("select h from EmployeeLeaveLedger h where id=?1")
	EmployeeLeaveLedger getById(UUID id);

	@Query(value = "SELECT * FROM hrms.employee_leave_ledger a WHERE a.employee_id = ?1 AND TO_CHAR(a.application_date, 'YYYY-MM') = ?2", nativeQuery = true)
	List<EmployeeLeaveLedger> findByEmployeeIdAndApplicationDate(Long valueOf, String cleanMonthYear);
	
	@Query(value = "SELECT * FROM hrms.employee_leave_ledger a WHERE a.id = ?1", nativeQuery = true)
	Optional<EmployeeLeaveLedger> findByTranscationId(UUID id);

	
	@Query("SELECT l FROM EmployeeLeaveLedger l WHERE l.employeeId = ?1 AND (FUNCTION('TO_CHAR', l.applFromDate, 'YYYY-MM') = ?2 OR FUNCTION('TO_CHAR', l.applToDate, 'YYYY-MM') = ?2)")
	List<EmployeeLeaveLedger> findByEmployeeId(Long employeeId, String monthYear);
	
	// CREATE case
	@Query(value = "SELECT * FROM hrms.employee_leave_ledger " +
	        "WHERE employee_id = :employeeId " +
	        "AND status NOT IN ('REJECTED', 'CANCELLED') " +
	        "AND (DATE(appl_from_date) <= DATE(:toDate) AND DATE(appl_to_date) >= DATE(:fromDate))",
	        nativeQuery = true)
	List<EmployeeLeaveLedger> findOverlappingLeavesForCreate(
	        @Param("employeeId") Long employeeId,
	        @Param("fromDate") LocalDate fromDate,
	        @Param("toDate") LocalDate toDate);

	// UPDATE case
	@Query(value = "SELECT * FROM hrms.employee_leave_ledger " +
	        "WHERE employee_id = :employeeId " +
	        "AND status NOT IN ('REJECTED', 'CANCELLED') " +
	        "AND id != :id " +
	        "AND (DATE(appl_from_date) <= DATE(:toDate) AND DATE(appl_to_date) >= DATE(:fromDate))",
	        nativeQuery = true)
	List<EmployeeLeaveLedger> findOverlappingLeavesForUpdate(
	        @Param("employeeId") Long employeeId,
	        @Param("fromDate") LocalDate fromDate,
	        @Param("toDate") LocalDate toDate,
	        @Param("id") UUID id);
//
//	@Query(value = "select * from hrms.employee_leave_ledger where employee_id = ?1",nativeQuery = true)
//	List<EmployeeLeaveLedger> findByEmployeeId1(Long employeeId);
//
//

	@Query(value = "SELECT * FROM hrms.employee_leave_ledger " +
            "WHERE employee_id = :employeeId " +
            "AND appl_from_date >= :startDate", nativeQuery = true)
List<EmployeeLeaveLedger> findByEmployeeIdFromDate(@Param("employeeId") Long employeeId, @Param("startDate") LocalDate startDate);



}

