package api;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

public class SearchManagerTest
{
    private HashSet<Content> content;
    private SearchManager searchManager;

    @Before
    public void setUp() throws Exception
    {
        content = new HashSet<>();
        Season season1 = new Season(1, 2008,new Episode(40),new Episode(37),new Episode(38));
        Season season2 = new Season(2, 2009,new Episode(48),new Episode(53),new Episode(40));

        ArrayList<Season> seasons=new ArrayList<>();
        seasons.add(season1);
        seasons.add(season2);
        Movie movie = new Movie("Movie1", "Synopsis1", "Yes", "Action", "Daniel Day-Lewis", 2022, 120);
        Series series = new Series("Series1", "Synopsis2", "No", "Drama", "Viola Davis", seasons);
        content.add(movie);
        content.add(series);
        searchManager = new SearchManager(content);
    }

    @Test
    public void searchContent()
    {
        ArrayList<Content> results = searchManager.searchContent("genre", "Action");
        assertEquals(1, results.size());
        assertEquals("Movie1", results.get(0).getTitle());

        results = searchManager.searchContent("minrating", "4");
        assertEquals(0, results.size());  // No reviews added yet, so no content should match


        results = searchManager.searchContent("Series1");
        assertEquals(1, results.size());
        assertEquals("Series1", results.get(0).getTitle());
    }

    @Test
    public void matchedCriteriaInSearchBar()
    {
        int fieldsMatched=0;

        for (Content content1 : content)
        {
            fieldsMatched=searchManager.matchedCriteriaInSearchBar(content1,"Movie1");
            if(content1.getTitle()=="Movie1")
            {
                assertEquals(1,fieldsMatched);
            }
        }

        for (Content content1 : content)
        {
            fieldsMatched=searchManager.matchedCriteriaInSearchBar(content1,"Viola Davis");
            if(content1.getPeopleInvolved().contains("Viola Davis"))
            {
                assertEquals(1,fieldsMatched);
            }
        }
    }

    @Test
    public void matchesCriteriaInFilters()
    {
        Movie movie = new Movie("Movie1", "Synopsis1", "Yes", "Action", "Daniel Day-Lewis", 2022, 120);

        boolean criterionMatched = searchManager.matchesCriteriaInFilters(movie, "category", "movies");
        assertTrue(criterionMatched);

        criterionMatched = searchManager.matchesCriteriaInFilters(movie, "genre", "Drama");
        assertFalse(criterionMatched);

        criterionMatched=searchManager.matchesCriteriaInFilters(movie,"suitableforminors","no");
        assertFalse(criterionMatched);
    }
}