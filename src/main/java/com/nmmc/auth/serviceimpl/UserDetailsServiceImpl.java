package com.nmmc.auth.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nmmc.auth.dao.Menu;
import com.nmmc.auth.dao.Roles;
import com.nmmc.auth.dao.UserDetailsImpl;
import com.nmmc.auth.dao.Workflow;
import com.nmmc.auth.dao.WorkflowDetails;
import com.nmmc.auth.entity.TitleMaster;
import com.nmmc.auth.entity.User;
import com.nmmc.auth.repository.MstRoleMenuMappingRepository;
import com.nmmc.auth.repository.MstUserRoleMappingRepository;
import com.nmmc.auth.repository.TitleMasterRepository;
import com.nmmc.auth.repository.UserRepository;
import com.nmmc.auth.service.UserDetailsService;

// import com.mpad.reports.dto.UserDetailsImpl;
// import com.mpad.reports.entity.User;
// import com.mpad.reports.repository.UserRepository;
// import com.mpad.reports.service.UserDetailsService;

// import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private MstUserRoleMappingRepository mstUserRoleMappingRepository;

	@Autowired
	private MstRoleMenuMappingRepository mstRoleMenuMappingRepository;

	@Autowired
	private TitleMasterRepository titleMasterRepository;

	// @Autowired
	// MstRoleMenuMappingRepository roleRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<User> userOpt = userRepository.findByUserName(userName);
		HashMap<String, Object> addOnInfo = new HashMap<>();
		try {
			userOpt.ifPresent(user -> {
				// if (user.getUserPassword().equalsIgnoreCase(usrPassword)) {
	
				JsonObject sucJo = new JsonObject();
				sucJo.addProperty("status", "Success");
	
				addOnInfo.put("status", "Success");
	
				JsonObject jobj = new JsonObject();
				// UUID usrId = user.getId();
				jobj.addProperty("usrName", user.getUserName());
				jobj.addProperty("ugpPrevId", user.getUgpPrevId().toString());
				jobj.addProperty("usrFullName", user.getUserFirstName() + " " + user.getUserLastName());
				jobj.addProperty("dptId", user.getDptId() == null ? null : user.getDptId().toString());
				jobj.addProperty("wardId", user.getWardId() == null ? null : user.getWardId().toString());
				jobj.addProperty("designationId",
						user.getDesignationId() == null ? null : user.getDesignationId().toString());
				jobj.addProperty("documents", user.getDocuments() == null ? null : user.getDocuments());
				jobj.addProperty("isCFCUser", user.getIsCfcUser() == null ? null : user.getIsCfcUser().toString());
				String title = null;
				if (user.getTitleId() != null) {
					TitleMaster titleMaster = titleMasterRepository.findById(user.getTitleId()).get();
					title = titleMaster.getTitleName();
				}
	
				jobj.addProperty("title", title);
				jobj.addProperty("firstName", user.getUserFirstName() == null ? null : user.getUserFirstName());
				jobj.addProperty("middleName", user.getUserMiddleName() == null ? null : user.getUserMiddleName());
				jobj.addProperty("lastName", user.getUserLastName() == null ? null : user.getUserLastName());
				jobj.addProperty("email", user.getUserEmail() == null ? null : user.getUserEmail());
				jobj.addProperty("mobile", user.getUserMobile() == null ? null : user.getUserMobile());
				jobj.addProperty("address", user.getAdress() == null ? null : user.getAdress());
				jobj.addProperty("city", user.getCity() == null ? null : user.getCity());
				jobj.addProperty("pinCode", user.getPinCode() == null ? null : user.getPinCode());
	
				UUID driverId = userRepository.getDriverId(user.getId());
				if (driverId != null) {
					jobj.addProperty("driverId", driverId.toString());
				}
	
				sucJo.add("data", jobj);
	
				addOnInfo.put("data", jobj);
	
				// List<Object[]> workflowList = userRepository.getWorkflowByUserId1(user.getId());
				// JsonArray workflowArray = new JsonArray();
	
				// for (Object[] workflow : workflowList) {
				// 	String templateName = (String) workflow[1]; 
				// 	String stage = (String) workflow[0]; 
	
				// 	JsonObject workflowDetailsJsonObject = new JsonObject();
				// 	workflowDetailsJsonObject.addProperty("stage", stage);
	
				// 	JsonArray workflowDetailsJsonArray = new JsonArray();
				// 	workflowDetailsJsonArray.add(workflowDetailsJsonObject);
	
				// 	JsonObject workflowJsonObject = new JsonObject();
				// 	workflowJsonObject.addProperty("templateName", templateName);
				// 	workflowJsonObject.add("workflowDetails", workflowDetailsJsonArray);
	
				// 	workflowArray.add(workflowJsonObject);
				// }
	
				// sucJo.add("workflow", workflowArray);
	
				// addOnInfo.put("workflow", workflowArray);
	
				
				// for (Object[] roles : rolesList) {
				// 	JsonObject rolesJsonObject = new JsonObject();
				// 	rolesJsonObject.addProperty("role", roles[2] == null ? "" : roles[2].toString());
	
				// 	// Fetch menus for this role
				// 	UUID roleId = UUID.fromString(roles[1].toString());
				// 	List<Object[]> menuList = mstRoleMenuMappingRepository.getRoleMenuList(roleId);
	
				// 	JsonArray menuArray = new JsonArray();
				// 	for (Object[] menu : menuList) {
				// 		JsonObject menuJsonObject = new JsonObject();
				// 		menuJsonObject.addProperty("menuId", menu[0] == null ? "" : menu[0].toString());
				// 		menuJsonObject.addProperty("menuNameEng", menu[1] == null ? "" : menu[1].toString());
				// 		menuJsonObject.addProperty("menuNameMr", menu[2] == null ? "" : menu[2].toString());
				// 		menuJsonObject.addProperty("menuFlag", menu[3] == null ? "" : menu[3].toString());
				// 		menuJsonObject.addProperty("url", menu[4] == null ? "" : menu[4].toString());
	
				// 		menuArray.add(menuJsonObject);
				// 	}
				// 	rolesJsonObject.add("menu", menuArray);
				// 	rolesArray.add(rolesJsonObject);
				// }

				List<Object[]> workflowList = userRepository.getWorkflowByUserId1(user.getId());

				List<Workflow> workflows = workflowList.stream().map(workflow -> {
					String stage = (String) workflow[0];
					String templateName = (String) workflow[1];

					List<WorkflowDetails> workflowDetailsList = new ArrayList<>();
					workflowDetailsList.add(new WorkflowDetails(stage));

					return new Workflow(templateName, workflowDetailsList);
				}).collect(Collectors.toList());

				addOnInfo.put("workflow", workflows);

				List<Object[]> rolesList = mstUserRoleMappingRepository.getRolesByUserId(user.getId());
				JsonArray rolesArray = new JsonArray();
	
				List<Roles> roles = rolesList.stream().map(rrr -> {
					Roles role = new Roles();
					role.setRoleId(UUID.fromString(rrr[1].toString()));
					role.setRole(rrr[2].toString());
	
					// Fetch menus for this role
					UUID roleId = UUID.fromString(rrr[1].toString());
					role.setMenus(mstRoleMenuMappingRepository.getRoleMenuList(roleId).stream().map(mmm -> {
						Menu menu = new Menu();
						menu.setMenuId(UUID.fromString(mmm[0].toString()));
						menu.setMenuNameEng(mmm[1].toString());
						menu.setMenuNameMr(mmm[2].toString());
						menu.setUrl(mmm[4].toString());
						menu.setMenuFlag(mmm[3].toString());
						menu.setModuleId(UUID.fromString(mmm[8].toString()));
						menu.setModuleName(mmm[9].toString());
	
						return menu;
					}).collect(Collectors.toList()));
					return role;
				}).collect(Collectors.toList());
				addOnInfo.put("rolesArray", rolesArray);
				addOnInfo.put("roles", roles);
				// String token = jwtUtil.generateToken(userDetails, rolesArray);
				// sucJo.addProperty("token", token);
				// return sucJo.toString();
				// }
			});
		
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return UserDetailsImpl.build(userOpt.get(), addOnInfo);

		// if (user.isPresent()) {
		// Set<String> roles;
		// if (user.get().getActiveFlag() != null &&
		// user.get().getActiveFlag().equals('Y')) {
		// return UserDetailsImpl.build(user.get(), roles);
		// } else {
		// throw new UsernameNotFoundException(username + " Is not active");
		// }
		// } else {
		// throw new UsernameNotFoundException("User Not Found with username: " +
		// username);
		// }
	}
}
