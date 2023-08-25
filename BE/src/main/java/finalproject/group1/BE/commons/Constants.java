package finalproject.group1.BE.commons;

/**
 * contain project static value
 */
public final class Constants {
    public static final String VALID_DATE_FORMAT = "yyyy-MM-dd";
    public static final String VALID_EMAIL_PATERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String VALID_SKU_PATERN = "^[A-Z][0-9-]{0,51}";
    public static final String VALID_MIMETYPE = "image/jpeg";
    public static final String VALID_IMAGE_FILE_EXTENSION = ".jpg";
    public static final String VALID_IMPORT_FILE_EXTENSION = ".csv";
    public static final String DISPLAY_ID_FORMAT = "B%04d";
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
    public static final String PRODUCT_NOT_AVAILABLE = "%s no longer available";
    public static final String FAVORITE_PRODUCT_UPDATE_SUBJECT = "Favorite product updated";
    public static final String FAVORITE_PRODUCT_UPDATE_CONTENT = "%s has been updated";
    public static final String FAVORITE_PRODUCT_DELETE_SUBJECT = "Favorite product removed";
    public static final String FAVORITE_PRODUCT_DELETE_CONTENT = "%s has been removed";


}
