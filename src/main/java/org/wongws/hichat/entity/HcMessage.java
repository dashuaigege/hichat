package org.wongws.hichat.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_message")
public class HcMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "autoid")
	private Long autoid;
	@Column(name = "send_id")
	private String send_id;
	@Column(name = "reciver_id")
	private String reciver_id;
	@Column(name = "content")
	private String content;
	@Column(name = "time")
	private LocalDateTime time;

	public Long getAutoid() {
		return autoid;
	}

	public void setAutoid(Long autoid) {
		this.autoid = autoid;
	}

	public String getSend_id() {
		return send_id;
	}

	public void setSend_id(String send_id) {
		this.send_id = send_id;
	}

	public String getReciver_id() {
		return reciver_id;
	}

	public void setReciver_id(String reciver_id) {
		this.reciver_id = reciver_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

}
