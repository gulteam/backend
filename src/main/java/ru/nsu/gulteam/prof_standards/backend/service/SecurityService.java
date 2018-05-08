package ru.nsu.gulteam.prof_standards.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Token;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.TokenRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.UserRepository;
import ru.nsu.gulteam.prof_standards.backend.entity.FullUserInfo;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectTokenException;
import ru.nsu.gulteam.prof_standards.backend.exception.NotAuthorizedException;
import ru.nsu.gulteam.prof_standards.backend.exception.RegisterException;
import ru.nsu.gulteam.prof_standards.backend.web.dto.mapping.UserMapper;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.AuthData;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.RegisterData;

@Service
public class SecurityService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;
    private final UserService userService;

    @Autowired
    public SecurityService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, UserMapper userMapper, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.userMapper = userMapper;
        this.userService = userService;
    }

    public org.springframework.security.core.userdetails.User getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
    }

    public Token signIn(AuthData authData) {
        User user = userRepository.findByLoginIgnoreCase(authData.getLogin());

        if (user == null || !passwordEncoder.matches(authData.getPassword(), user.getPasswordHash())) {
            throw new UsernameNotFoundException("Incorrect login or password");
        }

        String uniqueTokenData = Integer.toString((int) (Math.random() * Integer.MAX_VALUE));
        Token token = tokenRepository.save(new Token(uniqueTokenData));
        token = tokenRepository.connectToUser(token, user);

        return token;
    }

    public void removeToken(User user, String authTokenData) {
        Token token = tokenRepository.findByData(authTokenData);

        if (token == null || !userRepository.hasToken(user, token)) {
            throw new IncorrectTokenException("Token not exists or not associated with current user");
        }

        tokenRepository.delete(token);
    }

    public User signUp(RegisterData registerData) {
        String login = registerData.getAuthData().getLogin();

        if (userRepository.findByLoginIgnoreCase(login) != null) {
            throw new RegisterException("This login is already used");
        }

        return userService.addNew(userMapper.toUser(registerData));
    }
}
