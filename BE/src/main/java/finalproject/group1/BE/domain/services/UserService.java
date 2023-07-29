package finalproject.group1.BE.domain.services;

import finalproject.group1.BE.commons.Constants;
import finalproject.group1.BE.commons.EmailCommons;
import finalproject.group1.BE.domain.entities.ChangedPasswordToken;
import finalproject.group1.BE.domain.entities.User;
import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.Role;
import finalproject.group1.BE.domain.enums.UserStatus;
import finalproject.group1.BE.domain.repository.ChangedPasswordTokenRepository;
import finalproject.group1.BE.domain.repository.UserRepository;
import finalproject.group1.BE.web.dto.request.user.*;
import finalproject.group1.BE.web.dto.response.user.UserDetailResponse;
import finalproject.group1.BE.web.dto.response.user.UserListResponse;
import finalproject.group1.BE.web.dto.response.user.UserLoginResponse;
import finalproject.group1.BE.web.exception.ExistException;
import finalproject.group1.BE.web.exception.NotFoundException;
import finalproject.group1.BE.web.exception.UserLockException;
import finalproject.group1.BE.web.security.JwtHelper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private ChangedPasswordTokenRepository tokenRepository;
    private EmailCommons emailCommons;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtHelper jwtHelper;

    @Transactional
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

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword()));

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

        if (listRequest.getEndBirthDay() != null) {
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

    /**
     * set user status to locked
     *
     * @param id - id of the user
     */
    @Transactional
    public void lockUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && user.getStatus() != UserStatus.LOCKED) {// if user exist
            user.setStatus(UserStatus.LOCKED);
            userRepository.save(user);

            String emailContent = String.format(Constants.USER_LOCK_EMAIL_CONTENT, user.getEmail());
            emailCommons.sendSimpleMessage(user.getEmail(), Constants.USER_LOCK_EMAIL_SUBJECT, emailContent);
        }
    }

    /**
     * update user information
     *
     * @param updateRequest
     * @throws LockedException if user is locked
     */
    public void update(UserUpdateRequest updateRequest) {
        User user = userRepository.findByEmail(updateRequest.getLoginId()).orElse(null);

        if (user != null) {//if user exist
            //if user is locked, throw exception
            if (user.getStatus() == UserStatus.LOCKED) {
                throw new UserLockException();
            }

            //update user information
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.VALID_DATE_FORMAT);
            LocalDate newBirthDay = LocalDate.parse(updateRequest.getBirthDay(), formatter);
            String encodedNewPassword = passwordEncoder.encode(updateRequest.getPassword());

            user.setUserName(updateRequest.getUser_name());
            user.setPassword(encodedNewPassword);
            user.setBirthDay(newBirthDay);

            //save changes to db
            userRepository.save(user);
        }
    }

    /**
     * create a change password token ,
     * sent the reset password link to user email
     *
     * @param email - email of the user
     */
    @Transactional
    public void requestPassword(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        //if email dont match with any user
        if (user == null) {
            return;
        }
        //create new token
        ChangedPasswordToken token = new ChangedPasswordToken();
        token.setOwner(user);
        token.setToken(RandomString.make(20));
        token.setOwner(user);
        token.setExpireDate(LocalDateTime.now().now().plus(Duration.ofMinutes(30)));

        user.setChangedPasswordTokens(token);
        //save to DB
        ChangedPasswordToken savedToken = userRepository.save(user).getChangedPasswordTokens();

        //sent reset password email to user
        String emailContent = String.format(Constants.REQUEST_PASSWORD_EMAIL_CONTENT, savedToken.getToken());
        emailCommons.sendMimeMessage(user.getEmail(), Constants.REQUEST_PASSWORD_EMAIL_SUBJECT, emailContent);
    }

    /**
     * generate a random password for
     * user with change password token
     *
     * @param token - change password token
     */
    @Transactional
    public void resetPassword(String token) {
        ChangedPasswordToken passwordToken = tokenRepository.findByTokenAndNotExpired(
                token, LocalDateTime.now()).orElseThrow(() -> new NotFoundException("Token ko hop le"));

        User user = passwordToken.getOwner();
        //generate a random password for user
        Random rnd = new Random();
        StringBuilder newPassword = new StringBuilder(RandomString.make(10));
        newPassword.append((char) ('A' + rnd.nextInt(26)));//password must contain a uppercase char
        newPassword.append(rnd.nextInt(10));//password must contain a number

        user.setPassword(passwordEncoder.encode(newPassword));

        //delete change password token
        user.setChangedPasswordTokens(null);

        ///save changes to DB
        User savedUser = userRepository.save(user);
        //sent new password to user email
        String emailContent = String.format(Constants.RESET_PASSWORD_EMAIL_CONTENT, newPassword);
        emailCommons.sendSimpleMessage(user.getEmail(), Constants.RESET_PASSWORD_EMAIL_SUBJECT, emailContent);

    }

    /**
     * change password of user
     */
    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest, Authentication authentication) {
        User loginUser = null;
        if (authentication != null) {  //check if there are user login
            loginUser = (User) authentication.getPrincipal();
        }

        // Verify the old password
        String oldPassword = changePasswordRequest.getOldPassword();
        if (!passwordEncoder.matches(oldPassword, loginUser.getPassword())) {
            // Old password does not match the one in the database, throw an exception or handle the error
            throw new NotFoundException("Old password is incorrect");
        }
        else {
            // Change the password to the new one
            String newPassword = changePasswordRequest.getPassword();
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            loginUser.setPassword(encodedNewPassword);

            // Save the updated user with the new password
            userRepository.save(loginUser);
        }
    }

    /**
     * set user delete flag to true
     *
     * @param id - user id
     */
    @Transactional
    public void deleteUserByAdmin(int id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found"));
        //update user information
        user.setOldLoginId(user.getEmail());
        user.setEmail(null);
        user.setDeleteFlag(DeleteFlag.DELETED);

        //save to Db
        userRepository.save(user);
    }

    /**
     * delete user
     * set user delete flag to true
     */
    @Transactional
    public void deleteUser(Authentication authentication) {
        User loginUser = null;
        if (authentication != null) {  //check if there are user login
            loginUser = (User) authentication.getPrincipal();
        }

        //update user information
        loginUser.setOldLoginId(loginUser.getEmail());
        loginUser.setEmail(null);
        loginUser.setDeleteFlag(DeleteFlag.DELETED);

        //save to Db
        userRepository.save(loginUser);

        //sent reset password email to user
        String emailContent = String.format(Constants.DELETE_USER_EMAIL_CONTENT, loginUser.getUserName());
        emailCommons.sendSimpleMessage(loginUser.getOldLoginId(), Constants.DELETE_USER_EMAIL_SUBJECT, emailContent);
    }
}
