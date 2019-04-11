package org.wongws.hichat.service;

import org.wongws.hichat.context.SimpleUserContext;
import org.wongws.hichat.entity.HcUser;

public interface UserService {
	boolean hasMatchUsername(String username);

	boolean hasMatchUser(String username, String password);

	SimpleUserContext saveUser(String username, String password, String ip);

	void loadUsersByRole(int roleId);

	HcUser getUserById(long id);
	
	HcUser getUserByUsername(String username);

}
