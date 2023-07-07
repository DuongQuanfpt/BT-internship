package finalproject.group1.BE.domain.enums;

import lombok.Getter;

@Getter
public enum ThumbnailFlag {
    YES(true),
    NO(false);

    private boolean isThumbnailFlag;

    private ThumbnailFlag(boolean isThumbnailFlag) {
        this.isThumbnailFlag = isThumbnailFlag;
    }

    public static ThumbnailFlag getThumbnailFlag(boolean isThumbnailFlag) {
        if(isThumbnailFlag == true) {
            return ThumbnailFlag.YES;
        }
        else {
            return ThumbnailFlag.NO;
        }
    }

}
