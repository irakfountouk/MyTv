package api;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;

/**
 * The {@code DataBase} class represents a database that manages various types of content
 * (including viewable content, movies, and series) and user information.
 * It provides methods to manipulate and organize content and user data within the database.
 * <p>
 * The database is initialized with content and user information from specified file paths.
 * It utilizes a {@link FileManager} to read and write data to files.
 *
 * @authors Iraklis Fountoukidis, Chrysoula Tegousi
 * @version 2024-01-10
 */
public class DataBase
{
    private HashSet<Content> content;
    private HashSet<ViewableContent> viewableContent;
    private HashSet<Movie> movies;
    private HashSet<Series> series;
    private HashSet<Admin> admins;
    private HashSet<Subscriber> subscribers;
    private HashSet<User> users;
    private HashMap<String,User> authentication;
    private FileManager fileManager;


    /**
     * Constructs a new {@code DataBase} object with specified content and user file paths.
     * Initializes the database with content and user information from files if they exist.
     *
     * @param contentFilePath The file path for content information.
     * @param userFilePath    The file path for user information.
     */
    public DataBase(String contentFilePath,String userFilePath)
    {
        fileManager=new FileManager(contentFilePath,userFilePath);
        content=new HashSet<>();
        viewableContent = new HashSet<>();
        movies=new HashSet<>();
        series=new HashSet<>();
        authentication =new HashMap<>();
        users=new HashSet<>();
        admins=new HashSet<>();
        subscribers=new HashSet<>();


        File contentFile= new File(contentFilePath);
        if(contentFile.exists()) {
            content = fileManager.readContentFile();
            for (Content content1 : content)
            {
                if (content1 instanceof ViewableContent viewableContent)
                {
                    this.viewableContent.add(viewableContent);
                }
                if (content1 instanceof Movie movie)
                {
                    movies.add(movie);
                }
                else if (content1 instanceof Series series)
                {
                    this.series.add(series);
                }
            }
        }

        File userFile=new File(userFilePath);
        if(userFile.exists())
        {
            users = fileManager.readUserFile();
            for (User user : users) {
                authentication.put(user.getUsername(), user);
                if (user instanceof Subscriber subscriber) {
                    subscribers.add(subscriber);
                } else if (user instanceof Admin admin) {
                    admins.add(admin);
                }
            }
        }
    }

    /**
     * Adds content to the database.
     * Updates related data structures and writes to the content file.
     *
     * @param content The content to be added.
     */
    public void addToContent(Content...content)
    {
        Collections.addAll(this.content,content);
        for (Content content1 : content)
        {
            if(content1 instanceof ViewableContent viewableContent)
            {
                this.viewableContent.add(viewableContent);
            }
            if(content1 instanceof Movie movie)
            {
                movies.add(movie);
            }
            if(content1 instanceof Series series)
            {
                this.series.add(series);
            }
        }
        fileManager.writeContentFile(this.content);
    }


    public void addToContent(HashSet<Content> content)
    {
        this.content.addAll(content);
        for (Content content1 : content)
        {
            if(content1 instanceof ViewableContent viewableContent)
            {
                this.viewableContent.add(viewableContent);
            }
            if(content1 instanceof Movie movie)
            {
                movies.add(movie);
            }
            if(content1 instanceof Series series)
            {
                this.series.add(series);
            }
        }
        fileManager.writeContentFile(this.content);
    }


    /** Removes content from the database.
     * Updates related data structures and writes to the content file.
     *
     * @param content The content to be removed.
     */
    public void removeFromContent(Content...content)
    {
        if(this.content.isEmpty() && viewableContent.isEmpty() && movies.isEmpty() && series.isEmpty())
        {
            return;
        }

        for (Content content1 : content)
        {
            this.content.remove(content1);
            if(content1 instanceof ViewableContent viewableContent)
            {
                this.viewableContent.remove(viewableContent);
            }
            if(content1 instanceof Movie movie)
            {
                movies.remove(movie);
            }
            if(content1 instanceof Series series)
            {
                this.series.remove(series);
            }
        }

        for (Content content1 : this.content)
        {
            content1.removeFromRelatedContent(content);
        }

        for (Subscriber subscriber : subscribers)
        {
            subscriber.removeFromFavorites(content);
        }

        fileManager.writeContentFile(this.content);
        fileManager.writeUserFile(users);
    }

    /** Removes content from the database.
     * Updates related data structures and writes to the content file.
     *
     * @param content The content to be removed.
     */
    public void removeFromContent(HashSet<Content> content)
    {
        if(this.content.isEmpty() && viewableContent.isEmpty() && movies.isEmpty() && series.isEmpty())
        {
            return;
        }

        this.content.removeAll(content);

        for (Content content1 : content)
        {
            if(content1 instanceof ViewableContent viewableContent)
            {
                this.viewableContent.remove(viewableContent);
            }
            if(content1 instanceof Movie movie)
            {
                movies.remove(movie);
            }
            if(content1 instanceof Series series)
            {
                this.series.remove(series);
            }
        }

        for (Content content1 : this.content)
        {
            content1.removeFromRelatedContent(content);
        }

        for (Subscriber subscriber : subscribers)
        {
            subscriber.removeFromFavorites(content);
        }
        fileManager.writeContentFile(this.content);
        fileManager.writeUserFile(users);
    }

    /**
     * Edits content and user information in the database.
     * Writes updated content information to the content file.
     */

    public void editFiles()
    {
        fileManager.writeContentFile(content);
        fileManager.writeUserFile(users);
    }

    /**
     * Deletes all content from the database.
     * Clears related data structures and writes to the content file.
     */
    public void deleteAllContent()
    {
        if (content.isEmpty() && viewableContent.isEmpty() && movies.isEmpty() && series.isEmpty())
        {
            return;
        }
        content.clear();
        viewableContent.clear();
        series.clear();
        movies.clear();
        for (Subscriber subscriber : subscribers)
        {
            subscriber.deleteFavorites();
        }
        fileManager.writeContentFile(this.content);
        fileManager.writeUserFile(users);
    }

    /**
     * Deletes all viewable content from the database.
     * Removes viewable content from subscriber favorites and clears related data structures.
     * Writes updated content information to the content file.
     */
    public void deleteAllViewableContent()
    {
        if(content.isEmpty() && viewableContent.isEmpty() && movies.isEmpty() && series.isEmpty())
        {
            return;
        }

        for (ViewableContent viewableContent1 : viewableContent)
        {
            for (Subscriber subscriber : subscribers)
            {
                subscriber.removeFromFavorites(viewableContent1);
            }

            for (Content content1 : content)
            {
                content1.removeFromRelatedContent(viewableContent1);
            }
        }

        content.removeAll(viewableContent);
        viewableContent.clear();
        movies.clear();
        series.clear();
        fileManager.writeContentFile(this.content);
        fileManager.writeUserFile(users);
    }

    /**
     * Deletes all movies from the database.
     * Removes movies from subscriber favorites and clears related data structures.
     * Writes updated content information to the content file.
     */
    public void deleteAllMovies()
    {
        if(content.isEmpty() && viewableContent.isEmpty() && movies.isEmpty())
        {
            return;
        }

        for(Movie movie : movies)
        {
            for (Subscriber subscriber : subscribers)
            {
                subscriber.removeFromFavorites(movie);
            }
            for (Content content1 : content)
            {
                content1.removeFromRelatedContent(movie);
            }
        }

        content.removeAll(movies);
        viewableContent.removeAll(movies);
        movies.clear();
        fileManager.writeContentFile(this.content);
        fileManager.writeUserFile(users);
    }

    /**
     * Deletes all series from the database.
     * Removes series from subscriber favorites and clears related data structures.
     * Writes updated content information to the content file.
     */
    public void deleteAllSeries()
    {
        if(content.isEmpty() && viewableContent.isEmpty() && series.isEmpty())
        {
            return;
        }

        for(Series series1: series)
        {
            for (Subscriber subscriber : subscribers)
            {
                subscriber.removeFromFavorites(series1);
            }

            for (Content content1 : content)
            {
                content1.removeFromRelatedContent(series1);
            }
        }

        content.removeAll(series);
        viewableContent.removeAll(series);
        series.clear();
        fileManager.writeContentFile(this.content);
        fileManager.writeUserFile(users);
    }

    /**
     * Searches for content in the database based on specified search criteria.
     * Utilizes a {@link SearchManager} to perform the search.
     *
     * @param searchCriteria The criteria used for searching content.
     * @return An ArrayList of content matching the search criteria.
     */
    public ArrayList<Content> searchContent(String...searchCriteria)
    {
        SearchManager searchManager=new SearchManager(content);
        ArrayList<Content> results=new ArrayList<>();

        results=searchManager.searchContent(searchCriteria);

        return results;
    }

    /**
     * Adds users to the database.
     * Updates related data structures and writes to the user file.
     *
     * @param users The users to be added.
     */
    public void addToUsers(User...users)
    {
        Collections.addAll(this.users,users);
        for (User user : users)
        {
            authentication.put(user.getUsername(),user);

            if(user instanceof Admin admin)
            {
                admins.add(admin);
            }
            else if(user instanceof Subscriber subscriber)
            {
                subscribers.add(subscriber);
            }
        }
        fileManager.writeUserFile(this.users);
    }

    /**
     * Adds users to the database.
     * Updates related data structures and writes to the user file.
     *
     * @param users The users to be added.
     */
    public void addToUsers(HashSet<User> users)
    {
        this.users.addAll(users);
        for (User user : users)
        {
            authentication.put(user.getUsername(),user);

            if(user instanceof Admin admin)
            {
                admins.add(admin);
            }
            else if(user instanceof Subscriber subscriber)
            {
                subscribers.add(subscriber);
            }
        }
        fileManager.writeUserFile(this.users);
    }

    /**
     * Removes users from the database.
     * Updates related data structures and writes to the user file.
     *
     * @param users The users to be removed.
     */
    public void removeFromUsers(User...users)
    {
        if(this.users.isEmpty() && authentication.isEmpty() && admins.isEmpty() && subscribers.isEmpty())
        {
            return;
        }
        for(User user : users)
        {
            authentication.remove(user.getUsername(),user);
            this.users.remove(user);
            if(user instanceof Admin admin)
            {
                admins.remove(admin);
            }
            else if(user instanceof Subscriber subscriber)
            {
                subscribers.remove(subscriber);
            }
            for (Content content1 : content)
            {
                if(content1.getReviews().containsKey(user))
                {
                    content1.removeFromReviews(content1.getReviews().get(user));
                }
            }
        }
        fileManager.writeUserFile(this.users);
        fileManager.writeContentFile(content);
    }

    /**
     * Removes users from the database.
     * Updates related data structures and writes to the user file.
     *
     * @param users The users to be removed.
     */
    public void removeFromUsers(HashSet<User> users)
    {
        if(this.users.isEmpty() && authentication.isEmpty() && admins.isEmpty() && subscribers.isEmpty())
        {
            return;
        }

        this.users.removeAll(users);
        for(User user : users)
        {
            authentication.remove(user.getUsername(),user);

            if(user instanceof Admin admin)
            {
                admins.remove(admin);
            }
            else if(user instanceof Subscriber subscriber)
            {
                subscribers.remove(subscriber);
                for (Content content1 : content)
                {
                    if(content1.getReviews().containsKey(subscriber))
                    {
                        content1.removeFromReviews(content1.getReviews().get(subscriber));
                    }
                }
            }
        }
        fileManager.writeUserFile(this.users);
        fileManager.writeContentFile(content);
    }

    /**
     * Deletes all users from the database.
     * Clears related data structures and writes to the user file.
     */
    public void deleteUsers()
    {
        if(users.isEmpty() && authentication.isEmpty() && admins.isEmpty() && subscribers.isEmpty())
        {
            return;
        }

        for(User user: users)
        {
            for (Content content1 : content)
            {
                if(content1.getReviews().containsKey(user))
                {
                    content1.removeFromReviews(content1.getReviews().get(user));
                }
            }
        }

        authentication.clear();
        users.clear();
        subscribers.clear();
        admins.clear();
        fileManager.writeUserFile(this.users);
        fileManager.writeContentFile(content);
    }

    /**
     * Deletes all admins from the database.
     * Removes admins from the general user set and authentication map.
     * Writes updated user information to the user file.
     */
    public void deleteAdmins()
    {
        if(users.isEmpty() && authentication.isEmpty() && admins.isEmpty())
        {
            return;
        }

        users.removeAll(admins);
        for (Admin admin : admins)
        {
            authentication.remove(admin.getUsername(),admin);

            for (Content content1 : content)
            {
                if(content1.getReviews().containsKey(admin))
                {
                    content1.removeFromReviews(content1.getReviews().get(admin));
                }
            }
        }

        admins.clear();
        fileManager.writeUserFile(this.users);
        fileManager.writeContentFile(content);
    }

    /**
     * Deletes all subscribers from the database.
     * Removes subscribers from the general user set and authentication map.
     * Clears subscriber reviews from content.
     * Writes updated user information to the user file.
     */
    public void deleteSubscribers()
    {
        if(users.isEmpty() && authentication.isEmpty() && subscribers.isEmpty())
        {
            return;
        }

        for(Subscriber subscriber : subscribers)
        {
            authentication.remove(subscriber.getUsername(),subscriber);

            for (Content content1 : content)
            {
                if(!content1.getReviews().isEmpty())
                {
                    content1.getReviews().clear();
                    content1.getReviews().clear();
                }
            }
        }
        users.removeAll(subscribers);
        subscribers.clear();
        fileManager.writeUserFile(this.users);
        fileManager.writeContentFile(content);
    }


    /**
     * Gets the set of all content in the database.
     *
     * @return The set of all content.
     */
    public HashSet<Content> getContent()
    {
        return content;
    }

    /**
     * Gets the set of viewable content in the database.
     *
     * @return The set of viewable content.
     */
    public HashSet<ViewableContent> getViewableContent()
    {
        return viewableContent;
    }

    /**
     * Gets the set of movies in the database.
     *
     * @return The set of movies.
     */
    public HashSet<Movie> getMovies()
    {
        return movies;
    }

    /**
     * Gets the set of series in the database.
     *
     * @return The set of series.
     */
    public HashSet<Series> getSeries()
    {
        return series;
    }

    /**
     * Gets the set of users in the database.
     *
     * @return The set of users.
     */
    public HashSet<User> getUsers()
    {
        return users;
    }

    /**
     * Gets the set of admins in the database.
     *
     * @return The set of admins.
     */
    public HashSet<Admin> getAdmins()
    {
        return admins;
    }

    /**
     * Gets the authentication map in the database.
     *
     * @return The authentication map.
     */
    public HashMap<String,User> getAuthentication()
    {
        return authentication;
    }

    /**
     * Gets the set of subscribers in the database.
     *
     * @return The set of subscribers.
     */
    public HashSet<Subscriber> getSubscribers()
    {
        return subscribers;
    }
}
