package com.esc.identity.service.impl;

import static org.springframework.security.core.userdetails.User.withUsername;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.esc.identity.entity.Privilege;
import com.esc.identity.entity.Role;
import com.esc.identity.entity.User;
import com.esc.identity.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new UsernameNotFoundException(String.format("User with email %s does not exist", email)));

		return withUsername(user.getEmail())
				.password(user.getPassword())
				.authorities(getAuthorities(user.getRoles()))
				.accountExpired(false)
				.accountLocked(false)
				.credentialsExpired(false)
				.disabled(user.isDisabled())
				.build();
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
		return getGrantedAuthorities(getPrivileges(roles));
	}

	private Collection<String> getPrivileges(Collection<Role> roles) {
		Collection<String> privileges = new ArrayList<>();
		Collection<Privilege> collection = new ArrayList<>();
		for (Role role : roles) {
			privileges.add(role.getName());
			collection.addAll(role.getPrivileges());
		}
		
		for (Privilege privilege : collection) {
			privileges.add(privilege.getName());
		}
		
		// Returns a collection of the role name and all assigned privileges
		return privileges;
	}

	private Collection<GrantedAuthority> getGrantedAuthorities(Collection<String> privileges) {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

}
