package org.wongws.hichat.entity;

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
	@Column(name = "id")
	private Long id;
	@Column(name = "user_hid")
	private String user_hid;
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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUser_hid() {
		return user_hid;
	}

	public void setUser_hid(String user_hid) {
		this.user_hid = user_hid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
