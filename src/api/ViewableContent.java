package api;

import java.util.HashSet;
import java.util.Objects;

/**
 * The {@code ViewableContent} class represents content that can be viewed, such as movies or TV shows.
 * It extends the {@link Content} class and includes additional attributes like synopsis, suitability for minors,
 * and genre. This class is designed to store information about content that users can view and rate.
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 2024-01-08
 */
public class ViewableContent extends Content
{
    private String synopsis;
    private String isSuitableForMinors;
    private String genre;

    /**
     * Constructs a new ViewableContent object with the specified title, synopsis, suitability for minors, genre,
     * and related content.
     *
     * @param title                The title of the viewable content.
     * @param synopsis             A brief summary or description of the viewable content.
     * @param isSuitableForMinors Indicates whether the content is suitable for minors ("Yes" or "No").
     * @param genre                The genre of the viewable content.
     * @param actors               The people involved in the making of the content.
     * @param relatedContent      Other content related to this viewable content.
     */

    public ViewableContent(String title, String synopsis, String isSuitableForMinors, String genre, String actors,Content...relatedContent)
    {
        super(title,actors,relatedContent);
        this.synopsis=synopsis;
        try {
            setIsSuitableForMinors(isSuitableForMinors);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }
        try {
            setGenre(genre);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }
    }

    /**
     * Constructs a new ViewableContent object with the specified title, synopsis, suitability for minors, genre,
     * and related content.
     *
     * @param title                The title of the viewable content.
     * @param synopsis             A brief summary or description of the viewable content.
     * @param isSuitableForMinors Indicates whether the content is suitable for minors ("Yes" or "No").
     * @param genre                The genre of the viewable content.
     * @param actors               The people involved in the making of the content.
     * @param relatedContent      Other content related to this viewable content.
     */

    public ViewableContent(String title, String synopsis, String isSuitableForMinors, String genre, String actors, HashSet<Content> relatedContent)
    {
        super(title,actors,relatedContent);
        this.synopsis=synopsis;
        setIsSuitableForMinors(isSuitableForMinors);
        setGenre(genre);
    }


    public void setSynopsis(String synopsis)
    {
        this.synopsis=synopsis;
    }


    /**
     * Sets the suitability for minors of the viewable content.
     *
     * @param isSuitableForMinors The new suitability for minors ("Yes" or "No").
     * @throws IllegalArgumentException If an invalid argument is passed as suitability rating.
     */
    public void setIsSuitableForMinors(String isSuitableForMinors) throws IllegalArgumentException
    {
        if(isSuitableForMinors.toLowerCase().replaceAll("\\s", "").equals("yes"))
        {
            this.isSuitableForMinors="Yes";
        }
        else if(isSuitableForMinors.toLowerCase().replaceAll("\\s", "").equals("no"))
        {
            this.isSuitableForMinors="No";
        }
        else
        {
            throw new IllegalArgumentException("Invalid argument passed as suitability rating. Valid arguments are: Yes, No");
        }
    }

    /**
     * Checks if a given suitability for minors value is valid.
     *
     * @param suitableForMinors The suitability for minors value to check.
     * @return {@code true} if the value is valid, {@code false} otherwise.
     */
    public static boolean suitableForMinorsIsValid(String suitableForMinors)
    {

        if(suitableForMinors.toLowerCase().replaceAll("\\s", "").equals("yes"))
        {
            return true;
        }
        else return suitableForMinors.toLowerCase().replaceAll("\\s", "").equals("no");
    }

    /**
     * Sets the genre of the viewable content.
     *
     * @param genre The new genre of the viewable content.
     * @throws IllegalArgumentException If an invalid argument is passed as the genre.
     */
    public void setGenre(String genre) throws IllegalArgumentException
    {
        String newGenre=genre.toLowerCase().replaceAll("\\s", "");
        if(newGenre.equals("comedy"))
        {
            this.genre="Comedy";
        }
        else if(newGenre.equals("drama"))
        {
            this.genre="Drama";
        }
        else if(newGenre.equals("action"))
        {
            this.genre="Action";
        }
        else if(newGenre.contains("scifi") || newGenre.contains("sci-fi") || newGenre.contains("sciencefiction"))
        {
            this.genre="Science fiction";
        }
        else if(newGenre.equals("horror"))
        {
            this.genre="Horror";
        }
        else
        {
            throw new IllegalArgumentException("Invalid argument passed as genre. Valid arguments are: Action, Drama, Horror, Science fiction, Comedy");
        }

    }

    /**
     * Checks if a given genre value is valid.
     *
     * @param genre The genre value to check.
     * @return {@code true} if the value is valid, {@code false} otherwise.
     */
    public static boolean genreIsValid(String genre)
    {
        String newGenre=genre.toLowerCase().replaceAll("\\s", "");
        if(newGenre.equals("comedy"))
        {
            return true;
        }
        else if(newGenre.equals("drama"))
        {
            return true;
        }
        else if(newGenre.equals("action"))
        {
            return true;
        }
        else if(newGenre.contains("scifi") || newGenre.contains("sci-fi") || newGenre.contains("sciencefiction"))
        {
            return true;
        }
        else return newGenre.equals("horror");
    }

    /**
     * Returns the synopsis of the viewable content.
     *
     * @return The synopsis of the viewable content.
     */
    public String getSynopsis()
    {
        return synopsis;
    }

    /**
     * Returns the suitability for minors of the viewable content.
     *
     * @return The suitability for minors of the viewable content.
     */
    public String getIsSuitableForMinors()
    {
        return isSuitableForMinors;
    }

    /**
     * Returns the genre of the viewable content.
     *
     * @return The genre of the viewable content.
     */
    public String getGenre()
    {
        return genre;
    }

    /**
     * Edits the viewable content by updating the title, synopsis, suitability for minors, genre, and actors.
     *
     * @param title                The new title of the viewable content.
     * @param synopsis             The new synopsis of the viewable content.
     * @param isSuitableForMinors The new suitability for minors ("Yes" or "No").
     * @param genre                The new genre of the viewable content.
     * @param actors               The new people involved in the making of the content.
     */
    public void edit(String title, String synopsis, String isSuitableForMinors, String genre, String actors)
    {
        super.edit(title,actors);
        this.synopsis=synopsis;
        try {
            setIsSuitableForMinors(isSuitableForMinors);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }
        try {
            setGenre(genre);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }
    }

    /**
     * Edits the viewable content by updating the title, synopsis, suitability for minors, genre, actors, and related content.
     *
     * @param title                The new title of the viewable content.
     * @param synopsis             The new synopsis of the viewable content.
     * @param isSuitableForMinors The new suitability for minors ("Yes" or "No").
     * @param genre                The new genre of the viewable content.
     * @param actors               The new people involved in the making of the content.
     * @param relatedContent      The new set of related content associated with the viewable content.
     */
    public void edit(String title, String synopsis, String isSuitableForMinors, String genre, String actors, HashSet<Content> relatedContent)
    {
        super.edit(title,actors,relatedContent);
        this.synopsis=synopsis;
        try {
            setIsSuitableForMinors(isSuitableForMinors);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }
        try {
            setGenre(genre);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }
    }

    /**
     * Overrides the equals method to compare ViewableContent objects based on their title, synopsis,
     * suitability for minors, genre, and related content.
     *
     * @param obj The object to compare with this ViewableContent.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    public boolean equals(Object obj)
    {
        if(!(obj instanceof ViewableContent)) {
            return false;
        }

        ViewableContent viewableContent=(ViewableContent) obj;
        return super.equals(viewableContent) && synopsis.equals(viewableContent.getSynopsis())
                && isSuitableForMinors.equals(viewableContent.getIsSuitableForMinors()) &&
                genre.equals(viewableContent.getGenre());
    }

    /**
     * Generates a hash code for this ViewableContent object based on its title, synopsis,
     * suitability for minors, genre, and related content.
     *
     * @return The hash code value for this object.
     */
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), synopsis,genre,isSuitableForMinors);
    }

    /**
     * Returns a string representation of this ViewableContent object.
     *
     * @return A string representation including the title, synopsis, average rating, suitability for minors, genre, and actors.
     */
    public String toString()
    {
        return "Title: " + getTitle() + "\n" + "Synopsis: " + synopsis + "\n"
                + "Average rating: " + getFormattedAverageRating() + "\n"
                + "Suitable for minors: " + isSuitableForMinors + "\n" + "Genre: " + genre + "\n"
                + "Actors: " + getPeopleInvolved();
    }

}
