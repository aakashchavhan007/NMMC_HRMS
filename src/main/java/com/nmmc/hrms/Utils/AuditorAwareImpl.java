package com.nmmc.hrms.Utils;

import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.nmmc.auth.dao.UserDetailsImpl;
import com.nmmc.hrms.Dao.UserInfo;

@Component
public class AuditorAwareImpl implements AuditorAware<UUID> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Optional<UUID> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		if (authentication.getPrincipal() instanceof String) {
			String username = (String) authentication.getPrincipal();
			return fetchUserIdByUsername(username);
		}

		if (authentication.getPrincipal() instanceof UserDetailsImpl) {
			return Optional.of(((UserDetailsImpl) authentication.getPrincipal()).getId());
		}

		return Optional.empty();
	}

	public Optional<UUID> fetchUserIdByUsername(String username) {
		// Use createNativeQuery for raw SQL queries
		Query query = entityManager
				.createNativeQuery("SELECT CAST(id AS TEXT) FROM common.user_master WHERE user_name = :username");
		query.setParameter("username", username);
		// Retrieve the result and convert it to UUID
		String uuid = (String) query.getSingleResult();
		return Optional.of(UUID.fromString(uuid));
	}

	public Optional<String> fetchUsernameFullName(String username) {
		// Use createNativeQuery for raw SQL queries
		Query query = entityManager.createNativeQuery("SELECT  " + "    TRIM( " + "        CASE  "
				+ "            WHEN user_middle_name IS NOT NULL AND user_middle_name <> ''  "
				+ "            THEN COALESCE(user_first_name, '') || ' ' || user_middle_name || ' ' || COALESCE(user_last_name, '') "
				+ "            ELSE COALESCE(user_first_name, '') || ' ' || COALESCE(user_last_name, '') "
				+ "        END " + "    ) AS full_name " + "FROM  "
				+ "    common.user_master where user_name=:username");
		query.setParameter("username", username);
		// Retrieve the result and convert it to UUID
		String fullName = (String) query.getSingleResult();
		return Optional.of(fullName);
	}

	public Optional<UserInfo> fetchUsernameInfo(String username) {
		// Use createNativeQuery for raw SQL queries
		Query query = entityManager.createNativeQuery("SELECT " + "    cast(um.id as text) AS user_id, "
				+ "    um.user_name AS user_name, " + "    TRIM( " + "        CASE "
				+ "            WHEN user_middle_name IS NOT NULL AND user_middle_name <> '' "
				+ "            THEN COALESCE(user_first_name, '') || ' ' || user_middle_name || ' ' || COALESCE(user_last_name, '') "
				+ "            ELSE COALESCE(user_first_name, '') || ' ' || COALESCE(user_last_name, '') "
				+ "        END " + "    ) AS full_name, " + "    cast(dept.id as text) AS department_id, "
				+ "    dept.department AS department_name, " + "    cast(desg.id as text) AS designation_id, "
				+ "    desg.designation AS designation_name " + "FROM " + "    common.user_master um "
				+ "LEFT JOIN common.department_master dept ON um.dpt_id = dept.id "
				+ "LEFT JOIN common.designation_master desg ON um.designation_id = desg.id "
				+ "WHERE um.user_name = :username");

		query.setParameter("username", username);

		// Retrieve the result as an Object array
		Object[] result;
		try {
			result = (Object[]) query.getSingleResult();
		} catch (NoResultException e) {
			return Optional.empty();
		}

		// Map the result to UserInfo with null safety
		UserInfo userInfo = UserInfo
				.builder()
				.userId(result[0] != null ? UUID.fromString(result[0].toString()) : null)
				.userName(result[1] != null ? (String) result[1] : null)
				.userFullName(result[2] != null ? (String) result[2] : null)
				.departmentId(result[3] != null ?  UUID.fromString(result[3].toString()) : null)
				.departmentName(result[4] != null ? (String) result[4] : null)
				.desginationId(result[5] != null ? UUID.fromString(result[5].toString()) : null)
				.desginationName(result[6] != null ? (String) result[6] : null)
				.build();
		return Optional.of(userInfo);
	}

}

