import business.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {
    private UserManager userManager;

    @BeforeEach
    public void setUp() {
        userManager = new UserManager();
    }

    @Test
    public void testAddUser() {
        assertTrue(userManager.addUser("john_doe", "password"));
        assertFalse(userManager.addUser("max", "pass1")); // Trying to add an existing user
    }

    @Test
    public void testSearchByUsername() {
        assertNull(userManager.searchByUsername("nonexistent_user")); // Searching for a non-existent user
        assertNotNull(userManager.searchByUsername("max")); // Searching for an existing user
    }
}