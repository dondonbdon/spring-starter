package dev.bti.starters.util;

public class RegexUtils {

    /**
     * Checks if the given string is alphanumeric.
     */
    public static boolean isAlphanumeric(String str) {
        return str != null && str.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * Checks if the given string is a valid email.
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Checks if the given string is a valid phone number (simple international format).
     */
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^\\+?[1-9]\\d{1,14}$");
    }

    /**
     * Checks if the given string contains only numeric characters.
     */
    public static boolean isNumeric(String str) {
        return str != null && str.matches("^\\d+$");
    }
}
