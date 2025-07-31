package com.nmmc.hrms.Utils;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;

import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.nmmc.auth.dao.UserDetailsImpl;
import com.nmmc.auth.repository.UserRepository;

@Service
@Component
public class AuthenticationUtil {
	
	@PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;
    
//	public UUID getUserIdFromAuthentication(Authentication authentication) {
//        Object principal = authentication.getPrincipal();
//        UUID userId = null;
//    
//        if (principal instanceof UserDetailsImpl) {
//            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
//            userId = userDetails.getId();
//        } else if (principal instanceof String) {
//            String username = (String) principal;
//            userId = UUID.fromString((String) entityManager.createNativeQuery(
//                    "SELECT CAST(id AS TEXT) FROM common.user_master WHERE user_name='" + username + "'")
//                    .getSingleResult());
//        }
//    
//        return userId;
//    }
    
    public UUID getUserIdFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        UUID userId = null;

        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            userId = userDetails.getId();
        } else if (principal instanceof String) {
            String username = (String) principal;
            try {
                String result = (String) entityManager.createNativeQuery(
                        "SELECT CAST(id AS TEXT) FROM common.user_master WHERE user_name = :username")
                        .setParameter("username", username)
                        .getSingleResult();
                userId = UUID.fromString(result.trim());
            } catch (NoResultException e) {
                throw new RuntimeException("User not found: " + username);
            }
        }

        return userId;
    }


	
	public UUID getDesignationIdFromAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        UUID designationId = null;
    
        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            designationId = userDetails.getDesignationId();
        } else if (principal instanceof String) {
            String username = (String) principal;
            designationId = UUID.fromString((String) entityManager.createNativeQuery(
                    "SELECT CAST(id AS TEXT) FROM common.user_master WHERE user_name='" + username + "'")
                    .getSingleResult());
        }
    
        return designationId;
    }
	
	public String getUsernameFromAuthentication(Authentication authentication, UUID userId) {
		String username = "";
		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			username = userDetails.getUsername();
		}

		return username;
	}
    
	
//    public Map<String, List<String>> getAuthorizedStages(UUID userId) {
//        Map<String, List<String>> authorizedStagesByWardId = new HashMap<>();
//        List<Object[]> workflowList = userRepository.getWorkflowByTemplateIdAndUserId(9L, userId);
//    
//        for (Object[] row : workflowList) {
//            String wardId = row[6].toString();
//            String stages = row[8] != null ? row[8].toString() : "";
//            List<String> stageList = Arrays.asList(stages.split(","));
//            
//            
//    
//            authorizedStagesByWardId.merge(wardId, new ArrayList<>(new HashSet<>(stageList)),
//                    (existingStages, newStages) -> {
//                        Set<String> uniqueStages = new HashSet<>(existingStages);
//                        uniqueStages.addAll(newStages);
//                        return new ArrayList<>(uniqueStages);
//                    });
//        }
//    
//        return authorizedStagesByWardId;
//    }
	
	public List<String> getAuthorizedStages(Long templateId,UUID userId) {
        Set<String> authorizedStages = new HashSet<>();
        List<Object[]> workflowList = userRepository.getWorkflowByTemplateIdAndUserId(templateId, userId);
    
        for (Object[] row : workflowList) {
            String stages = row[8] != null ? row[8].toString() : "";
            List<String> stageList = Arrays.asList(stages.split(","));
            authorizedStages.addAll(stageList);
        }
    
        return new ArrayList<>(authorizedStages);
    }
    
    public Pageable getPageable(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending();
        return PageRequest.of(pageNo, pageSize, sort);
}
    
//    public Tuple getNextStageData(String status, Long templateId) {
//        String sql = "SELECT stage, sequence, template_id FROM enterpriseworkflowmanagement.mst_stages " +
//                "WHERE sequence = (" +
//                "    SELECT sequence + 1 FROM enterpriseworkflowmanagement.mst_stages " +
//                "    WHERE stage = :status AND template_id = :templateId" +
//                ") AND template_id = :templateId";
//
//        Query query = entityManager.createNativeQuery(sql, Tuple.class);
//        query.setParameter("status", status);
//        query.setParameter("templateId", templateId);
//
//        try {
//            return (Tuple) query.getSingleResult();
//        } catch (NoResultException e) {
//            return null;
//        }
//    }

    public Map<String, Object> getNextStageDataCustom(Character workLocation, UUID designationId, UUID leaveTypeId, String currentStatus, Long templateId, UUID leaveApplicationId) {
        try {
            Object[] result = (Object[]) entityManager.createNativeQuery(
                "SELECT ms.stage, ms.sequence, ms.template_id " +
                "FROM enterpriseworkflowmanagement.mst_stages ms, hrms.leave_application la " +
                "LEFT JOIN common.designation_master dm ON dm.id = la.designation_id " +
                "LEFT JOIN hrms.leave_application_details ld ON ld.leave_application_id = la.id " +
                "LEFT JOIN hrms.mst_leave_type lt ON lt.id = ld.leave_type_id " +
                "WHERE ms.sequence = ( " +
                "    SELECT s.sequence + 1 " +
                "    FROM enterpriseworkflowmanagement.mst_stages s " +
                "    WHERE s.stage = :currentStatus AND s.template_id = :templateId " +
                "    LIMIT 1 " +
                ") " +
                "AND la.id = :leaveApplicationId " +
                "AND ms.template_id = :templateId " 
                  )
            .setParameter("currentStatus", currentStatus)
            .setParameter("templateId", templateId)
            .setParameter("leaveApplicationId", leaveApplicationId)
            .getSingleResult();

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("stage", result[0]);
            resultMap.put("sequence", result[1]);
            resultMap.put("template_id", result[2]);
            return resultMap;
        } catch (NoResultException e) {
            return null;
        }
    }




    
    public Map<String, UUID> getNextStageUserId(UUID wardId, String stage, Long templateIds) {
        Map<String, UUID> userDetails = new HashMap<>();
        
        try {
            String sql = "SELECT id FROM enterpriseworkflowmanagement.mst_workflow_manager " +
                         "WHERE ward_id = :wardId AND template_id = :templateId";

            UUID workflowId = (UUID) entityManager.createNativeQuery(sql)
                    .setParameter("wardId", wardId)
                    .setParameter("templateId", templateIds)
                    .unwrap(NativeQuery.class)
                    .addScalar("id", StandardBasicTypes.UUID_BINARY)  // Use UUID type instead of STRING
                    .setMaxResults(1)
                    .getSingleResult();

            if (workflowId != null) {
                System.out.println("Workflow ID: " + workflowId); // Optional logging
                userDetails = getUserIdFromTheWorkFlow(workflowId, stage, wardId);
            }

        } catch (NoResultException e) {
            System.out.println("No workflow found for wardId: " + wardId + " and templateId: " + templateIds);
        } catch (Exception e) {
            System.err.println("Error in getNextStageUserId: " + e.getMessage());
        }

        return userDetails;
    }

    
    private Map<String, UUID> getUserIdFromTheWorkFlow(UUID workflowId, String stage, UUID wardId) {
        String sql = "SELECT DISTINCT user_id, designation_id " +
                "FROM enterpriseworkflowmanagement.mst_workflow_manager_details " +
                "WHERE workflow_id = :workflowId AND positive_stage = :stage";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("workflowId", workflowId);
        query.setParameter("stage", stage);

        query.unwrap(NativeQuery.class)
                .addScalar("user_id", StandardBasicTypes.STRING)
                .addScalar("designation_id", StandardBasicTypes.STRING);

        List<Object[]> results = query.getResultList();

        Map<String, UUID> resultData = new HashMap<>();
        for (Object[] result : results) {
            if (result != null && result.length == 2) {
                UUID userId = result[0] != null ? UUID.fromString(result[0].toString()) : null;
                UUID designationId = result[1] != null ? UUID.fromString(result[1].toString()) : null;

                if (userId != null) {
                    UUID fetchedWardId = getWardIdByUserId(userId);

                    if (wardId != null && wardId.equals(fetchedWardId)) {
                        resultData.put("user_id", userId);
                        resultData.put("designation_id", designationId);
                        System.out.println("Matching User ID: " + userId + ", Designation ID: " + designationId);
                        return resultData;
                    }
                }
            }
        }

        System.out.println("No matching data found for the given wardId.");
        return resultData;
    }
    
    private UUID getWardIdByUserId(UUID userId) {
        String sql = "SELECT cast(ward_id as varchar) as ward_id FROM common.user_master WHERE id = :userId";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);

        try {
            Object result = query.getSingleResult();
            return result != null ? UUID.fromString(result.toString()) : null;
        } catch (NoResultException e) {
            System.out.println("No wardId found for User ID: " + userId);
            return null;
        }
    }
    
    public String getUserNameOfNextStage(UUID stagedUserId) {
        String sql = "SELECT CONCAT(user_first_name, ' ', user_last_name) AS full_name " + "FROM common.user_master "
                + "WHERE id = :stagedUserId";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("stagedUserId", stagedUserId);

        String fullName = null;
        try {
            Object result = query.getSingleResult();
            if (result != null) {
                fullName = result.toString();
                System.out.println("Full Name: " + fullName);
            } else {
                System.out.println("No user found for the next stage.");
            }
        } catch (NoResultException e) {
            System.out.println("No user found for the next stage.");
        }

        return fullName;
    }


}
