package dev.bti.starters.util;

import java.security.SecureRandom;

public class GeneratorUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int RESET_CODE_MIN = 100000;
    private static final int RESET_CODE_MAX = 999999;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Generates a random alphanumeric ID of fixed length.
     */
    public static String generateRandomId() {

        final int LENGTH = 16;

        StringBuilder uid = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = SECURE_RANDOM.nextInt(CHARACTERS.length());
            uid.append(CHARACTERS.charAt(index));
        }
        return uid.toString();
    }

    /**
     * Generates a random 6-digit reset code.
     */
    public static String generateResetCode() {
        int resetCode = SECURE_RANDOM.nextInt(RESET_CODE_MAX - RESET_CODE_MIN + 1) + RESET_CODE_MIN;
        return String.valueOf(resetCode);
    }
}

