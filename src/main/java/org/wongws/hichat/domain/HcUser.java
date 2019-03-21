package org.wongws.hichat.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "t_user")
public class HcUser implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "autoid")
	private Long autoid;
	@Column(name = "userid")
	private String userid;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "lastip")
	private String lastip;
	@Column(name = "lastvisit")
	private LocalDateTime lastvisit;
	@Column(name = "registered_time")
	private LocalDateTime registered_time;
	@Column(name = "unregistered_time")
	private LocalDateTime unregistered_time;
	@ManyToMany(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinTable(name = "r_user_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private List<HcRole> roles;

	public Long getAutoid() {
		return autoid;
	}

	public void setAutoid(Long autoid) {
		this.autoid = autoid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getLastip() {
		return lastip;
	}

	public void setLastip(String lastip) {
		this.lastip = lastip;
	}

	public LocalDateTime getLastvisit() {
		return lastvisit;
	}

	public void setLastvisit(LocalDateTime lastvisit) {
		this.lastvisit = lastvisit;
	}

	public LocalDateTime getRegistered_time() {
		return registered_time;
	}

	public void setRegistered_time(LocalDateTime registered_time) {
		this.registered_time = registered_time;
	}

	public LocalDateTime getUnregistered_time() {
		return unregistered_time;
	}

	public void setUnregistered_time(LocalDateTime unregistered_time) {
		this.unregistered_time = unregistered_time;
	}

	public List<HcRole> getRoles() {
		return roles;
	}

	public void setRoles(List<HcRole> roles) {
		this.roles = roles;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		List<HcRole> roles = this.getRoles();
		for (HcRole role : roles) {
			auths.add(new SimpleGrantedAuthority(role.getName()));
		}
		return auths;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
