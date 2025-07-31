package com.nmmc.hrms.views;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Immutable
@Subselect("SELECT " +
	    "ell.id, " +
	    "ell.sr_no, " +
	    "ell.employee_id, " +
	    " me.ddo_id, " +
	    "CONCAT(me.first_name, ' ', me.middle_name, ' ', me.last_name) AS full_name, " +
	    "me.first_name, " +
	    "me.middle_name, " +
	    "me.last_name, " +
	    "ell.application_date, " +
	    "ell.leave_id, " +
	    "ell.appl_from_date, " +
	    "ell.appl_to_date, " +
	    "ell.appl_days_f, " +
	    "ell.appl_days_h, " +
	    "ell.before_after, " +
	    "ell.before_after_mar, " +
	    "ell.reason, " +
	    "ell.reason_mar, " +
	    "ell.address, " +
	    "ell.address_mar, " +
	    "ell.rec_auth_status, " +
	    "ell.recommended, " +
	    "ell.recommended_by, " +
	    "ell.approved, " +
	    "ell.approved_by, " +
	    "ell.ltc, " +
	    "ell.additional_charge, " +
	    "ell.approval_date, " +
	    "ell.balance_cl, " +
	    "ell.balance_el, " +
	    "ell.balance_hpl, " +
	    "ell.paid, " +
	    "ell.modified_on, " +
	    "ell.is_workflow_created, " +
	    "ell.leave_type_id, " +
	    "lt.leave_type, " +
	    "lt.leave_type_name, " +
	    "ell.designation_id, " +
	    "dm.designation, " +
	    "ell.department_id, " +
	    "md.department, " +
	    "ell.latest_remark, " +
	    "ell.sender_designation, " +
	    "ell.next_stage, " +
	    "ell.approve_remark, " +
	    "ell.approve_remark_mr, " +
	    "ell.reject_remark, " +
	    "ell.reject_remark_mr, " +
	    "ell.status, " +
	    "ell.location_id, " +
	    "lm.location_type, " +
	    "ell.leave_subject, " +
	    "ell.leave_reason, " +
	    "ell.leave_preference, " +
	    "ell.total_leave_days, " +
	    "ell.create_dt_tm, " +
	    "ell.active_flag, "+
	    "ell.half_pay_option, "+
	    "ell.holidays_after, "+
	    "ell.holidays_before, "+
	    "ell.leave_address, "+
	    "ell.add_charge_user_id, "+
	    "ell.earned_leave_extradited, "+
	    "ell.swadham_travel_concessions "+
	"FROM hrms.employee_leave_ledger ell " +
	"LEFT JOIN hrms.mst_employee me ON me.old_employee_id = ell.employee_id " +
	"LEFT JOIN common.department_master md ON md.id = me.dep_id  " +
	"LEFT JOIN common.designation_master dm ON dm.id = me.designation_id  " +
	"LEFT JOIN hrms.mst_leave_type lt ON lt.id = ell.leave_type_id " +
	"LEFT JOIN hrms.location_master lm ON lm.location_id = ell.location_id "+
	"ORDER BY ell.create_dt_tm DESC NULLS LAST ")
public class EmployeeLeaveLedgerView {

    @Id
    @Column(name="id")
    private UUID id;
    
    @Column(name = "sr_no")
    private Long srNo;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "application_date")
    private LocalDateTime applicationDate;

    @Column(name = "leave_id")
    private Long leaveId;

    @Column(name = "appl_from_date")
    private LocalDateTime applFromDate;

    @Column(name = "appl_to_date")
    private LocalDateTime applToDate;

    @Column(name = "appl_days_f")
    private Long applDaysF;

    @Column(name = "appl_days_h")
    private Long applDaysH;

    @Column(name = "before_after")
    private String beforeAfter;

    @Column(name = "before_after_mar")
    private String beforeAfterMar;

    @Column(name = "reason")
    private String reason;

    @Column(name = "reason_mar")
    private String reasonMar;

    @Column(name = "address")
    private String address;

    @Column(name = "address_mar")
    private String addressMar;

    @Column(name = "rec_auth_status")
    private Character recAuthStatus;

    @Column(name = "recommended")
    private Character recommended;

    @Column(name = "recommended_by")
    private Long recommendedBy;

    @Column(name = "approved")
    private Character approved;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "ltc")
    private Character ltc;

    @Column(name = "additional_charge")
    private Long additionalCharge;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "balance_cl")
    private Long balanceCl;

    @Column(name = "balance_el")
    private Long balanceEl;

    @Column(name = "balance_hpl")
    private Long balanceHpl;

    @Column(name = "paid")
    private Character paid;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Column(name = "is_workflow_created")
    private String isWorkflowCreated;
    
    @Column(name = "leave_type_id")
    private UUID leaveTypeId;
    
    @Column(name = "designation_id")
    private UUID designationId;
    
    @Column(name = "department_id")
    private UUID departmentId;
    
    @Column(name = "latest_remark")
    private String latestRemark;
    
    @Column(name="sender_designation")
    private UUID senderDesignation;
    
    @Column(name = "next_stage")
    private String nextStage;

    @Column(name = "approve_remark")
    private String approveRemark;

    @Column(name = "approve_remark_mr")
    private String approveRemarkMr;

    @Column(name = "reject_remark")
    private String rejectRemark;

    @Column(name = "reject_remark_mr")
    private String rejectRemarkMr;

    @Column(name = "status")
    private String status;

    @Column(name = "location_id")
    private Long locationId;
    
    @Column(name = "leave_subject", length = 10000)
    private String leaveSubject;
    
    @Column(name = "leave_reason", length = 50000)
    private String leaveReason;
    
    @Column(name = "leave_preference")
    private String leavePreference;
    
    @Column(name = "total_leave_days")
    private Long totalLeaveDays;

    @Column(name = "full_name")
    private String fullName;
	
	  @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "department")
    private String department;
  
    @Column(name = "designation")
    private String designation;
	
	 @Column(name = "create_dt_tm")
    private LocalDateTime createDtTm;    
  
    @Column(name = "leave_type")
    private String leaveType;	
	
    @Column(name = "leave_type_name")
    private String leaveTypeName;
    
    @Column(name = "active_flag")
    private Character activeFlag;
    
    
    @Column(name = "half_pay_option")
    private String halfPayOption;
    
    
    @Column(name = "holidays_before")
    private String holidaysBefore;
    
    @Column(name = "holidays_after")
    private String holidaysAfter;
    
    @Column(name = "leave_address")
    private String leaveAddress;
	
//    @Column(name = "add_charge_user_id")
//    private String addChargeUserId;
    @Column(name = "add_charge_user_id")
    private UUID addChargeUserId;

    @Column(name= "earned_leave_extradited")
    private String earnedLeaveExtradited;
    
    @Column(name = "swadham_travel_concessions")
    private String swadhamTravelConcessions;
    
    @Column(name = "ddo_id")
    private UUID ddoId;

    @Column(name = "location_type")
    private Character locationType;
}
