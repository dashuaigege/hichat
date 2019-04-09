package org.wongws.hichat.dao;

import org.springframework.stereotype.Repository;

@Repository
public class HcUserDao extends BaseDao {
	
	private final static String MATCH_COUNT_SQL = "SELECT COUNT(*) FROM t_user WHERE username=? AND password=?";
	private final static String MATCHUSERNAME_COUNT_SQL = "SELECT COUNT(*) FROM t_user WHERE username=?";

	public final int getMatchCount(String username, String password) {
		return getJdbcTemplate().queryForObject(MATCH_COUNT_SQL, new Object[] { username, password }, Integer.class);
	}

	public final int getMatchUsernameCount(String username) {
		return getJdbcTemplate().queryForObject(MATCHUSERNAME_COUNT_SQL, new Object[] { username }, Integer.class);
	}
}
