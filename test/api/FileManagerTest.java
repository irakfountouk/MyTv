package api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashSet;

import static org.junit.Assert.*;

public class FileManagerTest
{

    private FileManager fileManager;
    private String contentFilePath="testContent.dat",usersFilePath="testUsers.dat";

    @Before
    public void setUp() throws Exception
    {
        fileManager=new FileManager(contentFilePath,usersFilePath);
    }

    @Test
    public void readAndWriteContentFile()
    {
        HashSet<Content> testContent = new HashSet<>();

        Movie poorThings=new Movie("Poor Things","The incredible tale about the fantastical evolution of Bella Baxter; a young woman brought back to life by the brilliant and unorthodox scientist, Dr. Godwin Baxter.","No","Comedy","Emma Stone, Mark Ruffalo, William Dafoe",2023,141);
        Movie eternalSunshine=new Movie("Eternal Sunshine of the Spotless Mind","When their relationship turns sour, a couple undergoes a medical procedure to have each other erased from their memories for ever.","Yes","Drama","Jim Carrey,Kate Winslet,Tom Wilkinson",2004,108);

        testContent.add(poorThings);
        testContent.add(eternalSunshine);

        fileManager.writeContentFile(testContent);

        HashSet<Content> readContent = fileManager.readContentFile();

        assertEquals(testContent, readContent);
    }

    @Test
    public void readAndWriteUserFile()
    {
        HashSet<User> testUsers = new HashSet<>();
        testUsers.add(new Subscriber("subscriber1", "password1", "John", "Doe"));

        fileManager.writeUserFile(testUsers);

        HashSet<User> readUsers = fileManager.readUserFile();

        assertEquals(testUsers, readUsers);

    }

    @After
    public void cleanup()
    {
        try {
            deleteFile(contentFilePath);
        } catch (RuntimeException e) {
            System.out.println("RuntimeException: " + e.getMessage());
        }

        try {
            deleteFile(usersFilePath);
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