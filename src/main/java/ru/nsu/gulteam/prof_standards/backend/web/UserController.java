package ru.nsu.gulteam.prof_standards.backend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.UserRepository;
import ru.nsu.gulteam.prof_standards.backend.service.SecurityService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.UserMapper;

@RestController
@RequestMapping(path = "api/v1")
public class UserController extends BaseController {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final SecurityService securityService;

    @Autowired
    public UserController(UserMapper userMapper,
                          UserRepository userRepository,
                          SecurityService securityService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @RequestMapping(path = "user", method = RequestMethod.GET)
    public ResponseEntity<?> getUser() {
        User user = securityService.getUserDetails();
        return ResponseEntity.ok(userMapper.toDto(userRepository.findByLoginIgnoreCase(user.getUsername())));
    }
}
