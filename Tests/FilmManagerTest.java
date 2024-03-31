import business.Film;
import business.FilmManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FilmManagerTest {
    private FilmManager filmManager;

    @BeforeEach
    public void setUp() {
        filmManager = new FilmManager();
    }

    @Test
    public void testAddFilm() {
        Film film = new Film("Interstellar", "Sci-Fi", 2014, 9);
        assertTrue(filmManager.addFilm(film));
        assertFalse(filmManager.addFilm(film)); // Adding the same film again should return false
    }

    @Test
    public void testRemoveFilm() {
        Film film = new Film("Inception", "Sci-Fi", 2010, 8);
        filmManager.addFilm(film);
        assertTrue(filmManager.removeFilm("Inception"));
        assertFalse(filmManager.removeFilm("Nonexistent Film"));
    }

    @Test
    public void testRateFilm() {
        Film film = new Film("The Dark Knight", "Action", 2008, 9);
        filmManager.addFilm(film);
        assertTrue(filmManager.rateFilm("The Dark Knight", 8.5));
        assertEquals(1, filmManager.searchByTitle("The Dark Knight").getNumberOfRatings());
    }

    @Test
    public void testSearchByTitle() {
        Film film = new Film("Pulp Fiction", "Crime", 1994, 8);
        filmManager.addFilm(film);
        assertEquals(film, filmManager.searchByTitle("Pulp Fiction"));
        assertNull(filmManager.searchByTitle("Nonexistent Film"));
    }

    @Test
    public void testSearchByGenre() {
        Film film1 = new Film("The Shawshank Redemption", "Drama", 1994, 9);
        Film film2 = new Film("Forrest Gump", "Drama", 1994, 8);
        filmManager.addFilm(film1);
        filmManager.addFilm(film2);

        List<Film> dramaFilms = filmManager.searchByGenre("Drama");
        assertEquals(2, dramaFilms.size());
        assertEquals("Forrest Gump", dramaFilms.get(0).getTitle());
        assertEquals("The Shawshank Redemption", dramaFilms.get(1).getTitle());
    }
}
