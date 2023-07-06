package finalproject.group1.BE.domain.enums.converter;

import finalproject.group1.BE.domain.enums.DeleteFlag;
import finalproject.group1.BE.domain.enums.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DeleteFlagConverter implements AttributeConverter<DeleteFlag, Boolean> {
    @Override
    public Boolean convertToDatabaseColumn(DeleteFlag deleteFlag) {
        return deleteFlag.isValue();
    }

    @Override
    public DeleteFlag convertToEntityAttribute(Boolean value) {
        return DeleteFlag.fromValue(value);
    }
}
