package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdminTest
{

    private Admin admin;

    @Before
    public void setUp() throws Exception
    {
        admin=new Admin("testAdmin","testPass123","Cole","Smith");
    }
    @Test
    public void testEquals()
    {
        Subscriber subscriber=new Subscriber("testSub","testPass123","Cole","Smith");
        assertFalse(admin.equals(subscriber));
    }
}