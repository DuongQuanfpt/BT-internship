package finalproject.group1.BE.domain.enums;

import lombok.Getter;

@Getter
public enum DeleteFlag {
    DELETED(true),
    NORMAL(false);

    private boolean value;

    private DeleteFlag(boolean value) {
        this.value = value;
    }

    public static DeleteFlag fromValue(boolean value) {
        if(value == true) {
            return DeleteFlag.DELETED;
        }
        else {
            return DeleteFlag.NORMAL;
        }
    }
}
