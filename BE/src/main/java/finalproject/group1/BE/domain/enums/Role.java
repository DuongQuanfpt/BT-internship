package finalproject.group1.BE.domain.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Role {
    USER(false),
    ADMIN(true);

    public boolean isRoleValue() {
        return roleValue;
    }

    private boolean roleValue;

    private Role(boolean roleValue) {
        this.roleValue = roleValue;
    }

    public static Role fromValue(boolean value) {
        if(value == true) {
            return Role.ADMIN;
        }
        else {
            return Role.USER;
        }
    }
}
