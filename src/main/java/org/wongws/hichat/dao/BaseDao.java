package org.wongws.hichat.dao;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class BaseDao {
	@Autowired
	protected EntityManagerFactory entityManagerFactory;
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate()
	{
		return jdbcTemplate;
	}
}
