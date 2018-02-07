package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.entity.FullUserInfo;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.RegisterData;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.UserDto;

@Mapper(componentModel = "spring",
        uses = {RoleMapper.class,
                FacultyMapper.class,
                DepartmentMapper.class})
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mappings({
            @Mapping(target = "id", source = "user.id"),
            @Mapping(target = "firstName", source = "user.firstName"),
            @Mapping(target = "secondName", source = "user.secondName"),
            @Mapping(target = "login", source = "user.login"),
    })
    public abstract UserDto toDto(FullUserInfo user);

    @Mappings({
            @Mapping(target = "user.firstName", source = "firstName"),
            @Mapping(target = "user.secondName", source = "secondName"),
            @Mapping(target = "user.login", source = "login"),
    })
    public abstract FullUserInfo fromDto(UserDto userDto);

    @Mappings({
            @Mapping(target = "login", source = "authData.login"),
            @Mapping(target = "passwordHash", ignore = true),
    })
    public abstract User toUser(RegisterData registerData);

    @AfterMapping
    protected void afterMapping(RegisterData registerData, @MappingTarget User user) {
        user.setPasswordHash(passwordEncoder.encode(registerData.getAuthData().getPassword()));
    }
}
