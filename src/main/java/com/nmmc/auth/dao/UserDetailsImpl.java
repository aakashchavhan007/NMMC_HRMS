package com.nmmc.auth.dao;

import java.util.ArrayList;
/**
 * @author Ashish Dahale
 *
 */
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nmmc.auth.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String token;

	private UUID id;

	private String username;

	@JsonIgnore
	private String password;

	private String fullName;

	private String email;

	private String mobileNo;

	private List<Roles> roles;

	private List<Workflow> workflows;

	private Collection<? extends GrantedAuthority> authorities;

	private JsonArray rolesArray;

	private Long ugpPrevId;

	private UUID dptId;

	private UUID wardId;

	private String previousDocument;

	private UUID designationId;

	private String mobile;

	private Long pinCode;

	public static UserDetailsImpl build(User user, HashMap<String, Object> addOnInfo) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		if (addOnInfo.containsKey("rolesArray")) {
			JsonArray rolesArray = (JsonArray) addOnInfo.get("rolesArray");
			for (JsonElement roleElement : rolesArray) {
				JsonObject roleObject = roleElement.getAsJsonObject();
				String roleName = roleObject.get("role").getAsString();
				authorities.add(new SimpleGrantedAuthority(
						roleName.contains("ROLE_") ? roleName : "ROLE_".concat(roleName)));
			}
		}

		JsonArray rolesArray = (JsonArray) addOnInfo.get("rolesArray");

		return UserDetailsImpl.builder().id(user.getId()).username(user.getUserName()).password(user.getUserPassword())
				.fullName((user.getUserFirstName() != null ? user.getUserFirstName() : "")
						// + (user.getUserMiddleName() != null ? " " + user.getUserMiddleName() : "")
						+ (user.getUserLastName() != null ? " " + user.getUserLastName() : ""))
				.email(user.getUserEmail()).mobileNo(user.getUserMobile())
				.ugpPrevId(user.getUgpPrevId())
				.dptId(user.getDptId())
				.wardId(user.getWardId())
				.previousDocument(user.getDocuments())
				.designationId(user.getDesignationId())
				.mobile(user.getUserMobile())
				.pinCode(user.getPinCode())
				// .rolesArray(rolesArray)
				.roles((List<Roles>) addOnInfo.get("roles"))
				.workflows((List<Workflow>) addOnInfo.get("workflows"))
				.authorities(authorities)
				.build();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public UUID getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	public List<Roles> getRoles() {
		return roles;
	}

	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}

	public List<Workflow> getWorkflows() {
		return workflows;
	}

	public void setWorkflows(List<Workflow> workflows) {
		this.workflows = workflows;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getUgpPrevId() {
		return ugpPrevId;
	}

	public void setUgpPrevId(Long ugpPrevId) {
		this.ugpPrevId = ugpPrevId;
	}

	public UUID getDptId() {
		return dptId;
	}

	public void setDptId(UUID dptId) {
		this.dptId = dptId;
	}

	public UUID getWardId() {
		return wardId;
	}

	public void setWardId(UUID wardId) {
		this.wardId = wardId;
	}

	public String getPreviousDocument() {
		return previousDocument;
	}

	public void setPreviousDocument(String previousDocument) {
		this.previousDocument = previousDocument;
	}

	public UUID getDesignationId() {
		return designationId;
	}

	public void setDesignationId(UUID designationId) {
		this.designationId = designationId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getPinCode() {
		return pinCode;
	}

	public void setPinCode(Long pinCode) {
		this.pinCode = pinCode;
	}

	public JsonArray getRolesArray() {
		return rolesArray;
	}

	public void setRolesArray(JsonArray rolesArray) {
		this.rolesArray = rolesArray;
	}

}

// authorities.add(new GrantedAuthority() {
// @Override
// public String getAuthority() {
// return "ROLE_ADMIN";
// }
// });
// user.getRoles().stream().filter(Objects::nonNull)
// .map(role -> new SimpleGrantedAuthority(
// role.getName().contains("ROLE_") ? role.getName() :
// "ROLE_".concat(role.getName())))
// .collect(Collectors.toList());

// .roles(user.getRoles().stream().map(entity
// -> {
// RoleDto dto = new RoleDto();
// BeanUtils.copyProperties(entity, dto);
// dto.setMenus(entity.getMenus().stream().map(menuEntity -> {
// MenuDto menuDto = new MenuDto();
// BeanUtils.copyProperties(menuEntity, menuDto);
// return menuDto;
// }).collect(Collectors.toSet()));
// return dto;
// }).collect(Collectors.toSet()))
