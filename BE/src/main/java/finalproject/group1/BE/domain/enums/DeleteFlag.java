package finalproject.group1.BE.domain.enums;

import lombok.Getter;

@Getter
public enum DeleteFlag {
    DELETED(true),
    NORMAL(false);

    private boolean isDeleteFlag;

    private DeleteFlag(boolean isDeleteFlag) {
        this.isDeleteFlag = isDeleteFlag;
    }

    public static DeleteFlag getDeleteFlag(boolean isDeleteFlag) {
        if(isDeleteFlag == true) {
            return DeleteFlag.DELETED;
        }
        else {
            return DeleteFlag.NORMAL;
        }
    }
}
