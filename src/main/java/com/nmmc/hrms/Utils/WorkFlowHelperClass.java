package com.nmmc.hrms.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
public class WorkFlowHelperClass {

	@PersistenceContext
	private EntityManager entityManager;

	public List<String> fetchIdsByAuthorizedStages(List<String> authorizedStages) {
		List<String> ids = new ArrayList<>();

		if (!authorizedStages.isEmpty()) {
			StringBuilder queryBuilder = new StringBuilder("select id from (");

			for (String status : authorizedStages) {
				queryBuilder.append("select cast(id as text) from hrms.life_certificate_of_pensioner where status = '")
						.append(status).append("' union ");
			}

			int length = queryBuilder.length();
			queryBuilder.delete(length - 7, length);
			queryBuilder.append(") as t");

			ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
		}
		return ids;
	}
	
	public List<String> fetchIdsByAuthorizedStagesApplicationForm(List<String> authorizedStages) {
		List<String> ids = new ArrayList<>();

		if (!authorizedStages.isEmpty()) {
			StringBuilder queryBuilder = new StringBuilder("select id from (");

			for (String status : authorizedStages) {
				queryBuilder.append("select cast(id as text) from hrms.recruitment_process_application where status = '")
						.append(status).append("' union ");
			}

			int length = queryBuilder.length();
			queryBuilder.delete(length - 7, length);
			queryBuilder.append(") as t");

			ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
		}
		return ids;
	}
	
	public List<String> fetchIdsByAuthorizedStagesLoanForm(List<String> authorizedStages) {
		List<String> ids = new ArrayList<>();

		if (!authorizedStages.isEmpty()) {
			StringBuilder queryBuilder = new StringBuilder("select id from (");

			for (String status : authorizedStages) {
				queryBuilder.append("select cast(id as text) from hrms.trn_home_loan_application where status = '")
						.append(status).append("' union ");
			}

			int length = queryBuilder.length();
			queryBuilder.delete(length - 7, length);
			queryBuilder.append(") as t");

			ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
		}
		return ids;
	}
	
	public List<String> fetchIdsByAuthorizedStagesRecruitmentJobForm(List<String> authorizedStages) {
		List<String> ids = new ArrayList<>();

		if (!authorizedStages.isEmpty()) {
			StringBuilder queryBuilder = new StringBuilder("select id from (");

			for (String status : authorizedStages) {
				queryBuilder.append("select cast(id as text) from hrms.trn_recruitment_process where status = '")
						.append(status).append("' union ");
			}

			int length = queryBuilder.length();
			queryBuilder.delete(length - 7, length);
			queryBuilder.append(") as t");

			ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
		}
		return ids;
	}

	public List<String> fetchIdsByAuthorizedStagesForMedicalReimburment(List<String> authorizedStages) {
		List<String> ids = new ArrayList<>();

		if (!authorizedStages.isEmpty()) {
			StringBuilder queryBuilder = new StringBuilder("select id from (");

			for (String status : authorizedStages) {
				queryBuilder.append("select cast(id as text) from hrms.trn_medical_reimbursment_v1 where status = '")
						.append(status).append("' union ");
			}

			int length = queryBuilder.length();
			queryBuilder.delete(length - 7, length);
			queryBuilder.append(") as t");

			ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
		}
		return ids;
	}

	public List<String> fetchIdsByAuthorizedStagesForSeniorityList(List<String> authorizedStages) {
		List<String> ids = new ArrayList<>();

		if (!authorizedStages.isEmpty()) {
			StringBuilder queryBuilder = new StringBuilder("select id from (");

			for (String status : authorizedStages) {
				queryBuilder.append("select cast(id as text) from hrms.trn_seniority_list where status = '")
						.append(status).append("' union ");
			}

			int length = queryBuilder.length();
			queryBuilder.delete(length - 7, length);
			queryBuilder.append(") as t");

			ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
		}
		return ids;
	}

	public List<String> fetchIdsByAuthorizedStagesForMasterSeniorityList(List<String> authorizedStages) {
	       List<String> ids = new ArrayList<>();
	       
	       if (!authorizedStages.isEmpty()) {
	           StringBuilder queryBuilder = new StringBuilder("select id from (");
	       
	           for (String status : authorizedStages) {
	               queryBuilder.append("select cast(id as text) from hrms.mst_seniority_list where status = '")
	                           .append(status)
	                           .append("' union ");
	           }
	       
	           int length = queryBuilder.length();
	           queryBuilder.delete(length - 7, length);
	           queryBuilder.append(") as t");
	       
	           ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
	       }
	       return ids;
	}
	
	public List<String> fetchIdsByAuthorizedStagesForTaskManagemetList(List<String> authorizedStages) {
	       List<String> ids = new ArrayList<>();
	       
	       if (!authorizedStages.isEmpty()) {
	           StringBuilder queryBuilder = new StringBuilder("select id from (");
	       
	           for (String status : authorizedStages) {
	               queryBuilder.append("select cast(id as text) from hrms.trn_task_management where status = '")
	                           .append(status)
	                           .append("' union ");
	           }
	       
	           int length = queryBuilder.length();
	           queryBuilder.delete(length - 7, length);
	           queryBuilder.append(") as t");
	       
	           ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
	       }
	       return ids;
	}
	
	
	public List<String> fetchIdsByAuthorizedStagesForLeaveApplication(List<String> authorizedStages) {
	       List<String> ids = new ArrayList<>();
	       
	       if (!authorizedStages.isEmpty()) {
	           StringBuilder queryBuilder = new StringBuilder("select id from (");
	       
	           for (String status : authorizedStages) {
	               queryBuilder.append("select cast(id as text) from hrms.leave_application where status = '")
	                           .append(status)
	                           .append("' union ");
	           }
	       
	           int length = queryBuilder.length();
	           queryBuilder.delete(length - 7, length);
	           queryBuilder.append(") as t");
	       
	           ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
	       }
	       return ids;
	}

	
	public List<String> fetchIdsByAuthorizedStagesForLocationMaster(List<String> authorizedStages) {
	       List<String> ids = new ArrayList<>();
	       
	       if (!authorizedStages.isEmpty()) {
	           StringBuilder queryBuilder = new StringBuilder("select id from (");
	       
	           for (String status : authorizedStages) {
	               queryBuilder.append("select cast(id as text) from hrms.location_master where status = '")
	                           .append(status)
	                           .append("' union ");
	           }
	       
	           int length = queryBuilder.length();
	           queryBuilder.delete(length - 7, length);
	           queryBuilder.append(") as t");
	       
	           ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
	       }
	       return ids;
	}

	
	public List<String> fetchIdsByAuthorizedStagesForLeaveLedger(List<String> authorizedStages) {
	       List<String> ids = new ArrayList<>();
	       
	       if (!authorizedStages.isEmpty()) {
	           StringBuilder queryBuilder = new StringBuilder("select id from (");
	       
	           for (String status : authorizedStages) {
	               queryBuilder.append("select cast(id as text) from hrms.employee_leave_ledger where status = '")
	                           .append(status)
	                           .append("' union ");
	           }
	       
	           int length = queryBuilder.length();
	           queryBuilder.delete(length - 7, length);
	           queryBuilder.append(") as t");
	       
	           ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
	       }
	       return ids;
	}
	

	public List<String> fetchIdsByAuthorizedStagesForLicPolicy(List<String> authorizedStages) {
	       List<String> ids = new ArrayList<>();
	       
	       if (!authorizedStages.isEmpty()) {
	           StringBuilder queryBuilder = new StringBuilder("select id from (");
	       
	           for (String status : authorizedStages) {
	               queryBuilder.append("select cast(id as text) from hrms.employee_lic_policy where status = '")
	                           .append(status)
	                           .append("' union ");
	           }
	       
	           int length = queryBuilder.length();
	           queryBuilder.delete(length - 7, length);
	           queryBuilder.append(") as t");
	       
	           ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
	       }
	       return ids;
	}


	
	public List<String> fetchIdsByAuthorizedStagesForEmployeeAlwaysAbsent(List<String> authorizedStages) {
	       List<String> ids = new ArrayList<>();
	       
	       if (!authorizedStages.isEmpty()) {
	           StringBuilder queryBuilder = new StringBuilder("select id from (");
	       
	           for (String status : authorizedStages) {
	               queryBuilder.append("select cast(id as text) from hrms.employee_always_absent where status = '")
	                           .append(status)
	                           .append("' union ");
	           }
	       
	           int length = queryBuilder.length();
	           queryBuilder.delete(length - 7, length);
	           queryBuilder.append(") as t");
	       
	           ids = entityManager.createNativeQuery(queryBuilder.toString()).getResultList();
	       }
	       return ids;
	}
}
