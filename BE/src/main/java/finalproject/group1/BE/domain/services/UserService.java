package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.commons.Constants;
import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.Role;
import finalproject.group1.BE.domain.enums.UserStatus;
import finalproject.group1.BE.domain.repository.UserRepository;
import finalproject.group1.BE.web.dto.request.user.UserListRequest;
import finalproject.group1.BE.web.dto.request.user.UserLoginRequest;
import finalproject.group1.BE.web.dto.request.user.UserRegisterRequest;
import finalproject.group1.BE.web.dto.response.user.UserDetailResponse;
import finalproject.group1.BE.web.dto.response.user.UserListResponse;
import finalproject.group1.BE.web.dto.response.user.UserLoginResponse;
import finalproject.group1.BE.web.exception.ExistException;
import finalproject.group1.BE.web.exception.NotFoundException;
import finalproject.group1.BE.web.security.JwtHelper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        Optional<User> existUser = userRepository.findByEmail(registerRequest.getLoginId());
        if (existUser.isPresent()) {
            if (existUser.get().getStatus() == UserStatus.LOCKED) {
                throw new ExistException("account is locked");
            }
            throw new ExistException("account already exist");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VALID_DATE_FORMAT);

        User newUser = new User();

        newUser.setUserName(registerRequest.getUserName());
        newUser.setEmail(registerRequest.getLoginId());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setBirthDay(LocalDate.parse(registerRequest.getBirthDay(), formatter));
        newUser.setDeleteFlag(DeleteFlag.NORMAL);
        newUser.setStatus(UserStatus.NORMAL);
        newUser.setRole(Role.ROLE_USER);

        userRepository.save(newUser);
    }

    public UserLoginResponse authenticate(UserLoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getLoginId(), loginRequest.getPassword()));

        User user = (User) authentication.getPrincipal();
        user = userRepository.findByEmail(user.getEmail()).get();

        String token = jwtHelper.createToken(user);
        return new UserLoginResponse(token);
    }

    public List<UserListResponse> getUserList(UserListRequest listRequest, Pageable pageable) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VALID_DATE_FORMAT);

        String username = null;
        String email = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        Float totalPrice = null;

        if (listRequest.getUserName() != null && !listRequest.getUserName().isEmpty()) {
            username = listRequest.getUserName();
        }

        if (listRequest.getLoginId() != null && !listRequest.getLoginId().isEmpty()) {
            email = listRequest.getLoginId();
        }

        if (listRequest.getStartBirthDay() != null) {
            try {
                startDate = LocalDate.parse(listRequest.getStartBirthDay(), formatter);
            } catch (DateTimeParseException e) {
                //do nothing
            }
        }

        if(listRequest.getEndBirthDay() != null) {
            try {
                endDate = LocalDate.parse(listRequest.getEndBirthDay(), formatter);
            } catch (DateTimeParseException e) {
                //do nothing
            }
        }

        if (listRequest.getTotalPrice() != null) {
            totalPrice = listRequest.getTotalPrice();
        }

        return userRepository.findUserBySearchConditions(username, email,
                startDate, endDate, totalPrice, pageable);
    }

    public UserDetailResponse getUserDetails(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found"));
        UserDetailResponse detailResponse = modelMapper.map(user, UserDetailResponse.class);
        detailResponse.setLoginId(user.getEmail());

        return detailResponse;
    }
}
