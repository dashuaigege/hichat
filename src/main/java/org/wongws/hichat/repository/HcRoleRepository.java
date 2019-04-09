package org.wongws.hichat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wongws.hichat.entity.HcRole;

public interface HcRoleRepository extends JpaRepository<HcRole, Long> {
	HcRole findByName(String name);
}
