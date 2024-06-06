package api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DataBaseTest
{
    private DataBase dataBase;
    private Content content1, content2;
    private ViewableContent viewableContent1,viewableContent2;
    private Series breakingBad;
    private Movie poorThings,eternalSunshine,talkToMe;
    private Subscriber subscriber1,subscriber2;
    private Admin admin1,admin2;

    @Before
    public void setUp() throws Exception
    {
        dataBase=new DataBase("testContent.dat","testUsers.dat");

        content1 = new Content("Title", "Director",
                new Content("Related Content 1", "Actor 1"),
                new Content("Related Content 2", "Actor 2"));

        subscriber1 = new Subscriber("subscriber1", "password1","Name1","Surname1");
        subscriber2 = new Subscriber("subscriber2", "password2","Name2","Surname2");
        admin1=new Admin("admin1","password1","Name1","Surname1");
        admin2=new Admin("admin2","password2","Name2","Surname2");

        content1.addToReviews(
                new Review(5, "Great movie!", subscriber1),
                new Review(4, "Not bad", subscriber2)
        );

        content2=new Content("Another Title","Director1");
        content1.addToRelatedContent(content2);
        subscriber1.addToFavorites(content2);

        Season breakingSeason1=new Season(1,2008,new Episode(50),new Episode(45),new Episode(55),new Episode(40));
        Season breakingSeason2=new Season(2,2009,new Episode(40),new Episode(50),new Episode(50),new Episode(45));
        Season breakingSeason3=new Season(3,2010,new Episode(55),new Episode(42),new Episode(45),new Episode(52));
        Season breakingSeason4=new Season(4,2011,new Episode(50),new Episode(57),new Episode(43),new Episode(50));
        Season breakingSeason5=new Season(5,2012,new Episode(55),new Episode(46),new Episode(58),new Episode(49));
        ArrayList<Season> breakingBadSeasons=new ArrayList<>();
        breakingBadSeasons.add(breakingSeason1);
        breakingBadSeasons.add(breakingSeason2);
        breakingBadSeasons.add(breakingSeason3);
        breakingBadSeasons.add(breakingSeason4);
        breakingBadSeasons.add(breakingSeason5);
        breakingBad=new Series("Breaking Bad","A chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine with a former student in order to secure his family's future.","Yes","Drama","Bryan Cranston, Aaron Paul, Anna Gunn",breakingBadSeasons);

        talkToMe=new Movie("Talk to Me","When a group of friends discover how to conjure spirits using an embalmed hand, they become hooked on the new thrill, until one of them goes too far and unleashes terrifying supernatural forces.","No","Horror","Ari McCarthy, Hamish Philips, Kit Erhart-Bruce",2022,95);
        poorThings=new Movie("Poor Things","The incredible tale about the fantastical evolution of Bella Baxter; a young woman brought back to life by the brilliant and unorthodox scientist, Dr. Godwin Baxter.","No","Comedy","Emma Stone, Mark Ruffalo, William Dafoe",2023,141);
        eternalSunshine=new Movie("Eternal Sunshine of the Spotless Mind","When their relationship turns sour, a couple undergoes a medical procedure to have each other erased from their memories for ever.","Yes","Drama","Jim Carrey,Kate Winslet,Tom Wilkinson",2004,108);

        viewableContent1=new ViewableContent("Some Title","Test synopsis","yes","Drama","Some people");
        viewableContent2=new ViewableContent("Another Title","Another synopsis","no","horror","Some other people");

        dataBase.addToContent(content1,content2,breakingBad,eternalSunshine,poorThings,viewableContent1,viewableContent2,talkToMe);
        dataBase.addToUsers(subscriber1,subscriber2,admin1,admin2);
    }

    @Test
    public void testAddAndGetContent()
    {
        assertEquals(8,dataBase.getContent().size());

        assertTrue(dataBase.getContent().contains(content1));
        assertTrue(dataBase.getContent().contains(content2));
        assertTrue(dataBase.getContent().contains(viewableContent1));
        assertTrue(dataBase.getContent().contains(viewableContent2));
        assertTrue(dataBase.getContent().contains(poorThings));
        assertTrue(dataBase.getContent().contains(eternalSunshine));
        assertTrue(dataBase.getContent().contains(breakingBad));
        assertTrue(dataBase.getContent().contains(talkToMe));
    }

    @Test
    public void testGetViewableContent()
    {
        assertEquals(6,dataBase.getViewableContent().size());

        assertTrue(dataBase.getViewableContent().contains(viewableContent1));
        assertTrue(dataBase.getViewableContent().contains(viewableContent2));
        assertTrue(dataBase.getViewableContent().contains(talkToMe));
        assertTrue(dataBase.getViewableContent().contains(poorThings));
        assertTrue(dataBase.getViewableContent().contains(eternalSunshine));
        assertTrue(dataBase.getViewableContent().contains(breakingBad));
    }

    @Test
    public void testGetMovies()
    {
        assertEquals(3,dataBase.getMovies().size());

        assertTrue(dataBase.getMovies().contains(poorThings));
        assertTrue(dataBase.getMovies().contains(eternalSunshine));
        assertTrue(dataBase.getMovies().contains(talkToMe));
    }

    @Test
    public void testGetSeries()
    {
        assertEquals(1,dataBase.getSeries().size());

        assertTrue(dataBase.getSeries().contains(breakingBad));
    }

    @Test
    public void testGetUsers()
    {
        assertEquals(4,dataBase.getUsers().size());

        assertTrue(dataBase.getUsers().contains(admin1));
        assertTrue(dataBase.getUsers().contains(admin2));
        assertTrue(dataBase.getUsers().contains(subscriber1));
        assertTrue(dataBase.getUsers().contains(subscriber2));
    }

    @Test
    public void testGetAdmins()
    {
        assertEquals(2,dataBase.getAdmins().size());

        assertTrue(dataBase.getAdmins().contains(admin1));
        assertTrue(dataBase.getAdmins().contains(admin2));
    }

    @Test
    public void testGetAuthentication()
    {
        assertEquals(4,dataBase.getAuthentication().size());

        assertTrue(dataBase.getAuthentication().containsKey("admin1"));
        assertTrue(dataBase.getAuthentication().containsKey("admin2"));
        assertTrue(dataBase.getAuthentication().containsKey("subscriber1"));
        assertTrue(dataBase.getAuthentication().containsKey("subscriber2"));

        assertTrue(dataBase.getAuthentication().containsValue(admin1));
        assertTrue(dataBase.getAuthentication().containsValue(admin2));
        assertTrue(dataBase.getAuthentication().containsValue(subscriber1));
        assertTrue(dataBase.getAuthentication().containsValue(subscriber2));
    }

    @Test
    public void testGetSubscribers()
    {
        assertEquals(2,dataBase.getSubscribers().size());

        assertTrue(dataBase.getSubscribers().contains(subscriber1));
        assertTrue(dataBase.getSubscribers().contains(subscriber2));
    }


    @Test
    public void removeFromContent()
    {
        dataBase.removeFromContent(content2);
        assertFalse(dataBase.getContent().contains(content2));
        assertFalse(content1.getRelatedContent().contains(content2));
        assertFalse(subscriber1.getFavorites().contains(content2));

        dataBase.removeFromContent(poorThings);
        assertFalse(dataBase.getContent().contains(poorThings));
        assertFalse(dataBase.getViewableContent().contains(poorThings));
        assertFalse(dataBase.getMovies().contains(poorThings));
    }

    @Test
    public void removeFromUsers()
    {
        dataBase.removeFromUsers(subscriber1);
        assertFalse(dataBase.getUsers().contains(subscriber1));
        assertFalse(dataBase.getSubscribers().contains(subscriber1));
        assertFalse(dataBase.getAuthentication().containsKey("subscriber1"));
        assertFalse(dataBase.getAuthentication().containsValue(subscriber1));

        assertFalse(content1.getReviews().containsKey(subscriber1));
    }

    @Test
    public void deleteAllMovies()
    {
        dataBase.deleteAllMovies();

        assertTrue(dataBase.getMovies().isEmpty());

        assertFalse(dataBase.getViewableContent().contains(poorThings));
        assertFalse(dataBase.getViewableContent().contains(eternalSunshine));
        assertFalse(dataBase.getViewableContent().contains(talkToMe));

        assertFalse(dataBase.getContent().contains(poorThings));
        assertFalse(dataBase.getContent().contains(eternalSunshine));
        assertFalse(dataBase.getContent().contains(talkToMe));
    }

    @Test
    public void deleteAllSeries()
    {
        dataBase.deleteAllSeries();
        assertTrue(dataBase.getSeries().isEmpty());
        assertFalse(dataBase.getViewableContent().contains(breakingBad));
        assertFalse(dataBase.getContent().contains(breakingBad));
    }

    @Test
    public void deleteAllViewableContent()
    {
        dataBase.deleteAllViewableContent();

        assertTrue(dataBase.getViewableContent().isEmpty());

        assertTrue(dataBase.getMovies().isEmpty());

        assertTrue(dataBase.getSeries().isEmpty());

        assertEquals(2,dataBase.getContent().size());
        assertFalse(dataBase.getContent().contains(viewableContent1));
        assertFalse(dataBase.getContent().contains(viewableContent2));
        assertFalse(dataBase.getContent().contains(poorThings));
        assertFalse(dataBase.getContent().contains(eternalSunshine));
        assertFalse(dataBase.getContent().contains(talkToMe));
        assertFalse(dataBase.getContent().contains(breakingBad));
    }

    @Test
    public void deleteAdmins()
    {
        dataBase.deleteAdmins();
        assertFalse(dataBase.getUsers().contains(admin1));
        assertFalse(dataBase.getUsers().contains(admin2));
        assertTrue(dataBase.getAdmins().isEmpty());
    }

    @Test
    public void deleteSubscribers()
    {
        dataBase.deleteSubscribers();
        assertFalse(dataBase.getUsers().contains(subscriber1));
        assertFalse(dataBase.getUsers().contains(subscriber2));
        assertTrue(dataBase.getSubscribers().isEmpty());
    }

    @After
    public void cleanup()
    {
        try {
            deleteFile("testContent.dat");
        } catch (RuntimeException e) {
            System.out.println("RuntimeException: " + e.getMessage());
        }

        try {
            deleteFile("testUsers.dat");
        } catch (RuntimeException e) {
            System.out.println("RuntimeException: " + e.getMessage());
        }
    }

    private void deleteFile(String filePath) throws RuntimeException
    {
        File file = new File(filePath);
        if (file.exists())
        {
            if (!file.delete())
            {
                throw new RuntimeException("Failed to delete file: " + filePath);
            }
        }
    }
}