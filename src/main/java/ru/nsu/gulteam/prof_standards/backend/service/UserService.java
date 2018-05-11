package ru.nsu.gulteam.prof_standards.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Department;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Faculty;
import ru.nsu.gulteam.prof_standards.backend.domain.node.Role;
import ru.nsu.gulteam.prof_standards.backend.domain.node.User;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.DepartmentRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.FacultyRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.RoleRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.repository.UserRepository;
import ru.nsu.gulteam.prof_standards.backend.domain.type.UserRole;
import ru.nsu.gulteam.prof_standards.backend.entity.FullUserInfo;
import ru.nsu.gulteam.prof_standards.backend.exception.IncorrectIdentifierException;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final UserRole DEFAULT_ROLE = UserRole.USER;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initialize() {
        Arrays.stream(UserRole.values()).forEach(roleName -> {
            if (roleRepository.findRoleByName(roleName) == null) {
                roleRepository.save(new Role(roleName));
            }
        });

        if (getUsersByRole(UserRole.ADMINISTRATOR).isEmpty()) {
            User admin = addNew(new User("admin", "admin", "admin", passwordEncoder.encode("admin")));
            setRole(admin, UserRole.ADMINISTRATOR);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("Empty username");
        }

        User user = userRepository.findByLoginIgnoreCase(username.trim());
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
                user.getPasswordHash(),
                Collections.emptyList());
    }


    public User getUserEntity(org.springframework.security.core.userdetails.User userDetails) {
        if (userDetails == null) {
            return null;
        }

        User user = userRepository.findByLoginIgnoreCase(userDetails.getUsername());

        if (user == null) {
            //throw new UsernameNotFoundException("There is no username with same name");
            return null;
        }

        return user;
    }

    public FullUserInfo getFullUserInfo(User infoTarget) {
        return getFullUserInfo(null, infoTarget);
    }

    public FullUserInfo getFullUserInfo(User requester, User infoTarget) {
        return new FullUserInfo(infoTarget,
                roleRepository.getRole(infoTarget),
                departmentRepository.getDepartment(infoTarget),
                facultyRepository.getFaculty(infoTarget),
                canEdit(requester, infoTarget),
                canChangeRole(requester, infoTarget),
                canChangeFacultyAndDepartment(requester, infoTarget));
    }

    public User addNew(User user) {
        user = userRepository.save(user);
        setRole(user, DEFAULT_ROLE);
        return user;
    }

    public void setRole(User user, UserRole roleName) {
        userRepository.removeRole(user);
        Role role = roleRepository.findRoleByName(roleName);
        userRepository.setRole(user, role);
    }

    public List<User> getUsersByRole(UserRole roleName) {
        Role role = roleRepository.findRoleByName(roleName);
        List<User> users = userRepository.findAllByRole(role);
        return users;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(long userId) {
        User user = userRepository.findOne(userId);

        if (user == null) {
            throw new IncorrectIdentifierException("There is no user with id: " + userId);
        }

        return user;
    }

    public Role getUserRole(long userId) {
        User user = getUser(userId);
        return getUserRole(user);
    }

    public Role getUserRole(User user) {
        return roleRepository.getRole(user);
    }

    public User updateUser(User requester, FullUserInfo userInfo, long userId) {
        User user = userRepository.findOne(userId);

        if (user == null) {
            throw new IncorrectIdentifierException("There is no user with id: " + userId);
        }

        if (canEdit(requester, user)) {
            user.setFirstName(userInfo.getUser().getFirstName());
            user.setSecondName(userInfo.getUser().getSecondName());
        }

        user = userRepository.save(user);

        if (canChangeRole(requester, user)) {
            int requesterRoleOrd = getUserRole(requester).getName().ordinal();
            int targetRoleOrd = userInfo.getRole().getName().ordinal();

            int min = Math.min(requesterRoleOrd, targetRoleOrd);

            setRole(user, UserRole.values()[min]);
        }

        if (canChangeFacultyAndDepartment(requester, user)) {
            Faculty faculty = null;
            if (userInfo.getFaculty() != null &&
                    (faculty = facultyRepository.findOne(userInfo.getFaculty().getId())) == null) {
                throw new IncorrectIdentifierException("There is no faculty with id: " + userInfo.getFaculty().getId());
            }

            Department department = null;
            if (userInfo.getDepartment() != null &&
                    (department = departmentRepository.findOne(userInfo.getDepartment().getId())) == null) {
                throw new IncorrectIdentifierException("There is no department with id: " + userInfo.getDepartment().getId());
            }

            facultyRepository.disconnectFromFaculty(user);
            if (faculty != null) {
                facultyRepository.connectToUser(faculty, user);
            }

            departmentRepository.disconnectFromDepartment(user);
            if (department != null) {
                departmentRepository.connectToUser(department, user);
            }
        }

        return user;
    }

    // CRUD Permissions //--------------------------------------------------------------------------------------------//

    private boolean canChangeFacultyAndDepartment(User requester, User infoTarget) {
        if (requester == null) {
            return false;
        }

        return getUserRole(requester).getName().equals(UserRole.ADMINISTRATOR) ||
                getUserRole(requester).getName().equals(UserRole.DEAN_MEMBER) ||
                getUserRole(requester).getName().equals(UserRole.DEPARTMENT_MEMBER);
    }


    private boolean canChangeRole(User requester, User infoTarget) {
        if (requester == null) {
            return false;
        }

        int requesterRoleOrd = getUserRole(requester).getName().ordinal();
        int targetRoleOrd = getUserRole(infoTarget).getName().ordinal();

        return getUserRole(requester).getName().equals(UserRole.ADMINISTRATOR) ||

                (targetRoleOrd <= requesterRoleOrd) && (
                getUserRole(requester).getName().equals(UserRole.DEAN_MEMBER) ||
                getUserRole(requester).getName().equals(UserRole.DEPARTMENT_MEMBER));
    }

    private boolean canEdit(User requester, User infoTarget) {
        if (requester == null) {
            return false;
        }

        return getUserRole(requester).getName().equals(UserRole.ADMINISTRATOR) ||
                requester.getId().equals(infoTarget.getId());
    }
}
