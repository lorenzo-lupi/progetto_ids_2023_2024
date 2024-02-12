package it.cs.unicam.app_valorizzazione_territorio.model.utils;



//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

/**
 * This class provides utility methods for passwords.
 */
public class CredentialsUtils {

    private static final int leftLimit = 48; //'0'
    private static final int rightLimit = 122; //'z'
    private static final int targetStringLength = 10;

    //private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private static final Random random = new Random();


    /**
     * This method generates a random valid password made of alphanumeric characters of length 10.
     *
     * @return a random password
     */
    public static String getRandomPassword() {
        String password;
        do {
            password = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } while (!isPasswordValid(password));
        return password;
    }


    /**
     * This method encrypts a password using BCryptPasswordEncoder.
     *
     * @param password the password to encrypt
     * @return the encrypted password
     */
    /*
    public static String getEncryptedPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
    */

    /**
     * This method checks if a raw password matches a crypted password.
     *
     * @param rawPassword the raw password
     * @param cryptedPassword the crypted password
     * @return true if the raw password matches the crypted password, false otherwise
     */
    /*
    public static boolean matchesPassword(String rawPassword, String cryptedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, cryptedPassword);
    }
    */

    /**
     * This method checks if an email is valid.
     * An email is valid if it matches the regular expression "^[\w\-.]+@([\w-]+\.)+[\w-]{2,}$",
     *
     *
     * @param email the email to check
     * @return true if the email is valid, false otherwise
     */
    public static boolean isEmailValid(String email) {
        return email.matches("^[\\w\\-.]+@([\\w-]+\\.)+[\\w-]{2,}$");
    }

    /**
     * This method checks if a password is valid. A password is valid if it has at least 8 characters,
     * at least one uppercase letter, at least one lowercase letter and at least one digit.
     *
     * @param password the password to check
     * @return true if the password is valid, false otherwise
     */
    public static boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*[0-9].*");
    }
}
