package com.stage.elearning.controller.user;

import com.stage.elearning.dto.user.UserEntityDTO;
import com.stage.elearning.model.role.Role;
import com.stage.elearning.service.user.UserEntityService;
import com.stage.elearning.utility.CustomResponseEntity;
import com.stage.elearning.utility.CustomResponseList;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/user_entity")
@RestController
public class UserEntityController {

    private final UserEntityService userEntityService;

    public UserEntityController(UserEntityService userEntityService)
    {
        this.userEntityService = userEntityService;
    }

    @GetMapping("/admin/get/id/{userId}")
    public ResponseEntity<CustomResponseEntity<UserEntityDTO>> fetchUserById(@PathVariable("userId") final UUID userId) {
        return userEntityService.fetchUserById(userId);
    }

    @GetMapping("/admin/get/all/users")
    public ResponseEntity<CustomResponseList<UserEntityDTO>> fetchAllUsers(
            @RequestParam(value = "pageNumber", required = true) final long pageNumber
    )
    {
        return  userEntityService.fetchAllUsers(pageNumber);
    }

    @GetMapping("/admin/search")
    public ResponseEntity<CustomResponseEntity<List<UserEntityDTO>>> searchUsers(
            @RequestParam(name = "fullName", required = false) String fullName,
            @RequestParam(name = "address" , required = false) String adress,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber
    )
    {
        return userEntityService.searchUser(fullName , adress , email ,phoneNumber);
    }

    @PutMapping("/admin/update/enable/id/{userId}")
    public ResponseEntity<CustomResponseEntity<String>> enableOrDisableUser(@PathVariable("userId") final UUID userId , @RequestParam("enabled") boolean enabled)
    {
        return userEntityService.enableOrDisableUser(userId,enabled);
    }
    @GetMapping("/all/get/current_user")
    public ResponseEntity<CustomResponseEntity<UserEntityDTO>> fetchCurrentUser(@AuthenticationPrincipal UserDetails userDetails)
    {
        return userEntityService.fetchCurrentUser(userDetails);
    }


}
