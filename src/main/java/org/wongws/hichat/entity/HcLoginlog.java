package org.wongws.hichat.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_loginlog")
public class HcLoginlog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "autoid")
	private long autoid;
	@Column(name = "userid")
	private long userid;
	@Column(name = "ip")
	private String ip;
	@Column(name = "logindate")
	private LocalDateTime logindate;

	public long getAutoid() {
		return autoid;
	}

	public void setAutoid(long autoid) {
		this.autoid = autoid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public LocalDateTime getLogindate() {
		return logindate;
	}

	public void setLogindate(LocalDateTime logindate) {
		this.logindate = logindate;
	}
}
