package api;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SeriesTest
{

    private Series series;

    @Before
    public void setUp() throws Exception
    {
        Season season1 = new Season(1, 2008,new Episode(40),new Episode(37),new Episode(38));
        Season season2 = new Season(2, 2009,new Episode(48),new Episode(53),new Episode(40));

        ArrayList<Season> seasons=new ArrayList<>();
        seasons.add(season1);
        seasons.add(season2);
        series = new Series("Series Title", "Series Synopsis",
                "Yes", "Drama", "Actor 1, Actor 2",seasons);
    }

    @Test
    public void getSeasons()
    {
        Season season1 = new Season(1, 2008,new Episode(40),new Episode(37),new Episode(38));
        Season season2 = new Season(2, 2009,new Episode(48),new Episode(53),new Episode(40));

        ArrayList<Season> seasons=new ArrayList<>();
        seasons.add(season1);
        seasons.add(season2);

        assertTrue(series.getSeasons().containsAll(seasons));
        assertTrue(seasons.containsAll(series.getSeasons()));
    }

    @Test
    public void testEquals()
    {
        Season season1 = new Season(1, 2008,new Episode(40),new Episode(37),new Episode(38));
        Season season2 = new Season(2, 2009,new Episode(48),new Episode(53),new Episode(40));

        ArrayList<Season> seasons=new ArrayList<>();
        seasons.add(season1);
        seasons.add(season2);

        Series sameSeries = new Series("Series Title", "Series Synopsis",
                "Yes", "Drama", "Actor 1, Actor 2", seasons);
        assertEquals(series, sameSeries);

        Series differentSeries = new Series("Different Title", "Different Synopsis",
                "No", "Action", "Actor 5, Actor 6",
                seasons);
        assertNotEquals(series, differentSeries);
    }

    @Test
    public void testHashCode()
    {
        Season season1 = new Season(1, 2008,new Episode(40),new Episode(37),new Episode(38));
        Season season2 = new Season(2, 2009,new Episode(48),new Episode(53),new Episode(40));

        ArrayList<Season> seasons=new ArrayList<>();
        seasons.add(season1);
        seasons.add(season2);

        Series sameSeries = new Series("Series Title", "Series Synopsis",
                "Yes", "Drama", "Actor 1, Actor 2",
                seasons);
        assertEquals(series.hashCode(), sameSeries.hashCode());

        Series differentSeries = new Series("Different Title", "Different Synopsis",
                "No", "Action", "Actor 5, Actor 6",
                seasons);

        assertNotEquals(series.hashCode(),differentSeries.hashCode());
    }

    @Test
    public void sortSeasons()
    {
        Season season1 = new Season(1, 2008,new Episode(40),new Episode(37),new Episode(38));
        Season season2 = new Season(2, 2009,new Episode(48),new Episode(53),new Episode(40));


        assertEquals(season1, series.getSeasons().get(0));
        assertEquals(season2, series.getSeasons().get(1));

        Season season3=new Season(3,2010,new Episode(36),new Episode(29),new Episode(33));
        Season season4=new Season(4,2011,new Episode(51),new Episode(55),new Episode(57));
        Season season5=new Season(5,2012,new Episode(35),new Episode(42),new Episode(46));
        series.addToSeasons(season5);
        series.addToSeasons(season3);
        series.addToSeasons(season4);
        series.sortSeasons();
        assertEquals(season3, series.getSeasons().get(2));
        assertEquals(season4,series.getSeasons().get(3));
        assertEquals(season5,series.getSeasons().get(4));
    }

    @Test
    public void addToSeasons()
    {
        Season season6=new Season(6,2013,new Episode(36),new Episode(29),new Episode(33));
        series.addToSeasons(season6);
        assertTrue(series.getSeasons().contains(season6));
    }


    @Test
    public void removeFromSeasons()
    {
        Season season2 = new Season(2, 2009,new Episode(48),new Episode(53),new Episode(40));
        series.removeFromSeasons(season2);
        assertFalse(series.getSeasons().contains(season2));
    }

    @Test
    public void edit()
    {
        Season newSeason1 = new Season(1,2004,new Episode(52),new Episode(41),new Episode(47));
        Season newSeason2 = new Season(2, 2005,new Episode(44),new Episode(43),new Episode(37));
        Season newSeason3=new Season(3,2006,new Episode(32),new Episode(46),new Episode(41));
        Season newSeason4=new Season(4,2007,new Episode(38),new Episode(39),new Episode(43));

        ArrayList<Season> newSeasons=new ArrayList<>();
        newSeasons.add(newSeason1);
        newSeasons.add(newSeason2);
        newSeasons.add(newSeason3);
        newSeasons.add(newSeason4);
        series.edit("New Title", "New Synopsis", "No", "Comedy", "Actor 3, Actor 4", newSeasons);
        assertEquals("New Title", series.getTitle());
        assertEquals("New Synopsis", series.getSynopsis());
        assertEquals("No", series.getIsSuitableForMinors());
        assertEquals("Comedy", series.getGenre());
        assertEquals("Actor 3, Actor 4", series.getPeopleInvolved());
        assertTrue(series.getSeasons().containsAll(newSeasons));
        assertTrue(newSeasons.containsAll(series.getSeasons()));
    }
}