package api;

import java.io.*;
import java.util.HashSet;

/**
 * Handles reading from and writing to files for Content and User objects.
 *  @authors Iraklis Fountoukidis, Chrysoula Tegousi
 *  @version 2024-01-10
 */
public class FileManager
{
    private  String contentFilePath, usersFilePath;

    /**
     * Constructs a FileManager object with the provided file paths for content and users.
     *
     * @param contentFilePath the file path for storing content objects
     * @param usersFilePath   the file path for storing user objects
     */
    public FileManager(String contentFilePath,String usersFilePath)
    {
        this.contentFilePath=contentFilePath;
        this.usersFilePath = usersFilePath;
    }


    /**
     * Reads content objects from the content file.
     *
     * @return a HashSet containing Content objects read from the file
     */
    public HashSet<Content> readContentFile()
    {
        HashSet<Content> contentObjects = new HashSet<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(contentFilePath)))
        {
            while (true)
            {
                try
                {
                    Content obj = (Content) ois.readObject();
                    contentObjects.add(obj);
                }
                catch (EOFException e)
                {
                    break;
                }
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return contentObjects;
    }

    /**
     * Reads user objects from the user file.
     *
     * @return a HashSet containing User objects read from the file
     */
    public HashSet<User> readUserFile()
    {
        HashSet<User> objects = new HashSet<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usersFilePath)))
        {
            while (true)
            {
                try {
                    User obj = (User) ois.readObject();
                    objects.add(obj);
                }
                catch (EOFException e)
                {
                    break;
                }
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return objects;
    }


    /**
     * Writes content objects to the content file.
     *
     * @param objects a HashSet containing Content objects to be written to the file
     */
    public void writeContentFile(HashSet<Content> objects)
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(contentFilePath)))
        {
            for (Content obj : objects)
            {
                oos.writeObject(obj);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Writes user objects to the user file.
     *
     * @param objects a HashSet containing User objects to be written to the file
     */
    public void writeUserFile(HashSet<User> objects)
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersFilePath))) {
            for (User obj : objects) {
                oos.writeObject(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
