package api;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class ViewableContentTest
{
    private ViewableContent viewableContent;

    @Before
    public void setUp() throws Exception
    {
        viewableContent = new ViewableContent("Movie Title", "Movie Synopsis",
                "Yes", "Action", "Actor 1, Actor 2");
    }

    @Test
    public void getSynopsis()
    {
        assertEquals("Movie Synopsis",viewableContent.getSynopsis());
    }

    @Test
    public void getIsSuitableForMinors()
    {
        assertEquals("Yes",viewableContent.getIsSuitableForMinors());
    }

    @Test
    public void getGenre()
    {
        assertEquals("Action",viewableContent.getGenre());
    }

    @Test
    public void testEquals()
    {
        ViewableContent sameContent = new ViewableContent("Movie Title", "Movie Synopsis",
                "Yes", "Action", "Actor 1, Actor 2");
        ViewableContent differentContent = new ViewableContent("Different Title", "Different Synopsis",
                "No", "Comedy", "Actor 3, Actor 4");

        assertEquals(viewableContent, sameContent);
        assertNotEquals(viewableContent, differentContent);
    }


    @Test
    public void testHashCode()
    {
        // Test the hashCode method
        ViewableContent sameContent = new ViewableContent("Movie Title", "Movie Synopsis",
                "Yes", "Action", "Actor 1, Actor 2");

        assertEquals(viewableContent.hashCode(), sameContent.hashCode());

        ViewableContent differentContent = new ViewableContent("Different Title", "Different Synopsis",
                "No", "Comedy", "Actor 3, Actor 4");

        assertNotEquals(viewableContent.hashCode(),differentContent.hashCode());
    }

    @Test
    public void genreIsValid()
    {
        assertTrue(ViewableContent.genreIsValid("Comedy"));
        assertTrue(ViewableContent.genreIsValid("Drama"));
        assertTrue(ViewableContent.genreIsValid("Action"));
        assertTrue(ViewableContent.genreIsValid("Science fiction"));
        assertTrue(ViewableContent.genreIsValid("sci fi"));
        assertTrue(ViewableContent.genreIsValid("sci-fi"));
        assertTrue(ViewableContent.genreIsValid("Horror"));
        assertFalse(ViewableContent.genreIsValid("InvalidGenre"));
    }

    @Test
    public void suitabilityForMinorsIsValid()
    {
        assertTrue(ViewableContent.suitableForMinorsIsValid("Yes"));
        assertTrue(ViewableContent.suitableForMinorsIsValid("No"));
        assertFalse(ViewableContent.suitableForMinorsIsValid("InvalidRating"));
    }

    @Test
    public void setSynopsis()
    {
        viewableContent.setSynopsis("New Synopsis");
        assertEquals("New Synopsis",viewableContent.getSynopsis());
    }

    @Test
    public void setIsSuitableForMinors()
    {
        viewableContent.setIsSuitableForMinors("No");
        assertEquals("No",viewableContent.getIsSuitableForMinors());
        assertThrows(IllegalArgumentException.class, () -> viewableContent.setIsSuitableForMinors("InvalidRating"));
    }

    @Test
    public void setGenre()
    {
        viewableContent.setGenre("Horror");
        assertEquals("Horror",viewableContent.getGenre());
        assertThrows(IllegalArgumentException.class, () -> viewableContent.setGenre("InvalidGenre"));
    }

    @Test
    public void edit()
    {
        HashSet<Content> relatedContent = new HashSet<>();
        relatedContent.add(new Content("Related Content 1", "Actor 3"));
        viewableContent.edit("New Title", "New Synopsis", "No", "Drama", "Actor 4", relatedContent);

        assertEquals("New Title", viewableContent.getTitle());
        assertEquals("New Synopsis", viewableContent.getSynopsis());
        assertEquals("No", viewableContent.getIsSuitableForMinors());
        assertEquals("Drama", viewableContent.getGenre());
        assertEquals("Actor 4", viewableContent.getPeopleInvolved());
        assertEquals(relatedContent, viewableContent.getRelatedContent());
    }

}