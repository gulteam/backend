package ru.nsu.gulteam.prof_standards.backend.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Token;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;
import ru.nsu.gulteam.prof_standards.backend.service.FgosService;
import ru.nsu.gulteam.prof_standards.backend.service.ProgramService;
import ru.nsu.gulteam.prof_standards.backend.service.SecurityService;
import ru.nsu.gulteam.prof_standards.backend.service.UserService;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.TokenMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.UserMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.AuthData;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.RegisterData;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.GlobalPermissionsDto;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.Message;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.UserDto;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1")
public class UserController extends BaseController {
    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final SecurityService securityService;
    private final UserService userService;
    private final ProgramService programService;
    private final FgosService fgosService;

    @RequestMapping(path = "user", method = RequestMethod.GET)
    public ResponseEntity<?> getMe() {
        User user = userService.getUserEntity(securityService.getUserDetails());
        return ResponseEntity.ok(userMapper.toDto(userService.getFullUserInfo(user)));
    }

    @RequestMapping(path = "signIn", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody AuthData authData) {
        Token token = securityService.signIn(authData);
        return ResponseEntity.ok(tokenMapper.toDto(token));
    }

    @RequestMapping(path = "signOut", method = RequestMethod.GET)
    public ResponseEntity<?> signOut(@RequestHeader(name = "X-Auth-Token") String authTokenData) {
        User user = userService.getUserEntity(securityService.getUserDetails());
        securityService.removeToken(user, authTokenData);
        return ResponseEntity.ok(new Message("Successfully signed out"));
    }

    @RequestMapping(path = "signUp", method = RequestMethod.POST)
    public ResponseEntity<?> signIn(@RequestBody RegisterData registerData) {
        User user = securityService.signUp(registerData);
        return ResponseEntity.ok(userMapper.toDto(userService.getFullUserInfo(user)));
    }

    @RequestMapping(path = "allUsers", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUser() {
        User requester = userService.getUserEntity(securityService.getUserDetails());
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users.stream().map(u->userService.getFullUserInfo(requester, u)).map(userMapper::toDto).collect(Collectors.toList()));
    }

    @RequestMapping(path = "user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable int userId) {
        User requester = userService.getUserEntity(securityService.getUserDetails());
        User user = userService.getUser(userId);
        return ResponseEntity.ok(userMapper.toDto(userService.getFullUserInfo(requester, user)));
    }

    @RequestMapping(path = "user/{userId}", method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(@PathVariable int userId, @RequestBody UserDto userDto) {
        User requester = userService.getUserEntity(securityService.getUserDetails());

        User user = userService.updateUser(requester, userMapper.fromDto(userDto), userId);

        return ResponseEntity.ok(userMapper.toDto(userService.getFullUserInfo(requester, user)));
    }

    @RequestMapping(path = "userPermissions", method = RequestMethod.GET)
    public ResponseEntity<?> getMyPermissions() {
        User user = userService.getUserEntity(securityService.getUserDetails());

        GlobalPermissionsDto permissions = new GlobalPermissionsDto(
                programService.canCreateProgram(user),
                programService.canReadProgramsList(user),
                fgosService.canCreateFgos(user),
                fgosService.canReadFgosList(user),
                user != null && userService.getUserRole(user).getName().equals(UserRole.ADMINISTRATOR)
        );

        return ResponseEntity.ok(permissions);
    }
}
