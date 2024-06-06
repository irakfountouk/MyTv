package api;

/**
 * Represents an administrative user extending the functionality of a User.
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 2024-01-10
 */
public class Admin extends User
{
    /**
     * Constructs an Admin object with the provided username, password, name, and last name.
     *
     * @param username   the username for the admin
     * @param password   the password for the admin
     * @param name       the name of the admin
     * @param lastName   the last name of the admin
     */
    public Admin(String username,String password,String name,String lastName)
    {
        super(username,password,name,lastName);
    }

    /**
     * Compares if two objects are equal, specifically comparing if the object is an instance of Admin
     * and checking equality using the User superclass's equals method.
     *
     * @param obj the object to compare
     * @return true if the object is an instance of Admin and equals the current Admin object; otherwise, false
     */
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Admin))
        {
            return false;
        }

        return super.equals(obj);
    }

}
