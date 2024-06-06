package api;


import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class ContentTest
{
    private Content content;

    @Before
    public void setUp() throws Exception
    {
        content = new Content("Title", "Director",
                new Content("Related Content 1", "Actor 1"),
                new Content("Related Content 2", "Actor 2"));
        User reviewer1 = new User("user1", "password1","Name1","Surname1");
        User reviewer2 = new User("user2", "password2","Name2","Surname2");

        content.addToReviews(
                new Review(5, "Great movie!", reviewer1),
                new Review(4, "Not bad", reviewer2)
        );
    }

    @Test
    public void getTitle()
    {
        assertEquals("Title", content.getTitle());
    }

    @Test
    public void getPeopleInvolved()
    {
        assertEquals("Director", content.getPeopleInvolved());
    }

    @Test
    public void getRelatedContent()
    {
        HashSet<Content> relatedContent=new HashSet<>();
        relatedContent.add(new Content("Related Content 1", "Actor 1"));
        relatedContent.add(new Content("Related Content 2", "Actor 2"));
        assertTrue(content.getRelatedContent().containsAll(relatedContent));
        assertTrue(relatedContent.containsAll(content.getRelatedContent()));
    }

    @Test
    public void getReviews()
    {
        HashMap<User,Review> reviewers= content.getReviews();
        assertEquals(2, reviewers.size());
        User reviewer1 = new User("user1", "password1","Name1","Surname1");
        User reviewer2 = new User("user2", "password2","Name2","Surname2");
        Review review1=new Review(5, "Great movie!", reviewer1);
        Review review2=new Review(4, "Not bad", reviewer2);
        assertTrue(content.getReviews().containsKey(reviewer1));
        assertTrue(content.getReviews().containsKey(reviewer2));
        assertTrue(content.getReviews().containsValue(review1));
        assertTrue(content.getReviews().containsValue(review2));
    }


    @Test
    public void getAverageRating()
    {
        assertEquals(4.5, content.getAverageRating(), 0.01);
    }

    @Test
    public void getFormattedAverageRating()
    {
        assertEquals("4.5", content.getFormattedAverageRating());
    }

    @Test
    public void  equals()
    {
        Content sameContent = new Content("Title", "Director",
                new Content("Related Content 1", "Actor 1"),
                new Content("Related Content 2", "Actor 2"));

        Content differentContent=new Content("Another Title","Director1");
        assertEquals(content, sameContent);
        assertNotEquals(content, differentContent);
    }


    @Test
    public void testHashCode()
    {
        Content sameContent = new Content("Title", "Director",
                new Content("Related Content 1", "Actor 1"),
                new Content("Related Content 2", "Actor 2"));

        assertEquals(content.hashCode(), sameContent.hashCode());

        Content differentContent=new Content("Another Title","Director1");
        assertNotEquals(content.hashCode(),differentContent.hashCode());
    }

    @Test
    public void setTitle()
    {
        content.setTitle("New Title");
        assertEquals("New Title",content.getTitle());
    }

    @Test
    public void setPeopleInvolved()
    {
        content.setPeopleInvolved("New People");
        assertEquals("New People",content.getPeopleInvolved());
    }


    @Test
    public void addToReviews()
    {
        User reviewer3=new User("user3","password3","Name3","Surname3");
        Review review=new Review(3,"Meh meh",reviewer3);
        content.addToReviews(review);
        assertTrue(content.getReviews().containsKey(reviewer3));
        assertTrue(content.getReviews().containsValue(review));
    }

    @Test
    public void removeFromReviews()
    {
        User reviewer2 = new User("user2", "password2","Name2","Surname2");
        Review review=new Review(4, "Not bad", reviewer2);
        content.removeFromReviews(review);
        assertFalse(content.getReviews().containsKey(reviewer2));
        assertFalse(content.getReviews().containsValue(review));
    }


    @Test
    public void addToRelatedContent()
    {
        Content newRelatedContent = new Content("New Related Content", "Actor 3");
        content.addToRelatedContent(newRelatedContent);
        assertTrue(content.getRelatedContent().contains(newRelatedContent));
    }


    @Test
    public void removeFromRelatedContent()
    {
        Content content1=new Content("Related Content 1", "Actor 1");
        content.removeFromRelatedContent(content1);
        assertFalse(content.getRelatedContent().contains(content1));
    }



    @Test
    public void edit()
    {
        HashSet<Content> newRelatedContent=new HashSet<>();
        newRelatedContent.add(new Content("Title 1","Person1"));
        newRelatedContent.add(new Content("Title2","Person2"));
        content.edit("Edited Title", "New Director",newRelatedContent);
        assertEquals("Edited Title", content.getTitle());
        assertEquals("New Director", content.getPeopleInvolved());
        assertTrue(content.getRelatedContent().containsAll(newRelatedContent));
        assertTrue(newRelatedContent.containsAll(content.getRelatedContent()));
    }

}