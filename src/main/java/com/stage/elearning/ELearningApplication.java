package com.stage.elearning;

import com.stage.elearning.dto.auth.RegisterDTO;
import com.stage.elearning.model.role.Role;
import com.stage.elearning.repository.RoleRepository;
import com.stage.elearning.service.email.EmailSenderServiceImpl;
import com.stage.elearning.service.auth.AuthServiceImpl;
import com.stage.elearning.service.user.UserEntityServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
@RequiredArgsConstructor

public class ELearningApplication {
	private final RoleRepository roleRepository;
	private final UserEntityServiceImpl userEntityServiceImpl;
	private final AuthServiceImpl authServiceImpl;
	private final EmailSenderServiceImpl emailSenderService;
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