package finalproject.group1.BE.commons;

import lombok.NoArgsConstructor;

/**
 * contain project static value
 */
@NoArgsConstructor
public final class Constants {
    public static final String IMAGE_FOLDER_PATH = "src/main/resources/img/";
    public static final String VALID_DATE_FORMAT = "yyyy-MM-dd";
    public static final String VALID_IMAGE_FILE_EXTENSION = ".jpg";
    public static final String THUMBNAIL_IMAGE_PREFIX = "_thumbnail";
    public static final String DETAIL_IMAGE_PREFIX = "_detail_";
    public static final String ORDER_EMAIL_SUBJECT = "Order placed";
    public static final String ORDER_EMAIL_CONTENT = "Order %s has been placed";
    public static final String USER_LOCK_EMAIL_SUBJECT = "Account lock";
    public static final String USER_LOCK_EMAIL_CONTENT = "Account %s have been locked";
    public static final String COMMON_TOKEN_EXCEPTION_RESPONSE = "invalid token";
    public static final String REQUEST_PASSWORD_EMAIL_SUBJECT = "Reset password";
    public static final String REQUEST_PASSWORD_EMAIL_CONTENT = "<a href=\"url/%s\">reset password link</a>";
    public static final String RESET_PASSWORD_EMAIL_SUBJECT = "Reset password";
    public static final String RESET_PASSWORD_EMAIL_CONTENT = "New password : %s";
    public static final String DELETE_USER_EMAIL_SUBJECT = "Delete user";
    public static final String DELETE_USER_EMAIL_CONTENT = "Account %s have been deleted";
}
