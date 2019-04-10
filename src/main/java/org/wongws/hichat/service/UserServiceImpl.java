package org.wongws.hichat.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wongws.hichat.context.SimpleUserContext;
import org.wongws.hichat.dao.HcUserDao;
import org.wongws.hichat.domain.SimpleUser;
import org.wongws.hichat.entity.HcRole;
import org.wongws.hichat.entity.HcUser;
import org.wongws.hichat.function.CreateUserImg;
import org.wongws.hichat.repository.HcRoleRepository;
import org.wongws.hichat.repository.HcUserRepository;
import org.wongws.hichat.util.ImageProducerUtil;
import org.wongws.hichat.util.Util;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {
	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private HcUserDao hcUserDao;

	@Autowired
	private HcUserRepository hcUserRepository;

	@Autowired
	private HcRoleRepository hcRoleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Executor asyncServiceExecutor;

	@Override
	public boolean hasMatchUser(String username, String password) {
		int matchCount = hcUserDao.getMatchCount(username, password);
		return matchCount > 0;
	}

	@Override
	public boolean hasMatchUsername(String username) {
		int matchCount = hcUserDao.getMatchUsernameCount(username);
		return matchCount > 0;
	}

	@Override
	public SimpleUserContext saveUser(String username, String password, String ip) {
		HcUser user = new HcUser();
		user.setUser_hid(UUID.randomUUID().toString());
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setLastip(ip);
		user.setRoles(Arrays.asList(hcRoleRepository.findByName("ROLE_USER")));
		user = hcUserRepository.save(user);
		if (user.getId() != null) {
			try {
				/**
				 * 此处采用多线程，一个线程创建Pic 一个线程将HcUser加载到内存中
				 */
				final CountDownLatch countDownLatch = new CountDownLatch(1);
				asyncServiceExecutor.execute(new CreateUserImg(user.getId(), user.getUsername(), countDownLatch));
				SimpleUser simpleUser = new SimpleUser(user.getUser_hid(), user.getUsername(), user.getId(), false);
				if (!Util.User_OnOff_Dic.containsKey(simpleUser.getId()))
					Util.User_OnOff_Dic.put(simpleUser.getId(), simpleUser);
				countDownLatch.await();
				SimpleUserContext simpleUserContext = new SimpleUserContext();
				simpleUserContext.setUser(simpleUser);
				logger.info("saveUser username: " + username + " success");
				return simpleUserContext;
			} catch (InterruptedException e) {
				logger.warn("asyncServiceExecutor execute failed:" + e.getMessage());
			}
		}
		return null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		HcUser user = hcUserRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("该用户不存在!");
		}
		return user;
	}

	@Override
	public void loadUsersByRole(int roleId) {
		List<HcUser> users = hcUserRepository.findByHcRole_Id(roleId);
		if (users != null && users.size() > 0) {
			for (HcUser user : users) {
				SimpleUser simpleUser = new SimpleUser(user.getUser_hid(), user.getUsername(), user.getId(), false);
				if (!Util.User_OnOff_Dic.containsKey(simpleUser.getId()))
					Util.User_OnOff_Dic.put(simpleUser.getId(), simpleUser);
			}
		}
	}

	@Override
	public HcUser getUserById(long id) {
		HcUser user = hcUserRepository.findById(id);
		return user;
	}

	@Override
	public HcUser getUserByUsername(String username) {
		HcUser user = hcUserRepository.findByUsername(username);
		return user;
	}

}
