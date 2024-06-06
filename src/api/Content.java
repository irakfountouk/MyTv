package api;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.*;

/**
 * The {@code Content} class represents a piece of content, such as a movie, book, or any creative work.
 * It includes information about the title, people involved, related content, and reviews.
 * This class is Serializable for data persistence.
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 2024-01-10
 */
public class Content implements Serializable
{
    private String title,peopleInvolved;
    private HashSet<Content> relatedContent;
    private HashMap<User,Review> reviews;

    public Content(String title,String peopleInvolved,Content... relatedContent)
    {
        this.title=title;
        this.peopleInvolved=peopleInvolved;
        this.relatedContent =new HashSet<>();
        addToRelatedContent(relatedContent);
        reviews =new HashMap<>();
    }

    /**
     * Constructs a new Content object with the specified title, people involved, and related content.
     *
     * @param title  The title of the content.
     * @param peopleInvolved The people involved in the making of the content.
     * @param relatedContent Other content related to this content.
     */
    public Content(String title,String peopleInvolved,HashSet<Content> relatedContent)
    {
        this.title=title;
        this.peopleInvolved=peopleInvolved;
        this.relatedContent =new HashSet<>();
        this.relatedContent=relatedContent;
        reviews =new HashMap<>();
    }

    /**
     * Sets the title of the content.
     *
     * @param title The new title of the content.
     */
    public void setTitle(String title)
    {
        this.title=title;
    }


    /**
     * Sets the people involved in the making of the content.
     *
     * @param peopleInvolved The new people involved in the content.
     */
    public void setPeopleInvolved(String peopleInvolved)
    {
        this.peopleInvolved=peopleInvolved;
    }

    /**
     * Adds one or more reviews to the content.
     *
     * @param reviews The reviews to be added.
     */
    public void addToReviews(Review...reviews)
    {
        for (Review review : reviews)
        {
            this.reviews.put(review.getReviewer(),review);
        }
    }

    /**
     * Adds a set of reviews to the content.
     *
     * @param reviews The set of reviews to be added.
     */
    public void addToReviews(HashSet<Review> reviews)
    {

        for (Review review : reviews)
        {
            this.reviews.put(review.getReviewer(),review);
        }
    }

    /**
     * Removes one or more reviews from the content.
     *
     * @param reviews The reviews to be removed.
     */
    public void removeFromReviews(Review...reviews)
    {
        for (Review review : reviews)
        {
            this.reviews.remove(review.getReviewer(),review);
        }
    }

    /**
     * Removes a set of reviews from the content.
     *
     * @param reviews The set of reviews to be removed.
     */
    public void removeFromReviews(HashSet<Review> reviews)
    {
        for (Review review : reviews)
        {
            this.reviews.remove(review.getReviewer(),review);
        }
    }

    /**
     * Deletes all reviews associated with the content.
     */
    public void deleteReviews()
    {
        reviews.clear();
    }


    /**
     * Adds one or more pieces of related content to the content.
     *
     * @param relatedContent The related content to be added.
     */
    public void addToRelatedContent(Content...relatedContent)
    {
        Collections.addAll(this.relatedContent, relatedContent);
    }

    /**
     * Adds a set of related content to the content.
     *
     * @param relatedContent The set of related content to be added.
     */
    public void addToRelatedContent(HashSet<Content> relatedContent)
    {
        this.relatedContent.addAll(relatedContent);
    }

    /**
     * Removes one or more pieces of related content from the content.
     *
     * @param relatedContent The related content to be removed.
     */
    public void removeFromRelatedContent(Content...relatedContent)
    {
        if(this.relatedContent.isEmpty())
        {
            return;
        }
        for (Content content : relatedContent)
        {
            this.relatedContent.remove(content);
        }
    }

    /**
     * Removes a set of related content from the content.
     *
     * @param relatedContent The set of related content to be removed.
     */
    public void removeFromRelatedContent(HashSet<Content> relatedContent)
    {
        if(this.relatedContent.isEmpty())
        {
            return;
        }
        this.relatedContent.removeAll(relatedContent);
    }


    /**
     * Deletes all related content associated with the content.
     */
    public void deleteRelatedContent()
    {
        relatedContent.clear();
    }

    /**
     * Returns the title of the content.
     *
     * @return The title of the content.
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Returns the people involved in the making of the content.
     *
     * @return The people involved in the content.
     */
    public String getPeopleInvolved()
    {
        return peopleInvolved;
    }


    /**
     * Returns a set of related content associated with the content.
     *
     * @return The set of related content.
     */
    public HashSet<Content> getRelatedContent()
    {
        return relatedContent;
    }

    /**
     * Returns a map of reviews associated with the content.
     *
     * @return The map of reviews.
     */
    public HashMap<User,Review> getReviews()
    {
        return reviews;
    }

    /**
     * Calculates the average rating based on the reviews associated with this content.
     *
     * @return The average rating, or 0.0 if there are no reviews.
     */
    public Double getAverageRating()
    {
        int sum=0;
        if(!reviews.isEmpty())
        {
            for (Review value : reviews.values())
            {
                sum+=value.getRating();
            }
            return (double) sum/ reviews.size();
        }
        return (double) 0;
    }

    /**
     * Returns the formatted average rating as a string with one decimal place.
     *
     * @return The formatted average rating or "-" if there are no reviews.
     */
    public String getFormattedAverageRating()
    {
        String averageRatingFormatted="-";

        if(!reviews.isEmpty())
        {
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

            numberFormat.setMaximumFractionDigits(1);

            averageRatingFormatted = numberFormat.format(getAverageRating());
        }

        return averageRatingFormatted;
    }

    /**
     * Edits the content by updating the title and people involved.
     *
     * @param title           The new title of the content.
     * @param peopleInvolved The new people involved in the content.
     */
    public void edit(String title,String peopleInvolved)
    {
        setTitle(title);
        setPeopleInvolved(peopleInvolved);
    }

    /**
     * Edits the content by updating the title, people involved, and related content.
     *
     * @param title           The new title of the content.
     * @param peopleInvolved The new people involved in the content.
     * @param relatedContent  The new set of related content associated with the content.
     */
    public void edit(String title,String peopleInvolved,HashSet<Content> relatedContent)
    {
        setTitle(title);
        setPeopleInvolved(peopleInvolved);

        this.relatedContent=relatedContent;
    }

    /**
     * Overrides the equals method to compare Content objects based on their title, people involved,
     * and related content.
     *
     * @param obj The object to compare with this Content.
     * @return {@code true} if the objects are equal, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Content content)) {
            return false;
        }

        if(this==content) {
            return true;
        }

        return title.equals(content.getTitle()) && peopleInvolved.equals(content.getPeopleInvolved());
    }

    /**
     * Returns a string representation of this Content object.
     *
     * @return A string representation including the title, people involved, and average rating.
     */
    public String toString()
    {
        return "Title: " + title + '\n' + "People involved in the making: " + peopleInvolved + '\n'
                + "Average Rating: " +getFormattedAverageRating();
    }

    /**
     * Generates a hash code for this Content object based on its title and people involved.
     *
     * @return The hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(title, peopleInvolved);
    }

}
