package finalproject.group1.BE.domain.enums.converter;

import finalproject.group1.BE.domain.enums.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Boolean> {

    @Override
    public Boolean convertToDatabaseColumn(Role role) {
        return role.isRoleValue();
    }

    @Override
    public Role convertToEntityAttribute(Boolean value) {
        return Role.fromValue(value);
    }
}
