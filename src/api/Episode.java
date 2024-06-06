package api;

import java.io.Serializable;
import java.util.Objects;


/**
 * The {@code Episode} class represents an episode within a series. Each episode has a duration
 * (in minutes).
 * This class is designed to store information about an episode, including its duration.
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 24-01-10
 */
public class Episode implements Serializable
{
    private int duration;

    /**
     * Constructs a new Episode object with the specified duration.
     *
     * @param duration The duration of the episode (in minutes).
     */
    public Episode(int duration)
    {
        try
        {
            setDuration(duration);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }
    }

    /**
     * Sets the duration of the episode.
     *
     * @param duration The duration of the episode (in minutes).
     */
    public void setDuration(int duration) throws IllegalArgumentException
    {
        if(durationIsValid(duration))
        {
            this.duration=duration;
        }
        else
        {
            throw new IllegalArgumentException("Invalid argument passed as episode duration. Duration must be a positive integer.");
        }
    }

    /**
     * Checks if the provided duration is valid.
     *
     * @param duration The duration to validate.
     * @return {@code true} if the duration is valid, {@code false} otherwise.
     */
    public static boolean durationIsValid(int duration)
    {
        return durationIsValid(String.valueOf(duration));
    }


    /**
     * Checks if the provided duration is valid.
     * The string is valid if it represents a positive integer
     * @param duration The duration to validate.
     * @return {@code true} if the duration is valid, {@code false} otherwise.
     */
    public static boolean durationIsValid(String duration)
    {
        String validForm="\\d+";

        if(duration.replaceAll("\\s", "").matches(validForm))
        {
            return Integer.parseInt(duration.replaceAll("\\s", "")) > 0;
        }

        return false;
    }

    /**
     * Returns the duration of the episode.
     *
     * @return The duration of the episode (in minutes).
     */
    public int getDuration()
    {
        return duration;
    }

    /**
     * Edits the episode by updating its duration.
     *
     * @param duration The new duration of the episode (in minutes).
     */
    public void edit(int duration)
    {
        try
        {
            setDuration(duration);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }
    }

    /**
     * Returns a string representation of this Episode object.
     *
     * @return A string representation including the duration of the episode.
     */
    public String toString()
    {
        return "Duration: " + duration;
    }

    /**
     * Overrides the equals method to compare Episode objects based on their duration.
     *
     * @param obj The object to compare with this Episode.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if(! (obj instanceof Episode episode))
        {
            return false;
        }

        if(this==obj)
        {
            return true;
        }

        return duration==episode.getDuration();
    }

    /**
     * Generates a hash code for this Episode object based on its duration.
     *
     * @return The hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(duration);
    }
}
