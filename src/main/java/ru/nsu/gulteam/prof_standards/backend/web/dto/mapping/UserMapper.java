package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.RegisterData;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.UserDto;

@Configuration
public class UserMapper {
    PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public UserMapper createUserMapper(PasswordEncoder passwordEncoder) {
        return new UserMapper(passwordEncoder);
    }

    public UserDto toDto(User user){
        UserDto userDto = new UserDto();

        userDto.setFirstName(user.getFirstName());
        userDto.setSecondName(user.getSecondName());
        userDto.setLogin(user.getLogin());

        return userDto;
    };

    public User toUser(RegisterData registerData){
        User user = new User();

        user.setFirstName(registerData.getFirstName());
        user.setSecondName(registerData.getSecondName());
        user.setLogin(registerData.getAuthData().getLogin());
        user.setPasswordHash(passwordEncoder.encode(registerData.getAuthData().getPassword()));

        return user;
    }
}
