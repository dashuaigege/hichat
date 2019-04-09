package org.wongws.hichat.domain;

import org.wongws.hichat.util.ChatEnum;

public class SimpleUser {
	public SimpleUser(String id, String name, Long img, boolean online) {
		this.id = id;
		this.name = name;
		if (online) {
			this.img = "/hichat/toFindUserimg?id=" + img + "_" + ChatEnum.EnuPicType.online.getValue();
		} else {
			this.img = "/hichat/toFindUserimg?id=" + img + "_" + ChatEnum.EnuPicType.offline.getValue();
		}
		this.online = online;
	}

	private String id;
	private String name;
	private String img;
	private boolean online;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

}
