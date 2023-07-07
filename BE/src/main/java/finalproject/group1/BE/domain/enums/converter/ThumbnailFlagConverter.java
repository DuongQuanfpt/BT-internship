package finalproject.group1.BE.domain.enums.converter;

import finalproject.group1.BE.domain.enums.ThumbnailFlag;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ThumbnailFlagConverter implements AttributeConverter<ThumbnailFlag, Boolean> {
    @Override
    public Boolean convertToDatabaseColumn(ThumbnailFlag thumbnailFlag) {
        return thumbnailFlag.isThumbnailFlag();
    }

    @Override
    public ThumbnailFlag convertToEntityAttribute(Boolean isThumbnailFlag) {
        return ThumbnailFlag.getThumbnailFlag(isThumbnailFlag);
    }
}
