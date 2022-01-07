package com.esc.identity.config;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esc.identity.entity.Privilege;
import com.esc.identity.entity.Role;
import com.esc.identity.entity.User;
import com.esc.identity.repository.PrivilegeRepository;
import com.esc.identity.repository.RoleRepository;
import com.esc.identity.repository.UserRepository;

@Component
@Transactional
public class DataLoader implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final UserRepository userRepo;
	private RoleRepository roleRepo;
	private final PrivilegeRepository privilegeRepo;
	private final PasswordEncoder passwordEncoder;

	public DataLoader(UserRepository userRepo, RoleRepository roleRepo, PrivilegeRepository privilegeRepo, PasswordEncoder passwordEncoder) {
		super();
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.privilegeRepo = privilegeRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Creating Privileges");
		Privilege read = createPrivilege("READ_PRIVILEGE");
		Privilege write = createPrivilege("WRITE_PRIVILEGE");
		logger.info("Read and Write Privileges created");
		
		logger.info("Creating Admin Role");
		Role admin = createRole("ROLE_ADMIN", Arrays.asList(read, write));
		logger.info("Admin Role Created");
		
		logger.info("Creating Admin User");
		User user = new User();
		user.setEmail("admin@gmail.com");
		user.setFirstName("Admin");
		user.setLastName("Admin");
		user.setDisabled(false);
		user.setPassword(passwordEncoder.encode("admin123"));
		user.setRoles(Arrays.asList(admin));
		
		userRepo.save(user);
		logger.info("Admin User Created");

	}

	Role createRole(String name, Collection<Privilege> privileges) {
		return roleRepo.findByName(name).orElse(roleRepo.save(new Role(name, privileges)));
	}

	Privilege createPrivilege(String name) {
		return privilegeRepo.findByName(name).orElse(privilegeRepo.save(new Privilege(name)));
	}

}
