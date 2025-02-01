package dev.bti.starters.common;

import dev.bti.starters.exceptions.auth.*;


public class Constants {

    public static class Server {
        public static final String URL = "http://localhost:7070/api/v1";
    }

    public static class Auth {
        public static final String _400 = "Password reset code is incorrect.";
        public static final String _401 = "Provided token is invalid.";
        public static final String _401_1 = "Provided password was incorrect.";
        public static final String _403 = "Max password failed attempts.";
        public static final String _403_1 = "Provided email already in use.";
        public static final String _403_2 = "Provided phone number already in use.";
        public static final String _404 = "User not found.";
        public static final String _410 = "Password reset code expired.";

        public static final Integer MAX_ATTEMPTS = 5;
    }

    public static class Throws {

        public static AuthException UserNotFound = new UserNotFoundException();
        public static AuthException PasswordIncorrect = new PasswordIncorrectException();
        public static AuthException MaxFailedAttempts = new MaxFailedAttemptsException();
        public static AuthException ResetCodeExpired = new ResetCodeExpiredException();
        public static AuthException ResetCodeIncorrect = new ResetCodeIncorrectException();
        public static AuthException NotAcceptingResets = new NotAcceptingResetsException();

        public static AuthException EmailAlreadyExists = new CredentialExistsException(Auth._403_1);
        public static AuthException PhoneNumberAlreadyExists = new CredentialExistsException(Auth._403_2);
    }

    public static class Filters {
        public static final int MAX_USER_REQUESTS = 100;
        public static final long WINDOW_SIZE = 60;
        public static final int MAX_GLOBAL_REQUESTS = 1000;
    }
}
