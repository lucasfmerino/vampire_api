package api.rpg.vampire.modules.auth.controller;

import api.rpg.vampire.modules.auth.domain.TokenDto;
import api.rpg.vampire.modules.auth.service.AuthService;
import api.rpg.vampire.modules.auth.service.TokenService;
import api.rpg.vampire.modules.user.domain.dto.UserDisplayDto;
import api.rpg.vampire.modules.user.domain.dto.UserLoginDto;
import api.rpg.vampire.modules.user.domain.dto.UserRegistrationDto;
import api.rpg.vampire.modules.user.domain.model.User;
import api.rpg.vampire.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthService authService;


    /*
     * LOGIN
     *
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDto userLoginDto)
    {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userLoginDto.username(),
                userLoginDto.password()
        );

        try
        {
            Authentication authentication = authenticationManager.authenticate(authToken);
            String token = tokenService.generateToken((User) authentication.getPrincipal());

            return ResponseEntity.ok(new TokenDto(token));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /*
     * REGISTER
     *
     */
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<UserDisplayDto> register(
            @RequestBody @Valid UserRegistrationDto userRegistrationDto, UriComponentsBuilder uriBuilder
            )
    {
        UserDisplayDto newUserDisplayDto = userService.create(userRegistrationDto);

        URI uri = uriBuilder.path("api/users/{id}").buildAndExpand(newUserDisplayDto.id()).toUri();

        return ResponseEntity.created(uri).body(newUserDisplayDto);
    }


    /*
     * START
     *
     */
    @PostMapping("/start")
    @Transactional
    public ResponseEntity<?> start()
    {
        return ResponseEntity.ok(authService.start());
    }

}
