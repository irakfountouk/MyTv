package api;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Represents a review made by a user for content, including text, rating, and reviewer information.
 *  @authors Iraklis Fountoukidis, Chrysoula Tegousi
 *  @version 2024-01-10
 */
public class Review implements Serializable
{
    private String text;
    private int rating;
    private LocalDate date;
    private User reviewer;

    /**
     * Constructs a Review object with a rating, text, and reviewer.
     *
     * @param rating   the rating given in the review
     * @param text     the text content of the review
     * @param reviewer the user who made the review
     */
    public Review(int rating, String text, User reviewer)
    {
        try {
            setRating(rating);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }
        this.text =text;
        this.reviewer = reviewer;
        date=LocalDate.now();
    }

    /**
     * Sets the rating for the review if it is valid.
     *
     * @param rating the rating to set for the review
     */
    public void setRating(int rating) throws IllegalArgumentException
    {
        if(ratingIsValid(rating))
        {
            this.rating = rating;
        }
        else
        {
            throw new IllegalArgumentException("Invalid argument passed as rating. Rating must be a positive integer");
        }
    }


    /**
     * Checks if a given rating is valid.
     *
     * @param rating the rating to validate
     * @return true if the rating is a valid positive integer, false otherwise
     */
    public static boolean ratingIsValid(int rating)
    {
        return ratingIsValid(String.valueOf(rating));
    }

    /**
     * Checks if a given rating is valid.
     * The rating is valid if it represents a positive integer
     * @param rating the rating to validate
     * @return true if the rating is a valid positive integer, false otherwise
     */
    public static boolean ratingIsValid(String rating)
    {
        String validForm="\\d+";
        if(rating.replaceAll("\\s", "").matches(validForm))
        {
            return Integer.parseInt(rating) > 0;
        }

        return false;
    }

    /**
     * Sets the text content of the review.
     *
     * @param text the text content to set for the review
     */
    public void setText(String text)
    {
        this.text=text;
    }

    /**
     * Gets the rating assigned to the content.
     *
     * @return The rating of the content.
     */
    public int getRating()
    {
        return rating;
    }

    /**
     * Gets the text of the review.
     *
     * @return The text of the review.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Gets the user who wrote the review.
     *
     * @return The reviewer of the content.
     */
    public User getReviewer()
    {
        return reviewer;
    }

    /**
     * Edits the review with new rating and text, updating the date to the current date.
     *
     * @param rating the new rating for the review
     * @param text   the new text content for the review
     */
    public void edit(int rating, String text)
    {
        date=LocalDate.now();

        try {
            setRating(rating);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException: " + e.getMessage());
        }

        this.text=text;
    }

    /**
     * Overrides the equals method to check if two Review objects are equal based on reviewer, text, and rating.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Review review))
            return false;

        if(this==obj)
            return true;

        return reviewer.equals(review.getReviewer()) &&
                text.equals(review.getText()) && rating==review.getRating();
    }

    /**
     * Generates a hash code for the Review based on text, rating, and reviewer.
     *
     * @return the hash code value for the Review
     */
    public int hashCode()
    {
        return Objects.hash(text,rating, reviewer);
    }

    /**
     * Gets a string representation of the Review containing rating, text, date, and reviewer information.
     *
     * @return a string representation of the Review
     */
    public String toString()
    {
        return "Rating: " + rating + "\n" + text + "\n" + "Date: " + getFormattedDate() + "\n" +
                "User: " + reviewer + "\n";
    }

    /**
     * Gets the date of the review.
     *
     * @return the date of the review
     */
    public LocalDate getDate()
    {
        return date;
    }

    /**
     * Gets the formatted date of the review.
     *
     * @return a string representing the formatted date of the review
     */
    public String getFormattedDate()
    {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return date.format(formatter);
    }
}
