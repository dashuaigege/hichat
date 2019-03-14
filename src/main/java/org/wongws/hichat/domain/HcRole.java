package org.wongws.hichat.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class HcRole {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
}
