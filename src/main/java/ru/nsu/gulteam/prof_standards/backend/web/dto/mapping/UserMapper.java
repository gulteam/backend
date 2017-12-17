package ru.nsu.gulteam.prof_standards.backend.web.dto.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.entity.FullUserInfo;
import ru.nsu.gulteam.prof_standards.backend.web.dto.request.RegisterData;
import ru.nsu.gulteam.prof_standards.backend.web.dto.response.UserDto;

@Configuration
public class UserMapper {
    private PasswordEncoder passwordEncoder;
    private RoleMapper roleMapper;
    private FacultyMapper facultyMapper;
    private DepartmentMapper departmentMapper;

    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder, RoleMapper roleMapper, FacultyMapper facultyMapper, DepartmentMapper departmentMapper) {
        this.passwordEncoder = passwordEncoder;
        this.roleMapper = roleMapper;
        this.facultyMapper = facultyMapper;
        this.departmentMapper = departmentMapper;
    }

    @Bean
    public UserMapper createUserMapper(PasswordEncoder passwordEncoder, RoleMapper roleMapper, FacultyMapper facultyMapper, DepartmentMapper departmentMapper) {
        return new UserMapper(passwordEncoder, roleMapper, facultyMapper, departmentMapper);
    }

    public UserDto toDto(FullUserInfo user){
        UserDto userDto = new UserDto();

        User userEntity = user.getUser();

        userDto.setFirstName(userEntity.getFirstName());
        userDto.setSecondName(userEntity.getSecondName());
        userDto.setLogin(userEntity.getLogin());
        userDto.setId(userEntity.getId());

        userDto.setRole(roleMapper.toDto(user.getRole()));
        userDto.setDepartment(departmentMapper.toDto(user.getDepartment()));
        userDto.setFaculty(facultyMapper.toDto(user.getFaculty()));

        return userDto;
    };


    public FullUserInfo fromDto(UserDto userDto){
        FullUserInfo fullUserInfo = new FullUserInfo();

        fullUserInfo.setUser(new User(userDto.getId(), userDto.getFirstName(), userDto.getSecondName(), userDto.getLogin()));
        fullUserInfo.setRole(roleMapper.fromDto(userDto.getRole()));

        if(userDto.getFaculty() != null){
            fullUserInfo.setFaculty(facultyMapper.fromDto(userDto.getFaculty()));
        }

        if(userDto.getDepartment() != null){
            fullUserInfo.setDepartment(departmentMapper.fromDto(userDto.getDepartment()));
        }

        return fullUserInfo;
    }

    public User toUser(RegisterData registerData){
        User user = new User();

        user.setFirstName(registerData.getFirstName());
        user.setSecondName(registerData.getSecondName());
        user.setLogin(registerData.getAuthData().getLogin());
        user.setPasswordHash(passwordEncoder.encode(registerData.getAuthData().getPassword()));

        return user;
    }
}
