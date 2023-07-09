package finalproject.group1.BE.domain.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Role {
    ROLE_USER(false),
    ROLE_ADMIN(true);

    private boolean isAdmin;

    private Role(boolean roleValue) {
        this.isAdmin = roleValue;
    }

    public static Role getUserRole(boolean roleValue) {
        if(roleValue) {
            return Role.ROLE_ADMIN;
        }
        else {
            return Role.ROLE_USER;
        }
    }
}
