package org.wongws.hichat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wongws.hichat.entity.HcLoginlog;

public interface HcLoginlogRepository extends JpaRepository<HcLoginlog, Long> {

}
