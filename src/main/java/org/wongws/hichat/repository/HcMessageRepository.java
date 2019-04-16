package org.wongws.hichat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wongws.hichat.entity.HcMessage;

public interface HcMessageRepository extends JpaRepository<HcMessage, Long> {

}
