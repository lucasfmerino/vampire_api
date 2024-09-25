package api.rpg.vampire.modules.user.repository;

import api.rpg.vampire.modules.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>
{
    /*
     *  FIND USER BY USERNAME
     */
    UserDetails findByUsername(String email);


    /*
     *  FIND USER BY USERNAME (OPTIONAL)
     */
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findUserByUsername(@Param("username") String username);
}