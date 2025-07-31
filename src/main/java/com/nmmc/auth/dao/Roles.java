package com.nmmc.auth.dao;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Roles {

	public UUID roleId;
	public String role;
	public List<Menu> menus;

}

