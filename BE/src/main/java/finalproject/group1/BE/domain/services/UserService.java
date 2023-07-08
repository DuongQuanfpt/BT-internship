package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.Role;
import finalproject.group1.BE.domain.enums.UserStatus;
import finalproject.group1.BE.domain.repository.UserRepository;
import finalproject.group1.BE.web.dto.request.UserRegisterRequest;
import finalproject.group1.BE.web.dto.response.UserResponse;
import finalproject.group1.BE.web.exception.UserExistException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

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
        newUser.setRole(Role.USER);

        userRepository.save(newUser);
    }
}
