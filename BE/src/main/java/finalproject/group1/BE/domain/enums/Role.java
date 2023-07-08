package finalproject.group1.BE.domain.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Role {
    USER(false),
    ADMIN(true);

    private boolean isAdmin;

    private Role(boolean roleValue) {
        this.isAdmin = roleValue;
    }

    public static Role getUserRole(boolean roleValue) {
        if(roleValue == true) {
            return Role.ADMIN;
        }
        else {
            return Role.USER;
        }
    }
}
