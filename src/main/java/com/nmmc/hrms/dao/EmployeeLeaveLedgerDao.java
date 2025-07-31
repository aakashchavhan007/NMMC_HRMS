
package com.nmmc.hrms.Dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class EmployeeLeaveLedgerDao {

	private UUID id;
	private Long srNo;
	private Long employeeId;
	private LocalDateTime applicationDate;
	private Long leaveId;
	private LocalDateTime applFromDate;
	private LocalDateTime applToDate;
	private Long applDaysF;
	private Long applDaysH;
	private String beforeAfter;
	private String beforeAfterMar;
	private String reason;
	private String reasonMar;
	private String address;
	private String addressMar;
	private Character recAuthStatus;
	private Character recommended;
	private Long recommendedBy;
	private Character approved;
	private Long approvedBy;
	private Character ltc;
	private Long additionalCharge;
	private LocalDateTime approvalDate;
	private Long balanceCl;
	private Long balanceEl;
	private Long balanceHpl;
	private Character paid;
	private LocalDateTime modifiedOn;
	private String isWorkflowCreated;
	private UUID leaveTypeId;
	private UUID designationId;
	private UUID departmentId;
	private String latestRemark;
	private UUID senderDesignation;
	private String nextStage;
	private String approveRemark;
	private String approveRemarkMr;
	private String rejectRemark;
	private String rejectRemarkMr;
	private String status;
	private Long locationId;
	private String leaveSubject;
	private String leaveReason;
	private String leavePreference;
	private Long totalLeaveDays;
	private List<TrnDocumentListDao> documentList;

	private List<String> documents;

	private List<String> docList;

	private List<MstLeaveAllocationListDao> mstLeaveAllocationListDao;
	private List<EmployeeLeaveLedgerHistoryDao> employeeLeaveLedgerHistoryDao;
	private String leaveTypeName;
	private boolean historyPresent;

	private String fullName;

	private String firstName;

	private String middleName;

	private String lastName;

	private String department;

	private String designation;

	private LocalDateTime createDtTm;

	private String halfPayOption;

	private String sanctionLetterFile;

	private String leaveAddress;

	private String holidaysBefore;

	private String holidaysAfter;
	
   // private String addChargeUserId;
	private UUID addChargeUserId;

	private String earnedLeaveExtradited;
    
    private String swadhamTravelConcessions;
    





}
