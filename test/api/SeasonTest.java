package api;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SeasonTest
{
    private Season season;

    @Before
    public void setUp() throws Exception
    {
        season=new Season(1,2009,new Episode(40),new Episode(42),new Episode(39));
    }

    @Test
    public void getEpisodes()
    {
        ArrayList<Episode> episodes=new ArrayList<>();
        episodes.add(new Episode(40));
        episodes.add(new Episode(42));
        episodes.add(new Episode(39));

        assertTrue(season.getEpisodes().containsAll(episodes));
        assertTrue(episodes.containsAll(season.getEpisodes()));
    }

    @Test
    public void getYearOfRelease()
    {
        assertEquals(2009,season.getYearOfRelease());
    }

    @Test
    public void getSeasonNumber()
    {
        assertEquals(1,season.getSeasonNumber());
    }

    @Test
    public void testEquals()
    {
        Season sameSeason=new Season(1,2009,new Episode(40),new Episode(42),new Episode(39));
        assertEquals(season, sameSeason);

        Season differentSeason=new Season(2,2009,new Episode(40),new Episode(43));
        assertNotEquals(season, differentSeason);
    }


    @Test
    public void testHashCode()
    {
        Season sameSeason=new Season(1,2009,new Episode(40),new Episode(42),new Episode(39));
        assertEquals(season.hashCode(), sameSeason.hashCode());

        Season differentSeason=new Season(2,2009,new Episode(40),new Episode(43));
        assertNotEquals(season.hashCode(), differentSeason.hashCode());
    }

    @Test
    public void setYearOfRelease()
    {
        season.setYearOfRelease(2004);
        assertEquals(2004,season.getYearOfRelease());
        assertThrows(IllegalArgumentException.class, () -> season.setYearOfRelease(-2));
        assertThrows(IllegalArgumentException.class, () -> season.setYearOfRelease(145));
    }

    @Test
    public void setSeasonNumber()
    {
        season.setSeasonNumber(3);
        assertEquals(3,season.getSeasonNumber());
        assertThrows(IllegalArgumentException.class, () -> season.setYearOfRelease(-2));
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
    public void seasonNumberIsValid()
    {
        assertTrue(Season.seasonNumberIsValid("8"));
        assertTrue(Season.seasonNumberIsValid(8));

        assertFalse(Season.seasonNumberIsValid("7s"));
        assertFalse(Season.seasonNumberIsValid(-1));
    }

    @Test
    public void addToEpisodes()
    {
        Episode episode=new Episode(54);
        season.addToEpisodes(episode);
        assertTrue(season.getEpisodes().contains(episode));
    }

    @Test
    public void removeFromEpisodes()
    {
        Episode episode=new Episode(40);
        season.removeFromEpisodes(episode);
        assertFalse(season.getEpisodes().contains(episode));
    }

    @Test
    public void edit()
    {
        ArrayList<Episode> editedEpisodes=new ArrayList<>();
        editedEpisodes.add(new Episode(32));
        editedEpisodes.add(new Episode(41));

        season.edit(10,2013,editedEpisodes);
        assertEquals(10,season.getSeasonNumber());
        assertEquals(2013,season.getYearOfRelease());
        assertTrue(season.getEpisodes().containsAll(editedEpisodes));
        assertTrue(editedEpisodes.containsAll(season.getEpisodes()));
    }
}