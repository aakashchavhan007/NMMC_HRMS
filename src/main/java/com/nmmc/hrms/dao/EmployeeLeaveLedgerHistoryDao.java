
package com.nmmc.hrms.Dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class EmployeeLeaveLedgerHistoryDao {

	private UUID id;
	
	private UUID employeeLeaveLedgerId;

	private String employeeId;

	private String leaveSubject;

	private String leaveReason;

	private String leavePreference;

	private Double leavedlDaysFull;

	private Double leaveDaysHalf;

	private Double totalLeaveDays;

	private String status;

	private String approveRemark;

	private String approveRemarkMr;

	private String rejectRemark;

	private String rejectRemarkMr;

	private String activeFlag;

	// private List<TrnLeaveApplicationDetailsDao> leaveDetails;

	private String fullName;

	private String firstName;

	private String middleName;

	private String lastName;

	private UUID departmentId;

	private String department;

	private UUID designationId;

	private String remark;

	private List<TrnDocumentListDao> documentList;

	private List<String> documents;

	private List<String> docList;

	private String macAddress;

	private String ispIpAddess;

	private String userFullName;

	private LocalDateTime createDtTm;

	private String latestRemark;

	private UUID senderDesignation;

	private String designation;

}
