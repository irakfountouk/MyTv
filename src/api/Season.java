package api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * The {@code Season} class represents a season of episodes within a series. Each season has a
 * season number, a year of release, and a list of episodes.
 * This class is designed to store information about a season, including its episodes.
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 2024-01-10
 */
public class Season implements Serializable
{
    private ArrayList<Episode> episodes;
    private int yearOfRelease,seasonNumber;

    /**
     * Constructs a new Season object with the specified season number, year of release, and episodes.
     *
     * @param seasonNumber   The season number.
     * @param yearOfRelease  The year of release.
     * @param episodes       The episodes associated with the season.
     */
    public Season(int seasonNumber,int yearOfRelease,Episode...episodes)
    {
        this.episodes=new ArrayList<>();
        addToEpisodes(episodes);


        try
        {
            setYearOfRelease(yearOfRelease);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }

        setSeasonNumber(seasonNumber);
    }

    /**
     * Constructs a new Season object with the specified season number, year of release, and episodes.
     *
     * @param seasonNumber   The season number.
     * @param yearOfRelease  The year of release.
     * @param episodes       The list of episodes associated with the season.
     */
    public Season(int seasonNumber,int yearOfRelease,ArrayList<Episode> episodes)
    {
        this.episodes=new ArrayList<>();
        this.episodes=episodes;

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
            setSeasonNumber(seasonNumber);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }

    }

    /**
     * Sets the year of release for the season.
     *
     * @param yearOfRelease The year of release.
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
     * Sets the season number for the season.
     *
     * @param seasonNumber The season number.
     */
    public void setSeasonNumber(int seasonNumber) throws IllegalArgumentException
    {
        if (seasonNumberIsValid(seasonNumber))
        {
            this.seasonNumber=seasonNumber;
        }
        else
        {
            throw new IllegalArgumentException("Invalid argument passed as season number. Season number must be a positive integer");
        }
    }

    /**
     * Checks if the provided year of release is valid.
     *
     * @param yearOfRelease The year of release to validate.
     * @return {@code true} if the year of release is valid, {@code false} otherwise.
     */
    public static boolean yearOfReleaseIsValid(int yearOfRelease)
    {
        return yearOfReleaseIsValid(String.valueOf(yearOfRelease));
    }

    /**
     * Checks if the provided year of release is valid. The string is valid if
     * it represents a positive integer with four digits
     * @param yearOfRelease The year of release to validate.
     * @return {@code true} if the year of release is valid, {@code false} otherwise.
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
     * Checks if the provided season number is valid.
     *
     * @param seasonNumber The season number to validate.
     * @return {@code true} if the season number is valid, {@code false} otherwise.
     */
    public static boolean seasonNumberIsValid(int seasonNumber)
    {
        return  seasonNumberIsValid(String.valueOf(seasonNumber));
    }

    /**
     * Checks if the provided season number is valid.
     * The string is valid if it represents a positive integer
     * @param seasonNumber The season number to validate.
     * @return {@code true} if the season number is valid, {@code false} otherwise.
     */
    public static boolean seasonNumberIsValid(String seasonNumber)
    {
        String validForm="\\d+";

        if(seasonNumber.replaceAll("\\s", "").matches(validForm))
        {
            return Integer.parseInt(seasonNumber.replaceAll("\\s", "")) > 0;
        }

        return false;
    }

    /**
     * Adds one or more episodes to the season.
     *
     * @param episodes The episodes to add.
     */
    public void addToEpisodes(Episode...episodes)
    {
        Collections.addAll(this.episodes, episodes);
    }

    /**
     * Adds a list of episodes to the season.
     *
     * @param episodes The list of episodes to add.
     */
    public void addToEpisodes(ArrayList<Episode> episodes)
    {
        this.episodes.addAll(episodes);
    }

    /**
     * Removes one or more episodes from the season.
     *
     * @param episodes The episodes to remove.
     */
    public void removeFromEpisodes(Episode...episodes)
    {
        for (Episode episode : episodes)
        {
            this.episodes.remove(episode);
        }
    }

    /**
     * Removes a list of episodes from the season.
     *
     * @param episodes The list of episodes to remove.
     */
    public void removeFromEpisodes(ArrayList<Episode> episodes)
    {
        this.episodes.removeAll(episodes);
    }


    /**
     * Deletes all episodes from the season.
     */
    public void deleteEpisodes()
    {
        episodes.clear();
    }

    /**
     * Returns the list of episodes associated with the season.
     *
     * @return The list of episodes.
     */
    public ArrayList<Episode> getEpisodes()
    {
        return episodes;
    }

    /**
     * Returns the year of release for the season.
     *
     * @return The year of release.
     */
    public int getYearOfRelease()
    {
        return yearOfRelease;
    }

    /**
     * Returns the season number.
     *
     * @return The season number.
     */
    public int getSeasonNumber()
    {
        return this.seasonNumber;
    }

    /**
     * Edits the season by updating the season number, year of release, and the list of episodes.
     *
     * @param seasonNumber The new season number.
     * @param yearOfRelease The new year of release.
     * @param episodes The new list of episodes.
     */
    public void edit(int seasonNumber,int yearOfRelease,ArrayList<Episode> episodes)
    {

        try
        {
            setSeasonNumber(seasonNumber);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }

        try
        {
            setYearOfRelease(yearOfRelease);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }

        this.episodes=episodes;
    }

    /**
     * Overrides the equals method to compare Season objects based on their season number,
     * year of release, and the list of episodes.
     *
     * @param obj The object to compare with this Season.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    public boolean equals(Object obj)
    {
        boolean episodesMatch=true;

        if(!(obj instanceof Season season))
        {
            return false;
        }

        if(season==this)
        {
            return true;
        }

        if(this.episodes.size()!=season.getEpisodes().size())
        {
            episodesMatch = false;
        }
        else if(!episodes.isEmpty() && !season.getEpisodes().isEmpty())
        {
            episodesMatch=episodes.containsAll(season.getEpisodes()) && season.getEpisodes().containsAll(episodes);
        }

        return seasonNumber==season.getSeasonNumber() && yearOfRelease==season.getYearOfRelease()
                && episodesMatch;
    }

    /**
     * Generates a hash code for this Season object based on its
     * season number, year of release and list of episodes
     *
     * @return The hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(seasonNumber,yearOfRelease,episodes);
    }

    /**
     * Returns a string representation of this Season object.
     *
     * @return A string representation including the season number and the number of episodes.
     */
    @Override
    public String toString()
    {
        return "Season number: " + seasonNumber + "\n" + "Episodes: " + episodes.size() + "\n";
    }
}
