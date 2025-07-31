package com.nmmc.hrms.Controller;

import com.google.gson.JsonObject;
import com.nmmc.hrms.Dao.EmployeeLeaveLedgerDao;
import com.nmmc.hrms.Dao.ResponseDao;
import com.nmmc.hrms.Dao.ScrutinyNoteDao;
import com.nmmc.hrms.Dao.TrnLeaveApplicationDao;
import com.nmmc.hrms.Entity.EmployeeLeaveLedger;
import com.nmmc.hrms.Entity.LocationMaster;
import com.nmmc.hrms.Entity.TrnLeaveApplication;
import com.nmmc.hrms.Repository.EmployeeLeaveLedgerRepository;
import com.nmmc.hrms.Repository.LocationMasterRepository;
import com.nmmc.hrms.Repository.MstLeaveTypeRepository;
import com.nmmc.hrms.Repository.UserRoleRepository;
import com.nmmc.hrms.Service.EmployeeLeaveLedgerService;
import com.nmmc.hrms.Utils.AuthenticationUtil;
import com.nmmc.hrms.Utils.CommonConstants;
import com.nmmc.hrms.Utils.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction/employeeLeaveLedger")
public class EmployeeLeaveLedgerController {

    @Autowired
    private EmployeeLeaveLedgerService employeeLeaveLedgerService;    
   
    @Autowired
    private EmployeeLeaveLedgerRepository employeeLeaveLedgerRepository;

    @Autowired
    private LocationMasterRepository locationMasterRepository;
    
    @Autowired
    private MstLeaveTypeRepository  mstLeaveTypeRepository;
    
    @Autowired
	private AuthenticationUtil authenticationUtil;
    
    @Autowired
    private UserRoleRepository  userRoleRepository;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public String save(@Valid @RequestBody EmployeeLeaveLedgerDao employeeLeaveLedgerDao, HttpServletRequest request) {
        return employeeLeaveLedgerService.save(employeeLeaveLedgerDao, request);
    }
    
//    @GetMapping("/getAll")
//    public ResponseEntity<ResponseDao> getAll(
//            @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
//            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
//            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
//            @RequestParam(value = "sortDir", required = false, defaultValue = "DESC") String sortDir,
//            @RequestParam(value = "searchParam", required = false) String searchParam,
//            Locale locale,
//            HttpServletRequest request) {
//
//        ResponseDao response = employeeLeaveLedgerService.getAll(pageNo, pageSize, sortBy, sortDir, searchParam, locale, request);
//        return ResponseEntity.ok(response);
//    }      
    
    @GetMapping("/getAll")
    public ResponseEntity<ResponseDao> getAll(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createDtTm") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "DESC") String sortDir,
            @RequestParam(value = "searchParam", required = false) String searchParam,
            Locale locale,
            HttpServletRequest request) {

        ResponseDao response = employeeLeaveLedgerService.getAll(pageNo, pageSize, sortBy, sortDir, searchParam, locale, request);
        return ResponseEntity.ok(response);
    }

    
    
    @GetMapping("/getAllCompleted")
    public ResponseEntity<ResponseDao> getAllCompleted(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "DESC") String sortDir,
            @RequestParam(value = "searchParam", required = false) String searchParam,
            Locale locale,
            HttpServletRequest request) {

        ResponseDao response = employeeLeaveLedgerService.getAllCompleted(pageNo, pageSize, sortBy, sortDir, searchParam, locale, request);
        return ResponseEntity.ok(response);
    }      
    
    @GetMapping("/getById")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeLeaveLedgerDao getById(@Valid @RequestParam UUID id) {
        return employeeLeaveLedgerService.getByLaId(id);
    }    
         
//    @PostMapping("/positiveScrutiny")
//    @ResponseStatus(HttpStatus.OK)
//    public String positiveScrutiny(@Valid @RequestBody EmployeeLeaveLedgerDao leaveLedgerDao, HttpServletRequest request) {
//        JsonObject ob = new JsonObject();
//
//        if (leaveLedgerDao == null) {
//            ob.addProperty("status", "Failed");
//            ob.addProperty("message", "Data not found");
//            return ob.toString();
//        }
//
//        // Fetch Leave Ledger by ID
//        EmployeeLeaveLedger leaveLedger = employeeLeaveLedgerService.getById(leaveLedgerDao.getId());
//        if (leaveLedger == null) {
//            ob.addProperty("status", "Failed");
//            ob.addProperty("message", "Leave application not found");
//            return ob.toString();
//        }
//
//        // Copy properties to DAO
//        EmployeeLeaveLedgerDao updatedDao = new EmployeeLeaveLedgerDao();
//        BeanUtils.copyProperties(leaveLedger, updatedDao);
//
//        // Fetch Leave Type ID and Name
//        UUID leaveTypeId = leaveLedger.getLeaveTypeId();
//        if (leaveTypeId == null) {
//            ob.addProperty("status", "Failed");
//            ob.addProperty("message", "Leave Type not found");
//            return ob.toString();
//        }
//        String leaveTypeName = mstLeaveTypeRepository.getLeaveTypeNameById(leaveTypeId);
//
//        // Fetch Designation Name by designationId
//        UUID designationId = leaveLedger.getDesignationId();
//        if (designationId == null) {
//            ob.addProperty("status", "Failed");
//            ob.addProperty("message", "Designation not found");
//            return ob.toString();
//        }
//        String designationName = employeeLeaveLedgerRepository.findDesignationNameById(designationId);
//
//        // Fetch Location Name by locationId
//        Long locationId = leaveLedger.getLocationId();
//        String locationName = locationMasterRepository.findLocationNameById1(locationId);
//
//        // Determine locationType based on locationName
//        String locationType = "B"; // default
//        if ("NMMC HEAD OFFICE".equalsIgnoreCase(locationName) || "NMMC SCHOOL NAVI MUMBAI CORPORATION".equalsIgnoreCase(locationName)) {
//            locationType = "N";
//        }
//
//     // Define your special leave types
//        List<String> specialLeaveTypes = Arrays.asList(
//            "SPECIAL LEAVE",
//            "SPECIAL CASUAL LEAVE",
//            "SPECIAL DISABILITY LEAVE"
//        );
//
//        // Check if leaveTypeName is one of them (case insensitive)
//        if (specialLeaveTypes.stream().anyMatch(s -> s.equalsIgnoreCase(leaveTypeName))) {
//            processSpecialLeave(leaveLedger, leaveLedgerDao, updatedDao, designationName, locationType);
//        } else {
//            processOtherLeaves(leaveLedger, leaveLedgerDao, updatedDao, designationName, locationType);
//        }
//
//        //updatedDao.setApproved('P');
//
//        // ✅ Enforce status LEAVE_APPROVED when NextStage becomes 'Leave Approved'
//        if ("Leave Approved".equalsIgnoreCase(updatedDao.getNextStage())) {
//            updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
//            updatedDao.setApproved('Y');
//        }
//
//        // Final approval flag if both status & stage confirm approval
//        if (CommonConstants.ApplicationStatus.LEAVE_APPROVED.equalsIgnoreCase(updatedDao.getStatus())) {
//            updatedDao.setApproved('Y');
//            updatedDao.setNextStage(null);
//        }
//
//        return employeeLeaveLedgerService.save(updatedDao, request);
//    }
    
    @PostMapping("/positiveScrutiny")
    @ResponseStatus(HttpStatus.OK)
    public String positiveScrutiny(@Valid @RequestBody EmployeeLeaveLedgerDao leaveLedgerDao, HttpServletRequest request) {
        JsonObject ob = new JsonObject();

        if (leaveLedgerDao == null) {
            ob.addProperty("status", "Failed");
            ob.addProperty("message", "Data not found");
            return ob.toString();
        }

        // Fetch Leave Ledger by ID
        EmployeeLeaveLedger leaveLedger = employeeLeaveLedgerService.getById(leaveLedgerDao.getId());
        if (leaveLedger == null) {
            ob.addProperty("status", "Failed");
            ob.addProperty("message", "Leave application not found");
            return ob.toString();
        }

        // Copy properties to DAO
        EmployeeLeaveLedgerDao updatedDao = new EmployeeLeaveLedgerDao();
        BeanUtils.copyProperties(leaveLedger, updatedDao);

     // ✅ Set addChargeUserId from incoming payload
        updatedDao.setAddChargeUserId(leaveLedgerDao.getAddChargeUserId());
        // Fetch Leave Type Name
        UUID leaveTypeId = leaveLedger.getLeaveTypeId();
        if (leaveTypeId == null) {
            ob.addProperty("status", "Failed");
            ob.addProperty("message", "Leave Type not found");
            return ob.toString();
        }
        String leaveTypeName = mstLeaveTypeRepository.getLeaveTypeNameById(leaveTypeId);
        
        Long employeeId = leaveLedger.getEmployeeId();


        // Fetch Designation Name
        UUID designationId = leaveLedger.getDesignationId();
        if (designationId == null) {
            ob.addProperty("status", "Failed");
            ob.addProperty("message", "Designation not found");
            return ob.toString();
        }
        String designationName = employeeLeaveLedgerRepository.findDesignationNameById(designationId);

        // Fetch LocationMaster by locationId
        Long locationId = leaveLedger.getLocationId();
        LocationMaster locationMaster = locationMasterRepository.findByLocationId(locationId);
        if (locationMaster == null) {
            ob.addProperty("status", "Failed");
            ob.addProperty("message", "Location not found");
            return ob.toString();
        }

        // Get locationType dynamically from LocationMaster
        Character locationType = locationMaster.getLocationType();
        if (locationType == null) {
            locationType = 'B'; // default fallback if null
        }

        // Special Leave Type list
        List<String> specialLeaveTypes = Arrays.asList(
            "SPECIAL LEAVE",
            "SPECIAL CASUAL LEAVE",
            "SPECIAL DISABILITY LEAVE"
        );

        // Process based on leave type
        if (specialLeaveTypes.stream().anyMatch(s -> s.equalsIgnoreCase(leaveTypeName))) {
            processSpecialLeave(leaveLedger, leaveLedgerDao, updatedDao, locationType.toString(), employeeId);
        } else {
            processOtherLeaves(leaveLedger, leaveLedgerDao, updatedDao, locationType.toString(), employeeId);
        }


        // Final LEAVE_APPROVED status check
        if ("Leave Approved".equalsIgnoreCase(updatedDao.getNextStage())) {
            updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
            updatedDao.setApproved('Y');
        }

        if (CommonConstants.ApplicationStatus.LEAVE_APPROVED.equalsIgnoreCase(updatedDao.getStatus())) {
            updatedDao.setApproved('Y');
            updatedDao.setNextStage(null);
        }

        return employeeLeaveLedgerService.save(updatedDao, request);
    }

    private boolean isHOD(Long employeeId) {
        return userRoleRepository.existsByEmployeeIdAndHODRole(employeeId);
    }


    private void processSpecialLeave(EmployeeLeaveLedger leaveLedger, EmployeeLeaveLedgerDao leaveLedgerDao,
            EmployeeLeaveLedgerDao updatedDao, String locationType, Long employeeId) {

		String currentStatus = leaveLedger.getStatus();
		double totalLeaveDays = leaveLedger.getTotalLeaveDays();
		updatedDao.setApproveRemark(leaveLedgerDao.getApproveRemark());

// Direct approval for special designations
		// Direct approval for special designations
		if (isHOD(employeeId)) {
		    switch (leaveLedger.getStatus()) {
		        case CommonConstants.ApplicationStatus.APPLICATION_CREATED:
		       		            updatedDao.setStatus(CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED);
		            updatedDao.setNextStage("Commissioner Approval");
		            break;

		        case CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED:
		            updatedDao.setStatus(CommonConstants.ApplicationStatus.COMMISSIONER_APPROVED);
		            updatedDao.setNextStage("Leave Approved");
		            break;

		        case CommonConstants.ApplicationStatus.COMMISSIONER_APPROVED:
		            updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
		            updatedDao.setNextStage(null);
		            break;

		        default:
		            updatedDao.setStatus(leaveLedger.getStatus());
		            updatedDao.setNextStage(null);
		            break;
		    }

		    updatedDao.setLatestRemark(leaveLedgerDao.getApproveRemark());
		    return;
		}

		switch (currentStatus) {

		case CommonConstants.ApplicationStatus.APPLICATION_CREATED:
			if ("B".equals(locationType)) {
				updatedDao.setStatus(CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED);
				updatedDao.setNextStage("HOD Approval");
			} else {
				updatedDao.setStatus(CommonConstants.ApplicationStatus.HOD_APPROVED);
				updatedDao.setNextStage("DMC Approval");
			}
			break;

		case CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED:
			updatedDao.setStatus(CommonConstants.ApplicationStatus.HOD_APPROVED);
			updatedDao.setNextStage("DMC Approval");
			break;

		case CommonConstants.ApplicationStatus.HOD_APPROVED:
			updatedDao.setStatus(CommonConstants.ApplicationStatus.DMC_APPROVED);
			if (totalLeaveDays >= 365) {
				updatedDao.setNextStage("Additional Commissioner Approval");
			} else {
				updatedDao.setNextStage("Leave Approved");
			}
			break;

		case CommonConstants.ApplicationStatus.DMC_APPROVED:
			if (totalLeaveDays >= 365) {
				updatedDao.setStatus(CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED);
				updatedDao.setNextStage("Commissioner Approval");
			} else {
				updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
				updatedDao.setNextStage(null);
			}
			break;

		case CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED:
			updatedDao.setStatus(CommonConstants.ApplicationStatus.COMMISSIONER_APPROVED);
			updatedDao.setNextStage("Leave Approved");
			break;
			
		case CommonConstants.ApplicationStatus.COMMISSIONER_APPROVED:
            updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
            updatedDao.setNextStage(null);
            break;

		default:
			updatedDao.setStatus(currentStatus);
			updatedDao.setNextStage(null);
			break;
		}

		updatedDao.setLatestRemark(leaveLedgerDao.getApproveRemark());
	}

    private void processOtherLeaves(EmployeeLeaveLedger leaveLedger, EmployeeLeaveLedgerDao leaveLedgerDao,
            EmployeeLeaveLedgerDao updatedDao, String locationType, Long employeeId) {

        String currentStatus = leaveLedger.getStatus();
        double totalLeaveDays = leaveLedger.getTotalLeaveDays();
        updatedDao.setApproveRemark(leaveLedgerDao.getApproveRemark());

        // Direct approval for specific designations
        if (isHOD(employeeId)) {
		    switch (leaveLedger.getStatus()) {
		        case CommonConstants.ApplicationStatus.APPLICATION_CREATED:
		       		            updatedDao.setStatus(CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED);
		            updatedDao.setNextStage("Commissioner Approval");
		            break;

		        case CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED:
		            updatedDao.setStatus(CommonConstants.ApplicationStatus.COMMISSIONER_APPROVED);
		            updatedDao.setNextStage("Leave Approved");
		            break;

		        case CommonConstants.ApplicationStatus.COMMISSIONER_APPROVED:
		            updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
		            updatedDao.setNextStage(null);
		            break;

		        default:
		            updatedDao.setStatus(leaveLedger.getStatus());
		            updatedDao.setNextStage(null);
		            break;
		    }

		    updatedDao.setLatestRemark(leaveLedgerDao.getApproveRemark());
		    return;
		}

        switch (currentStatus) {

            case CommonConstants.ApplicationStatus.APPLICATION_CREATED:
                if ("B".equals(locationType)) {
                    updatedDao.setStatus(CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED);
                } else if ("N".equals(locationType)) {
                    updatedDao.setStatus(CommonConstants.ApplicationStatus.REPORTING_OFFICER_APPROVED);
                }

                if (totalLeaveDays <= 90) {
                    updatedDao.setNextStage("Leave Approved");
                } else if (totalLeaveDays > 90 && totalLeaveDays <= 180) {
                    updatedDao.setNextStage("HOD Approval");
                } else if (totalLeaveDays > 180 && totalLeaveDays <= 365) {
                    updatedDao.setNextStage("HOD Approval");
                } else { // totalLeaveDays > 365
                    updatedDao.setNextStage("HOD Approval");
                }
                break;

            case CommonConstants.ApplicationStatus.REPORTING_OFFICER_APPROVED:
            case CommonConstants.ApplicationStatus.BRANCH_OFFICER_APPROVED:
                if (totalLeaveDays <= 90) {
                    updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
                    updatedDao.setNextStage(null);
                } else {
                    updatedDao.setStatus(CommonConstants.ApplicationStatus.HOD_APPROVED);
                    updatedDao.setNextStage(totalLeaveDays <= 180 ? "Leave Approved" : "DMC Approval");
                }
                break;

            case CommonConstants.ApplicationStatus.HOD_APPROVED:
                if (totalLeaveDays > 180 && totalLeaveDays <= 365) {
                    updatedDao.setStatus(CommonConstants.ApplicationStatus.DMC_APPROVED);
                    updatedDao.setNextStage("Leave Approved");
                    updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
                } else if (totalLeaveDays > 365) {
                    updatedDao.setStatus(CommonConstants.ApplicationStatus.DMC_APPROVED);
                    updatedDao.setNextStage("Additional Commissioner Approval");
                } else {
                    updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
                    updatedDao.setNextStage(null);
                }
                break;

            case CommonConstants.ApplicationStatus.DMC_APPROVED:
                if (totalLeaveDays > 365) {
                    updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
                } else {
                    updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
                }
                updatedDao.setNextStage(null);
                break;
               
            case CommonConstants.ApplicationStatus.ADDITIONAL_COMMISSIONER_APPROVED:
                updatedDao.setStatus(CommonConstants.ApplicationStatus.COMMISSIONER_APPROVED);
                updatedDao.setNextStage("Leave Approved");
                break;

            case CommonConstants.ApplicationStatus.COMMISSIONER_APPROVED:
                updatedDao.setStatus(CommonConstants.ApplicationStatus.LEAVE_APPROVED);
                updatedDao.setNextStage(null);
                break;


            default:
                updatedDao.setStatus(currentStatus);
                updatedDao.setNextStage(null);
                break;
        }

        updatedDao.setLatestRemark(leaveLedgerDao.getApproveRemark());
    }

    
    @PostMapping("/negativeScrutiny")
    @ResponseStatus(HttpStatus.OK)
    public String negativeScrutiny(@Valid @RequestBody EmployeeLeaveLedgerDao employeeLeaveLedgerDao,HttpServletRequest request) {

        JsonObject ob = new JsonObject();
        EmployeeLeaveLedger employeeLeaveLedger = employeeLeaveLedgerService.getById(employeeLeaveLedgerDao.getId());
        EmployeeLeaveLedgerDao employeeLeaveLedgerDao1 = new EmployeeLeaveLedgerDao();
        BeanUtils.copyProperties(employeeLeaveLedger, employeeLeaveLedgerDao1);

        if (employeeLeaveLedger == null) {
            ob.addProperty("status", "Failed");
            ob.addProperty("message", "Leave application not found");
            return ob.toString();
        } else {
        	employeeLeaveLedgerDao1.setRejectRemark(employeeLeaveLedgerDao.getRejectRemark());
        	employeeLeaveLedgerDao1.setStatus(CommonConstants.ApplicationStatus.LEAVE_REJECTED);
        	employeeLeaveLedgerDao1.setLatestRemark(employeeLeaveLedgerDao.getRejectRemark());
        	employeeLeaveLedgerDao1.setApproved('N'); // <--- Set as rejected


        }
        return employeeLeaveLedgerService.save(employeeLeaveLedgerDao1,request);

    }
    
    
    @PostMapping("/savePreviewNoteContentLeaveApplication")
  	@ResponseStatus(HttpStatus.OK)
  	public String savePreviewNoteContent(@RequestBody ScrutinyNoteDao scrutinyNoteDao) {
  		return employeeLeaveLedgerService.savePreviewNoteContent(scrutinyNoteDao);
  	}
    
    
    
    @GetMapping("/getParticularEmployeeLeaves")
    public ResponseEntity<ResponseDao> getAllByFilter(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createDtTm") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "DESC") String sortDir,
            @RequestParam(value = "searchParam", required = false) String searchParam,
            @RequestParam(value = "filterType", required = false, defaultValue = "pending") String filterType, // 👈 add this
            Locale locale,
            HttpServletRequest request) {

        ResponseDao response = employeeLeaveLedgerService.getAllByFilter(pageNo, pageSize, sortBy, sortDir, searchParam, filterType, locale, request);
        return ResponseEntity.ok(response);
    }

      
}



