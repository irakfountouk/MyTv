package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReviewTest
{

    private Review review;

    @Before
    public void setUp() throws Exception
    {
        User reviewer = new User("testUser", "password123", "John", "Doe");
        review=new Review(5, "Great movie!", reviewer);
    }

    @Test
    public void getRating()
    {
        assertEquals(5,review.getRating());
    }

    @Test
    public void getText()
    {
        assertEquals("Great movie!",review.getText());
    }

    @Test
    public void getReviewer()
    {
        User sameReviewer=new User("testUser", "password123", "John", "Doe");
        assertEquals(sameReviewer,review.getReviewer());
    }

    @Test
    public void getDate()
    {
        assertNotNull(review.getDate());
    }

    @Test
    public void testEquals()
    {
        User reviewer1 = new User("testUser", "password123", "John", "Doe");
        User reviewer2 = new User("differentUser", "securepass", "Jane", "Doe");

        Review review1 = new Review(5, "Great movie!", reviewer1);
        Review review2 = new Review(3, "meh meh", reviewer2);

        assertEquals(review, review1);
        assertNotEquals(review, review2);
    }

    @Test
    public void testHashCode()
    {
        User reviewer1 = new User("testUser", "password123", "John", "Doe");
        User reviewer2 = new User("differentUser", "securepass", "Jane", "Doe");

        Review review1 = new Review(5, "Great movie!", reviewer1);
        Review review2 = new Review(3, "meh meh", reviewer2);

        assertEquals(review.hashCode(),review1.hashCode());
        assertNotEquals(review.hashCode(),review2.hashCode());
    }

    @Test
    public void setRating()
    {
        review.setRating(4);
        assertEquals(4,review.getRating());
        assertThrows(IllegalArgumentException.class, () -> review.setRating(-2));
    }

    @Test
    public void ratingIsValid()
    {
        assertTrue(Review.ratingIsValid("3"));
        assertTrue(Review.ratingIsValid(3));

        assertFalse(Review.ratingIsValid("3s"));
        assertFalse(Review.ratingIsValid(-3));
    }

    @Test
    public void setText()
    {
        review.setText("Not half bad");
        assertEquals("Not half bad",review.getText());
    }

    @Test
    public void edit()
    {
        User reviewer = new User("testUser", "password123", "John", "Doe");
        Review review = new Review(3, "Okay movie", reviewer);

        review.edit(2, "Biggest flop of the year");

        assertEquals(2, review.getRating());
        assertEquals("Biggest flop of the year", review.getText());
    }

}