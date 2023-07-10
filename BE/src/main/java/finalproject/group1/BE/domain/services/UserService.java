package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.constant.Constants;
import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.Role;
import finalproject.group1.BE.domain.enums.UserStatus;
import finalproject.group1.BE.domain.repository.UserRepository;
import finalproject.group1.BE.web.dto.request.UserListRequest;
import finalproject.group1.BE.web.dto.request.UserLoginRequest;
import finalproject.group1.BE.web.dto.request.UserRegisterRequest;
import finalproject.group1.BE.web.dto.response.UserListResponse;
import finalproject.group1.BE.web.dto.response.UserLoginResponse;
import finalproject.group1.BE.web.exception.ExistException;
import finalproject.group1.BE.web.security.JwtHelper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtHelper jwtHelper;

    public void saveUser(UserRegisterRequest registerRequest) {
        Optional<User> existUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existUser.isPresent()) {
            if (existUser.get().getStatus() == UserStatus.LOCKED) {
                throw new ExistException();
            }
            throw new ExistException();
        }
        User newUser = modelMapper.map(registerRequest, User.class);

        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VALID_DATE_FORMAT);
        newUser.setBirthday(LocalDate.parse(registerRequest.getBirthDay(), formatter));

        newUser.setDeleteFlag(DeleteFlag.NORMAL);
        newUser.setStatus(UserStatus.NORMAL);
        newUser.setRole(Role.ROLE_USER);

        userRepository.save(newUser);
    }

    public UserLoginResponse authenticate(UserLoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));

        User user = (User) authentication.getPrincipal();
        user = userRepository.findByEmail(user.getEmail()).get();

        System.out.println(user.getDeleteFlag());
        System.out.println(user.getStatus());
        System.out.println(user.getRole());
        String token = jwtHelper.createToken(user);
        return new UserLoginResponse(token);
    }

    public List<UserListResponse> getUserList(UserListRequest listRequest,Pageable pageable) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VALID_DATE_FORMAT);

        String username = null;
        String email = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        Float totalPrice = null;

        if (listRequest.getUsername() != null && !listRequest.getUsername().isEmpty()) {
            username = listRequest.getUsername();
        }

        if (listRequest.getEmail() != null && !listRequest.getEmail().isEmpty()) {
            email = listRequest.getEmail();
        }

        if (listRequest.getStartDate() != null && !listRequest.getStartDate().isEmpty()) {
            startDate = LocalDate.parse(listRequest.getStartDate(), formatter);
        }

        if (listRequest.getEndDate() != null && !listRequest.getEndDate().isEmpty()) {
            endDate = LocalDate.parse(listRequest.getEndDate(), formatter);
        }

        if (listRequest.getTotalPrice() != null) {
            totalPrice = listRequest.getTotalPrice();
        }

        return userRepository.findUserBySearchConditions(username, email,
                startDate, endDate, totalPrice, pageable);
    }
}
