package finalproject.group1.BE.domain.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    LOCKED(true),
    NORMAL(false);

    private boolean isLocked;

    private UserStatus(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public static UserStatus getUserStatus(boolean isLocked) {
        if(isLocked == true) {
            return UserStatus.LOCKED;
        }
        else {
            return UserStatus.NORMAL;
        }
    }
}
