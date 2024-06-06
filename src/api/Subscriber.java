package api;

import java.util.Collections;
import java.util.HashSet;
/**
 * Represents a subscriber in the system, extending the User class.
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 2024-01-10
 */
public class Subscriber extends User
{
    private HashSet<Content> favorites;

    /**
     * Constructs a Subscriber object with specified username, password, name, and last name.
     *
     * @param username the username for the subscriber
     * @param password the password for the subscriber
     * @param name     the name of the subscriber
     * @param lastName the last name of the subscriber
     */
    public Subscriber(String username, String password, String name, String lastName)
    {
        super(username, password, name, lastName);
        favorites=new HashSet<>();
    }

    /**
     * Adds content to the subscriber's favorites.
     *
     * @param content an array of Content objects to add to favorites
     */
    public void addToFavorites(Content...content)
    {
        Collections.addAll(favorites, content);
    }


    /**
     * Adds a collection of content to the subscriber's favorites.
     *
     * @param content a HashSet of Content objects to add to favorites
     */
    public void addToFavorites(HashSet<Content> content)
    {
        favorites.addAll(content);
    }

    /**
     * Removes content from the subscriber's favorites.
     *
     * @param content an array Content objects to remove from favorites
     */
    public void removeFromFavorites(Content... content)
    {
        if(favorites.isEmpty())
        {
            return;
        }
        for (Content content1 : content)
        {
            favorites.remove(content1);
        }
    }

    /**
     * Removes a collection of content from the subscriber's favorites.
     *
     * @param content a HashSet of Content objects to remove from favorites
     */
    public void removeFromFavorites(HashSet<Content> content)
    {
        if(favorites.isEmpty())
        {
            return;
        }
        favorites.removeAll(content);
    }

    /**
     * Clears all content from the subscriber's favorites.
     */
    public void deleteFavorites()
    {
        favorites.clear();
    }

    public HashSet<Content> getFavorites()
    {
        return favorites;
    }


    /**
     * Overrides the equals method to compare if two objects are equal.
     *
     * @param obj the object to compare
     * @return true if the object is a Subscriber and equals the current object; otherwise, false
     */
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Subscriber))
        {
            return false;
        }
        return super.equals(obj);
    }

}
