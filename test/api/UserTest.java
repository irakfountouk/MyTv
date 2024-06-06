package api;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class UserTest
{

    private User user;
    private HashMap<String, User> authentication;

    @Before
    public void setUp() throws Exception
    {
        user = new User("testUser", "testPassword1", "John", "Doe");
        authentication = new HashMap<>();
        authentication.put(user.getUsername(), user);
    }

    @Test
    public void getUsername()
    {
        assertEquals("testUser",user.getUsername());
    }

    @Test
    public void getName()
    {
        assertEquals("John",user.getName());
    }

    @Test
    public void getSurname()
    {
        assertEquals("Doe",user.getSurname());
    }

    @Test
    public void validatePassword()
    {
        assertTrue(user.validatePassword("testPassword1"));
        assertFalse(user.validatePassword("wrong2Password"));
    }

    @Test
    public void testEquals()
    {
        User sameUser=new User("testUser","testPassword1","John","Doe");
        User differentUser=new User("differentUser","diffPass2","Mike","Colman");

        assertTrue(user.equals(sameUser));
        assertFalse(user.equals(differentUser));
    }

    @Test
    public void setUsername()
    {
        user.setUsername("newUsername",authentication);
        assertEquals("newUsername",user.getUsername());
        assertFalse(authentication.containsKey("testUser"));
        assertEquals(authentication.get("newUsername"),user);
    }

    @Test
    public void setName()
    {
        user.setName("George");
        assertEquals("George",user.getName());
    }

    @Test
    public void setSurname()
    {
        user.setSurname("Smith");
        assertEquals("Smith",user.getSurname());
    }

    @Test
    public void edit()
    {
        user.edit("editedUser", "newPassword1", "Will", "Simons", authentication);
        User editedUser=new User("editedUser", "newPassword1", "Will", "Simons");
        assertEquals(user,editedUser);
        assertFalse(authentication.containsKey("testUser"));
        assertEquals(authentication.get("editedUser"),user);
    }
}