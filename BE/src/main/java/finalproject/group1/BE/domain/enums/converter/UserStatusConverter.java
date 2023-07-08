package finalproject.group1.BE.domain.enums.converter;

import finalproject.group1.BE.domain.enums.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, Boolean> {

    @Override
    public Boolean convertToDatabaseColumn(UserStatus userStatus) {
        return userStatus.isLocked();
    }

    @Override
    public UserStatus convertToEntityAttribute(Boolean isLocked) {
        return UserStatus.getUserStatus(isLocked);
    }
}
