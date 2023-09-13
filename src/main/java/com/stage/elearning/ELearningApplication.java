package com.stage.elearning;

import com.stage.elearning.model.role.Role;
import com.stage.elearning.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@RequiredArgsConstructor
@EnableCaching
public class ELearningApplication {

	private final RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(ELearningApplication.class, args);
	}

	@PostConstruct
	private void initProject()
	{
		initRoles();
	}
	private void initRoles()
	{
		roleRepository.save(new Role("ADMIN"));
		roleRepository.save(new Role("CLIENT"));
	}
}