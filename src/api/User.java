package api;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a user in the system and provides functionality related to user management.
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 2024-01-10
 */
public class User implements Serializable
{
    private String username,password,name,surname,salt;

    /**
     * Constructs a User object with specified username, password, name, and surname.
     *
     * @param username the username for the user
     * @param password the password for the user
     * @param name     the name of the user
     * @param surname  the surname of the user
     */
    public User(String username,String password,String name,String surname)
    {
        this.username=username.trim();
        salt=generateSalt();
        try
        {
            this.password=hashPassword(password.trim(),salt);
        }
        catch (RuntimeException e)
        {
            System.out.println("Runtime Exception: " + e.getMessage());
        }

        this.name=name.trim();
        this.surname=surname.trim();
    }

    /**
     * Sets a new username for the user, updating the authentication map.
     *
     * @param username      the new username for the user
     * @param authentication the authentication map containing users
     */
    public void setUsername(String username,HashMap<String,User> authentication)
    {
        authentication.remove(this.username,this);
        this.username=username.trim();
        authentication.put(this.username,this);
    }


    /**
     * Sets a new password for the user, generating a new salt and hashing the password.
     *
     * @param password the new password for the user
     */
    public void setPassword(String password)
    {
        if (!password.trim().isEmpty())
        {
            salt = generateSalt();
            try
            {
                this.password=hashPassword(password.trim(),salt);
            }
            catch (RuntimeException e)
            {
                System.out.println("Runtime Exception: " + e.getMessage());
            }
        }
    }

    /**
     * Sets a new name for the user.
     *
     * @param name the new name for the user
     */
    public void setName(String name)
    {
        this.name=name.trim();
    }


    /**
     * Sets a new surname for the user.
     *
     * @param surname the new surname for the user
     */
    public void setSurname(String surname)
    {
        this.surname = surname.trim();
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Gets the hashed password of the user.
     *
     * @return the hashed password of the user
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Gets the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the surname of the user.
     *
     * @return The surname of the user.
     */
    public String getSurname()
    {
        return surname;
    }


    /**
     * Generates a random salt for password hashing.
     *
     * @return A Base64-encoded string representing the generated salt.
     */
    String generateSalt()
    {
        SecureRandom secureRandom = new SecureRandom();
        byte[] saltBytes = new byte[16];
        secureRandom.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    /**
     * Hashes the given password with the provided salt using SHA-256.
     *
     * @param password The password to be hashed.
     * @param salt The salt used for hashing.
     * @return A Base64-encoded string representing the hashed password.
     * @throws RuntimeException If an error occurs during the hashing process.
     */
    String hashPassword(String password, String salt) throws RuntimeException
    {
        String saltedPassword = password + salt;
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(saltedPassword.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Validates the entered password during login by comparing its hash
     * with the stored hashed password.
     *
     * @param enteredPassword The password entered during login/editing of profile.
     * @return True if the entered password is valid, false otherwise.
     */
    public boolean validatePassword(String enteredPassword)
    {
        String enteredPasswordHash = hashPassword(enteredPassword,salt);
        return enteredPasswordHash.equals(password);
    }

    /**
     * Edits the user's information, including the username, password, name, and surname.
     *
     * @param username The new username.
     * @param password The new password.
     * @param name     The new name.
     * @param surname  The new surname.
     * @param authentication The authentication map to update after editing the user.
     */
    public void edit(String username, String password, String name, String surname, HashMap<String,User> authentication)
    {
        authentication.remove(this.username,this);

        this.username=username.trim();
        if (!password.trim().isEmpty())
        {
            this.salt = generateSalt();
            try
            {
                this.password=hashPassword(password.trim(),salt);
            }
            catch (RuntimeException e)
            {
                System.out.println("Runtime Exception: " + e.getMessage());
            }
        }
        this.name=name.trim();
        this.surname=surname.trim();
        authentication.put(this.username,this);
    }

    /**
     * Generates a string containing non-sensitive information about the user (name, surname, username).
     *
     * @return A string with non-sensitive user information.
     */
    public String nonSensitiveInfoToString()
    {
        return "Name: " + name + "\n" + "Surname: " + surname + "\n" +
                "Username: " + username + "\n";
    }

    /**
     * Generates a string containing all information about the user (including password).
     *
     * @return A string with all user information.
     */
    public String allInfoToString()
    {
        return nonSensitiveInfoToString() + password + "\n";
    }

    /**
     * Checks if two user objects are equal based on their username, password, name, and surname.
     *
     * @param obj The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof User user)) {
            return false;
        }

        if(this==obj) {
            return true;
        }

        //due to hashing and salting, the randomly generated password isn't checked
        return username.equals(user.getUsername()) && name.equals(user.getName()) && surname.equals(user.getSurname());
    }

    /**
     * Generates a hash code for the user based on their username and password.
     *
     * @return The hash code for the user.
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(username,name,surname);
    }
}
