package api;

import java.util.*;


/**
 * The {@code Series} class represents a series, a specific type of viewable content with multiple seasons.
 * It extends the {@link ViewableContent} class and includes an {@link ArrayList} of {@link Season}s.
 * This class is designed to store information about series that users can view, rate, and provide related content.
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 2024-01-10
 */

public class Series extends ViewableContent
{
    private ArrayList<Season> seasons;

    /**
     * Constructs a new Series object with the specified title, synopsis, suitability for minors, genre, actors,
     * seasons, and related content.
     *
     * @param title                The title of the series.
     * @param synopsis             A brief summary or description of the series.
     * @param isSuitableForMinors Indicates whether the series is suitable for minors ("Yes" or "No").
     * @param genre                The genre of the series.
     * @param actors               The people involved in the making of the series.
     * @param seasons              The list of seasons associated with the series.
     * @param relatedContent      Other content related to this series.
     */
    public Series(String title, String synopsis, String isSuitableForMinors, String genre, String actors, ArrayList<Season> seasons, Content...relatedContent)
    {
        super(title,synopsis,isSuitableForMinors,genre,actors,relatedContent);
        this.seasons=new ArrayList<>();
        addToSeasons(seasons);
    }

    /**
     * Constructs a new Series object with the specified title, synopsis, suitability for minors, genre, actors,
     * seasons, and related content.
     *
     * @param title                The title of the series.
     * @param synopsis             A brief summary or description of the series.
     * @param isSuitableForMinors Indicates whether the series is suitable for minors ("Yes" or "No").
     * @param genre                The genre of the series.
     * @param actors               The people involved in the making of the series.
     * @param seasons              The list of seasons associated with the series.
     * @param relatedContent      Other content related to this series.
     */
    public Series(String title, String synopsis, String isSuitableForMinors, String genre, String actors, ArrayList<Season> seasons, HashSet<Content> relatedContent)
    {
        super(title,synopsis,isSuitableForMinors,genre,actors,relatedContent);
        this.seasons=new ArrayList<>();
        addToSeasons(seasons);
    }

    /**
     * Adds one or more seasons to the series.
     *
     * @param seasons The seasons to add.
     */
    public void addToSeasons(Season...seasons)
    {
        for (Season season : seasons)
        {
            if(!this.seasons.contains(season))
            {
                this.seasons.add(season);
            }
        }
        sortSeasons();
    }

    /**
     * Adds a list of seasons to the series.
     *
     * @param seasons The list of seasons to add.
     */
    public void addToSeasons(ArrayList<Season> seasons)
    {
        for (Season season : seasons)
        {
            if(!this.seasons.contains(season))
            {
                this.seasons.add(season);
            }
        }
        sortSeasons();
    }

    /**
     * Removes one or more seasons from the series.
     *
     * @param seasons The seasons to remove.
     */
    public void removeFromSeasons(Season...seasons)
    {
        if(this.seasons.isEmpty())
        {
            return;
        }

        for (Season season : seasons)
        {
            this.seasons.remove(season);
        }

        if(!this.seasons.isEmpty())
        {
            sortSeasons();
        }
    }

    /**
     * Removes a list of seasons from the series.
     *
     * @param seasons The list of seasons to remove.
     */
    public void removeFromSeasons(ArrayList<Season> seasons)
    {
        if(this.seasons.isEmpty())
        {
            return;
        }

        this.seasons.removeAll(seasons);

        if(!this.seasons.isEmpty())
        {
            sortSeasons();
        }
    }

    /**
     * Deletes all seasons from the series.
     */

    public void deleteSeasons()
    {
        seasons.clear();
    }

    /**
     * Sorts the seasons based on their season numbers.
     */
    public void sortSeasons()
    {
        seasons.sort(Comparator.comparingInt(Season::getSeasonNumber));
    }

    /**
     * Returns the list of seasons associated with the series.
     *
     * @return The list of seasons.
     */
    public ArrayList<Season> getSeasons()
    {
        return seasons;
    }

    /**
     * Edits the series by updating the title, synopsis, suitability for minors, genre, actors,
     * and the list of seasons.
     *
     * @param title                The new title of the series.
     * @param synopsis             The new synopsis of the series.
     * @param isSuitableForMinors The new suitability for minors ("Yes" or "No").
     * @param genre                The new genre of the series.
     * @param actors               The new people involved in the making of the series.
     * @param seasons              The new list of seasons associated with the series.
     */
    public void edit(String title, String synopsis, String isSuitableForMinors, String genre, String actors, ArrayList<Season> seasons)
    {
        super.edit(title,synopsis,isSuitableForMinors,genre,actors);
        for (Season season : seasons)
        {
            if(!this.seasons.contains(season))
            {
                this.seasons.add(season);
            }
        }

        Iterator<Season> iterator = this.seasons.iterator();
        while (iterator.hasNext())
        {
            Season season = iterator.next();
            if (!seasons.contains(season))
            {
                iterator.remove();
            }
        }

        if(this.seasons.isEmpty()) {
            sortSeasons();
        }
    }

    /**
     * Edits the series by updating the title, synopsis, suitability for minors, genre, actors,
     * the list of seasons, and related content.
     *
     * @param title                The new title of the series.
     * @param synopsis             The new synopsis of the series.
     * @param isSuitableForMinors The new suitability for minors ("Yes" or "No").
     * @param genre                The new genre of the series.
     * @param actors               The new people involved in the making of the series.
     * @param seasons              The new list of seasons associated with the series.
     * @param related              The new set of related content associated with the series.
     */
    public void edit(String title, String synopsis, String isSuitableForMinors, String genre, String actors, ArrayList<Season> seasons, HashSet<Content> related)
    {
        super.edit(title,synopsis,isSuitableForMinors,genre,actors,related);

        for (Season season : seasons)
        {
            if(!this.seasons.contains(season))
            {
                this.seasons.add(season);
            }
        }

        Iterator<Season> iterator = this.seasons.iterator();
        while (iterator.hasNext())
        {
            Season season = iterator.next();
            if (!seasons.contains(season))
            {
                iterator.remove();
            }
        }

        if(!this.seasons.isEmpty())
        {
            sortSeasons();
        }

    }

    /**
     * Overrides the equals method to compare Series objects based on their title, synopsis,
     * suitability for minors, genre, and the list of seasons.
     *
     * @param obj The object to compare with this Series.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    public boolean equals(Object obj)
    {
        boolean seasonsMatch=true;
        if(!(obj instanceof Series series))
        {
            return false;
        }

        if(seasons.size()!=series.getSeasons().size())
        {
            seasonsMatch = false;
        }
        else if(!seasons.isEmpty() && !series.getSeasons().isEmpty())
        {
            seasonsMatch=seasons.containsAll(series.getSeasons()) && series.getSeasons().containsAll(seasons);
        }

        return super.equals(series) && seasonsMatch;
    }


    /**
     * Generates a hash code for this Series object based on its title, synopsis,
     * suitability for minors, genre, and the list of seasons.
     *
     * @return The hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(),seasons);
    }


    /**
     * Returns a string representation of this Series object.
     *
     * @return A string representation including the title, synopsis, average rating, suitability for minors,
     * genre, and the number of seasons.
     */
    public String toString()
    {
        return super.toString() + "\n" + "Seasons: " + seasons.size();
    }

}
