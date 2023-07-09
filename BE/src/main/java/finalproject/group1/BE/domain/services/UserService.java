package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.Role;
import finalproject.group1.BE.domain.enums.UserStatus;
import finalproject.group1.BE.domain.repository.UserRepository;
import finalproject.group1.BE.web.dto.request.UserLoginRequest;
import finalproject.group1.BE.web.dto.request.UserRegisterRequest;
import finalproject.group1.BE.web.dto.response.UserLoginResponse;
import finalproject.group1.BE.web.dto.response.UserResponse;
import finalproject.group1.BE.web.exception.UserExistException;
import finalproject.group1.BE.web.exception.UserLockException;
import finalproject.group1.BE.web.security.JwtHelper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtHelper jwtHelper;

    @Value("${validDateFormat}")
    private String dateFormat;

    public void saveUser(UserRegisterRequest registerRequest){
        Optional<User> existUser = userRepository.findByEmail(registerRequest.getEmail());
        if(existUser.isPresent()){
            if(existUser.get().getStatus() == UserStatus.LOCKED){
                throw new UserExistException();
            }
            throw new UserExistException();
        }
        User newUser = modelMapper.map(registerRequest, User.class);

        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        newUser.setBirthday( LocalDate.parse(registerRequest.getBirthDay(),formatter));

        newUser.setDeleteFlag(DeleteFlag.NORMAL);
        newUser.setStatus(UserStatus.NORMAL);
        newUser.setRole(Role.ROLE_USER);

        userRepository.save(newUser);
    }

    public UserLoginResponse authenticate(UserLoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));

        User user = (User) authentication.getPrincipal();
        user = userRepository.findByEmail(user.getEmail()).get();

        System.out.println(user.getAuthorities());
        String token = jwtHelper.createToken(user);
        return new UserLoginResponse(token);
    }
}
