package api.rpg.vampire.modules.auth.service;

import api.rpg.vampire.modules.role.Role;
import api.rpg.vampire.modules.user.domain.model.User;
import api.rpg.vampire.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService
{
    @Value("${api.security.role.password}")
    private String rolePassword;

    @Value("${api.security.role.username}")
    private String roleUsername;

    @Autowired
    private UserRepository userRepository;


    /*
     * LOAD USER
     *
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return userRepository.findByUsername(username);
    }


    /*
     * START
     *
     */
    public String start()
    {
        Optional<User> superAdmin = userRepository.findUserByUsername(roleUsername);
        superAdmin.ifPresent(user -> userRepository.delete(user));

        createSuperAdmin();

        return "Status: ON";
    }


    /*
     * CREATE SUPER ADMIN
     *
     */
    private void createSuperAdmin()
    {
        String encryptedPassword = new BCryptPasswordEncoder().encode(rolePassword);
        User superAdmin = new User();

        superAdmin.setUsername(roleUsername);
        superAdmin.setEmail("superadmin@vampire.com");
        superAdmin.setPassword(encryptedPassword);
        superAdmin.setName(roleUsername);
        superAdmin.setDiscord(roleUsername);
        superAdmin.setPhone("9999999999");
        superAdmin.setIsActive(true);
        superAdmin.setRoles(List.of(Role.WEB147, Role.WEB136, Role.WEB101));

        userRepository.save(superAdmin);
    }

}
