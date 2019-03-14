package org.wongws.hichat.domain;

import java.util.Date;

public class Message {
	private String text;
	private Date date;
	private boolean self;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean self) {
		this.self = self;
	}
}
