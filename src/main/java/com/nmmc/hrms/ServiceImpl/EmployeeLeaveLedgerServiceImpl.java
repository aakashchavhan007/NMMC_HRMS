package com.nmmc.hrms.ServiceImpl;

import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import com.nmmc.hrms.Dao.EmployeeLeaveLedgerDao;
import com.nmmc.hrms.Dao.EmployeeLeaveLedgerHistoryDao;
import com.nmmc.hrms.Dao.MstLeaveAllocationListDao;
import com.nmmc.hrms.Dao.ResponseDao;
import com.nmmc.hrms.Dao.ScrutinyNoteDao;
import com.nmmc.hrms.Dao.TrnDocumentListDao;
import com.nmmc.hrms.Dao.TrnLeaveApplicationDao;
import com.nmmc.hrms.Entity.EmployeeAttendance;
import com.nmmc.hrms.Entity.EmployeeLeaveLedger;
import com.nmmc.hrms.Entity.EmployeeLeaveLedgerHistory;
import com.nmmc.hrms.Entity.LocationMaster;
import com.nmmc.hrms.Entity.MstEmployee;
import com.nmmc.hrms.Entity.MstLeaveAllocation;
import com.nmmc.hrms.Entity.MstLeaveAllocationList;
import com.nmmc.hrms.Entity.MstLeaveType;
import com.nmmc.hrms.Entity.Responsibility;
import com.nmmc.hrms.Entity.ScrutinyNote;
import com.nmmc.hrms.Entity.TrnAttendance;
import com.nmmc.hrms.Entity.TrnLeaveApplication;
import com.nmmc.hrms.Entity.UserResponsibilitiesDetails;
import com.nmmc.hrms.Repository.EmployeeLeaveLedgerRepository;
import com.nmmc.hrms.Repository.EmployeeLeaveLedgerViewRepository;
import com.nmmc.hrms.Repository.LocationMasterRepository;
import com.nmmc.hrms.Repository.MstEmployeeRepository;
import com.nmmc.hrms.Repository.MstEmployeeViewRepository;
import com.nmmc.hrms.Repository.MstLeaveAllocationListRepository;
import com.nmmc.hrms.Repository.MstLeaveAllocationRepository;
import com.nmmc.hrms.Repository.MstLeaveTypeRepository;
import com.nmmc.hrms.Repository.ResponsibilityRepository;
import com.nmmc.hrms.Repository.ScrutinyNoteRepository;
import com.nmmc.hrms.Repository.TrnAttendanceRepository;
import com.nmmc.hrms.Repository.UserResponsibilitiesDetailsRepository;
import com.nmmc.hrms.Repository.UserRoleRepository;
import com.nmmc.hrms.Repository.EmployeeAttendanceRepository;
import com.nmmc.hrms.Repository.EmployeeLeaveLedgerHistoryRepository;
import com.nmmc.hrms.Service.EmployeeLeaveLedgerService;
import com.nmmc.hrms.Service.TrnDocumentListService;
import com.nmmc.hrms.Utils.AuthenticationUtil;
import com.nmmc.hrms.Utils.CommonConstants;
import com.nmmc.hrms.Utils.DocumentUtil;
import com.nmmc.hrms.Utils.IpGenerator;
import com.nmmc.hrms.Utils.Util;
import com.nmmc.hrms.Utils.WorkFlowHelperClass;
import com.nmmc.hrms.views.EmployeeLeaveLedgerView;
import com.nmmc.hrms.views.MstEmployeeView;
import com.nmmc.hrms.views.TrnLeaveApplicationView;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Expression;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Service
public class EmployeeLeaveLedgerServiceImpl implements EmployeeLeaveLedgerService {

	@Autowired
	private TrnDocumentListService trnDocumentListService;

	@Autowired
	private DocumentUtil documentUtil;

	@Autowired
	private MstEmployeeRepository mstEmployeeRepository;

	@Autowired
	private TrnAttendanceRepository trnAttendanceRepository;

	@Autowired
	private MstLeaveTypeRepository mstLeaveTypeRepository;

	@Autowired
	private WorkFlowHelperClass workFlowHelperClass;

	@Autowired
	private EmployeeLeaveLedgerRepository employeeLeaveLedgerRepository;

	@Autowired
	private EmployeeLeaveLedgerHistoryRepository employeeLeaveLedgerHistoryRepository;

	@Autowired
	private AuthenticationUtil authenticationUtil;

	@Autowired
	private IpGenerator ipGenerator;

	@Autowired
	private Util util;

	@Value("${app.documentStorageFinal}")
	private String DOCUMENT_STORAGE_FINAL;
	
	@Autowired
	private MstLeaveAllocationRepository  mstLeaveAllocationRepository;
	
	@Autowired
	private MstLeaveAllocationListRepository  mstLeaveAllocationListRepository;
	
	@Autowired
	private EmployeeLeaveLedgerViewRepository employeeLeaveLedgerViewRepository;
	
	@Autowired
	private LocationMasterRepository  locationMasterRepository;
	
	@Autowired
	private EmployeeAttendanceRepository  employeeAttendanceRepository;
	
	@Autowired
	private ScrutinyNoteRepository  scrutinyNoteRepository;
	
    @Autowired
	private UserResponsibilitiesDetailsRepository userResponsibilitiesDetailsRepository;
    

    @Autowired
    private MstEmployeeViewRepository mstEmployeeViewRepository;
    
    @Autowired
    private ResponsibilityRepository responsibilityRepository;
    
    @Autowired
    private UserRoleRepository  userRoleRepository;
    
    @Autowired
    private EmployeeLeaveLedgerService employeeLeaveLedgerService;
	    
	
//	@Override
//	public String save(@Valid EmployeeLeaveLedgerDao employeeLeaveLedgerDao, HttpServletRequest request) {
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UUID userId = authenticationUtil.getUserIdFromAuthentication(authentication);
//	    String username = authenticationUtil.getUsernameFromAuthentication(authentication, userId);
//	    UUID designationId = authenticationUtil.getDesignationIdFromAuthentication(authentication);
//
//	    JsonObject ob = new JsonObject();
//	    try {
//	        EmployeeLeaveLedger employeeLeaveLedger;
//
//	        if (employeeLeaveLedgerDao.getId() != null) {
//	            employeeLeaveLedger = employeeLeaveLedgerRepository.findById(employeeLeaveLedgerDao.getId())
//	                    .orElse(null);
//	            if(employeeLeaveLedger.getStatus().equalsIgnoreCase(CommonConstants.ApplicationStatus.APPLICATION_DRAFT)) {
//	            	employeeLeaveLedgerDao.setStatus(CommonConstants.ApplicationStatus.APPLICATION_CREATED);
//	            }
//
//	            if (employeeLeaveLedger == null) {
//	                ob.addProperty("status", "Failed");
//	                ob.addProperty("message", "Data not found");
//	                return ob.toString();
//	            }
//	        } else {
//	            employeeLeaveLedger = new EmployeeLeaveLedger();
//	            UUID timebaseUUID = UUID.randomUUID();
//	            employeeLeaveLedgerDao.setId(timebaseUUID);
//	            employeeLeaveLedgerDao.setSenderDesignation(designationId);
//	            employeeLeaveLedgerDao.setStatus(CommonConstants.ApplicationStatus.APPLICATION_DRAFT);
//	            employeeLeaveLedgerDao.setApplicationDate(LocalDateTime.now());
//	        }
//
//            //handle document
//            if(employeeLeaveLedgerDao.getDocumentList() != null) {
//            	for(TrnDocumentListDao documentListDao : employeeLeaveLedgerDao.getDocumentList()) {
//            		documentListDao.setFilePath(util.decodeBase64ToFile(documentListDao.getDocumentName(), DOCUMENT_STORAGE_FINAL, documentListDao.getFilePath()));
//            		if(documentListDao.getId() == null) {
//            			documentListDao.setTransactionId(employeeLeaveLedgerDao.getId());
//            			documentListDao.setTransactionKey("LEAVE_LEDGER");
//            		}
//            		trnDocumentListService.save(documentListDao);
//            	}
//            }
//
//	        // Save ledger
//	       // BeanUtils.copyProperties(employeeLeaveLedgerDao, employeeLeaveLedger);
//            BeanUtils.copyProperties(employeeLeaveLedgerDao, employeeLeaveLedger, 
//            	    "createDtTm", "updateDtTm", "createdUserId", "updateUserId", "activeFlag");
//	        employeeLeaveLedgerRepository.save(employeeLeaveLedger);
//
//	        // Save ledger history
//	        EmployeeLeaveLedgerHistory history = new EmployeeLeaveLedgerHistory();
//	        BeanUtils.copyProperties(employeeLeaveLedgerDao, history);
//	        history.setId(UUID.randomUUID());
//	        history.setEmployeeLeaveLedgerId(employeeLeaveLedgerDao.getId());
//	        history.setMacAddress(ipGenerator.getMacId());
//	        history.setIspIpAddess(ipGenerator.getPublicIp());
//	        history.setCreatedUserId(userId);
//	        history.setUserFullName(username);
//	        history.setSenderDesignation(designationId);
//
//	        employeeLeaveLedgerHistoryRepository.save(history);
//
//	        // If leave is approved, update allocation and attendance
//	        if ("APPLICATION_CREATED".equalsIgnoreCase(employeeLeaveLedger.getStatus())) {
//	            UUID leaveTypeId = employeeLeaveLedger.getLeaveTypeId();
//	            Long employeeId = employeeLeaveLedger.getEmployeeId();
//
//	            // Fetch leave type short code dynamically
//	            String leaveTypeShortCode = "";
//	            Optional<MstLeaveType> leaveTypeOpt = mstLeaveTypeRepository.findById(leaveTypeId);
//	            if (leaveTypeOpt.isPresent()) {
//	                leaveTypeShortCode = leaveTypeOpt.get().getShortCode();
//	            }
//
//	            // Leave allocation update
//	            Optional<MstLeaveAllocation> allocationMasterOpt = mstLeaveAllocationRepository.findByEmpId(employeeId);
//	            if (allocationMasterOpt.isPresent()) {
//	                UUID mstLeaveAllocationId = allocationMasterOpt.get().getId();
//	                Optional<MstLeaveAllocationList> allocationOpt = mstLeaveAllocationListRepository
//	                        .findByLeaveTypeIdAndMstLeaveAllocationId(leaveTypeId, mstLeaveAllocationId);
//
//	                if (allocationOpt.isPresent()) {
//	                    MstLeaveAllocationList allocation = allocationOpt.get();
//	                    BigDecimal totalLeaveDays = BigDecimal.valueOf(employeeLeaveLedger.getTotalLeaveDays() != null
//	                            ? employeeLeaveLedger.getTotalLeaveDays()
//	                            : 0.0);
//
//	                    BigDecimal usedLeaves = allocation.getUsedLeaves() != null ? allocation.getUsedLeaves() : BigDecimal.ZERO;
//	                    BigDecimal unUsedLeaves = allocation.getUnUsedLeaves() != null ? allocation.getUnUsedLeaves() : BigDecimal.ZERO;
//
//	                    allocation.setUsedLeaves(usedLeaves.add(totalLeaveDays));
//	                    allocation.setUnUsedLeaves(unUsedLeaves.subtract(totalLeaveDays));
//
//	                    mstLeaveAllocationListRepository.save(allocation);
//	                }
//	            }
//
//	            // Attendance update for each day in date range
//	            LocalDateTime fromDate = employeeLeaveLedger.getApplFromDate();
//	            LocalDateTime toDate = employeeLeaveLedger.getApplToDate();
//	            LocalDateTime currentDate = fromDate;
//
//	            while (!currentDate.isAfter(toDate)) {
//	                if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
//	                    Long attnMonth = Long.valueOf(currentDate.getYear() + String.format("%02d", currentDate.getMonthValue()));
//
//	                    EmployeeAttendance attendance = employeeAttendanceRepository
//	                            .findByEmployeeIdAndAttnMonth(employeeId.toString(), attnMonth);
//
//	                    if (attendance != null) {
//	                        // If HPL, increment half pay leave
//	                        if ("HPL".equalsIgnoreCase(leaveTypeShortCode)) {
//	                            attendance.setLeaveHalfPay(attendance.getLeaveHalfPay() + 1);
//	                        } 
//	                        // If other full pay leaves but NOT CL or CO
//	                        else if (isFullPayLeave(leaveTypeShortCode)
//	                                && !"CL".equalsIgnoreCase(leaveTypeShortCode)
//	                                && !"CO".equalsIgnoreCase(leaveTypeShortCode)) {
//	                            attendance.setLeaveFullPay(attendance.getLeaveFullPay() + 1);
//	                        }
//	                        // If neither, increment unpaid leave
//	                        else if (!"CL".equalsIgnoreCase(leaveTypeShortCode) && !"CO".equalsIgnoreCase(leaveTypeShortCode)) {
//	                            attendance.setLeaveUnpaid(attendance.getLeaveUnpaid() + 1);
//	                        }
//
//	                        // No adjustment to 'present' for CL and CO now — removed this block
//
//	                        // For other leaves (excluding CL and CO) — decrease present if applicable
//	                        if (!"CL".equalsIgnoreCase(leaveTypeShortCode)
//	                                && !"CO".equalsIgnoreCase(leaveTypeShortCode)
//	                                && attendance.getPresent() != null
//	                                && attendance.getPresent() > 0) {
//	                            attendance.setPresent(attendance.getPresent() - 1);
//	                        }
//
//	                        employeeAttendanceRepository.save(attendance);
//	                    }
//	                }
//	                currentDate = currentDate.plusDays(1);
//	            }
//	        }
//	        ob.addProperty("status", "success");
//	        ob.addProperty("message", "Data Saved Sucessfully");
//	        ob.addProperty("appStatus",employeeLeaveLedgerDao.getStatus() );
//	        ob.addProperty("id", employeeLeaveLedgerDao.getId().toString());
//
//
//	        return ob.toString();
//
//	    } catch (Exception ex) {
//	        ob.addProperty("status", "failure");
//	        ob.addProperty("message", ex.getMessage());
//	        return ob.toString();
//	    }
//	}	
//above save api code is correct 
    
    @Override
    public String save(@Valid EmployeeLeaveLedgerDao employeeLeaveLedgerDao, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = authenticationUtil.getUserIdFromAuthentication(authentication);
        String username = authenticationUtil.getUsernameFromAuthentication(authentication, userId);
        UUID designationId = authenticationUtil.getDesignationIdFromAuthentication(authentication);

        JsonObject ob = new JsonObject();
        try {
            EmployeeLeaveLedger employeeLeaveLedger;

            Gson gson = new Gson();
            JsonObject ob1 = new JsonObject();

            List<EmployeeLeaveLedger> overlappingLeaves;

            if (employeeLeaveLedgerDao.getId() != null) {
                // UPDATE case
                overlappingLeaves = employeeLeaveLedgerRepository.findOverlappingLeavesForUpdate(
                    employeeLeaveLedgerDao.getEmployeeId(),
                    employeeLeaveLedgerDao.getApplFromDate().toLocalDate(),  // convert to LocalDate
                    employeeLeaveLedgerDao.getApplToDate().toLocalDate(),    // convert to LocalDate
                    employeeLeaveLedgerDao.getId()
                );
            } else {
                // CREATE case
                overlappingLeaves = employeeLeaveLedgerRepository.findOverlappingLeavesForCreate(
                    employeeLeaveLedgerDao.getEmployeeId(),
                    employeeLeaveLedgerDao.getApplFromDate().toLocalDate(),  // convert to LocalDate
                    employeeLeaveLedgerDao.getApplToDate().toLocalDate()     // convert to LocalDate
                );
            }

            // If overlap found, return failure message
            if (!overlappingLeaves.isEmpty()) {
                EmployeeLeaveLedger existingLeave = overlappingLeaves.get(0);
                ob1.addProperty("status", "Failed");
                ob1.addProperty("message", "Leave dates overlap with existing leave from "
                    + existingLeave.getApplFromDate().toLocalDate() + " to "
                    + existingLeave.getApplToDate().toLocalDate());
                return ob1.toString();
            }
            // ✅ If no overlap, continue saving



            if (employeeLeaveLedgerDao.getId() != null) {
                employeeLeaveLedger = employeeLeaveLedgerRepository.findById(employeeLeaveLedgerDao.getId()).orElse(null);

                if (employeeLeaveLedger != null &&
                    CommonConstants.ApplicationStatus.APPLICATION_DRAFT.equalsIgnoreCase(employeeLeaveLedger.getStatus())) {
                    employeeLeaveLedgerDao.setStatus(CommonConstants.ApplicationStatus.APPLICATION_CREATED);
                }

                if (employeeLeaveLedger == null) {
                    ob.addProperty("status", "Failed");
                    ob.addProperty("message", "Data not found");
                    return ob.toString();
                }
            } else {
                employeeLeaveLedger = new EmployeeLeaveLedger();
                UUID timebaseUUID = UUID.randomUUID();
                employeeLeaveLedgerDao.setId(timebaseUUID);
                employeeLeaveLedgerDao.setSenderDesignation(designationId);
                employeeLeaveLedgerDao.setStatus(CommonConstants.ApplicationStatus.APPLICATION_DRAFT);
                employeeLeaveLedgerDao.setApplicationDate(LocalDateTime.now());
            }

            // 🔽 Save documents
            if (employeeLeaveLedgerDao.getDocumentList() != null) {
                for (TrnDocumentListDao documentListDao : employeeLeaveLedgerDao.getDocumentList()) {
                    documentListDao.setFilePath(util.decodeBase64ToFile(documentListDao.getDocumentName(),
                            DOCUMENT_STORAGE_FINAL, documentListDao.getFilePath()));
                    if (documentListDao.getId() == null) {
                        documentListDao.setTransactionId(employeeLeaveLedgerDao.getId());
                        documentListDao.setTransactionKey("LEAVE_LEDGER");
                    }
                    trnDocumentListService.save(documentListDao);
                }
            }

            // 🔽 Save main ledger
            BeanUtils.copyProperties(employeeLeaveLedgerDao, employeeLeaveLedger,
                    "createDtTm", "updateDtTm", "createdUserId", "updateUserId", "activeFlag");
            employeeLeaveLedgerRepository.save(employeeLeaveLedger);

            // 🔽 Save history
            EmployeeLeaveLedgerHistory history = new EmployeeLeaveLedgerHistory();
            BeanUtils.copyProperties(employeeLeaveLedgerDao, history);
            history.setId(UUID.randomUUID());
            history.setEmployeeLeaveLedgerId(employeeLeaveLedgerDao.getId());
            history.setMacAddress(ipGenerator.getMacId());
            history.setIspIpAddess(ipGenerator.getPublicIp());
            history.setCreatedUserId(userId);
            history.setUserFullName(username);
            history.setSenderDesignation(designationId);
            employeeLeaveLedgerHistoryRepository.save(history);

            // 🔽 If application was just created — update leave allocation and attendance
            if ("APPLICATION_CREATED".equalsIgnoreCase(employeeLeaveLedger.getStatus())) {
                UUID leaveTypeId = employeeLeaveLedger.getLeaveTypeId();
                Long employeeId = employeeLeaveLedger.getEmployeeId();

                // Get leave short code
                String leaveTypeShortCode = "";
                Optional<MstLeaveType> leaveTypeOpt = mstLeaveTypeRepository.findById(leaveTypeId);
                if (leaveTypeOpt.isPresent()) {
                    leaveTypeShortCode = leaveTypeOpt.get().getShortCode();
                }

                // Leave allocation update
                Optional<MstLeaveAllocation> allocationMasterOpt = mstLeaveAllocationRepository.findByEmpId(employeeId);
                if (allocationMasterOpt.isPresent()) {
                    UUID mstLeaveAllocationId = allocationMasterOpt.get().getId();
                    Optional<MstLeaveAllocationList> allocationOpt = mstLeaveAllocationListRepository
                            .findByLeaveTypeIdAndMstLeaveAllocationId(leaveTypeId, mstLeaveAllocationId);

                    if (allocationOpt.isPresent()) {
                        MstLeaveAllocationList allocation = allocationOpt.get();
                        BigDecimal totalLeaveDays = BigDecimal.valueOf(employeeLeaveLedger.getTotalLeaveDays() != null
                                ? employeeLeaveLedger.getTotalLeaveDays()
                                : 0.0);
                        BigDecimal usedLeaves = allocation.getUsedLeaves() != null ? allocation.getUsedLeaves() : BigDecimal.ZERO;
                        BigDecimal unUsedLeaves = allocation.getUnUsedLeaves() != null ? allocation.getUnUsedLeaves() : BigDecimal.ZERO;

                        allocation.setUsedLeaves(usedLeaves.add(totalLeaveDays));
                        allocation.setUnUsedLeaves(unUsedLeaves.subtract(totalLeaveDays));
                        mstLeaveAllocationListRepository.save(allocation);
                    }
                }

                // Attendance update
                LocalDateTime fromDate = employeeLeaveLedger.getApplFromDate();
                LocalDateTime toDate = employeeLeaveLedger.getApplToDate();
                LocalDateTime currentDate = fromDate;

                while (!currentDate.isAfter(toDate)) {
                    if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                        Long attnMonth = Long.valueOf(currentDate.getYear() + String.format("%02d", currentDate.getMonthValue()));
                        EmployeeAttendance attendance = employeeAttendanceRepository
                                .findByEmployeeIdAndAttnMonth(employeeId.toString(), attnMonth);

                        if (attendance != null) {
                            if ("HPL".equalsIgnoreCase(leaveTypeShortCode)) {
                                attendance.setLeaveHalfPay(attendance.getLeaveHalfPay() + 1);
                            } else if (isFullPayLeave(leaveTypeShortCode)
                                    && !"CL".equalsIgnoreCase(leaveTypeShortCode)
                                    && !"CO".equalsIgnoreCase(leaveTypeShortCode)) {
                                attendance.setLeaveFullPay(attendance.getLeaveFullPay() + 1);
                            } else if (!"CL".equalsIgnoreCase(leaveTypeShortCode)
                                    && !"CO".equalsIgnoreCase(leaveTypeShortCode)) {
                                attendance.setLeaveUnpaid(attendance.getLeaveUnpaid() + 1);
                            }

                            if (!"CL".equalsIgnoreCase(leaveTypeShortCode)
                                    && !"CO".equalsIgnoreCase(leaveTypeShortCode)
                                    && attendance.getPresent() != null
                                    && attendance.getPresent() > 0) {
                                attendance.setPresent(attendance.getPresent() - 1);
                            }

                            employeeAttendanceRepository.save(attendance);
                        }
                    }
                    currentDate = currentDate.plusDays(1);
                }
            }

            ob.addProperty("status", "success");
            ob.addProperty("message", "Data Saved Successfully");
            ob.addProperty("appStatus", employeeLeaveLedgerDao.getStatus());
            ob.addProperty("id", employeeLeaveLedgerDao.getId().toString());
            return ob.toString();

        } catch (Exception ex) {
            ob.addProperty("status", "Failed");
            ob.addProperty("message", ex.getMessage());
            return ob.toString();
        }
    }
	
	private boolean isFullPayLeave(String shortCode) {
	    return Arrays.asList(
	            "EOL_WITH_MED", "LND", "LUC", "SUSP", "OD", "CL", "CML", "SDL", "EL",
	            "CO", "SCL", "HL", "PTL", "MTP", "RH", "EOL_NO_MED", "MTL", "CCL", "ML",
	            "TBL", "CNL", "BDL", "SPL", "EOL", "CAL", "FPL", "SL", "LPL", "PRL",
	            "AIDSL", "DBL"
	    ).contains(shortCode.toUpperCase());
	}

	
	private String getLeaveTypeName(UUID leaveTypeId) {
	    if (leaveTypeId == null) return "A"; // or any default fallback string
	    String leaveTypeName = mstLeaveTypeRepository.getLeaveTypeNameById(leaveTypeId);
	    return (leaveTypeName != null && !leaveTypeName.trim().isEmpty()) ? leaveTypeName : "A";
	}


	
//	
//	@Override
//	public ResponseDao getAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir, String searchParam,
//	                          Locale locale, HttpServletRequest request) {
//
//	    ResponseDao responseDao = new ResponseDao();
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UUID userId = authenticationUtil.getUserIdFromAuthentication(authentication);
//
//	    UUID superAdminId = UUID.fromString("018d8776-b566-7d3c-9904-47166ad9dc0a");
//	    boolean isSuperAdmin = userId.equals(superAdminId);
//
//	    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
//	            ? Sort.by(sortBy).ascending()
//	            : Sort.by(sortBy).descending();
//	    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//
//	    if (isSuperAdmin) {
//	        Specification<EmployeeLeaveLedgerView> spec = buildLedgerSpecification1(searchParam, null, null, null, true);
//	        Page<EmployeeLeaveLedgerView> page = employeeLeaveLedgerViewRepository.findAll(spec, pageable);
//	        return buildResponse(responseDao, page, searchParam);
//	    }
//
//	    Optional<MstEmployeeView> loggedInEmpOpt = mstEmployeeViewRepository.findByUsrId(userId);
//	    if (!loggedInEmpOpt.isPresent()) {
//	        responseDao.setEmployeeLeaveLedgerDaoList(Collections.emptyList());
//	        return responseDao;
//	    }
//
//	    MstEmployeeView loggedInEmployee = loggedInEmpOpt.get();
//	    UUID ddoId = loggedInEmployee.getDdoId();
//	    if (ddoId == null) {
//	        responseDao.setEmployeeLeaveLedgerDaoList(Collections.emptyList());
//	        return responseDao;
//	    }
//
//	    List<Long> allowedLevels = userResponsibilitiesDetailsRepository.findLevelsByUserId(userId);
//	    if (allowedLevels.isEmpty()) {
//	        responseDao.setEmployeeLeaveLedgerDaoList(Collections.emptyList());
//	        return responseDao;
//	    }
//
//	    List<UUID> ddoIds = userResponsibilitiesDetailsRepository.findDdoIdsByUserId(userId);
//
//	    if (ddoIds.isEmpty()) {
//	        responseDao.setEmployeeLeaveLedgerDaoList(Collections.emptyList());
//	        return responseDao;
//	    }
//
//	    // get employee ids under multiple DDO IDs
//	    List<String> employeeIdsUnderDdo = mstEmployeeViewRepository.findEmployeeIdsByDdoIds(ddoIds);
//
//	   
//	    Specification<EmployeeLeaveLedgerView> spec = buildLedgerSpecification1(
//	    	    searchParam, allowedLevels, employeeIdsUnderDdo, ddoId, false
//	    	);
//
//
//	    Page<EmployeeLeaveLedgerView> page = employeeLeaveLedgerViewRepository.findAll(spec, pageable);
//	    return buildResponse(responseDao, page, searchParam);
//	}
//	
//	private Specification<EmployeeLeaveLedgerView> buildLedgerSpecification1(
//	        String searchParam,
//	        List<Long> allowedLevels,
//	        List<String> employeeIdsUnderDdo,
//	        UUID ddoId,
//	        boolean isSuperAdmin) {
//		
//		
//	    return (root, query, cb) -> {
//	        List<Predicate> predicates = new ArrayList<>();
//
//	        // Search conditions
//	        if (searchParam != null && !searchParam.trim().isEmpty()) {
//	            String pattern = "%" + searchParam.trim().toUpperCase() + "%";
//	            predicates.add(cb.or(
//	                    cb.like(cb.upper(root.get("reason")), pattern),
//	                    cb.like(cb.upper(root.get("employeeId").as(String.class)), pattern),
//	                    cb.like(cb.upper(root.get("status")), pattern),
//	                    cb.like(cb.upper(root.get("fullName")), pattern),
//	                    cb.like(cb.upper(root.get("department")), pattern),
//	                    cb.like(cb.upper(root.get("leaveTypeName")), pattern)
//	            ));
//	        }
//
//	        // Super admin — show all except approved, rejected, -
//	        if (isSuperAdmin) {
//	            Predicate excludeStatuses = root.get("status").in(
//	                    CommonConstants.ApplicationStatus.LEAVE_APPROVED,
//	                    CommonConstants.ApplicationStatus.LEAVE_REJECTED,
//	                    "-"
//	            ).not();
//	            predicates.add(excludeStatuses);
//	            return cb.and(predicates.toArray(new Predicate[0]));
//	        }
//
//	        // No allowed levels or no employee list
//	        if (allowedLevels == null || allowedLevels.isEmpty() ||
//	                employeeIdsUnderDdo == null || employeeIdsUnderDdo.isEmpty()) {
//	            predicates.add(cb.disjunction());
//	            return cb.and(predicates.toArray(new Predicate[0]));
//	        }
//
//	        // Common expressions
//	        Expression<Character> locationTypeExpr = root.get("locationType");
//	        Expression<String> designationExpr = root.get("designation");
//	        Expression<String> leaveTypeNameExpr = root.get("leaveTypeName");
//	        Expression<Long> totalLeaveDaysExpr = root.get("totalLeaveDays");
//
//	        Predicate statusPredicate = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.APPLICATION_CREATED);
//	        Predicate employeeIdPredicate = root.get("employeeId").in(employeeIdsUnderDdo);
//
//	        List<String> specialLeaveTypesUpper = Arrays.asList(
//	                "SPECIAL LEAVE", "SPECIAL CASUAL LEAVE", "SPECIAL DISABILITY LEAVE"
//	        );
//
//	        Expression<String> leaveTypeNameUpperExpr = cb.upper(cb.trim(leaveTypeNameExpr));
//	        Expression<Integer> totalLeaveDaysInt = cb.toInteger(totalLeaveDaysExpr);
//
//	        // Priority-based one-level-only logic
//	        if (allowedLevels.contains(1L)) {
//	            // Level 1: Branch Officer
//	            Predicate isB = cb.equal(locationTypeExpr, 'B');
//	            Predicate specialLeave = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper);
//
//	            // Condition 1: Location is B
//	            Predicate p1 = cb.and(employeeIdPredicate, statusPredicate, isB);
//
//	            // Condition 2: Location is B and Leave Type is in special leaves
//	            Predicate p2 = cb.and(employeeIdPredicate, statusPredicate, isB, specialLeave);
//
//	            // Add both conditions using OR
//	            predicates.add(cb.or(p1, p2));
//	        }else if (allowedLevels.contains(2L)) {
//	            // Level 2: Reporting Officer
//	            Predicate isN = cb.equal(locationTypeExpr, 'N');
//	            Predicate leaveTypeNotNull = cb.isNotNull(leaveTypeNameExpr);
//	            Predicate notSpecialLeave = cb.not(leaveTypeNameUpperExpr.in(specialLeaveTypesUpper));
//                 System.out.println(isN);
//                 System.out.println(leaveTypeNameExpr);
//
//	            predicates.add(cb.and(
//	                    employeeIdPredicate,
//	                    statusPredicate,
//	                    isN,
//	                    leaveTypeNotNull,
//	                    notSpecialLeave
//	            ));
//
//	        } else if (allowedLevels.contains(3L)) {
//	            // Level 3: HOD
//	            Predicate isN = cb.equal(locationTypeExpr, 'N');
//	            Predicate isB = cb.equal(locationTypeExpr, 'B');
//	            Predicate specialLeave = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper);
//	            Predicate leaveDaysBetween90And180 = cb.and(
//	                    cb.greaterThanOrEqualTo(totalLeaveDaysInt, 90),
//	                    cb.lessThan(totalLeaveDaysInt, 180)
//	            );
//	            Predicate leaveDaysGreaterThan90 = cb.greaterThan(totalLeaveDaysInt, 90);
//
//	            // condition 1: APPLICATION_CREATED + N + Special Leave
//	            Predicate condition1 = cb.and(
//	                    employeeIdPredicate,
//	                    statusPredicate,
//	                    isN,
//	                    specialLeave
//	            );
//
//	            // condition 2: BRANCH_OFFICER_APPROVED + B + leaveDays 90-180
//	            Predicate statusBranchAndBOfficerApproved = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED);
//	            Predicate condition2 = cb.and(
//	                    employeeIdPredicate,
//	                    statusBranchAndBOfficerApproved,
//	                    isB,
//	                    leaveDaysBetween90And180
//	            );
//
//	            // condition 3: BRANCH_OFFICER_APPROVED + B + Special Leave
//	            Predicate condition3 = cb.and(
//	                    employeeIdPredicate,
//	                    statusBranchAndBOfficerApproved,
//	                    isB,
//	                    specialLeave
//	            );
//
//	            // condition 4: REPORTING_OFFICER_APPROVED + N + leaveDays 90-180
//	            Predicate statusReportingOfficerApproved = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.REPORTING_OFFICER_APPROVED);
//	            Predicate condition4 = cb.and(
//	                    employeeIdPredicate,
//	                    statusReportingOfficerApproved,
//	                    isN,
//	                    leaveDaysBetween90And180
//	            );
//
//	            // ✅ condition 5: (BRANCH_OFFICER_APPROVED or REPORTING_OFFICER_APPROVED) + leaveDays > 90
//	            Predicate statusBranchOrReportingOfficerApproved = root.get("status").in(
//	                    CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED,
//	                    CommonConstants.ApplicationStatus.REPORTING_OFFICER_APPROVED
//	            );
//	            Predicate condition5 = cb.and(
//	                    employeeIdPredicate,
//	                    statusBranchOrReportingOfficerApproved,
//	                    leaveDaysGreaterThan90
//	            );
//
//	            // Add all conditions with OR
//	            predicates.add(cb.or(condition1, condition2, condition3, condition4, condition5));	            
//	        }if (allowedLevels.contains(4L)) {
//	            // ADDITIONAL COMMISSIONER
//
//	            // ✅ Condition 1: DMC_APPROVED and totalLeaveDays > 365
//	            Predicate dmcApprovedAndLeaveDaysGreaterThan365 = cb.and(
//	                employeeIdPredicate,
//	                cb.equal(root.get("status"), CommonConstants.ApplicationStatus.DMC_APPROVED),
//	                cb.greaterThan(totalLeaveDaysExpr, 365L)
//	            );
//	            predicates.add(dmcApprovedAndLeaveDaysGreaterThan365);
//
//	            // ✅ Condition 2: special leave type and totalLeaveDays >= 365
//	            Predicate specialLeaveType = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper); // make sure this is defined
//	            Predicate specialLeaveDaysCondition = cb.and(
//	                employeeIdPredicate,
//	                specialLeaveType,
//	                cb.greaterThanOrEqualTo(totalLeaveDaysExpr, 365L)
//	            );
//	            predicates.add(specialLeaveDaysCondition);
//	        
//	 	       } if (allowedLevels.contains(7L)) {
//	 	    	    // DMC Officer logic
//
//	 	    	    Predicate statusHodApproved = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.HOD_APPROVED);
//
//	 	    	    // ✅ Condition 1: totalLeaveDays > 180 (removed ≤ 365)
//	 	    	    Predicate totalLeaveDaysGreaterThan180 = cb.greaterThan(totalLeaveDaysExpr, 180L);
//	 	    	    Predicate condition1 = cb.and(statusHodApproved, totalLeaveDaysGreaterThan180);
//
//	 	    	    // ✅ Condition 2: leaveType is special leave AND status = HOD_APPROVED
//	 	    	    Predicate specialLeaveType = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper);
//	 	    	    Predicate condition2 = cb.and(statusHodApproved, specialLeaveType);
//
//	 	    	    // Combine both with OR
//	 	    	    Predicate dmcCondition = cb.or(condition1, condition2);
//
//	 	    	    predicates.add(dmcCondition);
//	 	    	}
//
//	        if (allowedLevels.contains(9L)) {
//	        	// commissioner
//	            Predicate statusHodApproved = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED);
//	            predicates.add(statusHodApproved);
//	        }
//
//
//	        return cb.and(predicates.toArray(new Predicate[0]));
//	    };
//	}
	
////perfect getAll with work flow  but HOD not include 
	
	@Override
	public ResponseDao getAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir, String searchParam,
	                          Locale locale, HttpServletRequest request) {

	    ResponseDao responseDao = new ResponseDao();
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UUID userId = authenticationUtil.getUserIdFromAuthentication(authentication);

	    UUID superAdminId = UUID.fromString("018d8776-b566-7d3c-9904-47166ad9dc0a");
	    boolean isSuperAdmin = userId.equals(superAdminId);

	    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
	            ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();
	    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

	    if (isSuperAdmin) {
	        Specification<EmployeeLeaveLedgerView> spec = buildLedgerSpecification1(searchParam, null, null, null, false, true);
	        Page<EmployeeLeaveLedgerView> page = employeeLeaveLedgerViewRepository.findAll(spec, pageable);
	        return buildResponse(responseDao, page, searchParam);
	    }

	    Optional<MstEmployeeView> loggedInEmpOpt = mstEmployeeViewRepository.findByUsrId(userId);
	    if (!loggedInEmpOpt.isPresent()) {
	        responseDao.setEmployeeLeaveLedgerDaoList(Collections.emptyList());
	        return responseDao;
	    }

	    MstEmployeeView loggedInEmployee = loggedInEmpOpt.get();
	    UUID ddoId = loggedInEmployee.getDdoId();
	    if (ddoId == null) {
	        responseDao.setEmployeeLeaveLedgerDaoList(Collections.emptyList());
	        return responseDao;
	    }

	    List<Long> allowedLevels = userResponsibilitiesDetailsRepository.findLevelsByUserId(userId);
	    if (allowedLevels.isEmpty()) {
	        responseDao.setEmployeeLeaveLedgerDaoList(Collections.emptyList());
	        return responseDao;
	    }

	    List<UUID> ddoIds = userResponsibilitiesDetailsRepository.findDdoIdsByUserId(userId);
	    if (ddoIds.isEmpty()) {
	        responseDao.setEmployeeLeaveLedgerDaoList(Collections.emptyList());
	        return responseDao;
	    }

	    List<String> employeeIdsUnderDdo = mstEmployeeViewRepository.findEmployeeIdsByDdoIds(ddoIds);

	    List<Long> empIdsLong = employeeIdsUnderDdo.stream()
	            .map(id -> {
	                try {
	                    return Long.parseLong(id);
	                } catch (NumberFormatException e) {
	                    return null;
	                }
	            })
	            .filter(Objects::nonNull)
	            .collect(Collectors.toList());

	    List<Long> hodLongIds = userRoleRepository.findHodEmployeeIds(empIdsLong);
	    List<String> hodEmployeeIds = hodLongIds.stream().map(String::valueOf).collect(Collectors.toList());
	    boolean applyLevel4Logic = !hodEmployeeIds.isEmpty();

	    Specification<EmployeeLeaveLedgerView> spec = buildLedgerSpecification1(
	            searchParam, allowedLevels, employeeIdsUnderDdo, hodEmployeeIds, applyLevel4Logic, false
	    );

	    Page<EmployeeLeaveLedgerView> page = employeeLeaveLedgerViewRepository.findAll(spec, pageable);
	    return buildResponse(responseDao, page, searchParam);
	}

//	private Specification<EmployeeLeaveLedgerView> buildLedgerSpecification1(
//	        String searchParam,
//	        List<Long> allowedLevels,
//	        List<String> employeeIdsUnderDdo,
//	        List<String> hodEmployeeIds,
//	        boolean applyLevel4Logic,
//	        boolean isSuperAdmin) {
//
//	    return (root, query, cb) -> {
//	        List<Predicate> predicates = new ArrayList<>();
//
//	        if (searchParam != null && !searchParam.trim().isEmpty()) {
//	            String pattern = "%" + searchParam.trim().toUpperCase() + "%";
//	            predicates.add(cb.or(
//	                    cb.like(cb.upper(root.get("reason")), pattern),
//	                    cb.like(cb.upper(root.get("employeeId").as(String.class)), pattern),
//	                    cb.like(cb.upper(root.get("status")), pattern),
//	                    cb.like(cb.upper(root.get("fullName")), pattern),
//	                    cb.like(cb.upper(root.get("department")), pattern),
//	                    cb.like(cb.upper(root.get("leaveTypeName")), pattern)
//	            ));
//	        }
//
//	        if (isSuperAdmin) {
//	            Predicate excludeStatuses = root.get("status").in(
//	                    CommonConstants.ApplicationStatus.LEAVE_APPROVED,
//	                    CommonConstants.ApplicationStatus.LEAVE_REJECTED,
//	                    "-"
//	            ).not();
//	            predicates.add(excludeStatuses);
//	            return cb.and(predicates.toArray(new Predicate[0]));
//	        }
//
//	        if (allowedLevels == null || allowedLevels.isEmpty() || employeeIdsUnderDdo == null || employeeIdsUnderDdo.isEmpty()) {
//	            predicates.add(cb.disjunction());
//	            return cb.and(predicates.toArray(new Predicate[0]));
//	        }
//
//	        Expression<Character> locationTypeExpr = root.get("locationType");
//	        Expression<String> designationExpr = root.get("designation");
//	        Expression<String> leaveTypeNameExpr = root.get("leaveTypeName");
//	        Expression<Long> totalLeaveDaysExpr = root.get("totalLeaveDays");
//
//	        Predicate statusPredicate = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.APPLICATION_CREATED);
//	        Predicate employeeIdPredicate = root.get("employeeId").in(employeeIdsUnderDdo);
//
//	        List<String> specialLeaveTypesUpper = Arrays.asList(
//	                "SPECIAL LEAVE", "SPECIAL CASUAL LEAVE", "SPECIAL DISABILITY LEAVE"
//	        );
//
//	        Expression<String> leaveTypeNameUpperExpr = cb.upper(cb.trim(leaveTypeNameExpr));
//	        Expression<Integer> totalLeaveDaysInt = cb.toInteger(totalLeaveDaysExpr);
//
//	        if (allowedLevels.contains(1L)) {
//	            // Level 1: Branch Officer (excluding HODs)
//	            Predicate isB = cb.equal(locationTypeExpr, 'B');
//	            Predicate specialLeave = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper);
//	            Predicate excludeHods = cb.not(root.get("employeeId").in(hodEmployeeIds)); // ❌ Exclude HODs
//
//	            Predicate p1 = cb.and(employeeIdPredicate, statusPredicate, isB, excludeHods);
//	            Predicate p2 = cb.and(employeeIdPredicate, statusPredicate, isB, specialLeave, excludeHods);
//
//	            predicates.add(cb.or(p1, p2));
//	        } else if (allowedLevels.stream().anyMatch(level -> level == 2L)) {
//	            // Level 2: Reporting Officer
//	            Predicate isN = cb.equal(locationTypeExpr, 'N');
//	            Predicate leaveTypeNotNull = cb.isNotNull(leaveTypeNameExpr);
//	            Predicate notSpecialLeave = cb.not(leaveTypeNameUpperExpr.in(specialLeaveTypesUpper));
//	            Predicate excludeHods = cb.not(root.get("employeeId").in(hodEmployeeIds));  // ❌ Exclude HODs
//
//	            predicates.add(cb.and(
//	                employeeIdPredicate,
//	                statusPredicate,
//	                isN,
//	                leaveTypeNotNull,
//	                notSpecialLeave,
//	                excludeHods              // ❌ HODs explicitly excluded
//	            ));
//	        } else if (allowedLevels.contains(3L)) {
//	        	//hod
//	            Predicate isN = cb.equal(locationTypeExpr, 'N');
//	            Predicate isB = cb.equal(locationTypeExpr, 'B');
//	            Predicate specialLeave = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper);
//	            Predicate leaveDaysBetween90And180 = cb.and(cb.greaterThanOrEqualTo(totalLeaveDaysInt, 90), cb.lessThan(totalLeaveDaysInt, 180));
//	            Predicate leaveDaysGreaterThan90 = cb.greaterThan(totalLeaveDaysInt, 90);
//
//	            Predicate condition1 = cb.and(employeeIdPredicate, statusPredicate, isN, specialLeave);
//	            Predicate statusBranchApproved = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED);
//	            Predicate statusReportingApproved = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.REPORTING_OFFICER_APPROVED);
//
//	            Predicate condition2 = cb.and(employeeIdPredicate, statusBranchApproved, isB, leaveDaysBetween90And180);
//	            Predicate condition3 = cb.and(employeeIdPredicate, statusBranchApproved, isB, specialLeave);
//	            Predicate condition4 = cb.and(employeeIdPredicate, statusReportingApproved, isN, leaveDaysBetween90And180);
//	            Predicate condition5 = cb.and(employeeIdPredicate, root.get("status").in(
//	                    CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED,
//	                    CommonConstants.ApplicationStatus.REPORTING_OFFICER_APPROVED
//	            ), leaveDaysGreaterThan90);
//
//	            predicates.add(cb.or(condition1, condition2, condition3, condition4, condition5));
//	        }
//
//	        if (allowedLevels.contains(4L)) {
//	            List<Predicate> level4Predicates = new ArrayList<>();
//
//	            // ✅ Condition 1: DMC_APPROVED and totalLeaveDays > 365
//	            Predicate dmcApprovedAndLeaveDaysGreaterThan365 = cb.and(
//	                employeeIdPredicate,
//	                cb.equal(root.get("status"), CommonConstants.ApplicationStatus.DMC_APPROVED),
//	                cb.greaterThan(totalLeaveDaysExpr, 365L)
//	            );
//	            level4Predicates.add(dmcApprovedAndLeaveDaysGreaterThan365);
//
//	            // ✅ Condition 2: Special leave type AND totalLeaveDays ≥ 365 AND status ≠ ADDITIONAL_COMMISSIONER_APPROVED
//	            Predicate specialLeaveType = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper);
//	            Predicate statusNotAdditionalCommissionerApproved = cb.notEqual(
//	                root.get("status"),
//	                CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED
//	            );
//	            Predicate specialLeaveDaysCondition = cb.and(
//	                employeeIdPredicate,
//	                specialLeaveType,
//	                cb.greaterThanOrEqualTo(totalLeaveDaysExpr, 365L),
//	                statusNotAdditionalCommissionerApproved
//	            );
//	            level4Predicates.add(specialLeaveDaysCondition);
//
//	            // ✅ Condition 3: HOD employee with status = APPLICATION_CREATED
//	            Predicate hodEmployeePredicate = cb.and(
//	                root.get("employeeId").in(hodEmployeeIds),
//	                statusPredicate // assumed: cb.equal(root.get("status"), CommonConstants.ApplicationStatus.APPLICATION_CREATED)
//	            );
//	            level4Predicates.add(hodEmployeePredicate);
//
//	            // ✅ Combine Level 4 conditions with OR
//	            Predicate level4Combined = cb.or(level4Predicates.toArray(new Predicate[0]));
//	            predicates.add(level4Combined);
//	        }
//
//	        // ✅ Globally exclude status = LEAVE_APPROVED
//	        Predicate statusNotLeaveApproved = cb.notEqual(
//	            root.get("status"),
//	            CommonConstants.ApplicationStatus.LEAVE_APPROVED
//	        );
//	        predicates.add(statusNotLeaveApproved);
//
//	        if (allowedLevels.contains(7L)) {
// 	    	    // DMC Officer logic
//
// 	    	    Predicate statusHodApproved = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.HOD_APPROVED);
//
// 	    	    // ✅ Condition 1: totalLeaveDays > 180 (removed ≤ 365)
// 	    	    Predicate totalLeaveDaysGreaterThan180 = cb.greaterThan(totalLeaveDaysExpr, 180L);
// 	    	    Predicate condition1 = cb.and(statusHodApproved, totalLeaveDaysGreaterThan180);
//
// 	    	    // ✅ Condition 2: leaveType is special leave AND status = HOD_APPROVED
// 	    	    Predicate specialLeaveType = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper);
// 	    	    Predicate condition2 = cb.and(statusHodApproved, specialLeaveType);
//
// 	    	    // Combine both with OR
// 	    	    Predicate dmcCondition = cb.or(condition1, condition2);
//
// 	    	    predicates.add(dmcCondition);
// 	    	}
//
//        if (allowedLevels.contains(9L)) {
//        	// commissioner
//            Predicate statusHodApproved = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED);
//            predicates.add(statusHodApproved);
//        }
//
//
//
//	        return cb.and(predicates.toArray(new Predicate[0]));
//	    };
//	}
	// above correct sfeci
	
	private Specification<EmployeeLeaveLedgerView> buildLedgerSpecification1(
	        String searchParam,
	        List<Long> allowedLevels,
	        List<String> employeeIdsUnderDdo,
	        List<String> hodEmployeeIds,
	        boolean applyLevel4Logic,
	        boolean isSuperAdmin) {

	    return (root, query, cb) -> {
	        List<Predicate> finalPredicates = new ArrayList<>();

	        // 🔍 Global search
	        if (searchParam != null && !searchParam.trim().isEmpty()) {
	            String pattern = "%" + searchParam.trim().toUpperCase() + "%";
	            finalPredicates.add(cb.or(
	                cb.like(cb.upper(root.get("reason")), pattern),
	                cb.like(cb.upper(root.get("employeeId").as(String.class)), pattern),
	                cb.like(cb.upper(root.get("status")), pattern),
	                cb.like(cb.upper(root.get("fullName")), pattern),
	                cb.like(cb.upper(root.get("department")), pattern),
	                cb.like(cb.upper(root.get("leaveTypeName")), pattern)
	            ));
	        }

	        // 🛡️ Super Admin
	        if (isSuperAdmin) {
	            Predicate excludeStatuses = cb.not(root.get("status").in(
	                CommonConstants.ApplicationStatus.LEAVE_APPROVED,
	                CommonConstants.ApplicationStatus.LEAVE_REJECTED,
	                "-"
	            ));
	            finalPredicates.add(excludeStatuses);
	            return cb.and(finalPredicates.toArray(new Predicate[0]));
	        }

	        // 🚫 Guard clause: missing essential inputs
	        if (allowedLevels == null || allowedLevels.isEmpty() ||
	            employeeIdsUnderDdo == null || employeeIdsUnderDdo.isEmpty()) {
	            return cb.disjunction(); // always false
	        }

	        // Shared expressions
	        Expression<Character> locationTypeExpr = root.get("locationType");
	        Expression<String> leaveTypeNameExpr = root.get("leaveTypeName");
	        Expression<Long> totalLeaveDaysExpr = root.get("totalLeaveDays");
	        Expression<String> leaveTypeNameUpperExpr = cb.upper(cb.trim(leaveTypeNameExpr));
	        Expression<Integer> totalLeaveDaysInt = cb.toInteger(totalLeaveDaysExpr);

	        Predicate statusPredicate = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.APPLICATION_CREATED);
	        Predicate employeeIdPredicate = root.get("employeeId").in(employeeIdsUnderDdo);

	        List<String> specialLeaveTypesUpper = Arrays.asList(
	            "SPECIAL LEAVE", "SPECIAL CASUAL LEAVE", "SPECIAL DISABILITY LEAVE"
	        );

	        // ✅ Collect all level predicates here
	        List<Predicate> levelPredicates = new ArrayList<>();

	     // ✅ Level 1
	        if (allowedLevels.contains(1L)) {
	            Predicate isB = cb.equal(locationTypeExpr, 'B');
	            Predicate specialLeave = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper);
	            Predicate excludeHods = cb.not(root.get("employeeId").in(hodEmployeeIds));

	            Predicate statusCreated = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.APPLICATION_CREATED);

	            Predicate p1 = cb.and(employeeIdPredicate, statusCreated, isB, excludeHods);
	            Predicate p2 = cb.and(employeeIdPredicate, statusCreated, isB, specialLeave, excludeHods);

	            levelPredicates.add(cb.or(p1, p2));
	        }

	        // ✅ Level 2
	        if (allowedLevels.contains(2L)) {
	            Predicate isN = cb.equal(locationTypeExpr, 'N');
	            Predicate leaveTypeNotNull = cb.isNotNull(leaveTypeNameExpr);
	            Predicate notSpecialLeave = cb.not(leaveTypeNameUpperExpr.in(specialLeaveTypesUpper));
	            Predicate excludeHods = cb.not(root.get("employeeId").in(hodEmployeeIds));

	            Predicate statusCreated = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.APPLICATION_CREATED);

	            levelPredicates.add(cb.and(
	                employeeIdPredicate,
	                statusCreated,
	                isN,
	                leaveTypeNotNull,
	                notSpecialLeave,
	                excludeHods
	            ));
	        }


	        // ✅ Level 3
	        if (allowedLevels.contains(3L)) {
	            Predicate isN = cb.equal(locationTypeExpr, 'N');
	            Predicate isB = cb.equal(locationTypeExpr, 'B');
	            Predicate specialLeave = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper);
	            Predicate leaveDaysBetween90And180 = cb.and(cb.greaterThanOrEqualTo(totalLeaveDaysInt, 90), cb.lessThan(totalLeaveDaysInt, 180));
	            Predicate leaveDaysGreaterThan90 = cb.greaterThan(totalLeaveDaysInt, 90);

	            Predicate condition1 = cb.and(employeeIdPredicate, statusPredicate, isN, specialLeave);
	            Predicate statusBranchApproved = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED);
	            Predicate statusReportingApproved = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.REPORTING_OFFICER_APPROVED);

	            Predicate condition2 = cb.and(employeeIdPredicate, statusBranchApproved, isB, leaveDaysBetween90And180);
	            Predicate condition3 = cb.and(employeeIdPredicate, statusBranchApproved, isB, specialLeave);
	            Predicate condition4 = cb.and(employeeIdPredicate, statusReportingApproved, isN, leaveDaysBetween90And180);
	            Predicate condition5 = cb.and(employeeIdPredicate, root.get("status").in(
	                CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED,
	                CommonConstants.ApplicationStatus.REPORTING_OFFICER_APPROVED
	            ), leaveDaysGreaterThan90);

	            levelPredicates.add(cb.or(condition1, condition2, condition3, condition4, condition5));
	        }

	        // ✅ Level 4
	        if (allowedLevels.contains(4L)) {
	            List<Predicate> level4Predicates = new ArrayList<>();

	            Predicate dmcApprovedAndLeaveDaysGreaterThan365 = cb.and(
	                employeeIdPredicate,
	                cb.equal(root.get("status"), CommonConstants.ApplicationStatus.DMC_APPROVED),
	                cb.greaterThan(totalLeaveDaysExpr, 365L)
	            );

	            Predicate specialLeaveType = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper);
	            Predicate statusNotAdditionalCommissionerApproved = cb.notEqual(
	                root.get("status"),
	                CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED
	            );

	            Predicate specialLeaveDaysCondition = cb.and(
	                employeeIdPredicate,
	                specialLeaveType,
	                cb.greaterThanOrEqualTo(totalLeaveDaysExpr, 365L),
	                statusNotAdditionalCommissionerApproved
	            );

	            Predicate hodEmployeePredicate = cb.and(
	                root.get("employeeId").in(hodEmployeeIds),
	                statusPredicate
	            );

	            level4Predicates.add(dmcApprovedAndLeaveDaysGreaterThan365);
	            level4Predicates.add(specialLeaveDaysCondition);
	            level4Predicates.add(hodEmployeePredicate);

	            levelPredicates.add(cb.or(level4Predicates.toArray(new Predicate[0])));
	        }

	        // ✅ Level 7 (DMC Officer)
	        if (allowedLevels.contains(7L)) {
	            Predicate statusHodApproved = cb.equal(root.get("status"), CommonConstants.ApplicationStatus.HOD_APPROVED);
	            Predicate totalLeaveDaysGreaterThan180 = cb.greaterThan(totalLeaveDaysExpr, 180L);
	            Predicate specialLeaveType = leaveTypeNameUpperExpr.in(specialLeaveTypesUpper);

	            Predicate condition1 = cb.and(statusHodApproved, totalLeaveDaysGreaterThan180);
	            Predicate condition2 = cb.and(statusHodApproved, specialLeaveType);

	            levelPredicates.add(cb.or(condition1, condition2));
	        }

	        // ✅ Level 9 (Commissioner)
	        if (allowedLevels.contains(9L)) {
	            Predicate commissionerApproved = cb.equal(root.get("status"),
	                CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED);
	            levelPredicates.add(commissionerApproved);
	        }

	        // ✅ Add all level predicates to finalPredicates (combined with OR)
	        if (!levelPredicates.isEmpty()) {
	            finalPredicates.add(cb.or(levelPredicates.toArray(new Predicate[0])));
	        }

	        // ✅ Globally exclude LEAVE_APPROVED
	        finalPredicates.add(cb.notEqual(
	            root.get("status"),
	            CommonConstants.ApplicationStatus.LEAVE_APPROVED
	        ));

	        return cb.and(finalPredicates.toArray(new Predicate[0]));
	    };
	}
	  
	private ResponseDao buildResponse(ResponseDao responseDao, Page<EmployeeLeaveLedgerView> page, String searchParam) {
	    List<EmployeeLeaveLedgerDao> ledgerDaoList = page.getContent().stream().map(view -> {
	        EmployeeLeaveLedgerDao dao = new EmployeeLeaveLedgerDao();
	        BeanUtils.copyProperties(view, dao);
	        return dao;
	    }).collect(Collectors.toList());

	    responseDao.setPageNo(page.getNumber());
	    responseDao.setPageSize(page.getSize());
	    responseDao.setTotalElements(page.getTotalElements());
	    responseDao.setTotalPages(page.getTotalPages());
	    responseDao.setActualPageSize(page.getContent().size());
	    responseDao.setLast(page.isLast());
	    responseDao.setSearchParam(searchParam);
	    responseDao.setEmployeeLeaveLedgerDaoList(ledgerDaoList);
	    return responseDao;
	}


 
	@Override
	public EmployeeLeaveLedgerDao getByLaId(UUID id) {
	    EmployeeLeaveLedgerDao employeeLeaveLedgerDao = new EmployeeLeaveLedgerDao();

	    // Fetch main application view
	    EmployeeLeaveLedgerView employeeLeaveLedgerView = employeeLeaveLedgerViewRepository.findByIds(id);
	    if (employeeLeaveLedgerView == null) {
	        return employeeLeaveLedgerDao;
	    }

	    BeanUtils.copyProperties(employeeLeaveLedgerView, employeeLeaveLedgerDao);

	    // Set leave type name
	    if (employeeLeaveLedgerView.getLeaveTypeId() != null) {
	        MstLeaveType leaveType = mstLeaveTypeRepository.findByMstLeaveTypeId(employeeLeaveLedgerView.getLeaveTypeId());
	        if (leaveType != null) {
	            employeeLeaveLedgerDao.setLeaveTypeName(leaveType.getLeaveTypeName());
	        }
	    }

	    // Fetch documents
	    List<TrnDocumentListDao> documentList = trnDocumentListService
	        .findAllDocumentListByTransactionIdAndTransactionKey(employeeLeaveLedgerDao.getId(), "LEAVE_LEDGER");

	    if (!documentList.isEmpty()) {
	        List<TrnDocumentListDao> processedDocuments = documentList.stream().map(doc -> {
	            TrnDocumentListDao newDoc = new TrnDocumentListDao();
	            BeanUtils.copyProperties(doc, newDoc);

	            String filePath = DOCUMENT_STORAGE_FINAL + doc.getFilePath();
	            String finalFilePath = filePath;

	            if (doc.getFileName() != null && doc.getFileName().toLowerCase().matches(".*\\.(jpg|jpeg|png)$")) {
	                finalFilePath = convertImageToPdf(filePath);
	            }

	            String base64EncodedFile = util.encodeFileToBase64Binary(finalFilePath, doc.getDocumentName());
	            if (finalFilePath.toLowerCase().endsWith(".pdf") &&
	                !base64EncodedFile.startsWith("data:application/pdf;base64,")) {
	                base64EncodedFile = "data:application/pdf;base64," + base64EncodedFile;
	            }

	            newDoc.setFilePath(base64EncodedFile);
	            return newDoc;
	        }).sorted(Comparator.comparing(TrnDocumentListDao::getId)).collect(Collectors.toList());

	        employeeLeaveLedgerDao.setDocumentList(processedDocuments);
	    }

	    // Fetch history
	    List<EmployeeLeaveLedgerHistory> historyList = employeeLeaveLedgerHistoryRepository.findByLeaveApplicationId(id);
	    if (historyList != null && !historyList.isEmpty()) {
	        List<EmployeeLeaveLedgerHistoryDao> historyDaoList = historyList.stream().map(history -> {
	            EmployeeLeaveLedgerHistoryDao dao = new EmployeeLeaveLedgerHistoryDao();
	            BeanUtils.copyProperties(history, dao);
	            dao.setUserFullName(employeeLeaveLedgerHistoryRepository.getUserName(history.getCreatedUserId()));
	            dao.setDesignation(employeeLeaveLedgerHistoryRepository.findByDesignationId(history.getSenderDesignation()));
	            return dao;
	        }).collect(Collectors.toList());
	        employeeLeaveLedgerDao.setEmployeeLeaveLedgerHistoryDao(historyDaoList);
	        employeeLeaveLedgerDao.setHistoryPresent(true);
	    } else {
	        employeeLeaveLedgerDao.setHistoryPresent(false);
	    }

	    // Fetch leave allocation
	    String currentYear = String.valueOf(LocalDate.now().getYear());
	    Long employeeId = employeeLeaveLedgerView.getEmployeeId();
	    mstLeaveAllocationRepository.findByOldEmployeeIdAndYear(employeeId, currentYear).ifPresent(allocation -> {
	        List<MstLeaveAllocationList> allocationLists =
	                mstLeaveAllocationListRepository.findByMstLeaveAllocationId(allocation.getId());
	        List<MstLeaveAllocationListDao> allocationListDaos = allocationLists.stream().map(entity -> {
	            MstLeaveAllocationListDao dao = new MstLeaveAllocationListDao();
	            BeanUtils.copyProperties(entity, dao);
	            return dao;
	        }).collect(Collectors.toList());
	        employeeLeaveLedgerDao.setMstLeaveAllocationListDao(allocationListDaos);
	    });

	    // Set nextStage logic
	 // Set nextStage logic
	    EmployeeLeaveLedger application = employeeLeaveLedgerRepository.findById(id).orElse(null);
	    if (application != null) {
	        String currentStatus = application.getStatus();
	        Long locationId = application.getLocationId();
	        UUID designationId = application.getDesignationId();
	        String leaveTypeName = employeeLeaveLedgerDao.getLeaveTypeName();
	        double totalLeaveDays = employeeLeaveLedgerDao.getTotalLeaveDays();

	        Long employeeId1 = employeeLeaveLedgerView.getEmployeeId();

	        String locationName = "";
	        if (locationId != null) {
	            locationName = locationMasterRepository.findLocationNameById(locationId).orElse("");
	        }

	        String locationType = "B";
	        if ("NMMC HEAD OFFICE".equalsIgnoreCase(locationName)
	                || "NMMC SCHOOL NAVI MUMBAI CORPORATION".equalsIgnoreCase(locationName)) {
	            locationType = "N";
	        }

	        String nextStage = null;

	        List<String> specialLeaveTypes = Arrays.asList(
	                "SPECIAL LEAVE", "SPECIAL CASUAL LEAVE", "SPECIAL DISABILITY LEAVE"
	        );

	        boolean isSpecialLeave = leaveTypeName != null && specialLeaveTypes.contains(leaveTypeName.toUpperCase());

	        // Direct HOD bypass check
	        boolean isHOD = userRoleRepository.existsByEmployeeIdAndHODRole(employeeId1);

	        // Special Leave Flow
	        if (isSpecialLeave) {
	            switch (currentStatus) {
	                case CommonConstants.ApplicationStatus.APPLICATION_CREATED:
	                    nextStage = isHOD ? "Additional Commissioner Approval"
	                            : ("B".equals(locationType) ? "Branch Officer Approval" : "HOD Approval");
	                    break;
	                case CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED:
	                    nextStage = "HOD Approval";
	                    break;
	                case CommonConstants.ApplicationStatus.HOD_APPROVED:
	                    nextStage = "DMC Approval";
	                    break;
	                case CommonConstants.ApplicationStatus.DMC_APPROVED:
	                    nextStage = (totalLeaveDays >= 365) ? "Additional Commissioner Approval" : "Leave Approved";
	                    break;
	                case CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED:
	                    nextStage = "Commissioner Approval";
	                    break;
	                case CommonConstants.ApplicationStatus.COMMISSIONER_APPROVED:
	                    nextStage = "Leave Approved";
	                    break;
	            }
	        }
	        // Other Leave Flow
	        else {
	            switch (currentStatus) {
	                case CommonConstants.ApplicationStatus.APPLICATION_CREATED:
	                    nextStage = isHOD ? "Additional Commissioner Approval"
	                            : ("B".equals(locationType) ? "Branch Officer Approval" : "Reporting Officer Approval");
	                    break;
	                case CommonConstants.ApplicationStatus.REPORTING_OFFICER_APPROVED:
	                case CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED:
	                    if (totalLeaveDays > 90) {
	                        nextStage = "HOD Approval";
	                    } else {
	                        nextStage = "Leave Approved";
	                    }
	                    break;
	                case CommonConstants.ApplicationStatus.HOD_APPROVED:
	                    if (totalLeaveDays > 180 && totalLeaveDays <= 365) {
	                        nextStage = "DMC Approval";
	                    } else if (totalLeaveDays > 365) {
	                        nextStage = "DMC Approval";
	                    } else {
	                        nextStage = "Leave Approved";
	                    }
	                    break;
	                case CommonConstants.ApplicationStatus.DMC_APPROVED:
	                    nextStage = (totalLeaveDays > 365) ? "Additional Commissioner Approval" : "Leave Approved";
	                    break;
	                case CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED:
	                    nextStage = "Commissioner Approval";
	                    break;
	                case CommonConstants.ApplicationStatus.COMMISSIONER_APPROVED:
	                    nextStage = "Leave Approved";
	                    break;
	            }
	        }

	        if (CommonConstants.ApplicationStatus.LEAVE_REJECTED.equals(currentStatus)) {
	            nextStage = "Leave Rejected";
	        }

	        if (nextStage != null) {
	            employeeLeaveLedgerDao.setNextStage(nextStage);
	            application.setNextStage(nextStage);
	            employeeLeaveLedgerRepository.save(application);
	        }
	    }

      

	    return employeeLeaveLedgerDao;
	}

	private String convertImageToPdf(String imagePath) {
        String pdfPath = imagePath.replaceAll("\\.(jpg|jpeg|png)$", ".pdf");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            BufferedImage image = ImageIO.read(new File(imagePath));
            if (image != null) {
                Image pdfImage = Image.getInstance(imagePath);
                pdfImage.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                document.add(pdfImage);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pdfPath;
    }

	@Override
	public EmployeeLeaveLedger getById(UUID id) {
		// TODO Auto-generated method stub
		return employeeLeaveLedgerRepository.getById(id);
	}
	
	

//	@Override
//	public ResponseDao getAllCompleted(Integer pageNo, Integer pageSize, String sortBy, String sortDir, String searchParam, Locale locale, HttpServletRequest request) {
//
//	    ResponseDao responseDao = new ResponseDao();
//	    List<EmployeeLeaveLedgerDao> ledgerDaoList = new ArrayList<>();
//
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UUID userId = authenticationUtil.getUserIdFromAuthentication(authentication);
//	    String username = authenticationUtil.getUsernameFromAuthentication(authentication, userId);
//
//	    Pageable pageable = PageRequest.of(pageNo, pageSize);
//
//
//	    Specification<EmployeeLeaveLedgerView> spec;
//	    Page<EmployeeLeaveLedgerView> ledgerViewPage;
//
//	    if ("9300000450".equalsIgnoreCase(username)) {
//	        spec = buildLedgerSpecificationStatus(searchParam, null, null);
//	    } else {
//	        Long template = 95L; //  workflow template for ledger
//	        List<String> authorizedStages = authenticationUtil.getAuthorizedStages(template, userId);
//	        List<String> ids = workFlowHelperClass.fetchIdsByAuthorizedStagesForLeaveLedger(authorizedStages);
//	        spec = buildLedgerSpecificationStatus(searchParam, ids, username);
//	    }
//
//	    ledgerViewPage = employeeLeaveLedgerViewRepository.findAll(spec, pageable);
//
//	    if (ledgerViewPage.hasContent()) {
//	        List<EmployeeLeaveLedgerView> content = ledgerViewPage.getContent();
//	        responseDao.setPageNo(ledgerViewPage.getNumber());
//	        responseDao.setPageSize(ledgerViewPage.getSize());
//	        responseDao.setTotalElements(ledgerViewPage.getTotalElements());
//	        responseDao.setTotalPages(ledgerViewPage.getTotalPages());
//	        responseDao.setActualPageSize(content.size());
//	        responseDao.setLast(ledgerViewPage.isLast());
//	        responseDao.setSearchParam(searchParam);
//
//	        ledgerDaoList = content.stream()
//	                .map(view -> {
//	                    EmployeeLeaveLedgerDao dao = new EmployeeLeaveLedgerDao();
//	                    BeanUtils.copyProperties(view, dao);
//	                    return dao;
//	                }).collect(Collectors.toList());
//	    }
//
//	    responseDao.setEmployeeLeaveLedgerDaoList(ledgerDaoList);
//	    return responseDao;
//	}
//	
//	public static Specification<EmployeeLeaveLedgerView> buildLedgerSpecificationStatus(String searchParam, List<String> ids, String username) {
//	    return (root, query, criteriaBuilder) -> {
//	        List<Predicate> predicates = new ArrayList<>();
//
//	        // Filter by LEAVE_APPROVED, LEAVE_REJECTED and '-'
//	        List<String> statusList = Arrays.asList("LEAVE_APPROVED", "LEAVE_REJECTED", "-");
//	        predicates.add(root.get("status").in(statusList));
//
//	        // Search on multiple fields if searchParam is provided
//	        if (searchParam != null && !searchParam.trim().isEmpty()) {
//	            String searchPattern = "%" + searchParam.trim().toUpperCase() + "%";
//
//	            Predicate leaveReasonPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("reason")), searchPattern);
//	            Predicate employeeIdPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("employeeId").as(String.class)), searchPattern);
//	            Predicate statusPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("status")), searchPattern);
//	            Predicate fullNamePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("fullName")), searchPattern);
//	            Predicate departmentPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("department")), searchPattern);
//	            Predicate totalLeaveDaysPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("totalLeaveDays").as(String.class)), searchPattern);
//	            Predicate leaveTypeNamePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("leaveTypeName")), searchPattern);
//
//	            predicates.add(criteriaBuilder.or(
//	                    leaveReasonPredicate,
//	                    employeeIdPredicate,
//	                    statusPredicate,
//	                    fullNamePredicate,
//	                    departmentPredicate,
//	                    totalLeaveDaysPredicate,
//	                    leaveTypeNamePredicate
//	            ));
//	        }
//
//	        // filter by ids if provided
//	        if (ids != null && !ids.isEmpty()) {
//	            predicates.add(root.get("id").in(ids));
//	        }
//
//	        
//
//	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//	    };
//	}
//
//old getAll complted 
	
//	@Override
//	public ResponseDao getAllCompleted(Integer pageNo, Integer pageSize, String sortBy, String sortDir, String searchParam, Locale locale, HttpServletRequest request) {
//
//	    ResponseDao responseDao = new ResponseDao();
//	    List<EmployeeLeaveLedgerDao> ledgerDaoList = new ArrayList<>();
//
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UUID userId = authenticationUtil.getUserIdFromAuthentication(authentication);
//	    String username = authenticationUtil.getUsernameFromAuthentication(authentication, userId);
//
//	    Pageable pageable = PageRequest.of(pageNo, pageSize);
//
//	    Specification<EmployeeLeaveLedgerView> spec;
//	    Page<EmployeeLeaveLedgerView> ledgerViewPage;
//
//	    if ("9300000450".equalsIgnoreCase(username)) {
//	        // SuperAdmin — fetch all
//	        spec = buildLedgerSpecificationStatus(searchParam, null, null, null);
//	    } else {
//	        Long template = 95L;
//	        List<String> authorizedStages = authenticationUtil.getAuthorizedStages(template, userId);
//	        List<String> workflowIds = workFlowHelperClass.fetchIdsByAuthorizedStagesForLeaveLedger(authorizedStages);
//
//	        // Fetch logged-in employee's DDO
//	        Optional<MstEmployeeView> optionalEmpView = mstEmployeeViewRepository.findByUserId1(userId);
//	        if (optionalEmpView.isPresent()) {
//	            UUID ddoId = optionalEmpView.get().getDdoId();
//	            List<String> employeeIdsUnderDdo = new ArrayList<>();
//	            if (ddoId != null) {
//	                employeeIdsUnderDdo = mstEmployeeViewRepository.findEmployeeIdsByDdoIds(Collections.singletonList(ddoId));
//	            }
//
//	            spec = buildLedgerSpecificationStatus(searchParam, workflowIds, username, employeeIdsUnderDdo);
//	        } else {
//	            // Fallback: only by workflowIds
//	            spec = buildLedgerSpecificationStatus(searchParam, workflowIds, username, null);
//	        }
//	    }
//
//	    ledgerViewPage = employeeLeaveLedgerViewRepository.findAll(spec, pageable);
//
//	    if (ledgerViewPage.hasContent()) {
//	        List<EmployeeLeaveLedgerView> content = ledgerViewPage.getContent();
//	        responseDao.setPageNo(ledgerViewPage.getNumber());
//	        responseDao.setPageSize(ledgerViewPage.getSize());
//	        responseDao.setTotalElements(ledgerViewPage.getTotalElements());
//	        responseDao.setTotalPages(ledgerViewPage.getTotalPages());
//	        responseDao.setActualPageSize(content.size());
//	        responseDao.setLast(ledgerViewPage.isLast());
//	        responseDao.setSearchParam(searchParam);
//
//	        ledgerDaoList = content.stream()
//	                .map(view -> {
//	                    EmployeeLeaveLedgerDao dao = new EmployeeLeaveLedgerDao();
//	                    BeanUtils.copyProperties(view, dao);
//	                    return dao;
//	                }).collect(Collectors.toList());
//	    }
//
//	    responseDao.setEmployeeLeaveLedgerDaoList(ledgerDaoList);
//	    return responseDao;
//	}

	@Override
	public ResponseDao getAllCompleted(Integer pageNo, Integer pageSize, String sortBy, String sortDir,
	                                   String searchParam, Locale locale, HttpServletRequest request) {

	    ResponseDao responseDao = new ResponseDao();
	    List<EmployeeLeaveLedgerDao> ledgerDaoList = new ArrayList<>();

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UUID userId = authenticationUtil.getUserIdFromAuthentication(authentication);
	    String username = authenticationUtil.getUsernameFromAuthentication(authentication, userId);

	    Pageable pageable = PageRequest.of(pageNo, pageSize);

	    Specification<EmployeeLeaveLedgerView> spec;
	    Page<EmployeeLeaveLedgerView> ledgerViewPage;

	    if ("9300000450".equalsIgnoreCase(username)) {
	        // ✅ SuperAdmin: fetch all from view
	        spec = buildLedgerSpecificationStatus(searchParam, null, null, null);
	        ledgerViewPage = employeeLeaveLedgerViewRepository.findAll(spec, pageable);

	        if (ledgerViewPage.hasContent()) {
	            List<EmployeeLeaveLedgerView> content = ledgerViewPage.getContent();
	            responseDao.setPageNo(ledgerViewPage.getNumber());
	            responseDao.setPageSize(ledgerViewPage.getSize());
	            responseDao.setTotalElements(ledgerViewPage.getTotalElements());
	            responseDao.setTotalPages(ledgerViewPage.getTotalPages());
	            responseDao.setActualPageSize(content.size());
	            responseDao.setLast(ledgerViewPage.isLast());
	            responseDao.setSearchParam(searchParam);

	            ledgerDaoList = content.stream()
	                    .map(view -> {
	                        EmployeeLeaveLedgerDao dao = new EmployeeLeaveLedgerDao();
	                        BeanUtils.copyProperties(view, dao);
	                        return dao;
	                    }).collect(Collectors.toList());
	        }

	    } else {
	        // ✅ For non-super-admins
	        Long template = 95L;
	        List<String> authorizedStages = authenticationUtil.getAuthorizedStages(template, userId);
	        List<String> workflowIds = workFlowHelperClass.fetchIdsByAuthorizedStagesForLeaveLedger(authorizedStages);

	        // ✅ Get DDO IDs from user responsibilities
	        List<UUID> ddoIds = userResponsibilitiesDetailsRepository.findDdoIdsByUserId(userId);
	        List<String> employeeIdsUnderDdo = new ArrayList<>();

	        if (!ddoIds.isEmpty()) {
	            employeeIdsUnderDdo = mstEmployeeViewRepository.findEmployeeIdsByDdoIds(ddoIds);

	            // ✅ Fetch LEAVE_APPROVED history from history table
	            List<EmployeeLeaveLedgerHistory> approvedHistory =
	                    employeeLeaveLedgerHistoryRepository.findApprovedLeaveHistoryByEmployeeIds(employeeIdsUnderDdo);

	            List<EmployeeLeaveLedgerDao> historyDaoList = approvedHistory.stream()
	                    .map(history -> {
	                        EmployeeLeaveLedgerDao dao = new EmployeeLeaveLedgerDao();
	                        BeanUtils.copyProperties(history, dao);
	                        return dao;
	                    }).collect(Collectors.toList());

	            ledgerDaoList.addAll(historyDaoList);

	            // ✅ Add records from the view too (filtered)
	            spec = buildLedgerSpecificationStatus(searchParam, workflowIds, username, employeeIdsUnderDdo);
	        } else {
	            // fallback if no DDOs found
	            spec = buildLedgerSpecificationStatus(searchParam, workflowIds, username, null);
	        }

	     // ✅ View results (for non-super-admins)
	        ledgerViewPage = employeeLeaveLedgerViewRepository.findAll(spec, pageable);

	        List<EmployeeLeaveLedgerDao> viewDaoList = new ArrayList<>();
	        if (ledgerViewPage.hasContent()) {
	            List<EmployeeLeaveLedgerView> content = ledgerViewPage.getContent();
	            viewDaoList = content.stream()
	                    .map(view -> {
	                        EmployeeLeaveLedgerDao dao = new EmployeeLeaveLedgerDao();
	                        BeanUtils.copyProperties(view, dao);
	                        return dao;
	                    }).collect(Collectors.toList());
	        }

	        // ✅ Add history + view
	        ledgerDaoList.addAll(viewDaoList);

	        // ✅ Pagination based on view only
	        responseDao.setPageNo(ledgerViewPage.getNumber());
	        responseDao.setPageSize(ledgerViewPage.getSize());
	        responseDao.setTotalElements(ledgerViewPage.getTotalElements());
	        responseDao.setTotalPages(ledgerViewPage.getTotalPages());
	        responseDao.setActualPageSize(viewDaoList.size());
	        responseDao.setLast(ledgerViewPage.isLast());
	        responseDao.setSearchParam(searchParam);

	    }

	    responseDao.setEmployeeLeaveLedgerDaoList(ledgerDaoList);
	    return responseDao;
	}

	public static Specification<EmployeeLeaveLedgerView> buildLedgerSpecificationStatus(
	        String searchParam, List<String> workflowIds, String username, List<String> employeeIdsUnderDdo) {

	    return (root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        // ✅ Status filter
	        predicates.add(root.get("status").in(Arrays.asList("LEAVE_APPROVED", "LEAVE_REJECTED", "-")));

	        // ✅ Search filter
	        if (searchParam != null && !searchParam.trim().isEmpty()) {
	            String searchPattern = "%" + searchParam.trim().toUpperCase() + "%";

	            predicates.add(criteriaBuilder.or(
	                criteriaBuilder.like(criteriaBuilder.upper(root.get("reason")), searchPattern),
	                criteriaBuilder.like(criteriaBuilder.upper(root.get("employeeId").as(String.class)), searchPattern),
	                criteriaBuilder.like(criteriaBuilder.upper(root.get("status")), searchPattern),
	                criteriaBuilder.like(criteriaBuilder.upper(root.get("fullName")), searchPattern),
	                criteriaBuilder.like(criteriaBuilder.upper(root.get("department")), searchPattern),
	                criteriaBuilder.like(criteriaBuilder.upper(root.get("totalLeaveDays").as(String.class)), searchPattern),
	                criteriaBuilder.like(criteriaBuilder.upper(root.get("leaveTypeName")), searchPattern)
	            ));
	        }

	        // ✅ Workflow ID filter
	        if (workflowIds != null && !workflowIds.isEmpty()) {
	            predicates.add(root.get("id").in(workflowIds));
	        }

	        // ✅ Employee ID (DDO) filter
	        if (employeeIdsUnderDdo != null && !employeeIdsUnderDdo.isEmpty()) {
	            predicates.add(root.get("employeeId").in(employeeIdsUnderDdo));
	        }

	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    };
	}

	
//	public static Specification<EmployeeLeaveLedgerView> buildLedgerSpecificationStatus(
//	        String searchParam, List<String> workflowIds, String username, List<String> employeeIdsUnderDdo) {
//	    return (root, query, criteriaBuilder) -> {
//	        List<Predicate> predicates = new ArrayList<>();
//
//	        // Status filter: LEAVE_APPROVED, LEAVE_REJECTED, -
//	        List<String> statusList = Arrays.asList("LEAVE_APPROVED", "LEAVE_REJECTED", "-");
//	        predicates.add(root.get("status").in(statusList));
//
//	        // Search filter
//	        if (searchParam != null && !searchParam.trim().isEmpty()) {
//	            String searchPattern = "%" + searchParam.trim().toUpperCase() + "%";
//
//	            Predicate leaveReasonPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("reason")), searchPattern);
//	            Predicate employeeIdPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("employeeId").as(String.class)), searchPattern);
//	            Predicate statusPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("status")), searchPattern);
//	            Predicate fullNamePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("fullName")), searchPattern);
//	            Predicate departmentPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("department")), searchPattern);
//	            Predicate totalLeaveDaysPredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("totalLeaveDays").as(String.class)), searchPattern);
//	            Predicate leaveTypeNamePredicate = criteriaBuilder.like(criteriaBuilder.upper(root.get("leaveTypeName")), searchPattern);
//
//	            predicates.add(criteriaBuilder.or(
//	                    leaveReasonPredicate,
//	                    employeeIdPredicate,
//	                    statusPredicate,
//	                    fullNamePredicate,
//	                    departmentPredicate,
//	                    totalLeaveDaysPredicate,
//	                    leaveTypeNamePredicate
//	            ));
//	        }
//
//	        // Workflow IDs filter (if available)
//	        if (workflowIds != null && !workflowIds.isEmpty()) {
//	            predicates.add(root.get("id").in(workflowIds));
//	        }
//
//	        // DDO Employee Ids filter (if available)
//	        if (employeeIdsUnderDdo != null && !employeeIdsUnderDdo.isEmpty()) {
//	            predicates.add(root.get("employeeId").in(employeeIdsUnderDdo));
//	        }
//
//	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//	    };
//	}
//

	@Override
	public String savePreviewNoteContent(ScrutinyNoteDao scrutinyNoteDao) {
	    Optional<EmployeeLeaveLedger> leaveApplication = employeeLeaveLedgerRepository.findByTranscationId(scrutinyNoteDao.getTransactionId());

	    if (leaveApplication.isEmpty()) {
	        JsonObject errorResponse = new JsonObject();
	        errorResponse.addProperty("status", "ERROR");
	        errorResponse.addProperty("message", "No Leave Application found for the given employee.");
	        return errorResponse.toString();
	    }

	    UUID leaveApplicationId = leaveApplication.get().getId(); // Assign leave application UUID as transactionId

	    List<ScrutinyNote> existingNotes = scrutinyNoteRepository.findByTransactionId(leaveApplicationId);
	    ScrutinyNote scrutinyNote;

	    if (!existingNotes.isEmpty()) {
	        scrutinyNote = existingNotes.get(0); // Edit the first existing record
	    } else {
	        scrutinyNote = new ScrutinyNote(); // Create a new record
	        scrutinyNote.setId(Generators.timeBasedEpochGenerator().generate());
	    }

	    scrutinyNote.setNoteDescription(scrutinyNoteDao.getNoteDescription());
	    scrutinyNote.setNoteSubject(scrutinyNoteDao.getNoteSubject());
	    scrutinyNote.setTransactionId(leaveApplicationId); // Set transactionId as Leave Application ID
	    scrutinyNote.setTransactionName("Leave Application");
	    scrutinyNote.setCreatedUserId(scrutinyNoteDao.getCreatedUserId());
	    scrutinyNote.setStatus(scrutinyNoteDao.getStatus());

	    scrutinyNote = scrutinyNoteRepository.save(scrutinyNote);

	    // Ensure scrutinyNoteDao gets updated ID
	    scrutinyNoteDao.setId(scrutinyNote.getId());

	    JsonObject ob = new JsonObject();
	    ob.addProperty("status", "SUCCESS");
	    ob.addProperty("transactionId", leaveApplicationId.toString());
	    ob.addProperty("scrutinyId", scrutinyNoteDao.getId() != null ? scrutinyNoteDao.getId().toString() : null);
	    ob.addProperty("message","Scrunity Note Save Successfully");

	    return ob.toString();
	}


	
//
//	public ResponseDao getAllByFilter(Integer pageNo, Integer pageSize, String sortBy, String sortDir,
//            String searchParam, String filterType, Locale locale, HttpServletRequest request){
//{
//	    ResponseDao responseDao = new ResponseDao();
//	    List<EmployeeLeaveLedgerDao> ledgerDaoList = new ArrayList<>();
//
//	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//	    UUID userId = authenticationUtil.getUserIdFromAuthentication(authentication);
//
//	    // Fetch employeeId
//	    Optional<MstEmployeeView> employeeOpt = mstEmployeeViewRepository.findByUserId1(userId);
//
//	    if (!employeeOpt.isPresent()) {
//	        responseDao.setEmployeeLeaveLedgerDaoList(Collections.emptyList());
//	        responseDao.setMessage("No employee mapped to this user.");
//	        return responseDao;
//	    }
//
//	    String employeeId = employeeOpt.get().getEmployeeId();
//
//	    Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
//	            Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//	    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//
//	    // Always build spec with logged-in employeeId
//	    Specification<EmployeeLeaveLedgerView> spec = buildLedgerSpecification(searchParam, employeeId, filterType);
//
//	    Page<EmployeeLeaveLedgerView> ledgerViewPage = employeeLeaveLedgerViewRepository.findAll(spec, pageable);
//
//	    if (ledgerViewPage.hasContent()) {
//	        List<EmployeeLeaveLedgerView> content = ledgerViewPage.getContent();
//	        responseDao.setPageNo(ledgerViewPage.getNumber());
//	        responseDao.setPageSize(ledgerViewPage.getSize());
//	        responseDao.setTotalElements(ledgerViewPage.getTotalElements());
//	        responseDao.setTotalPages(ledgerViewPage.getTotalPages());
//	        responseDao.setActualPageSize(content.size());
//	        responseDao.setLast(ledgerViewPage.isLast());
//	        responseDao.setSearchParam(searchParam);
//
//	        ledgerDaoList = content.stream()
//	                .map(view -> {
//	                    EmployeeLeaveLedgerDao dao = new EmployeeLeaveLedgerDao();
//	                    BeanUtils.copyProperties(view, dao);
//	                    return dao;
//	                }).collect(Collectors.toList());
//	    }
//
//	    responseDao.setEmployeeLeaveLedgerDaoList(ledgerDaoList);
//	    return responseDao;
//	}
//	}
//	
//	private Specification<EmployeeLeaveLedgerView> buildLedgerSpecification(String searchParam, String employeeId, String filterType) {
//	    return (root, query, criteriaBuilder) -> {
//	        List<Predicate> predicates = new ArrayList<>();
//
//	        // Status filtering based on filterType
//	        List<String> targetStatuses = Arrays.asList("LEAVE_APPROVED", "LEAVE_REJECTED", "-");
//
//	        if ("completed".equalsIgnoreCase(filterType)) {
//	            // include only completed statuses
//	            predicates.add(root.get("status").in(targetStatuses));
//	        } else {
//	            // pending or default: exclude completed statuses
//	            predicates.add(criteriaBuilder.not(root.get("status").in(targetStatuses)));
//	        }
//
//	        // Search parameter conditions
//	        if (searchParam != null && !searchParam.trim().isEmpty()) {
//	            String searchPattern = "%" + searchParam.trim().toUpperCase() + "%";
//
//	            predicates.add(criteriaBuilder.or(
//	                criteriaBuilder.like(criteriaBuilder.upper(root.get("reason")), searchPattern),
//	                criteriaBuilder.like(criteriaBuilder.upper(root.get("employeeId").as(String.class)), searchPattern),
//	                criteriaBuilder.like(criteriaBuilder.upper(root.get("status")), searchPattern),
//	                criteriaBuilder.like(criteriaBuilder.upper(root.get("fullName")), searchPattern),
//	                criteriaBuilder.like(criteriaBuilder.upper(root.get("department")), searchPattern),
//	                criteriaBuilder.like(criteriaBuilder.upper(root.get("totalLeaveDays").as(String.class)), searchPattern),
//	                criteriaBuilder.like(criteriaBuilder.upper(root.get("leaveTypeName")), searchPattern)
//	            ));
//	        }
//
//	        // Always filter by logged-in user's employeeId
//	        predicates.add(criteriaBuilder.equal(root.get("employeeId"), employeeId));
//
//	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//	    };
//	}

	//above code is ok 
	
	
	public ResponseDao getAllByFilter(Integer pageNo, Integer pageSize, String sortBy, String sortDir,
			String searchParam, String filterType, Locale locale, HttpServletRequest request) {

		ResponseDao responseDao = new ResponseDao();
		List<EmployeeLeaveLedgerDao> ledgerDaoList = new ArrayList<>();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UUID userId = authenticationUtil.getUserIdFromAuthentication(authentication);

		UUID superAdminId = UUID.fromString("018d8776-b566-7d3c-9904-47166ad9dc0a");
		boolean isSuperAdmin = userId.equals(superAdminId);

		String employeeId = null;
		if (!isSuperAdmin) {
			Optional<MstEmployeeView> employeeOpt = mstEmployeeViewRepository.findByUserId1(userId);

			if (!employeeOpt.isPresent()) {
				responseDao.setEmployeeLeaveLedgerDaoList(Collections.emptyList());
				responseDao.setMessage("No employee mapped to this user.");
				return responseDao;
			}
			employeeId = employeeOpt.get().getEmployeeId();
		}

		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Specification<EmployeeLeaveLedgerView> spec = buildLedgerSpecification(searchParam, employeeId, filterType,
				isSuperAdmin);

		Page<EmployeeLeaveLedgerView> ledgerViewPage = employeeLeaveLedgerViewRepository.findAll(spec, pageable);

		if (ledgerViewPage.hasContent()) {
			List<EmployeeLeaveLedgerView> content = ledgerViewPage.getContent();
			responseDao.setPageNo(ledgerViewPage.getNumber());
			responseDao.setPageSize(ledgerViewPage.getSize());
			responseDao.setTotalElements(ledgerViewPage.getTotalElements());
			responseDao.setTotalPages(ledgerViewPage.getTotalPages());
			responseDao.setActualPageSize(content.size());
			responseDao.setLast(ledgerViewPage.isLast());
			responseDao.setSearchParam(searchParam);

			ledgerDaoList = content.stream().map(view -> {
				EmployeeLeaveLedgerDao dao = new EmployeeLeaveLedgerDao();
				BeanUtils.copyProperties(view, dao);
				return dao;
			}).collect(Collectors.toList());
		}

		responseDao.setEmployeeLeaveLedgerDaoList(ledgerDaoList);
		return responseDao;
	}

	private Specification<EmployeeLeaveLedgerView> buildLedgerSpecification(
	        String searchParam, String employeeId, String filterType, boolean isSuperAdmin) {

	    return (root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        // Status filtering based on filterType
	        List<String> targetStatuses = Arrays.asList("LEAVE_APPROVED", "LEAVE_REJECTED", "-");

	        if ("completed".equalsIgnoreCase(filterType)) {
	            predicates.add(root.get("status").in(targetStatuses));
	        } else {
	            predicates.add(criteriaBuilder.not(root.get("status").in(targetStatuses)));
	        }

	        // Search parameter conditions
	        if (searchParam != null && !searchParam.trim().isEmpty()) {
	            String searchPattern = "%" + searchParam.trim().toUpperCase() + "%";

	            predicates.add(criteriaBuilder.or(
	                    criteriaBuilder.like(criteriaBuilder.upper(root.get("reason")), searchPattern),
	                    criteriaBuilder.like(criteriaBuilder.upper(root.get("employeeId").as(String.class)), searchPattern),
	                    criteriaBuilder.like(criteriaBuilder.upper(root.get("status")), searchPattern),
	                    criteriaBuilder.like(criteriaBuilder.upper(root.get("fullName")), searchPattern),
	                    criteriaBuilder.like(criteriaBuilder.upper(root.get("department")), searchPattern),
	                    criteriaBuilder.like(criteriaBuilder.upper(root.get("totalLeaveDays").as(String.class)), searchPattern),
	                    criteriaBuilder.like(criteriaBuilder.upper(root.get("leaveTypeName")), searchPattern)
	            ));
	        }

	        // Filter by employeeId for regular user only
	        if (!isSuperAdmin && employeeId != null) {
	            predicates.add(criteriaBuilder.equal(root.get("employeeId"), employeeId));
	        }

	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    };
	}

//	
//	  @Override
//	  public List<EmployeeLeaveLedgerDao> getByEmployeeId(Long employeeId) {
//	      List<EmployeeLeaveLedger> attendanceList = employeeLeaveLedgerRepository.findByEmployeeId1(employeeId);
//	      return attendanceList.stream().map(attendance -> {
//	    	  EmployeeLeaveLedgerDao dao = new EmployeeLeaveLedgerDao();
//	          BeanUtils.copyProperties(attendance, dao);
//	          return dao;
//	      }).collect(Collectors.toList());
//	  }
	
	@Override
	public List<EmployeeLeaveLedgerDao> getByEmployeeId(Long employeeId) {
	    LocalDate startDate = LocalDate.now().minusMonths(3).withDayOfMonth(1); // Start from 1st of the month 3 months ago

	    List<EmployeeLeaveLedger> attendanceList = employeeLeaveLedgerRepository.findByEmployeeIdFromDate(employeeId, startDate);

	    return attendanceList.stream().map(attendance -> {
	        EmployeeLeaveLedgerDao dao = new EmployeeLeaveLedgerDao();
	        BeanUtils.copyProperties(attendance, dao);
	        return dao;
	    }).collect(Collectors.toList());
	}


	
	  
	

}
