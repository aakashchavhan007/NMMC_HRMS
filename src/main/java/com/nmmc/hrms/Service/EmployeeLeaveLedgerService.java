package com.nmmc.hrms.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.nmmc.hrms.Dao.EmployeeLeaveLedgerDao;
import com.nmmc.hrms.Dao.ResponseDao;
import com.nmmc.hrms.Dao.ScrutinyNoteDao;
import com.nmmc.hrms.Entity.EmployeeLeaveLedger;

public interface EmployeeLeaveLedgerService {

	String save(@Valid EmployeeLeaveLedgerDao employeeLeaveLedgerDao, HttpServletRequest request);

	ResponseDao getAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir, String searchParam,
			Locale locale, HttpServletRequest request);

	EmployeeLeaveLedgerDao getByLaId(@Valid UUID id);

	EmployeeLeaveLedger getById(UUID id);

	ResponseDao getAllCompleted(Integer pageNo, Integer pageSize, String sortBy, String sortDir, String searchParam,
			Locale locale, HttpServletRequest request);

	String savePreviewNoteContent(ScrutinyNoteDao scrutinyNoteDao);

	public ResponseDao getAllByFilter(Integer pageNo, Integer pageSize, String sortBy, String sortDir,
            String searchParam, String filterType, Locale locale, HttpServletRequest request);

	List<EmployeeLeaveLedgerDao> getByEmployeeId(Long employeeId);



}
