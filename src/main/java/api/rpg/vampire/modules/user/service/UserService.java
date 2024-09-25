package api.rpg.vampire.modules.user.service;

import api.rpg.vampire.modules.user.domain.dto.UserDisplayDto;
import api.rpg.vampire.modules.user.domain.dto.UserPasswordUpdateDto;
import api.rpg.vampire.modules.user.domain.dto.UserRegistrationDto;
import api.rpg.vampire.modules.user.domain.dto.UserUpdateDto;
import api.rpg.vampire.modules.user.domain.model.User;
import api.rpg.vampire.modules.user.exception.UserNotFoundException;
import api.rpg.vampire.modules.user.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService
{
    @Autowired
    private UserRepository userRepository;


    /*
     *  CREATE USER
     *
     */
    public UserDisplayDto create(UserRegistrationDto userRegistrationDto)
    {
        String encryptedPassword = new BCryptPasswordEncoder().encode(userRegistrationDto.password());
        User newUser = new User();

        BeanUtils.copyProperties(userRegistrationDto, newUser);
        newUser.setPassword(encryptedPassword);
        newUser.setIsActive(true);

        return new UserDisplayDto(userRepository.save(newUser));
    }


    /*
     *  GET USER BY ID
     *
     */
    public UserDisplayDto getById(UUID id)
    {
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent())
        {
            return new UserDisplayDto(userOptional.get());
        }
        else
        {
            throw new UserNotFoundException("User not found!");
        }
    }


    /*
     *  GET USER BY USERNAME
     *
     */
    public UserDisplayDto getByUsername(String username)
    {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if(userOptional.isPresent())
        {
            return new UserDisplayDto(userOptional.get());
        }
        else
        {
            throw new UserNotFoundException("User not found!");
        }
    }


    /*
     *  GET ALL USERS
     *
     */
    public Page<UserDisplayDto> getAll(Pageable pageable)
    {
        return userRepository.findAll(pageable).map(UserDisplayDto::new);
    }


    /*
     *  DELETE USER BY ID
     *
     */
    public void delete(UUID id)
    {
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent())
        {
            userRepository.delete(userOptional.get());
        }
        else
        {
            throw new UserNotFoundException("User not found!");
        }
    }


    /*
     *  UPDATE USER
     *
     */
    public UserDisplayDto update(UserUpdateDto userUpdateDto)
    {
        Optional<User> userOptional = userRepository.findById(userUpdateDto.id());

        if(userOptional.isPresent())
        {
            return generalUpdates(userUpdateDto, userOptional.get());
        }
        else
        {
            throw new UserNotFoundException("User not found");
        }
    }


    /*
     * PASSWORD UPDATE
     *
     */
    public UserDisplayDto updatePassword(UserPasswordUpdateDto userPasswordUpdateDto)
    {
        Optional<User> userOptional = userRepository.findById(userPasswordUpdateDto.id());

        String encryptedOldPassword = new BCryptPasswordEncoder().encode(userPasswordUpdateDto.oldPassword());

        if(userOptional.isPresent())
        {
            User updatedUser = new User();
            BeanUtils.copyProperties(userOptional.get(), updatedUser);

            if(Objects.equals(encryptedOldPassword, updatedUser.getPassword()) &&
                    userPasswordUpdateDto.newPassword() != null)
            {
                String encryptedNewPassword = new BCryptPasswordEncoder().encode(userPasswordUpdateDto.newPassword());
                updatedUser.setPassword(encryptedNewPassword);
            }

            return new UserDisplayDto(userRepository.save(updatedUser));
        }
        else
        {
            throw new UserNotFoundException("User not found");
        }
    }


    /*
     *  GENERAL USER UPDATES
     *
     */
    private UserDisplayDto generalUpdates(UserUpdateDto userUpdateDto, User user)
    {
        User updatedUser = new User();
        BeanUtils.copyProperties(user, updatedUser);
        if(userUpdateDto.username() != null) {updatedUser.setUsername(userUpdateDto.username());}
        if(userUpdateDto.name() != null) {updatedUser.setName(userUpdateDto.name());}
        if(userUpdateDto.discord() != null) {updatedUser.setDiscord(userUpdateDto.discord());}
        if(userUpdateDto.phone() != null) {updatedUser.setPhone(userUpdateDto.phone());}
        if(userUpdateDto.isActive() != null) {updatedUser.setIsActive(userUpdateDto.isActive());}
        if(userUpdateDto.roles() != null && !userUpdateDto.roles().isEmpty())
        {
            updatedUser.setUsername(userUpdateDto.username());
        }

        return new UserDisplayDto(userRepository.save(updatedUser));
    }

}
