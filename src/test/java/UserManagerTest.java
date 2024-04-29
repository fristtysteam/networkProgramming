import business.User;
import business.UserManager;
import junit.framework.TestCase;

public class UserManagerTest extends TestCase {
    private UserManager userManager;

    public void setUp() throws Exception {
        super.setUp();
        userManager = new UserManager();
    }

    public void tearDown() throws Exception {
        userManager = null;
        super.tearDown();
    }

    public void testAddUser() {
        assertTrue(userManager.addUser("testUser", "testPassword"));
        assertNotNull(userManager.searchByUsername("testUser"));
    }

    public void testSearchByUsername() {
        User user = new User("existingUser", "password");
        userManager.addUser(user.getUsername(), user.getPassword());
        assertEquals(user, userManager.searchByUsername("existingUser"));
    }
}
