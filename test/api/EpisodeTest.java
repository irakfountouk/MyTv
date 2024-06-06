package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EpisodeTest
{
    private Episode episode;

    @Before
    public void setUp() throws Exception
    {
        episode=new Episode(45);
    }

    @Test
    public void getDuration()
    {
        assertEquals(45,episode.getDuration());
    }

    @Test
    public void testEquals()
    {
        Episode sameEpisode=new Episode(45);
        Episode differentEpisode=new Episode(50);

        assertEquals(episode,sameEpisode);
        assertNotEquals(episode,differentEpisode);
    }

    @Test
    public void testHashCode()
    {
        Episode sameEpisode=new Episode(45);
        Episode differentEpisode=new Episode(50);

        assertEquals(episode.hashCode(),sameEpisode.hashCode());
        assertNotEquals(episode.hashCode(),differentEpisode.hashCode());
    }

    @Test
    public void setDuration()
    {
        episode.setDuration(40);
        assertEquals(40,episode.getDuration());
        assertThrows(IllegalArgumentException.class, () -> episode.setDuration(-3));
    }

    @Test
    public void durationIsValid()
    {
        assertTrue(Episode.durationIsValid("40"));
        assertTrue(Episode.durationIsValid(40));

        assertFalse(Episode.durationIsValid("40a"));
        assertFalse(Episode.durationIsValid(-1));
    }

    @Test
    public void edit()
    {
        episode.edit(35);
        assertEquals(35,episode.getDuration());
    }
}