package finalproject.group1.BE.domain.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    LOCKED(true),
    NORMAL(false);

    private boolean userStatus;

    private UserStatus(boolean userStatus) {
        this.userStatus = userStatus;
    }

    public static UserStatus fromValue(boolean value) {
        if(value == true) {
            return UserStatus.LOCKED;
        }
        else {
            return UserStatus.NORMAL;
        }
    }
}
