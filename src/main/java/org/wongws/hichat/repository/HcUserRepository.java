package org.wongws.hichat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wongws.hichat.entity.HcUser;

public interface HcUserRepository extends JpaRepository<HcUser, Long> {
	HcUser findByUsername(String username);

	HcUser findById(long id);

	@Query(value = "SELECT t.* FROM t_user t,r_user_roles r WHERE r.role_id=:roleId AND t.id=r.user_id", nativeQuery = true)
	List<HcUser> findByHcRole_Id(@Param("roleId") int roleId);
}
