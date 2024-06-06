package api;


import java.util.*;

/**
 * The {@code SearchManager} class provides methods for searching and filtering content based on various criteria.
 * It supports searching by title, people involved, category, genre, suitability for minors, and minimum rating.
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 24-01-10
 */
public class SearchManager
{
    /**
     * The set of content to be searched.
     */
    private final HashSet<Content> content;

    /**
     * Creates a new {@code SearchManager} with the specified content set.
     *
     * @param content The set of content to be searched.
     */
    public SearchManager(HashSet<Content> content)
    {
        this.content=content;
    }

    /**
     * Represents a pair of content and the number of matched search criteria. Used for sorting search results.
     */
    static class CriteriaMatched implements Comparable<CriteriaMatched>
    {
        private final Content content;
        private int matchedCriteria;

        /**
         * Constructs a {@code CriteriaMatched} object for the specified content.
         *
         * @param content The content for which the criteria are matched.
         */
        public CriteriaMatched(Content content)
        {
            this.content=content;
            matchedCriteria=0;
        }

        /**
         * Increases the number of matched criteria for the content.
         *
         * @param matchedCriteria The number of criteria matched.
         */
        public void increaseMatchedCriteria(int matchedCriteria)
        {
            this.matchedCriteria+=matchedCriteria;
        }

        /**
         * Gets the number of matched criteria for the content.
         *
         * @return The number of matched criteria.
         */
        public int getMatchedCriteria()
        {
            return matchedCriteria;
        }

        /**
         * Gets the content for which the criteria are matched.
         *
         * @return The matched content.
         */
        public Content getContent()
        {
            return content;
        }


        /**
         * Compares two {@code CriteriaMatched} objects based on the number of matched criteria.
         *
         * @param other The other {@code CriteriaMatched} object to compare.
         * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
         */
        @Override
        public int compareTo(CriteriaMatched other)
        {
            return Integer.compare(this.matchedCriteria, other.getMatchedCriteria());
        }
    }

    /**
     * Searches and filters content based on the specified search criteria.
     *
     * @param searchCriteria The search criteria to apply.
     * @return An {@code ArrayList} of content that matches the search criteria.
     */
    public ArrayList<Content> searchContent(String...searchCriteria)
    {
        ArrayList<Content> results=new ArrayList<>();
        ArrayList<CriteriaMatched> criteriaMatchedArrayList=new ArrayList<>();

        if(searchCriteria.length==0)
        {
            results.addAll(content);
            return results;
        }

        else if(searchCriteria.length%2==0)
        {
            for (Content content1 : content)
            {
                if(matchesCriteriaInFilters(content1,searchCriteria))
                {
                    CriteriaMatched criteriaMatched= new CriteriaMatched(content1);
                    criteriaMatched.increaseMatchedCriteria(1);
                    criteriaMatchedArrayList.add(criteriaMatched);
                }
            }
        }
        else if(searchCriteria.length==1)
        {
            String criterion=searchCriteria[0];
            for (Content content1 : content)
            {
                int fieldsMatched=matchedCriteriaInSearchBar(content1,criterion);
                if(fieldsMatched>0)
                {
                    CriteriaMatched criteriaMatched= new CriteriaMatched(content1);
                    criteriaMatched.increaseMatchedCriteria(fieldsMatched);
                    criteriaMatchedArrayList.add(criteriaMatched);
                }
            }
        }
        else
        {
            String lastCriterion=searchCriteria[searchCriteria.length-1];
            for (Content content1 : content)
            {
                if(matchesCriteriaInFilters(content1,searchCriteria))
                {
                    int fieldsMatched=1;
                    fieldsMatched+=matchedCriteriaInSearchBar(content1,lastCriterion);
                    if(fieldsMatched>1)
                    {
                        CriteriaMatched criteriaMatched= new CriteriaMatched(content1);
                        criteriaMatched.increaseMatchedCriteria(fieldsMatched);
                        criteriaMatchedArrayList.add(criteriaMatched);
                    }
                }
            }
        }

        criteriaMatchedArrayList.sort(Comparator.reverseOrder());

        if(!criteriaMatchedArrayList.isEmpty())
        {
            for (CriteriaMatched criteriaMatched : criteriaMatchedArrayList)
            {
                if(!results.contains(criteriaMatched.getContent()))
                {results.add(criteriaMatched.getContent());}
            }
        }

        return results;
    }


    /**
     * Calculates the number of matched criteria for a specific content in the search bar.
     *
     * @param content  The content to check for matches.
     * @param searchCriterion The search criterion to match against.
     * @return The number of fields matched.
     */
    public int matchedCriteriaInSearchBar(Content content, String searchCriterion)
    {
        int fieldsMatched=0;
        String contentTitle=content.getTitle().toLowerCase().replaceAll("\\s", "");
        String contentPeopleInvolved=content.getPeopleInvolved().toLowerCase().replaceAll("\\s", "");
        String contentCategory=content.getClass().toString().toLowerCase().replaceAll("\\s", "");

        String userSearch=searchCriterion.toLowerCase().replaceAll("\\s", "");
        if(userSearch.isEmpty())
        {
            fieldsMatched++;
        }
        if(contentTitle.contains(userSearch) || userSearch.contains(contentTitle))
        {
            fieldsMatched++;
        }
        if(contentPeopleInvolved.contains(userSearch) || userSearch.contains(contentPeopleInvolved))
        {
            fieldsMatched++;
        }
        if(contentCategory.contains(userSearch) || userSearch.contains(contentCategory))
        {
            fieldsMatched++;
        }
        if(content instanceof ViewableContent viewableContent)
        {
            String contentGenre=viewableContent.getGenre().toLowerCase().replaceAll("\\s", "");
            String contentSuitability=viewableContent.getIsSuitableForMinors().toLowerCase();

            if(contentGenre.contains(userSearch) || userSearch.contains(contentGenre))
            {
                fieldsMatched++;
            }
            else if(userSearch.contains("notsuitable") || userSearch.contains("nosuitable") || userSearch.contains(">18")
                    || userSearch.contains(">=18") || userSearch.contains("unsuitable"))
            {
                if(contentGenre.equals("no"))
                {
                    fieldsMatched++;
                }
            }
            else if(userSearch.contains("suitable") || userSearch.contains("<18"))
            {
                if(contentSuitability.equals("yes"))
                {
                    fieldsMatched++;
                }
            }
        }
        return fieldsMatched;
    }

    /**
     * Checks if the given content matches specified criteria in filters.
     *
     * @param content the content to check against the criteria.
     * @param searchCriteria The search criteria to apply for filtering.
     * Each pair of consecutive elements represents a criterion and its corresponding user-chosen filter.
     * Supported criteria include "category," "minrating," "genre," and "suitableforminors."
     * @return {@code true} if the content matches all specified criteria, {@code false} otherwise.
     */
    public boolean matchesCriteriaInFilters(Content content, String...searchCriteria)
    {

        boolean criterionMatched=true;

        if(content==null)
        {
            return false;
        }

        ViewableContent viewableContent;

        String contentCategory;
        if(content instanceof Movie)
        {
            contentCategory="movies";
        } else if (content instanceof Series)
        {
          contentCategory="series";
        } else if (content instanceof ViewableContent)
        {
            contentCategory="viewablecontent";
        }
        else
        {
            contentCategory="content";
        }

        String contentGenre=null,contentSuitability=null;

        if(content instanceof ViewableContent)
        {
            viewableContent=(ViewableContent) content;
            contentGenre=viewableContent.getGenre().toLowerCase().replaceAll("\\s", "");
            contentSuitability=viewableContent.getIsSuitableForMinors().toLowerCase().replaceAll("\\s", "");
        }

        for(int i=0;i<searchCriteria.length-1;i+=2)
        {
            String criterion=searchCriteria[i].toLowerCase().replaceAll("\\s", "");
            String userChosenFilter=searchCriteria[i+1].toLowerCase().replaceAll("\\s", "");

            if(userChosenFilter.isEmpty())
            {
                continue;
            }

            if(criterion.equals("category"))
            {
                if(userChosenFilter.equals("content") && contentCategory.equals("content"))
                {
                    return true;
                }
            }

            switch (criterion)
            {
                case "category":
                    criterionMatched=contentCategory.contains(userChosenFilter) || userChosenFilter.contains(contentCategory);
                    break;
                case "minrating":
                    assert content != null;
                    if(content.getReviews().isEmpty())
                    {
                        criterionMatched=false;
                    }
                    else {
                        criterionMatched = content.getAverageRating() >= Double.parseDouble(userChosenFilter);
                    }
                    break;
                case "genre":
                    if (contentGenre != null) {
                        criterionMatched=contentGenre.contains(userChosenFilter) || userChosenFilter.contains(contentGenre);
                    }
                    break;
                case "suitableforminors":
                    if(contentSuitability!=null)
                    {
                        if (userChosenFilter.contains("notsuitable") || userChosenFilter.contains("nosuitable")
                                || userChosenFilter.contains("unsuitable") || userChosenFilter.contains(">18") ||
                                userChosenFilter.contains(">=18")) {

                            criterionMatched = contentSuitability.equals("no");

                        }
                        else if (userChosenFilter.contains("suitable") || userChosenFilter.contains("<18")) {
                            criterionMatched = contentSuitability.equals("yes");
                        }
                        else {
                            criterionMatched = contentSuitability.contains(userChosenFilter) || userChosenFilter.contains(contentSuitability);
                        }
                    }
                    break;
            }

            if(!criterionMatched)
            {
                return false;
            }
        }
        return true;
    }
}
