package api;

import java.util.HashSet;
import java.util.Objects;


/**
 * The {@code Movie} class represents a movie, a specific type of viewable content.
 * It extends the {@link ViewableContent} class and includes additional attributes such as
 * duration and year of release. This class is designed to store information about movies
 * that users can view, rate, and provide related content.
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 2024-01-08
 */

public class Movie extends ViewableContent
{
    private int duration,yearOfRelease;

    /**
     * Constructs a new Movie object with the specified title, synopsis, suitability for minors, genre, actors,
     * year of release, duration, and related content.
     *
     * @param title                The title of the movie.
     * @param synopsis             A brief summary or description of the movie.
     * @param isSuitableForMinors Indicates whether the movie is suitable for minors ("Yes" or "No").
     * @param genre                The genre of the movie.
     * @param actors               The people involved in the making of the movie.
     * @param yearOfRelease        The year the movie was released.
     * @param duration             The duration of the movie in minutes.
     * @param relatedContent      Other content related to this movie.
     */

    public Movie(String title, String synopsis, String isSuitableForMinors, String genre, String actors, int yearOfRelease, int duration,Content...relatedContent)
    {
        super(title,synopsis,isSuitableForMinors, genre,actors,relatedContent);

        try
        {
            setYearOfRelease(yearOfRelease);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }

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
     * Constructs a new Movie object with the specified title, synopsis, suitability for minors, genre, actors,
     * year of release, duration, and related content.
     *
     * @param title                The title of the movie.
     * @param synopsis             A brief summary or synopsis of the movie.
     * @param isSuitableForMinors Indicates whether the movie is suitable for minors ("Yes" or "No").
     * @param genre                The genre of the movie.
     * @param actors               The people involved in the making of the movie.
     * @param yearOfRelease        The year the movie was released.
     * @param duration             The duration of the movie in minutes.
     * @param relatedContent      Other content related to this movie.
     */

    public Movie(String title, String synopsis, String isSuitableForMinors, String genre, String actors, int yearOfRelease, int duration,HashSet<Content> relatedContent)
    {
        super(title,synopsis,isSuitableForMinors, genre,actors,relatedContent);

        try
        {
            setYearOfRelease(yearOfRelease);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }

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
     * Sets the year of release for the movie.
     *
     * @param yearOfRelease The new year of release for the movie.
     */

    public void setYearOfRelease(int yearOfRelease) throws IllegalArgumentException
    {
        if(yearOfReleaseIsValid(yearOfRelease))
        {
            this.yearOfRelease = yearOfRelease;
        }
        else
        {
            throw new IllegalArgumentException("Invalid argument as year of release. Year of release must be a positive integer with four digits");
        }
    }

    /**
     * Sets the duration of the movie.
     *
     * @param duration The new duration of the movie in minutes.
     */
    public void setDuration(int duration) throws IllegalArgumentException
    {
        if(durationIsValid(duration))
        {
            this.duration = duration;
        }
        else
        {
            throw new IllegalArgumentException("Invalid argument passed as movie duration. Duration must be a positive integer.");
        }
    }

    /**
     * Returns the year of release for the movie.
     *
     * @return The year of release for the movie.
     */
    public int getYearOfRelease()
    {
        return yearOfRelease;
    }

    /**
     * Returns the duration of the movie in minutes.
     *
     * @return The duration of the movie in minutes.
     */
    public int getDuration()
    {
        return duration;
    }


    /**
     * Checks if a given year of release value is valid.
     *
     * @param yearOfRelease The year of release value to check.
     * @return {@code true} if the value is valid, {@code false} otherwise.
     */
    public static boolean yearOfReleaseIsValid(int yearOfRelease)
    {
        return yearOfReleaseIsValid(String.valueOf(yearOfRelease));
    }


    /**
     * Checks if a given year of release value is valid.The given string is valid
     * if it represents a positive integer that has four digits
     *
     * @param yearOfRelease The year of release value to check.
     * @return {@code true} if the value is valid, {@code false} otherwise.
     */
    public static boolean yearOfReleaseIsValid(String yearOfRelease)
    {
        String validForm="\\d{" + 4 + "}";
        if(yearOfRelease.replaceAll("\\s", "").matches(validForm))
        {
            return Integer.parseInt(yearOfRelease.replaceAll("\\s", "")) > 0;
        }

        return false;
    }

    /**
     * Checks if a given duration value is valid.
     *
     * @param duration The duration value to check.
     * @return {@code true} if the value is valid, {@code false} otherwise.
     */
    public static boolean durationIsValid(int duration)
    {
        return durationIsValid(String.valueOf(duration));
    }


    /**
     * Checks if a given duration value is valid.The string is valid if it represents a positive number
     *
     * @param duration The duration value to check.
     * @return {@code true} if the value is valid, {@code false} otherwise.
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
     * Edits the movie by updating the title, synopsis, suitability for minors, genre, actors,
     * year of release, and duration.
     *
     * @param title                The new title of the movie.
     * @param synopsis             The new synopsis of the movie.
     * @param isSuitableForMinors The new suitability for minors ("Yes" or "No").
     * @param genre                The new genre of the movie.
     * @param actors               The new people involved in the making of the movie.
     * @param yearOfRelease        The new year of release for the movie.
     * @param duration             The new duration of the movie in minutes.
     */
    public void edit(String title, String synopsis, String isSuitableForMinors, String genre, String actors, int yearOfRelease, int duration)
    {
        super.edit(title,synopsis,isSuitableForMinors,genre,actors);

        try
        {
            setYearOfRelease(yearOfRelease);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }

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
     * Edits the movie by updating the title, synopsis, suitability for minors, genre, actors,
     * year of release, duration, and related content.
     *
     * @param title                The new title of the movie.
     * @param synopsis             The new synopsis of the movie.
     * @param isSuitableForMinors The new suitability for minors ("Yes" or "No").
     * @param genre                The new genre of the movie.
     * @param actors               The new people involved in the making of the movie.
     * @param yearOfRelease        The new year of release for the movie.
     * @param duration             The new duration of the movie in minutes.
     * @param related              The new set of related content associated with the movie.
     */
    public void edit(String title, String synopsis, String isSuitableForMinors, String genre, String actors, int yearOfRelease, int duration, HashSet<Content> related)
    {
        super.edit(title,synopsis,isSuitableForMinors,genre,actors,related);

        try
        {
            setYearOfRelease(yearOfRelease);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }

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
     * Overrides the equals method to compare Movie objects based on their title, synopsis,
     * suitability for minors, genre, year of release, duration, and related content.
     *
     * @param obj The object to compare with this Movie.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Movie movie))
        {
            return false;
        }

        return super.equals(movie) && duration==movie.getDuration()
                && yearOfRelease==movie.getYearOfRelease();
    }

    /**
     * Generates a hash code for this Movie object based on its title, synopsis,
     * suitability for minors, genre, year of release, duration, and related content.
     *
     * @return The hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),yearOfRelease,duration);
    }


    /**
     * Returns a string representation of this Movie object.
     *
     * @return A string representation including the title, synopsis, average rating, suitability for minors,
     * genre, year of release, duration, and actors.
     */
    public String toString()
    {
        return super.toString() + "\n" + "Year of release: " + yearOfRelease + "\n" + "Duration: " + duration;
    }
}
