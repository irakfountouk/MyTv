package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MovieTest
{

    private Movie movie;

    @Before
    public void setUp() throws Exception
    {
        movie = new Movie("Movie Title", "Movie Synopsis",
                "Yes", "Action", "Actor 1, Actor 2", 2022, 120);
    }

    @Test
    public void getYearOfRelease()
    {
        assertEquals(2022,movie.getYearOfRelease());
    }

    @Test
    public void getDuration()
    {
        assertEquals(120,movie.getDuration());
    }

    @Test
    public void testEquals()
    {
        Movie sameMovie = new Movie("Movie Title", "Movie Synopsis",
                "Yes", "Action", "Actor 1, Actor 2", 2022, 120);
        assertEquals(movie, sameMovie);

        Movie differentMovie = new Movie("Different Title", "Different Synopsis",
                "No", "Comedy", "Actor 5, Actor 6", 2023, 150);
        assertNotEquals(movie, differentMovie);
    }

    @Test
    public void testHashCode()
    {
        Movie sameMovie = new Movie("Movie Title", "Movie Synopsis",
                "Yes", "Action", "Actor 1, Actor 2", 2022, 120);
        assertEquals(movie.hashCode(), sameMovie.hashCode());

        Movie differentMovie = new Movie("Different Title", "Different Synopsis",
                "No", "Comedy", "Actor 5, Actor 6", 2023, 150);

        assertNotEquals(movie.hashCode(),differentMovie.hashCode());
    }

    @Test
    public void setYearOfRelease()
    {
        movie.setYearOfRelease(2004);
        assertEquals(2004,movie.getYearOfRelease());
        assertThrows(IllegalArgumentException.class, () -> movie.setYearOfRelease(-2));
        assertThrows(IllegalArgumentException.class, () -> movie.setYearOfRelease(145));
    }

    @Test
    public void setDuration()
    {
        movie.setDuration(90);
        assertEquals(90,movie.getDuration());
        assertThrows(IllegalArgumentException.class, () -> movie.setDuration(-3));
    }


    @Test
    public void yearOfReleaseIsValid()
    {
        assertTrue(Movie.yearOfReleaseIsValid("2020"));
        assertTrue(Movie.yearOfReleaseIsValid(2020));

        assertFalse(Movie.yearOfReleaseIsValid("2020s"));
        assertFalse(Movie.yearOfReleaseIsValid(-1));
    }

    @Test
    public void durationIsValid()
    {
        assertTrue(Movie.durationIsValid("125"));
        assertTrue(Movie.durationIsValid(125));

        assertFalse(Movie.durationIsValid("150s"));
        assertFalse(Movie.durationIsValid(-1));
    }

    @Test
    public void edit()
    {
        movie.edit("New Title", "New Synopsis", "No", "Drama", "Actor 3, Actor 4", 2023, 150);
        assertEquals("New Title", movie.getTitle());
        assertEquals("New Synopsis", movie.getSynopsis());
        assertEquals("No", movie.getIsSuitableForMinors());
        assertEquals("Drama", movie.getGenre());
        assertEquals("Actor 3, Actor 4", movie.getPeopleInvolved());
        assertEquals(2023, movie.getYearOfRelease());
        assertEquals(150, movie.getDuration());
    }

}