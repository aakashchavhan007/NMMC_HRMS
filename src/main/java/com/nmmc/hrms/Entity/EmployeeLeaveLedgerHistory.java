
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
@Table(name = "employee_leave_ledger_history", schema = "hrms")
public class EmployeeLeaveLedgerHistory extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "employee_leave_ledger_id")
    private UUID employeeLeaveLedgerId;
    
    @Column(name = "employee_id")
    private String employeeId;
    
    @Column(name = "work_location")
    private Character workLocation;
    
    @Column(name = "leave_subject", length = 10000)
    private String leaveSubject;
    
    @Column(name = "leave_reason",length = 50000)
    private String leaveReason;
    
    @Column(name = "leave_preference")
    private String leavePreference;
    
    @Column(name = "leavedl_days_full")
    private Double leavedlDaysFull;
    
    @Column(name = "leave_days_half")
    private Double leaveDaysHalf;
    
    @Column(name = "total_leave_days")
    private Double totalLeaveDays;

    @Column(name = "status")
    private String status;

    @Column(name = "approve_remark")
    private String approveRemark;

    @Column(name = "approve_remark_mr")
    private String approveRemarkMr;

    @Column(name = "reject_remark")
    private String rejectRemark;

    @Column(name = "reject_remark_mr")
    private String rejectRemarkMr;
   
    @Column(name = "designation_id")
    private UUID designationId;
    
    @Column(name = "department_id")
    private UUID departmentId;
    
    @Column(name = "user_full_name")
    private String userFullName;
    
    @Column(name="mac_address")
    private String macAddress;

    @Column(name="isp_ip_address")
    private String ispIpAddess;

    @Column(name = "latest_remark")
    private String latestRemark;
    
    @Column(name="sender_designation")
	private UUID senderDesignation;
    
   

    
   

}
