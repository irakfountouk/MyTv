package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubscriberTest
{
    private Subscriber subscriber;
    private Content favorite;

    @Before
    public void setUp() throws Exception
    {
        subscriber=new Subscriber("testSubscriber","testPass123","Will","Coleman");
        favorite=new Content("Favorite1","Member");
        subscriber.addToFavorites(favorite);
    }

    @Test
    public void getFavorites()
    {
        assertTrue(subscriber.getFavorites().contains(favorite) && subscriber.getFavorites().size()==1);
    }

    @Test
    public void addToFavorites()
    {
        Content content=new Content("Title","Member1");
        subscriber.addToFavorites(content);
        assertTrue(subscriber.getFavorites().contains(content));
    }


    @Test
    public void removeFromFavorites()
    {
        subscriber.removeFromFavorites(favorite);
        assertFalse(subscriber.getFavorites().contains(favorite));
    }

    @Test
    public void deleteFavorites()
    {
        subscriber.deleteFavorites();
        assertTrue(subscriber.getFavorites().isEmpty());
    }
}